package article.functionalJava.pattern.decorator.functional;

import java.awt.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Camera {
    private Color color;
    private Stream<Function<Color, Color>> filterStream;

    public Camera(Color color, Function<Color, Color>... filters) {
        this.color = color;
        this.filterStream = Stream.of(filters);
    }

    public Color getColor() {
        Function<Color, Color> composedFilter = filterStream.reduce(
                (filter, next) -> filter.compose(next))
                    .orElse(color -> color);
        return composedFilter.apply(this.color);
    }

    public static void main(String[] args) {
        Camera camera = new Camera(new Color(155, 120, 30), Color::brighter, Color::darker);
        camera.getColor();
    }
}
