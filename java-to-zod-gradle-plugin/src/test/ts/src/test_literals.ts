export const validTestPersonRecord = {
    id: 123,  // valid integer
    dateOfBirth: new Date('1990-01-01'),  // valid date
    fName: 'John',  // non-blank string
    lName: 'Doe'  // non-blank string
};

export const invalidTestPersonRecord = {
    id: 'abc',  // invalid, should be a number or null
    dateOfBirth: 'not a date',  // invalid, should be a Date object or null
    fName: ' ',  // invalid, blank string doesn't meet regex and min length
    lName: ''  // invalid, empty string doesn't meet regex and min length
};
