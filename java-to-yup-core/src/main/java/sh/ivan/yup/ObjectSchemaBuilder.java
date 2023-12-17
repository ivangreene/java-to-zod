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
    private final ModelParser modelParser;

    public ObjectSchemaBuilder(JavaToYupConverter converter, ModelParser modelParser) {
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
