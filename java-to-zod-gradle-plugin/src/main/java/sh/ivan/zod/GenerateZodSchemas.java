package sh.ivan.zod;

import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.parser.Model;
import lombok.Setter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import sh.ivan.zod.schema.ObjectSchema;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Setter
public class GenerateZodSchemas extends DefaultTask {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GenerateZodSchemas.class);

    // Path and name of generated file
    private File outputFile = null;


    @Nested
    public PluginParameters getPluginParameters() {
        return pluginParameters;
    }

    private PluginParameters pluginParameters = new PluginParameters();

    public GenerateZodSchemas() {}

    @TaskAction
    public void runTask() {
        try {
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init() throws IOException {
        TypeScriptGenerator.setLogger(new Logger(pluginParameters.getLoggingLevel()));
        TypeScriptGenerator.printVersion();
        if (pluginParameters.isSkip()) {
            TypeScriptGenerator.getLogger().info("Skipping plugin execution");
            return;
        }

        // class loader
        List<URL> urls = new ArrayList<>();
        // Add the output directory for your compiled classes (this excludes dependencies)
        File classesDir = new File(getBuildDirectoryAsFile(), "classes/java/main");


        // Check if the directory exists before adding it
        if (classesDir.exists()) {
            urls.add(classesDir.toURI().toURL());
        }

        // Create a class loader using URLs from the classpath
        try (URLClassLoader classLoader = new URLClassLoader(
                urls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader())) {

            Settings settings = createSettings(classLoader);

            cz.habarta.typescript.generator.Input.Parameters parameters =
                    new cz.habarta.typescript.generator.Input.Parameters();
            parameters.classNames = pluginParameters.getClasses();
            parameters.classNamePatterns = pluginParameters.getClassPatterns();
            parameters.classesWithAnnotations = pluginParameters.getClassesWithAnnotations();
            parameters.classesImplementingInterfaces = pluginParameters.getClassesImplementingInterfaces();
            parameters.classesExtendingClasses = pluginParameters.getClassesExtendingClasses();
            parameters.jaxrsApplicationClassName = pluginParameters.getClassesFromJaxrsApplication();
            parameters.automaticJaxrsApplication = pluginParameters.isClassesFromAutomaticJaxrsApplication();
            parameters.isClassNameExcluded = settings.getExcludeFilter();
            parameters.classLoader = classLoader;
            parameters.scanningAcceptedPackages = pluginParameters.getScanningAcceptedPackages();
            parameters.debug = pluginParameters.getLoggingLevel() == Logger.Level.Debug;

            cz.habarta.typescript.generator.Input input = cz.habarta.typescript.generator.Input.from(parameters);
            TypeScriptGenerator typeScriptGenerator = new TypeScriptGenerator(settings);
            Model model = typeScriptGenerator.getModelParser().parseModel(input.getSourceTypes());
            Configuration configuration = Configuration.builder()
                    .schemaNamePrefix(pluginParameters.getSchemaNamePrefix())
                    .schemaNameSuffix(pluginParameters.getSchemaNameSuffix())
                    .build();
            JavaToZodConverter javaToZodConverter =
                    new JavaToZodConverter(typeScriptGenerator.getModelParser(), configuration);
            Map<String, ObjectSchema> beanSchemas = javaToZodConverter.getBeanSchemas(model);
            SchemaFileWriter schemaFileWriter = new SchemaFileWriter(beanSchemas, getOutputFile());
            schemaFileWriter.write();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Settings createSettings(URLClassLoader classLoader) {
        Settings settings = new Settings();
        settings.setExcludeFilter(pluginParameters.getExcludeClasses(), pluginParameters.getExcludeClassPatterns());
        settings.jsonLibrary = pluginParameters.getJsonLibrary();
        settings.setJackson2Configuration(classLoader, pluginParameters.getJackson2Configuration());
        settings.gsonConfiguration = pluginParameters.getGsonConfiguration();
        settings.jsonbConfiguration = pluginParameters.getJsonbConfiguration();
        settings.scanSpringApplication = pluginParameters.isScanSpringApplication();
        settings.loadIncludePropertyAnnotations(classLoader, pluginParameters.getIncludePropertyAnnotations());
        settings.loadExcludePropertyAnnotations(classLoader, pluginParameters.getIncludePropertyAnnotations());
        settings.classLoader = classLoader;
        settings.outputKind = TypeScriptOutputKind.global; // Unused, but required by settings validation
        settings.outputFileType = TypeScriptFileType.implementationFile;
        settings.loadOptionalAnnotations(classLoader, pluginParameters.getOptionalAnnotations());
        return settings;
    }

    @OutputFile
    public File getOutputFile() {

        if (outputFile == null) {
            // Set the default output file location based on the project directory
            outputFile =
                    new File(
                            getBuildDirectoryAsFile(), "java-to-zod/" + getProject().getName() + "-schemas.js");

        }
        return outputFile;
    }

    private @NotNull File getBuildDirectoryAsFile() {
        return getProject().getLayout().getBuildDirectory().get().getAsFile();
    }



}
