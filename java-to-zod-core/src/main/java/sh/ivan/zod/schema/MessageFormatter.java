package sh.ivan.zod.schema;

public final class MessageFormatter {
    private MessageFormatter() {}

    public static String addMessageToMethod(String method, String message) {
        return method.replaceFirst("([^(])\\)$", "$1, { message: " + quoteMessage(message) + " })")
                .replaceFirst("\\(\\)$", "({ message: " + quoteMessage(message) + " })");
    }

    private static String quoteMessage(String message) {
        return "'" + message.replace("\\", "\\\\\\\\").replace("'", "\\\\'") + "'";
    }
}
