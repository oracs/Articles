package article.functionalJava.pattern.command;

public class Right implements Command {
    @Override
    public void execute() {
        System.out.println("go right");
    }
}
