package sh.ivan.yup;

import cz.habarta.typescript.generator.parser.ModelParser;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import sh.ivan.yup.schema.ObjectSchema;
import sh.ivan.yup.schema.Schema;

public class ObjectSchemaBuilder {
    private final Jsr380ToYupConverter converter;
    private final AttributeProcessor attributeProcessor;
    private final ModelParser modelParser;

    public ObjectSchemaBuilder(
            Jsr380ToYupConverter converter, AttributeProcessor attributeProcessor, ModelParser modelParser) {
        this.converter = converter;
        this.attributeProcessor = attributeProcessor;
        this.modelParser = modelParser;
    }

    public ObjectSchema build(Class<?> clazz) {
        return new ObjectSchema(getFields(clazz), Set.of());
    }

    private Map<String, Schema> getFields(Class<?> clazz) {
        var model = modelParser.parseModel(clazz);
        var bean = model.getBean(clazz);
        var fields = new LinkedHashMap<String, Schema>();
        bean.getProperties()
                .forEach(propertyModel -> fields.put(
                        propertyModel.getName(),
                        converter.getPropertySchema(
                                (Class<?>) propertyModel.getType(),
                                attributeProcessor.getAttributes(
                                        clazz, propertyModel.getOriginalMember(), propertyModel.getName()))));
        return fields;
    }
}
