package article.functionalJava.pattern.strategy;

public class LowercaseFormatter implements Formatter{
    @Override
    public String format(String text) {
        return text.toLowerCase();
    }
}
