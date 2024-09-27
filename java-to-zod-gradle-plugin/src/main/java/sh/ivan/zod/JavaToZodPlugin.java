package sh.ivan.zod;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class JavaToZodPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        // Register the custom task (PojoParser)
        project.getTasks().create("parsePojo", PojoParser.class);
    }
}