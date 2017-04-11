package article.functionalJava.pattern.command;

public class Forward implements Command {
    @Override
    public void execute() {
        System.out.println("go forward");
    }
}
