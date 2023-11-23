package sh.ivan.yup;

import cz.habarta.typescript.generator.DefaultTypeProcessor;
import cz.habarta.typescript.generator.Settings;
import cz.habarta.typescript.generator.parser.Jackson2Parser;

abstract class JavaToYupConverterTest {
    JavaToYupConverter converter =
            new JavaToYupConverter(new Jackson2Parser(new Settings(), new DefaultTypeProcessor()));
}
