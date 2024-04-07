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

## Supported annotations
- [X] [AssertFalse](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/AssertFalse.html)
- [X] [AssertTrue](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/AssertTrue.html)
- [ ] [DecimalMax](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/DecimalMax.html) - Not yet implemented
- [ ] [DecimalMin](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/DecimalMin.html) - Not yet implemented
- [ ] [Digits](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/Digits.html) - Not yet implemented
- [ ] [Email](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/Email.html) - Not yet implemented
- [ ] [Future](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/Future.html) - Not yet implemented
- [ ] [FutureOrPresent](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/FutureOrPresent.html) - Not yet implemented
- [X] [Max](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/Max.html)
- [X] [Min](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/Min.html)
- [X] [Negative](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/Negative.html)
- [X] [NegativeOrZero](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/NegativeOrZero.html)
- [X] [NotBlank](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/NotBlank.html)
- [X] [NotEmpty](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/NotEmpty.html)
- [X] [NotNull](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/NotNull.html)
- [ ] [Null](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/Null.html) - Not yet implemented
- [ ] [Past](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/Past.html) - Not yet implemented
- [ ] [PastOrPresent](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/PastOrPresent.html) - Not yet implemented
- [X] [Pattern](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/Pattern.html)
  - No transformation is made to the regex pattern string. Keep in mind that the Java and
    JavaScript regex engines are not identical. The only flags supported are:
    - CASE_INSENSITIVE (`/i`)
    - MULTILINE (`/m`)
    - DOTALL (`/s`)
  - All other flags are ignored
- [X] [Positive](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/Positive.html)
- [X] [PositiveOrZero](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/PositiveOrZero.html)
- [X] [Size](https://javadoc.io/doc/jakarta.validation/jakarta.validation-api/latest/jakarta/validation/constraints/Size.html)
