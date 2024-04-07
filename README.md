# Java to Zod

Convert POJOs to [Zod](https://zod.dev/) schemas.
Uses [JSR-380 (bean validation) annotations](https://www.baeldung.com/java-validation)
to specify the constraints.

## Why?

Write your schema once, and use it for both frontend
and backend validation.

## Example
```java
public class Person {
    @NotEmpty
    public String givenName;

    @NotEmpty
    public String surname;

    @Positive
    public int age;

    @NotNull
    public AccountType accountType;
}

public enum AccountType {
    PREMIUM,
    STANDARD,
    BASIC,
}
```

Running the java-to-zod plugin will give you:
```js
const PersonSchema = zod.object({
    givenName: zod.string().min(1),
    surname: zod.string().min(1),
    age: zod.number().int().positive(),
    accountType: zod.enum(['PREMIUM', 'STANDARD', 'BASIC']),
});
```

Goes well with
[typescript-generator](https://github.com/vojtechhabarta/typescript-generator),
and shares many of the same configuration options
for scanning source classes.

## Example configuration
```xml
<plugin>
  <groupId>sh.ivan</groupId>
  <artifactId>java-to-zod-maven-plugin</artifactId>
  <version>VERSION</version>
  <configuration>
    <jsonLibrary>jackson2</jsonLibrary>
    <outputFile>${project.basedir}/../frontend/generated-schemas.js</outputFile>
    <classPatterns>
      <classPattern>sh.ivan.pojo.*</classPattern>
    </classPatterns>
  </configuration>
  <executions>
    <execution>
      <id>generate</id>
      <goals>
        <goal>generate</goal>
      </goals>
      <phase>process-classes</phase>
    </execution>
  </executions>
</plugin>
```

See [plugin configuration docs](https://java-to-zod.ivan.sh/java-to-zod-maven-plugin/generate-mojo.html)
for all configuration properties.
