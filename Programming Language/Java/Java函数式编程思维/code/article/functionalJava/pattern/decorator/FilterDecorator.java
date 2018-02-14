package article.functionalJava.pattern.decorator;

import java.awt.*;

public abstract class FilterDecorator implements Equipment {
    protected Equipment equipment;

    public FilterDecorator(Equipment equipment) {
        this.equipment = equipment;
    }

    public abstract Color getColor();
}
