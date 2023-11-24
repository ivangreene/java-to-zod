package sh.ivan.yup;

import cz.habarta.typescript.generator.parser.BeanModel;
import cz.habarta.typescript.generator.parser.Model;
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

    public Map<String, ObjectSchema> buildBeanSchemas(Model model) {
        var schemas = new LinkedHashMap<String, ObjectSchema>();
        model.getBeans().forEach(beanModel -> {
            if (schemas.containsKey(beanModel.getOrigin().getSimpleName())) {
                throw new IllegalStateException("Encountered duplicate schema name");
            }
            schemas.put(beanModel.getOrigin().getSimpleName(), build(beanModel, Set.of()));
        });
        return schemas;
    }

    public ObjectSchema build(Class<?> clazz, Set<Attribute> attributes) {
        return build(modelParser.parseModel(clazz).getBean(clazz), attributes);
    }

    private ObjectSchema build(BeanModel beanModel, Set<Attribute> attributes) {
        return new ObjectSchema(getFields(beanModel), attributes);
    }

    private Map<String, Schema> getFields(BeanModel beanModel) {
        var clazz = beanModel.getOrigin();
        var fields = new LinkedHashMap<String, Schema>();
        beanModel.getProperties().forEach(propertyModel -> {
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
