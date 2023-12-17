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

    test('should support NotEmpty String', () => {
        const lastName = PersonSchema.fields.lastName;
        expect(lastName.isValidSync('Foo')).toBe(true);
        expect(lastName.isValidSync(' \t\r\n')).toBe(true);
        expect(lastName.isValidSync(' ')).toBe(true);
        expect(lastName.isValidSync('\t')).toBe(true);
        expect(lastName.isValidSync('\n')).toBe(true);
        expect(lastName.isValidSync(null)).toBe(false);
        expect(lastName.isValidSync('')).toBe(false);
    });

    test('should support NotNull String', () => {
        const job = PersonSchema.fields.job;
        expect(job.isValidSync('Foo')).toBe(true);
        expect(job.isValidSync('')).toBe(true);
        expect(job.isValidSync(' ')).toBe(true);
        expect(job.isValidSync(null)).toBe(false);
    });

    test('should validate a valid person', () => {
        expect(PersonSchema.isValidSync({
            id: '389ba84f-0c10-41f6-9df5-86faf0ccae11', firstName: 'John', lastName: 'Doe', job: 'Human'
        })).toBe(true);
        expect(PersonSchema.isValidSync({
            firstName: 'John', lastName: 'Doe', job: 'Human'
        })).toBe(true);
    });

    test('should invalidate on missing NotEmpty/NotBlank/NotNull fields', () => {
        expect(PersonSchema.isValidSync({
            firstName: 'John', lastName: 'Doe'
        })).toBe(false);
        expect(PersonSchema.isValidSync({
            lastName: 'Doe', job: 'Human'
        })).toBe(false);
        expect(PersonSchema.isValidSync({
            firstName: 'John', job: 'Human'
        })).toBe(false);
    });
});
