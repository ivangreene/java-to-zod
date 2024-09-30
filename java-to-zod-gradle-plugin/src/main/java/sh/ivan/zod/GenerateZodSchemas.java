package sh.ivan.zod;

import cz.habarta.typescript.generator.Logger;
import cz.habarta.typescript.generator.TypeScriptGenerator;
import lombok.Setter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import sh.ivan.zod.plugins.PluginParameters;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;


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

            new JavaToZodConvertorWrapper(classLoader, pluginParameters, this::getOutputFile).run();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
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
