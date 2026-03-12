package ustitch.tensormodule;

import javafx.scene.image.Image;

/**
 * Simple container for a pair of pattern Images (horizontal + vertical).
 * No external dependencies, pure JavaFX.
 */
public class ImagePatternData {
    private Image horizontalPattern;
    private Image verticalPattern;

    public ImagePatternData(Image horizontalPattern, Image verticalPattern) {
        this.horizontalPattern = horizontalPattern;
        this.verticalPattern = verticalPattern;
    }

    public Image getHorizontalPattern() {
        return horizontalPattern;
    }

    public Image getVerticalPattern() {
        return verticalPattern;
    }
}
