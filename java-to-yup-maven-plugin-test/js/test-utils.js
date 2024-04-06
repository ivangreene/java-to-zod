exports.isValid = function isValid(schema, value) {
    try {
        schema.parse(value);
        return true;
    } catch (e) {
        // console.error(e);
        return false;
    }
}
