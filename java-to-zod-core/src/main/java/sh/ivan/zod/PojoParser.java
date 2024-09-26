package sh.ivan.zod;

import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.parser.Model;
import lombok.Setter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import sh.ivan.zod.schema.ObjectSchema;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Setter
public class PojoParser extends DefaultTask {

    // Path and name of generated file
    private File outputFile = null;

    // Prefix added to generated schema names
    private String schemaNamePrefix = "";

    // Suffix added to generated schema names
    private String schemaNameSuffix = "Schema";

    // Classes to process
    private List<String> classes = Collections.emptyList();

    // Classes to process specified using glob patterns
    private List<String> classPatterns = Collections.emptyList();

    // Classes to process specified by annotations
    private List<String> classesWithAnnotations = Collections.emptyList();

    // Classes to process specified by implemented interface
    private List<String> classesImplementingInterfaces = Collections.emptyList();

    // Classes to process specified by extended superclasses
    private List<String> classesExtendingClasses = Collections.emptyList();

    // Scans specified JAX-RS Application for classes to process
    private String classesFromJaxrsApplication;

    // Scans JAX-RS resources for JSON classes to process
    private boolean classesFromAutomaticJaxrsApplication = false;

    // Allows to speed up classpath scanning by limiting scanning to specified packages
    private List<String> scanningAcceptedPackages = Collections.emptyList();

    // List of classes excluded from processing
    private List<String> excludeClasses = Collections.emptyList();

    // Excluded classes specified using glob patterns
    private List<String> excludeClassPatterns = Collections.emptyList();

    // Only include properties with these annotations
    private List<String> includePropertyAnnotations = Collections.emptyList();

    // Exclude properties with these annotations
    private List<String> excludePropertyAnnotations = Collections.emptyList();

    // Library used in JSON classes (e.g., Jackson2, Gson)
    private JsonLibrary jsonLibrary;

    // Jackson 2 global configuration
    private Jackson2Configuration jackson2Configuration;

    // Gson global configuration
    private GsonConfiguration gsonConfiguration;

    // JSON-B global configuration
    private JsonbConfiguration jsonbConfiguration;

    // Scan Spring REST application for classes to process
    private boolean scanSpringApplication = false;

    // Specifies the level of logging output (Debug, Verbose, etc.)
    private static Logger.Level loggingLevel = Logger.Level.Verbose;

    // Skip processing
    private boolean skip = false;

    private static final Logger logger = new Logger(loggingLevel);;


    public PojoParser() {
    }

    @TaskAction
    public void runTask() {
        try {
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init() throws IOException {
        TypeScriptGenerator.setLogger(logger);
        TypeScriptGenerator.printVersion();
        if (skip) {
            TypeScriptGenerator.getLogger().info("Skipping plugin execution");
            return;
        }

        // class loader
        List<URL> urls = new ArrayList<>();
        // Add the output directory for your compiled classes (this excludes dependencies)
        File classesDir = new File(getProject().getBuildDir(), "classes/java/main");

        // Check if the directory exists before adding it
        if (classesDir.exists()) {
            urls.add(classesDir.toURI().toURL());
        }


        // Create a class loader using URLs from the classpath
        try (URLClassLoader classLoader = new URLClassLoader(
                urls.toArray(new URL[0]),
                Thread.currentThread().getContextClassLoader())) {

            Settings settings = createSettings(classLoader);



            cz.habarta.typescript.generator.Input.Parameters parameters = new cz.habarta.typescript.generator.Input.Parameters();
            parameters.classNames = classes;
            parameters.classNamePatterns = classPatterns;
            parameters.classesWithAnnotations = classesWithAnnotations;
            parameters.classesImplementingInterfaces = classesImplementingInterfaces;
            parameters.classesExtendingClasses = classesExtendingClasses;
            parameters.jaxrsApplicationClassName = classesFromJaxrsApplication;
            parameters.automaticJaxrsApplication = classesFromAutomaticJaxrsApplication;
            parameters.isClassNameExcluded = settings.getExcludeFilter();
            parameters.classLoader = classLoader;
            parameters.scanningAcceptedPackages = scanningAcceptedPackages;
            parameters.debug = loggingLevel == Logger.Level.Debug;

            cz.habarta.typescript.generator.Input input = cz.habarta.typescript.generator.Input.from(parameters);
            TypeScriptGenerator typeScriptGenerator = new TypeScriptGenerator(settings);
            Model model = typeScriptGenerator.getModelParser().parseModel(input.getSourceTypes());
            Configuration configuration = Configuration.builder()
                    .schemaNamePrefix(schemaNamePrefix)
                    .schemaNameSuffix(schemaNameSuffix)
                    .build();
            JavaToZodConverter javaToZodConverter = new JavaToZodConverter(typeScriptGenerator.getModelParser(), configuration);
            Map<String, ObjectSchema> beanSchemas = javaToZodConverter.getBeanSchemas(model);
            SchemaFileWriter schemaFileWriter = new SchemaFileWriter(beanSchemas, outputFile);
            schemaFileWriter.write();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Settings createSettings(URLClassLoader classLoader) {
        Settings settings = new Settings();
        settings.setExcludeFilter(excludeClasses, excludeClassPatterns);
        settings.jsonLibrary = jsonLibrary;
        settings.setJackson2Configuration(classLoader, jackson2Configuration);
        settings.gsonConfiguration = gsonConfiguration;
        settings.jsonbConfiguration = jsonbConfiguration;
        settings.scanSpringApplication = scanSpringApplication;
        settings.loadIncludePropertyAnnotations(classLoader, includePropertyAnnotations);
        settings.loadExcludePropertyAnnotations(classLoader, excludePropertyAnnotations);
        settings.classLoader = classLoader;
        settings.outputKind = TypeScriptOutputKind.global; // Unused, but required by settings validation
        settings.outputFileType = TypeScriptFileType.implementationFile;
        return settings;
    }

    @OutputFile
    public File getOutputFile() {
        if (outputFile == null) {
            // Set the default output file location based on the project directory
            outputFile = new File(getProject().getBuildDir(), "java-to-zod/" + getProject().getName() + "-schemas.js");
        }
        return outputFile;
    }

    @Input
    @Optional
    public String getSchemaNamePrefix() {
        return schemaNamePrefix;
    }

    @Input
    @Optional
    public String getSchemaNameSuffix() {
        return schemaNameSuffix;
    }

    @Input
    public List<String> getClasses() {
        return classes;
    }

    @Input
    @Optional
    public List<String> getClassPatterns() {
        return classPatterns;
    }

    @Input
    @Optional
    public List<String> getClassesWithAnnotations() {
        return classesWithAnnotations;
    }

    @Input
    @Optional
    public List<String> getClassesImplementingInterfaces() {
        return classesImplementingInterfaces;
    }

    @Input
    @Optional
    public List<String> getClassesExtendingClasses() {
        return classesExtendingClasses;
    }

    @Input
    @Optional
    public String getClassesFromJaxrsApplication() {
        return classesFromJaxrsApplication;
    }

    @Input
    public boolean isClassesFromAutomaticJaxrsApplication() {
        return classesFromAutomaticJaxrsApplication;
    }

    @Input
    @Optional
    public List<String> getScanningAcceptedPackages() {
        return scanningAcceptedPackages;
    }

    @Input
    @Optional
    public List<String> getExcludeClasses() {
        return excludeClasses;
    }

    @Input
    @Optional
    public List<String> getExcludeClassPatterns() {
        return excludeClassPatterns;
    }

    @Input
    @Optional
    public List<String> getIncludePropertyAnnotations() {
        return includePropertyAnnotations;
    }

    @Input
    @Optional
    public List<String> getExcludePropertyAnnotations() {
        return excludePropertyAnnotations;
    }

    @Input
    public JsonLibrary getJsonLibrary() {
        return jsonLibrary;
    }

    @Input
    @Optional
    public Jackson2Configuration getJackson2Configuration() {
        return jackson2Configuration;
    }

    @Input
    @Optional
    public GsonConfiguration getGsonConfiguration() {
        return gsonConfiguration;
    }

    @Input
    @Optional
    public JsonbConfiguration getJsonbConfiguration() {
        return jsonbConfiguration;
    }

    @Input
    public boolean isScanSpringApplication() {
        return scanSpringApplication;
    }

    @Input
    @Optional
    public Logger.Level getLoggingLevel() {
        return loggingLevel;
    }

    @Input
    public boolean isSkip() {
        return skip;
    }

}
