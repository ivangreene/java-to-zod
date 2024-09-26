package sh.ivan.zod;

import cz.habarta.typescript.generator.parser.BeanModel;
import cz.habarta.typescript.generator.parser.Model;
import cz.habarta.typescript.generator.parser.ModelParser;
import cz.habarta.typescript.generator.parser.PropertyModel;
import sh.ivan.zod.schema.ObjectSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.attribute.Attribute;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ObjectSchemaBuilder {
    private final JavaToZodConverter converter;
    private final ModelParser modelParser;

    public ObjectSchemaBuilder(JavaToZodConverter converter, ModelParser modelParser) {
        this.converter = converter;
        this.modelParser = modelParser;
    }

    public Map<String, ObjectSchema> buildBeanSchemas(Model model) {
        LinkedHashMap<String, ObjectSchema> schemas = new LinkedHashMap<>();
        model.getBeans().forEach(beanModel -> {
            String typeName = converter.getSchemaName(beanModel.getOrigin());
            if (schemas.containsKey(typeName)) {
                throw new IllegalStateException("Encountered duplicate schema name");
            }
            schemas.put(typeName, build(beanModel, Set.of()));
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
        Class<?> clazz = beanModel.getOrigin();
        LinkedHashMap<String, Schema> fields = new LinkedHashMap<>();
        if (clazz == null) {
            return fields;
        }
        for (PropertyModel propertyModel : beanModel.getProperties()) {
            TypeDescriptor typeDescriptor = new TypeDescriptor(clazz, propertyModel);
            fields.put(propertyModel.getName(), converter.getReferentialSchema(typeDescriptor));
        }
        return fields;
    }
}
