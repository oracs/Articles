package article.functionalJava.pattern.command;

import java.util.stream.Stream;

public class Robot {
    public static void move(Command... commands) {
        for (Command command : commands) {
            command.execute();
        }
    }

    public static void flexibleMove(Runnable... commands) {
        Stream.of(commands).forEach(Runnable::run);
    }

    public static void main(String[] args) {
        Robot.move(new Forward(), new Right());
        Robot.flexibleMove(Robot::forward, Robot::right);
    }

    public static void forward() {
        System.out.println("go forward");
    }

    public static void right() {
        System.out.println("go right");
    }
}
