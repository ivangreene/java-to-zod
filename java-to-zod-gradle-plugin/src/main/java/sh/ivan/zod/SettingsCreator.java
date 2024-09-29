package sh.ivan.zod;

import cz.habarta.typescript.generator.Settings;
import cz.habarta.typescript.generator.TypeScriptFileType;
import cz.habarta.typescript.generator.TypeScriptOutputKind;

import java.net.URLClassLoader;

public class SettingsCreator {

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
