# Java to Yup

Convert POJOs to [Yup](https://github.com/jquense/yup) schemas.
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

Running the java-to-yup plugin will give you:
```js
const PersonSchema = yup.object({
    givenName: yup.string().required(),
    surname: yup.string().required(),
    age: yup.number().defined().integer().positive(),
    accountType: yup.string().defined().oneOf(['PREMIUM', 'STANDARD', 'BASIC']),
});
```

Goes well with
[typescript-generator](https://github.com/vojtechhabarta/typescript-generator),
and shares many of the same configuration options
for scanning source classes.
