package sh.ivan.yup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import sh.ivan.yup.schema.ObjectSchema;

public class SchemaFileWriter {
    private final Map<String, ObjectSchema> schemas;
    private final File outputFile;

    public SchemaFileWriter(Map<String, ObjectSchema> schemas, File outputFile) {
        this.schemas = schemas;
        this.outputFile = outputFile;
    }

    public void write() throws IOException {
        var directory = outputFile.getParentFile();
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Failed to create directory " + directory);
            }
        } else if (!directory.isDirectory()) {
            throw new IOException(directory + " is not a directory");
        }
        try (var fileWriter = new FileWriter(outputFile);
                var printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("const yup = require('yup');");
            schemas.forEach((name, schema) -> {
                printWriter.println();
                printWriter.printf("exports.%sSchema = %s;", name, schema.asYupSchema("yup."));
                printWriter.println();
            });
        }
    }
}
