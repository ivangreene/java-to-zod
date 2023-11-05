package sh.ivan.yup;

import cz.habarta.typescript.generator.parser.ModelParser;
import cz.habarta.typescript.generator.parser.PropertyModel;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import sh.ivan.yup.schema.ObjectSchema;
import sh.ivan.yup.schema.Schema;
import sh.ivan.yup.schema.attribute.Attribute;

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

    public ObjectSchema build(Class<?> clazz, Set<Attribute> attributes) {
        return new ObjectSchema(getFields(clazz), attributes);
    }

    private Map<String, Schema> getFields(Class<?> clazz) {
        var model = modelParser.parseModel(clazz);
        var bean = model.getBean(clazz);
        var fields = new LinkedHashMap<String, Schema>();
        bean.getProperties()
                .forEach(propertyModel -> fields.put(
                        propertyModel.getName(),
                        converter.getPropertySchema(propertyModel.getType(), getAttributes(clazz, propertyModel))));
        return fields;
    }

    private Set<Attribute> getAttributes(Class<?> container, PropertyModel propertyModel) {
        Field field = null;
        try {
            field = container.getDeclaredField(propertyModel.getName());
        } catch (NoSuchFieldException ignored) {
        }
        var annotatedElements = new HashSet<AnnotatedElement>();
        if (field != null) {
            annotatedElements.add(field);
        }
        if (propertyModel.getOriginalMember() instanceof AnnotatedElement) {
            annotatedElements.add((AnnotatedElement) propertyModel.getOriginalMember());
        }
        return attributeProcessor.getAttributes(propertyModel.getType(), annotatedElements);
    }
}
