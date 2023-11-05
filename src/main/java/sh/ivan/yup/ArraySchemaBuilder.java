package sh.ivan.yup;

import cz.habarta.typescript.generator.type.JParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import sh.ivan.yup.schema.Schema;
import sh.ivan.yup.schema.attribute.Attribute;

public class ArraySchemaBuilder {
    public Schema build(Type type, Set<Attribute> attributes) {
        if (type instanceof Class<?> && ((Class<?>) type).isArray()) {
            return new sh.ivan.yup.schema.ArraySchema(
                    new Jsr380ToYupConverter().buildSchema(((Class<?>) type).getComponentType(), attributes),
                    attributes);
        } else if (type instanceof JParameterizedType) {
            var parameterizedType = (JParameterizedType) type;
            return new sh.ivan.yup.schema.ArraySchema(
                    new Jsr380ToYupConverter().buildSchema(parameterizedType.getActualTypeArguments()[0], attributes),
                    attributes);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}
