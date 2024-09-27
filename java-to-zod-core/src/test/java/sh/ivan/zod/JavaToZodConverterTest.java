package sh.ivan.zod;

import cz.habarta.typescript.generator.DefaultTypeProcessor;
import cz.habarta.typescript.generator.Settings;
import cz.habarta.typescript.generator.parser.Jackson2Parser;

abstract class JavaToZodConverterTest {
    JavaToZodConverter converter = new JavaToZodConverter(
            new Jackson2Parser(new Settings(), new DefaultTypeProcessor()),
            Configuration.builder().schemaNamePrefix("").schemaNameSuffix("").build(),
            new Settings());
}
