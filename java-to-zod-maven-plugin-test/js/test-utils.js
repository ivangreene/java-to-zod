export function isValid(schema, value) {
    try {
        schema.parse(value);
        return true;
    } catch (e) {
        // console.error(e);
        return false;
    }
}

export function getErrorMessages(schema, value) {
    try {
        schema.parse(value);
        return [];
    } catch (e) {
        return e.errors.map(error => error.message);
    }
}
