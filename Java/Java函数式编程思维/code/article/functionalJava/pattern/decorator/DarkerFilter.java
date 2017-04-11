package article.functionalJava.pattern.decorator;

import java.awt.*;

public class DarkerFilter extends FilterDecorator {
    public DarkerFilter(Equipment equipment) {
        super(equipment);
    }

    @Override
    public Color getColor() {
        return equipment.getColor().darker();
    }
}
