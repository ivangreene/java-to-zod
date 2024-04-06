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
    givenName: zod.string(),
    surname: zod.string(),
    age: zod.number().int().positive(),
    accountType: zod.enum(['PREMIUM', 'STANDARD', 'BASIC']),
});
```

Goes well with
[typescript-generator](https://github.com/vojtechhabarta/typescript-generator),
and shares many of the same configuration options
for scanning source classes.
