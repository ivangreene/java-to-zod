const yup = require('yup');

exports.PersonSchema = yup.object({ id: yup.string().nullable().uuid(), firstName: yup.string().matches(/[^\s]/), lastName: yup.string().matches(/[^\s]/), });
