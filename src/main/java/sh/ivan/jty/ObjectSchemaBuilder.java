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

public class ObjectSchemaBuilder {
    private static final Jsr380ToYupConverter CONVERTER = new Jsr380ToYupConverter();

    public ObjectSchema build(Class<?> clazz) {
        return new ObjectSchema(getFields(clazz));
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
                                CONVERTER.getPropertySchema(propertyDescriptor.getPropertyType())));
        return fields;
    }

    private String getFieldName(PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getName();
    }
}
