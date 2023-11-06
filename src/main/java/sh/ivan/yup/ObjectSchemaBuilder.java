package sh.ivan.yup;

import cz.habarta.typescript.generator.parser.ModelParser;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import sh.ivan.yup.schema.ObjectSchema;
import sh.ivan.yup.schema.Schema;
import sh.ivan.yup.schema.attribute.Attribute;

public class ObjectSchemaBuilder {
    private final JavaToYupConverter converter;
    private final AttributeProcessor attributeProcessor;
    private final ModelParser modelParser;

    public ObjectSchemaBuilder(
            JavaToYupConverter converter, AttributeProcessor attributeProcessor, ModelParser modelParser) {
        this.converter = converter;
        this.attributeProcessor = attributeProcessor;
        this.modelParser = modelParser;
    }

    public ObjectSchema build(Class<?> clazz, Set<Attribute> attributes) {
        return new ObjectSchema(getFields(clazz), attributes);
    }

    private Map<String, Schema> getFields(Class<?> clazz) {
        var model = modelParser.parseModel(clazz);
        var bean = model.getBean(clazz);
        var fields = new LinkedHashMap<String, Schema>();
        bean.getProperties().forEach(propertyModel -> {
            var propertyDescriptor = new PropertyDescriptor(clazz, propertyModel);
            fields.put(
                    propertyModel.getName(),
                    converter.getPropertySchema(
                            propertyDescriptor,
                            attributeProcessor.getAttributes(
                                    propertyModel.getType(), propertyDescriptor.getAnnotatedElements())));
        });
        return fields;
    }
}
