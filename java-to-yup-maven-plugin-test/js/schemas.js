const yup = require('yup');

const AddressSchema = yup.object({
  street: yup.string().nullable(),
  city: yup.string().nullable(),
  country: yup.string().nullable(),
  postalCode: yup.string().nullable(),
});
exports.AddressSchema = AddressSchema;

const BooleanHolderSchema = yup.object({
  mustBeTrue: yup.boolean().nullable().isTrue(),
  mustBeFalse: yup.boolean().isFalse(),
  boxed: yup.boolean().nullable(),
  primitive: yup.boolean(),
});
exports.BooleanHolderSchema = BooleanHolderSchema;

const NumberHolderSchema = yup.object({
  maxed: yup.number().nullable().integer().max(300),
  minned: yup.number().integer().min(100),
  negative: yup.number().nullable().integer().negative(),
  positive: yup.number().nullable().integer().positive(),
  negativeOrZero: yup.number().nullable().max(0),
  positiveOrZero: yup.number().min(0),
});
exports.NumberHolderSchema = NumberHolderSchema;

const PersonSchema = yup.object({
  id: yup.string().nullable().uuid(),
  firstName: yup.string().matches(/\S/),
  lastName: yup.string().required(),
  job: yup.string(),
  address: AddressSchema,
});
exports.PersonSchema = PersonSchema;
