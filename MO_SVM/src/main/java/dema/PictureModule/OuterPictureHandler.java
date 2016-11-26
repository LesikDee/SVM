package dema.PictureModule;

import dema.GeometryModule.*;
import dema.GeometryModule.Point;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Администратор on 26.11.2016.
 */
public class OuterPictureHandler extends JComponent {
    private String filename = "Paris.JPG";
    public BufferedImage image;
    private int imageWidth;
    private int imageHeight;
    boolean isItRead = true;

    public OuterPictureHandler(Plane hyperPlane) {
        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
            isItRead = false;
        }
        if (isItRead) {
            imageWidth = image.getWidth(this);
            imageHeight = image.getHeight(this);
            int[] pixels = new int[imageWidth * imageHeight];
            for (int i = 0; i < imageWidth; i++) {
                for (int j = 0; j < imageHeight; j++) {
                    Color color = new Color(image.getRGB(i, j));
                    Point point = new Point(color.getBlue(), color.getRed(), color.getGreen());
                    if (HyperPlane.pointPosition(point, hyperPlane) > 0)
                        image.setRGB(i, j, 0);
                    else
                        image.setRGB(i, j, -1);
                }
            }
            try {
                ImageIO.write(image, "jpg", new File("BW" + filename));//read(new File(filename))

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
