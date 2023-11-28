const yup = require('yup');

const BooleanHolderSchema = yup.object({
  mustBeTrue: yup.boolean().nullable().isTrue(),
  mustBeFalse: yup.boolean().defined().isFalse(),
  boxed: yup.boolean().nullable(),
  primitive: yup.boolean().defined(),
});
exports.BooleanHolderSchema = BooleanHolderSchema;

const NumberHolderSchema = yup.object({
  maxed: yup.number().nullable().integer().max(300),
  minned: yup.number().defined().integer().min(100),
  negative: yup.number().nullable().integer().negative(),
  positive: yup.number().nullable().integer().positive(),
  negativeOrZero: yup.number().nullable().max(0),
  positiveOrZero: yup.number().defined().min(0),
  negativeIntegers: yup.array().of(yup.number().defined().integer().negative()).nullable(),
});
exports.NumberHolderSchema = NumberHolderSchema;

const PersonSchema = yup.object({
  id: yup.string().nullable().uuid(),
  firstName: yup.string().defined().matches(/\S/),
  lastName: yup.string().required(),
  job: yup.string().defined(),
  child: yup.lazy(() => PersonSchema.default(undefined).nullable()),
});
exports.PersonSchema = PersonSchema;
