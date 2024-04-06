package sh.ivan.zod;

import cz.habarta.typescript.generator.parser.BeanModel;
import cz.habarta.typescript.generator.parser.Model;
import cz.habarta.typescript.generator.parser.ModelParser;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import sh.ivan.zod.schema.ObjectSchema;
import sh.ivan.zod.schema.Schema;
import sh.ivan.zod.schema.attribute.Attribute;

public class ObjectSchemaBuilder {
    private final JavaToZodConverter converter;
    private final ModelParser modelParser;

    public ObjectSchemaBuilder(JavaToZodConverter converter, ModelParser modelParser) {
        this.converter = converter;
        this.modelParser = modelParser;
    }

    public Map<String, ObjectSchema> buildBeanSchemas(Model model) {
        var schemas = new LinkedHashMap<String, ObjectSchema>();
        model.getBeans().forEach(beanModel -> {
            var typeName = converter.getSchemaName(beanModel.getOrigin());
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
        var clazz = beanModel.getOrigin();
        var fields = new LinkedHashMap<String, Schema>();
        beanModel.getProperties().forEach(propertyModel -> {
            var typeDescriptor = new TypeDescriptor(clazz, propertyModel);
            fields.put(propertyModel.getName(), converter.getReferentialSchema(typeDescriptor));
        });
        return fields;
    }
}
