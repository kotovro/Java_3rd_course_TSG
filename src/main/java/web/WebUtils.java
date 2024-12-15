package web;

public class WebUtils {
    public static String sanitizeOutput(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.replace("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
