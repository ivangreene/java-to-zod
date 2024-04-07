package sh.ivan.zod.schema.attribute;

import com.google.common.collect.Sets;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class RegexAttribute implements Attribute {
    private final String regex;
    private final Set<Pattern.Flag> flags;

    public RegexAttribute(String regex, Pattern.Flag... flags) {
        this.regex = regex;
        this.flags = Sets.newEnumSet(List.of(flags), Pattern.Flag.class);
    }

    @Override
    public String zodMethod() {
        return "regex(/^" + escapeRegex() + "$/" + flags() + ")";
    }

    private String escapeRegex() {
        return regex.replaceAll("(/)", "\\\\$0");
    }

    private String flags() {
        return flags.stream().map(JS_FLAGS::get).filter(Objects::nonNull).collect(Collectors.joining());
    }

    // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_expressions#advanced_searching_with_flags
    private static final Map<Pattern.Flag, String> JS_FLAGS = Map.of(
            Pattern.Flag.CASE_INSENSITIVE, "i",
            Pattern.Flag.MULTILINE, "m",
            Pattern.Flag.DOTALL, "s");
}
