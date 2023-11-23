const yup = require('yup');

exports.NumberHolderSchema = yup.object({
  maxed: yup.number().nullable().integer().max(300),
  minned: yup.number().integer().min(100),
  negative: yup.number().nullable().integer().negative(),
  positive: yup.number().nullable().integer().positive(),
  negativeOrZero: yup.number().nullable().max(0),
  positiveOrZero: yup.number().min(0),
});

exports.PersonSchema = yup.object({
  id: yup.string().nullable().uuid(),
  firstName: yup.string().matches(/\S/),
  lastName: yup.string().matches(/\S/),
});
