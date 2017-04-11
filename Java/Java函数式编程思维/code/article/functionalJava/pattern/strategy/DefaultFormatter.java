package article.functionalJava.pattern.strategy;

public class DefaultFormatter implements Formatter {
    @Override
    public String format(String text) {
        return text;
    }
}
