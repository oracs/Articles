package article.functionalJava.pattern.strategy.functional;

import java.util.function.UnaryOperator;

public class AdvancedWords {
    private String text;
    private UnaryOperator<String> formatter;

    public AdvancedWords(String text) {
        this.text = text;
        this.formatter = s -> s;
    }

    public void setFormatter(UnaryOperator<String> formatter) {
        this.formatter = formatter;
    }

    public String output() {
        return formatter.apply(text);
    }

    public static void main(String[] args) {
        AdvancedWords words = new AdvancedWords("Hello, World");
        String defaultText = words.output();
        words.setFormatter(String::toUpperCase);
        String upperText = words.output();
        System.out.println(defaultText + ", " + upperText);
    }
}
