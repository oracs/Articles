package article.functionalJava.pattern.decorator;

import org.junit.Test;

import java.awt.*;

public class TestCamera {
    @Test
    public void test_camera_filter() throws Exception {
        Equipment decoratedCamera = new DarkerFilter(
                                        new BrighterFilter(
                                            new Camera(new Color(155, 120, 30))));
        System.out.println(decoratedCamera.getColor());
    }
}
