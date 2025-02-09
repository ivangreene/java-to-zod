import { z as zod } from 'zod';

export const ApiResponseSchema = zod.object({
  status: zod.enum(['PASSED', 'FAILED', 'UNKNOWN']).optional().nullable(),
  received: zod.date().optional().nullable(),
});

export const BooleanHolderSchema = zod.object({
  mustBeTrue: zod.literal(true).optional().nullable(),
  mustBeFalse: zod.literal(false),
  boxed: zod.boolean().optional().nullable(),
  primitive: zod.boolean(),
});

export const BooleanHolderWithMessagesSchema = zod.object({
  mustBeTrue: zod.literal(true, { message: 'Needs to be true!' }).optional().nullable(),
  mustBeFalse: zod.literal(false, { message: 'Needs to be false!' }),
  boxed: zod.boolean().optional().nullable(),
  primitive: zod.boolean(),
});

export const NumberHolderSchema = zod.object({
  maxed: zod.number().int().max(300).optional().nullable(),
  minned: zod.number().int().min(100),
  negative: zod.number().int().negative().optional().nullable(),
  positive: zod.number().int().positive().optional().nullable(),
  negativeOrZero: zod.number().max(0).optional().nullable(),
  positiveOrZero: zod.number().min(0),
  negativeIntegers: zod.array(zod.number().int().negative()).optional().nullable(),
});

export const PersonSchema = zod.object({
  id: zod.string().uuid().optional().nullable(),
  firstName: zod.string().regex(/\S/, { message: 'cannot be blank' }),
  lastName: zod.string().min(1, { message: 'cannot be empty' }),
  job: zod.string(),
  homepage: zod.string().regex(/^https?:\/\/.*$/, { message: 'must be a valid \'URL\' (" - \\)' }).optional().nullable(),
  email: zod.string().email().optional().nullable(),
  child: zod.lazy(() => PersonSchema.optional().nullable()),
});

export const PersonExampleSchema = zod.object({
  givenName: zod.string().min(1, { message: 'must not be empty' }),
  surname: zod.string().min(1),
  age: zod.number().int().positive({ message: 'must be positive' }),
  email: zod.string().email({ message: 'must be a valid email' }).optional().nullable(),
  accountType: zod.enum(['PREMIUM', 'STANDARD', 'BASIC']),
});
