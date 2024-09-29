package sh.ivan.zod;


import cz.habarta.typescript.generator.*;
import lombok.Setter;
import org.apache.maven.plugins.annotations.Parameter;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

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
    private Logger.Level loggingLevel = Logger.Level.Verbose;

    // Skip processing
    private boolean skip = false;
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

