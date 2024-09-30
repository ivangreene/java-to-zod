package sh.ivan.zod;

import cz.habarta.typescript.generator.*;
import cz.habarta.typescript.generator.parser.Model;
import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.function.Supplier;
import sh.ivan.zod.plugins.PluginParameters;
import sh.ivan.zod.schema.ObjectSchema;

public class JavaToZodConvertorWrapper {
    private final URLClassLoader classLoader;
    private final PluginParameters pluginParameters;
    private final Supplier<File> getOutputFile;

    public JavaToZodConvertorWrapper(
            URLClassLoader classLoader, PluginParameters pluginParameters, Supplier<File> getOutputFile) {
        this.classLoader = classLoader;
        this.pluginParameters = pluginParameters;
        this.getOutputFile = getOutputFile;
    }

    public void run() throws IOException {
        Settings settings = from(classLoader, pluginParameters);

        Input.Parameters parameters = new Input.Parameters();
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

        Input input = Input.from(parameters);
        TypeScriptGenerator typeScriptGenerator = new TypeScriptGenerator(settings);
        Model model = typeScriptGenerator.getModelParser().parseModel(input.getSourceTypes());
        Configuration configuration = Configuration.builder()
                .schemaNamePrefix(pluginParameters.getSchemaNamePrefix())
                .schemaNameSuffix(pluginParameters.getSchemaNameSuffix())
                .build();
        JavaToZodConverter javaToZodConverter =
                new JavaToZodConverter(typeScriptGenerator.getModelParser(), configuration);
        Map<String, ObjectSchema> beanSchemas = javaToZodConverter.getBeanSchemas(model);
        SchemaFileWriter schemaFileWriter = new SchemaFileWriter(beanSchemas, getOutputFile.get());
        schemaFileWriter.write();
    }

    public Settings from(URLClassLoader classLoader, PluginParameters pluginParameters) {
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
}
