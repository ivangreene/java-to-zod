package sh.ivan.zod;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

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
        Path srcDir = Path.of(testProjectDir.getPath(), "src/main/java/sh/ivan/zod/resources");
        Files.createDirectories(srcDir);

        // Copy TestPersonClass.java from resources
        Path testPersonClass = Path.of("src/test/java/sh/ivan/zod/resources/TestPersonClass.java");
        Files.copy(testPersonClass, srcDir.resolve("TestPersonClass.java"), StandardCopyOption.REPLACE_EXISTING);

        // Copy TestPersonRecord.java from resources
        Path testPersonRecord = Path.of("src/test/java/sh/ivan/zod/resources/TestPersonRecord.java");
        Files.copy(testPersonRecord, srcDir.resolve("TestPersonRecord.java"), StandardCopyOption.REPLACE_EXISTING);

        // First: Build the Java class files
        BuildResult buildResult = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments("build") // or use 'compileJava' if you prefer
                .withPluginClasspath()
                .build();

        // Ensure the build was successful
        assertEquals(SUCCESS, buildResult.task(":build").getOutcome());

        // When: Run the Gradle task to generate Zod schemas
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments("generateZodSchemas")
                .withPluginClasspath()
                .build();

        // Then: Verify the task outcome and schema generation
        assertEquals(SUCCESS, result.task(":generateZodSchemas").getOutcome());

        File outputDir = new File(testProjectDir, "build/java-to-zod");
        System.out.println("Output directory contents: " + Arrays.toString(outputDir.listFiles()));


        // Verify that the output file has been generated
        File outputFile = new File(testProjectDir, "build/temp/generated-schemas/schemas.ts");
        assertTrue(outputFile.exists(), "Zod schema file should be generated");

        // Verify
        String outputContent = Files.readString(outputFile.toPath(), StandardCharsets.UTF_8);

        Path snapshotFilePath = Path.of("src/test/ts/src/schemas_snapshot.ts");
        String snapshotContent = Files.readString(snapshotFilePath, StandardCharsets.UTF_8);
        assertEquals(outputContent, snapshotContent, "The generated Zod schemas do not match the snapshot content.");


    }
}
