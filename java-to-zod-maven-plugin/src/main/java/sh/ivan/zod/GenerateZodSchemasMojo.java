package sh.ivan.zod;

import cz.habarta.typescript.generator.Logger;
import cz.habarta.typescript.generator.Settings;
import cz.habarta.typescript.generator.TypeScriptGenerator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import sh.ivan.zod.plugins.PluginParameters;

/**
 * Generates Zod schemas from specified java classes.
 */
@Mojo(
        name = "generate",
        defaultPhase = LifecyclePhase.PROCESS_CLASSES,
        requiresDependencyResolution = ResolutionScope.COMPILE,
        threadSafe = true)
public class GenerateZodSchemasMojo extends AbstractMojo {

    /**
     * Path and name of generated file.
     */
    @Parameter(
            defaultValue = "${project.build.directory}/java-to-zod/${project.artifactId}-schemas.js",
            required = true)
    public File outputFile;

    @Parameter
    private PluginParameters pluginParameters;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    public MavenProject project;

    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required = true)
    public String projectBuildDirectory;

    @Override
    public void execute() throws MojoExecutionException {
        TypeScriptGenerator.setLogger(new Logger(pluginParameters.getLoggingLevel()));
        TypeScriptGenerator.printVersion();
        if (pluginParameters.isSkip()) {
            TypeScriptGenerator.getLogger().info("Skipping plugin execution");
            return;
        }

        // class loader
        List<URL> urls = new ArrayList<>();
        try {
            for (String element : project.getCompileClasspathElements()) {
                urls.add(new File(element).toURI().toURL());
            }
        } catch (DependencyResolutionRequiredException | IOException e) {
            throw new MojoExecutionException(e);
        }

        try (URLClassLoader classLoader = Settings.createClassLoader(
                project.getArtifactId(),
                urls.toArray(new URL[0]),
                Thread.currentThread().getContextClassLoader())) {

            new JavaToZodConvertorWrapper(classLoader, pluginParameters, () -> outputFile).run();

        } catch (IOException e) {
            throw new MojoExecutionException(e);
        }
    }
}
