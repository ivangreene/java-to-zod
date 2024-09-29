import { z as zod } from 'zod';

export const TestPersonClassSchema = zod.object({
  id: zod.number().int().optional().nullable(),
  dateOfBirth: zod.date().optional().nullable(),
  fName: zod.string().regex(/\S/, { message: 'Must not be blank' }).min(1, { message: 'Please supply a name.' }),
  lName: zod.string().regex(/\S/, { message: 'Must not be blank' }).min(1, { message: 'Please supply a name.' }),
});

export const TestPersonRecordSchema = zod.object({
  id: zod.number().int().optional().nullable(),
  dateOfBirth: zod.date().optional().nullable(),
  fName: zod.string().regex(/\S/, { message: 'Must not be blank' }).min(1, { message: 'Please supply a name.' }),
  lName: zod.string().regex(/\S/, { message: 'Must not be blank' }).min(1, { message: 'Please supply a name.' }),
});
