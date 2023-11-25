const { PersonSchema, AddressSchema } = require('./schemas');

describe('using a reference', () => {
    test('should work', () => {
        expect(PersonSchema.fields.address).toEqual(AddressSchema);
    });
});
