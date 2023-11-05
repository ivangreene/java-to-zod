package sh.ivan.jty;

import cz.habarta.typescript.generator.DefaultTypeProcessor;
import cz.habarta.typescript.generator.Settings;
import cz.habarta.typescript.generator.parser.Jackson2Parser;
import cz.habarta.typescript.generator.parser.ModelParser;
import sh.ivan.jty.schema.ObjectSchema;
import sh.ivan.jty.schema.Schema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ObjectSchemaBuilder {
    private static final Jsr380ToYupConverter CONVERTER = new Jsr380ToYupConverter();
    private static final AttributeProcessor ATTRIBUTE_PROCESSOR = new AttributeProcessor();
    private static final ModelParser MODEL_PARSER = new Jackson2Parser(new Settings(), new DefaultTypeProcessor());

    public ObjectSchema build(Class<?> clazz) {
        return new ObjectSchema(getFields(clazz), Set.of());
    }

    private Map<String, Schema> getFields(Class<?> clazz) {
        if (clazz == Object.class) {
            return Map.of();
        }
        var model = MODEL_PARSER.parseModel(clazz);
        var bean = model.getBean(clazz);
        var fields = new LinkedHashMap<String, Schema>();
        bean.getProperties()
                .forEach(propertyModel ->
                        fields.put(propertyModel.getName(), CONVERTER.getPropertySchema((Class<?>) propertyModel.getType(),
                                ATTRIBUTE_PROCESSOR.getAttributes(clazz, propertyModel.getOriginalMember(),
                                        propertyModel.getName()))));
        return fields;
    }
}
