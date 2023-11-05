package sh.ivan.jty;

import sh.ivan.jty.schema.ObjectSchema;
import sh.ivan.jty.schema.Schema;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ObjectSchemaBuilder {
    private static final Jsr380ToYupConverter CONVERTER = new Jsr380ToYupConverter();
    private static final AttributeProcessor ATTRIBUTE_PROCESSOR = new AttributeProcessor();

    public ObjectSchema build(Class<?> clazz) {
        return new ObjectSchema(getFields(clazz), Set.of());
    }

    private Map<String, Schema> getFields(Class<?> clazz) {
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        var fields = new LinkedHashMap<String, Schema>();
        Arrays.stream(beanInfo.getPropertyDescriptors())
                .filter(propertyDescriptor -> !"class".equals(propertyDescriptor.getName()))
                .forEach(propertyDescriptor ->
                        fields.put(getFieldName(propertyDescriptor),
                                CONVERTER.getPropertySchema(propertyDescriptor.getPropertyType(),
                                        ATTRIBUTE_PROCESSOR.getAttributes(clazz, propertyDescriptor.getReadMethod(),
                                                getFieldName(propertyDescriptor)))));
        return fields;
    }

    private String getFieldName(PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getName();
    }
}
