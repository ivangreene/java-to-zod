package sh.ivan.zod.plugins;

import cz.habarta.typescript.generator.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import lombok.Setter;
import org.apache.maven.plugins.annotations.Parameter;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

@Setter
public class PluginParameters implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Prefix added to generated schema names.
     */
    @Parameter
    private String schemaNamePrefix = "";

    /**
     * Suffix added to generated schema names.
     */
    @Parameter(defaultValue = "Schema")
    private String schemaNameSuffix = "Schema";

    /**
     * Classes to process.
     */
    @Parameter
    private List<String> classes = Collections.emptyList();

    /**
     * Classes to process specified using glob patterns
     * so it is possible to specify package or class name suffix.
     * Glob patterns support two wildcards:
     * <ul>
     * <li>Single <code>*</code> wildcard matches any character except for <code>.</code> and <code>$</code>.</li>
     * <li>Double <code>**</code> wildcard matches any character.</li>
     * </ul>
     * For more information and examples see <a href="https://github.com/vojtechhabarta/typescript-generator/wiki/Class-Names-Glob-Patterns">Class Names Glob Patterns</a> Wiki page.
     */
    @Parameter
    private List<String> classPatterns = Collections.emptyList();

    /**
     * Classes to process specified by annotations.
     */
    @Parameter
    private List<String> classesWithAnnotations = Collections.emptyList();

    /**
     * Classes to process specified by implemented interface.
     */
    @Parameter
    private List<String> classesImplementingInterfaces = Collections.emptyList();

    /**
     * Classes to process specified by extended superclasses.
     */
    @Parameter
    private List<String> classesExtendingClasses = Collections.emptyList();

    /**
     * Scans specified JAX-RS {@link javax.ws.rs.core.Application} for classes to process.
     * Parameter contains fully-qualified class name.
     * It is possible to exclude particular REST resource classes using {@link #excludeClasses} parameter.
     */
    @Parameter
    private String classesFromJaxrsApplication;

    /**
     * Scans JAX-RS resources for JSON classes to process.
     * It is possible to exclude particular REST resource classes using {@link #excludeClasses} parameter.
     */
    @Parameter
    private boolean classesFromAutomaticJaxrsApplication = false;

    /**
     * Allows to speed up classpath scanning by limiting scanning to specified packages.
     * This optimization applies to following parameters:
     * <ul>
     * <li><code>classPatterns</code></li>
     * <li><code>classesImplementingInterfaces</code></li>
     * <li><code>classesExtendingClasses</code></li>
     * <li><code>classesWithAnnotations</code></li>
     * <li><code>classesFromAutomaticJaxrsApplication</code></li>
     * </ul>
     * This parameter is passed directly to underlying classpath scanning library (ClassGraph) without any validation or interpretation.
     */
    @Parameter
    private List<String> scanningAcceptedPackages = Collections.emptyList();

    /**
     * List of classes excluded from processing.
     */
    @Parameter
    private List<String> excludeClasses = Collections.emptyList();

    /**
     * Excluded classes specified using glob patterns.
     * For more information and examples see <a href="https://github.com/vojtechhabarta/typescript-generator/wiki/Class-Names-Glob-Patterns">Class Names Glob Patterns</a> Wiki page.
     */
    @Parameter
    private List<String> excludeClassPatterns = Collections.emptyList();

    /**
     * If this list is not empty then only properties with any of these annotations will be included.
     */
    @Parameter
    private List<String> includePropertyAnnotations = Collections.emptyList();

    /**
     * Properties with any of these annotations will be excluded.
     */
    @Parameter
    private List<String> excludePropertyAnnotations = Collections.emptyList();

    /**
     * Library used in JSON classes.
     * Supported values are:
     * <ul>
     * <li><code>jackson2</code> - annotations from `com.fasterxml.jackson.annotation` package</li>
     * <li><code>jaxb</code> - annotations from `javax.xml.bind.annotation` package<li>
     * <li><code>gson</code> - annotations from `com.google.gson.annotations` package<li>
     * <li><code>jsonb</code> - annotations from `javax.json.bind.annotation` package<li>
     * </ul>
     * Required parameter, recommended value is <code>jackson2</code>.
     */
    @Parameter(required = true)
    private JsonLibrary jsonLibrary;

    /**
     * Specifies Jackson 2 global configuration.
     * Description of individual parameters is in
     * <a href="https://github.com/vojtechhabarta/typescript-generator/blob/main/typescript-generator-core/src/main/java/cz/habarta/typescript/generator/Jackson2Configuration.java">Jackson2Configuration</a>
     * class on GitHub (latest version).
     */
    @Parameter
    private Jackson2Configuration jackson2Configuration;

    /**
     * Specifies Gson global configuration.
     * Description of individual parameters is in
     * <a href="https://github.com/vojtechhabarta/typescript-generator/blob/main/typescript-generator-core/src/main/java/cz/habarta/typescript/generator/GsonConfiguration.java">GsonConfiguration</a>
     * class on GitHub (latest version).
     */
    @Parameter
    private GsonConfiguration gsonConfiguration;

    /**
     * Specifies JSON-B global configuration.
     * Description of individual parameters is in
     * <a href="https://github.com/vojtechhabarta/typescript-generator/blob/main/typescript-generator-core/src/main/java/cz/habarta/typescript/generator/JsonbConfiguration.java">Jackson2Configuration</a>
     * class on GitHub (latest version).
     */
    @Parameter
    private JsonbConfiguration jsonbConfiguration;

    /**
     * If <code>true</code> Spring REST application will be loaded and scanned for classes to process.
     * It is needed to specify application class using another parameter (for example {@link #classes}).
     */
    @Parameter
    private boolean scanSpringApplication = false;

    /**
     * Specifies level of logging output.
     * Supported values are:
     * <ul>
     * <li><code>Debug</code></li>
     * <li><code>Verbose</code></li>
     * <li><code>Info</code></li>
     * <li><code>Warning</code></li>
     * <li><code>Error</code></li>
     * </ul>
     * Default value is <code>Verbose</code>.
     */
    @Parameter
    private Logger.Level loggingLevel = Logger.Level.Verbose;

    @Parameter(property = "java.to.zod.skip")
    private boolean skip = false;

    /**
     * The presence of any annotation in this list on a JSON property will cause
     * the typescript-generator to treat that property as optional when generating
     * the corresponding TypeScript interface.
     * Example optional annotation: <code>javax.annotation.Nullable</code>.
     */
    private List<String> optionalAnnotations = Collections.emptyList();

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
    @Optional
    public List<String> getOptionalAnnotations() {
        return optionalAnnotations;
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
