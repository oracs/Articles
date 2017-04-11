package article.functionalJava.pattern.strategy;

public class UppercaseFormatter implements Formatter {
    @Override
    public String format(String text) {
        return text.toUpperCase();
    }
}
