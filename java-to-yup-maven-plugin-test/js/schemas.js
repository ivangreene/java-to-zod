const zod = require('zod');

const ApiResponseSchema = zod.object({
  status: zod.enum(['PASSED', 'FAILED', 'UNKNOWN']).optional().nullable(),
});
exports.ApiResponseSchema = ApiResponseSchema;

const BooleanHolderSchema = zod.object({
  mustBeTrue: zod.literal(true).optional().nullable(),
  mustBeFalse: zod.literal(false),
  boxed: zod.boolean().optional().nullable(),
  primitive: zod.boolean(),
});
exports.BooleanHolderSchema = BooleanHolderSchema;

const NumberHolderSchema = zod.object({
  maxed: zod.number().int().max(300).optional().nullable(),
  minned: zod.number().int().min(100),
  negative: zod.number().int().negative().optional().nullable(),
  positive: zod.number().int().positive().optional().nullable(),
  negativeOrZero: zod.number().max(0).optional().nullable(),
  positiveOrZero: zod.number().min(0),
  negativeIntegers: zod.array(zod.number().int().negative()).optional().nullable(),
});
exports.NumberHolderSchema = NumberHolderSchema;

const PersonSchema = zod.object({
  id: zod.string().uuid().optional().nullable(),
  firstName: zod.string().regex(/\S/),
  lastName: zod.string().min(1),
  job: zod.string(),
  child: zod.lazy(() => PersonSchema.default(undefined).optional().nullable()),
});
exports.PersonSchema = PersonSchema;
