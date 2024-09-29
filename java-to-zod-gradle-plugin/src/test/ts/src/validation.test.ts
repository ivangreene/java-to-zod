import { validTestPersonRecord, invalidTestPersonRecord } from "./test_literals";
import { TestPersonRecordSchema } from "./schemas_snapshot";

// Helper function to replace one field in the valid record with the invalid counterpart
function replaceField(obj: any, field: string, value: any) {
    return {
        ...obj,
        [field]: value
    };
}

describe('TestPersonRecordSchema', () => {
    it('should validate a correct object', () => {
        const result = TestPersonRecordSchema.safeParse(validTestPersonRecord);
        expect(result.success).toBe(true);
    });

    it('should fail when any field from the invalid record is used', () => {
        const fields = Object.keys(invalidTestPersonRecord);

        fields.forEach((field) => {
            const testObject = replaceField(validTestPersonRecord, field, invalidTestPersonRecord[field as keyof typeof invalidTestPersonRecord]);
            const result = TestPersonRecordSchema.safeParse(testObject);

            // Expect the validation to fail when an invalid field is substituted
            expect(result.success).toBe(false);
        });
    });
});
