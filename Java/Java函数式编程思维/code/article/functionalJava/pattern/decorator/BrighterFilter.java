package article.functionalJava.pattern.decorator;

import java.awt.*;

public class BrighterFilter extends FilterDecorator {
    public BrighterFilter(Equipment equipment) {
        super(equipment);
    }

    @Override
    public Color getColor() {
        return equipment.getColor().brighter();
    }
}
