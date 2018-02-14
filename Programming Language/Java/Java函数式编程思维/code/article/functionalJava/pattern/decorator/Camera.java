package article.functionalJava.pattern.decorator;

import java.awt.*;

public class Camera implements Equipment {
    private Color color;

    public Camera(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return this.color;
    }
}
