package article.functionalJava.pattern.strategy;

public class Words {
    private Formatter formatter;
    private String text;

    public Words(String text) {
        this.text = text;
        this.formatter = new DefaultFormatter();
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public String output() {
        return formatter.format(text);
    }

    public static void main(String[] args) {
        String text = "Hello, World";
        Words words = new Words(text);
        String defaultText = words.output();
        words.setFormatter(new UppercaseFormatter());
        String upperText = words.output();
        System.out.println(defaultText + ", " + upperText);

    }

}
