
import {validTestPersonRecord, invalidTestPersonRecord} from "./test_literals";
import {TestPersonRecordSchema} from "./schemas_snapshot";


describe('TestPersonRecordSchema', () => {
    it('should validate a correct object', () => {

        const result = TestPersonRecordSchema.safeParse(validTestPersonRecord);
        expect(result.success).toBe(true);
    });

    it('should fail to validate an incorrect object', () => {

        const result = TestPersonRecordSchema.safeParse(invalidTestPersonRecord);
        expect(result.success).toBe(false);
    });
});
