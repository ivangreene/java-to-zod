package sh.ivan.zod;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenerateZodSchemasTest {

    @TempDir
    File testProjectDir;

    @BeforeEach
    public void setup() throws IOException {
        // Copy the external build.gradle.kts file to the test project directory
        Path buildFile = new File(testProjectDir, "build.gradle.kts").toPath();
        Path buildFileSource = Path.of("src/test/resources/build.gradle.kts");
        Files.copy(buildFileSource, buildFile, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void testGenerateZodSchemasTask() throws IOException {
        // Given: Add an actual Java test class to the test project directory
        Path srcDir = Path.of(testProjectDir.getPath(), "src/main/java/sh/ivan/zod/dto");
        Files.createDirectories(srcDir);

        // Copy TestPersonDto.java from resources
        Path testPersonDto = Path.of("src/test/java/sh/ivan/zod/resources/TestPersonClass.java");
        Files.copy(testPersonDto, srcDir.resolve("TestPersonDto.java"), StandardCopyOption.REPLACE_EXISTING);

        // When: Run the Gradle task to generate Zod schemas
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments("generateZodSchemas")
                .withPluginClasspath()
                .build();

        // Then: Verify the task outcome and schema generation
        assertEquals(SUCCESS, result.task(":generateZodSchemas").getOutcome());

        // Verify that the output file has been generated
        File outputFile = new File(testProjectDir, "build/java-to-zod/generated-schemas.js");
        assertTrue(outputFile.exists(), "Zod schema file should be generated");
    }
}
