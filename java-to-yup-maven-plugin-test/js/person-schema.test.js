const { PersonSchema } = require('./schemas');

describe('PersonSchema', () => {
    test('should support nullable UUID', () => {
        const id = PersonSchema.fields.id;
        expect(id.isValidSync('389ba84f-0c10-41f6-9df5-86faf0ccae11')).toBe(true);
        expect(id.isValidSync(null)).toBe(true);
        expect(id.isValidSync('389ba84f-0c10-41f6-9df5-86faf0ccae1')).toBe(false);
        expect(id.isValidSync('1000')).toBe(false);
    });

    test('should support NotBlank String', () => {
        const firstName = PersonSchema.fields.firstName;
        expect(firstName.isValidSync('Foo')).toBe(true);
        expect(firstName.isValidSync(null)).toBe(false);
        expect(firstName.isValidSync('')).toBe(false);
        expect(firstName.isValidSync(' \t\r\n')).toBe(false);
    });

    test('should validate a valid person', () => {
        expect(PersonSchema.isValidSync({
            id: '389ba84f-0c10-41f6-9df5-86faf0ccae11', firstName: 'John', lastName: 'Doe'
        })).toBe(true);
    });

    test('should invalidate an invalid person', () => {
        expect(PersonSchema.isValidSync({
            id: '1000', firstName: ' ', lastName: null
        })).toBe(false);
    });
});
