package dema.PictureModule;

import javax.swing.*;
public class ImageFrame extends JFrame
{
    public ImageFrame()
    {
        setTitle("ImageTest");
        // Добавление компонента к фрейму.
        ImageMain mainComponent = new ImageMain();
        add(mainComponent);
       // add(BMComponent);
        setSize(mainComponent.image.getWidth(this)*2, mainComponent.image.getHeight(this));
    }
    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;
}