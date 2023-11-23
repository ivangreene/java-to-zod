const { PersonSchema } = require('./schemas');

describe('PersonSchema', () => {
    test('should validate a valid person', () => {
        expect(PersonSchema.fields.id.isValidSync('389ba84f-0c10-41f6-9df5-86faf0ccae11')).toBe(true);
        expect(PersonSchema.fields.firstName.isValidSync('John')).toBe(true);
        expect(PersonSchema.fields.lastName.isValidSync('Doe')).toBe(true);
        expect(PersonSchema.isValidSync({
            id: '389ba84f-0c10-41f6-9df5-86faf0ccae11', firstName: 'John', lastName: 'Doe'
        })).toBe(true);
    });

    test('should invalidate an invalid person', () => {
        expect(PersonSchema.fields.id.isValidSync('1000')).toBe(false);
        expect(PersonSchema.fields.firstName.isValidSync(' \n\t')).toBe(false);
        expect(PersonSchema.fields.lastName.isValidSync('')).toBe(false);
        expect(PersonSchema.isValidSync({
            id: '1000', firstName: ' ', lastName: null
        })).toBe(false);
    });
});
