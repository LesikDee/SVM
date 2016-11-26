package dema.PictureModule;

import dema.AlgebraModule.*;
import dema.GeometryModule.*;
import dema.PictureModule.ColourSet;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class ImageMain extends JComponent {

    public BufferedImage image, imagebw;
    private int imageWidth;
    private int imageHeight;
    private ColourSet newSet;
    private MathSet mathSet;
    private String filename = "os.JPG";
    private String filenamebw = "osbw.JPG";//обучающая выборка
    public Plane plane = new Plane();
    boolean isItRead = true;
    public ImageMain() {
        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
            boolean isItRead = false;
        }
        try {
            imagebw = ImageIO.read(new File(filenamebw));
        } catch (IOException e) {
            e.printStackTrace();
            boolean isItRead = false;
        }
        if (isItRead == false) return;
        imageWidth = image.getWidth(this);
        imageHeight = image.getHeight(this);
        newSet = new ColourSet(imageWidth, imageHeight);
        int i, j;
        Color color;
        for (i = 0; i < imageWidth; i++)
            for (j = 0; j < imageHeight; j++) {
                color = new Color(image.getRGB(i, j));
                newSet.rgbArr[i][j].blueC = color.getBlue();
                newSet.rgbArr[i][j].greenC = color.getGreen();
                newSet.rgbArr[i][j].redC = color.getRed();
            }
        determPixelColor();

        optimizationSVM4();//main opt
        optimizationSVM();
        optimizationSVM2(1);
        optimizationSVM2(-1);
        optimizationSVM3();//прорежаем!
        //System.out.println("kol 1 " + kolSVP(1) +"::::" + " kol -1 " + kolSVP(-1));
        //printPoints();
        createMathSet(kolSVP(1)+ kolSVP(-1));
        LagrangSolution solve = new LagrangSolution(mathSet);
        solve.maker();
        plane.A = solve.W[0]; plane.B = solve.W[1]; plane.C = solve.W[2]; plane.D = solve.b;
        OuterPictureHandler out = new OuterPictureHandler(plane);
    }

    public void printPoints() {
        System.out.println("1");
        for (int i = 0; i < imageWidth; i++)
            for (int j = 0; j < imageHeight; j++) {
                if ((newSet.rgbArr[i][j].belonging == 1) && (newSet.rgbArr[i][j].isItSV == true)) {
                    System.out.print(newSet.rgbArr[i][j].blueC + " " + newSet.rgbArr[i][j].redC + " " + newSet.rgbArr[i][j].greenC);
                    System.out.println();
                }
            }
        System.out.println("-1////////////////////////////////////////////");
        for (int i = 0; i < imageWidth; i++)
            for (int j = 0; j < imageHeight; j++) {
                if ((newSet.rgbArr[i][j].belonging == -1) && (newSet.rgbArr[i][j].isItSV == true)) {
                    System.out.print(newSet.rgbArr[i][j].blueC + " " + newSet.rgbArr[i][j].redC + " " + newSet.rgbArr[i][j].greenC);
                    System.out.println();
                }
            }

    }

    public void determPixelColor() {
        int i, j;
        Color color;
        for (i = 0; i < imageWidth; i++)
            for (j = 0; j < imageHeight; j++) {
                color = new Color(imagebw.getRGB(i, j));
                if (color.getBlue() == 0) //(пиксель чёрный)
                    newSet.rgbArr[i][j].belonging = 1;
                else
                    newSet.rgbArr[i][j].belonging = -1;
            }
    }

    public int kolSVP(int b) {
        int k = 0;
        for (int i = 0; i < imageWidth; i++)
            for (int j = 0; j < imageHeight; j++)
                if ((newSet.rgbArr[i][j].isItSV == true) && (newSet.rgbArr[i][j].belonging == b))
                    k++;
        return k;
    }

    public void optimizationSVM() {//оптимизация, нахождение крайних(опроных) точек
        for (int p1x = 0; p1x < imageWidth - 1; p1x++)
            for (int p1y = 0; p1y < imageHeight - 1; p1y++)
                for (int p2x = p1x + 1; p2x < imageWidth; p2x++)
                    for (int p2y = p1y + 1; p2y < imageHeight; p2y++)
                        if ((newSet.rgbArr[p1x][p1y].belonging == 1) && (newSet.rgbArr[p1x][p1y].blueC < newSet.rgbArr[p2x][p2y].blueC) &&
                                (newSet.rgbArr[p1x][p1y].redC < newSet.rgbArr[p2x][p2y].redC) &&
                                (newSet.rgbArr[p1x][p1y].greenC < newSet.rgbArr[p2x][p2y].greenC)) {
                            newSet.rgbArr[p1x][p1y].isItSV = false;
                            break;
                        }
        for (int p1x = 0; p1x < imageWidth - 1; p1x++)
            for (int p1y = 0; p1y < imageHeight - 1; p1y++)
                for (int p2x = p1x + 1; p2x < imageWidth; p2x++)
                    for (int p2y = p1y + 1; p2y < imageHeight; p2y++)
                        if ((newSet.rgbArr[p1x][p1y].belonging == -1) && (newSet.rgbArr[p1x][p1y].blueC > newSet.rgbArr[p2x][p2y].blueC) &&
                                (newSet.rgbArr[p1x][p1y].redC > newSet.rgbArr[p2x][p2y].redC) &&
                                (newSet.rgbArr[p1x][p1y].greenC > newSet.rgbArr[p2x][p2y].greenC)) {
                            newSet.rgbArr[p1x][p1y].isItSV = false;
                            break;
                        }
    }

    public void optimizationSVM2(int b) {//b= 1/ b=-1
        double E = 0.00001;
        int maxIB = 0, maxJB = 0, maxIR = 0, maxJR = 0, maxIG = 0, maxJG = 0, c = 0;
        boolean fstB = true, fstG = true, fstR = true;
        if (b == -1) c = 255;
        for (int p1x = 0; p1x < imageWidth; p1x++)
            for (int p1y = 0; p1y < imageHeight; p1y++) {
                if (newSet.rgbArr[p1x][p1y].isItSV == true) {
                    if ((newSet.rgbArr[p1x][p1y].belonging == b) && ((fstB == true) ||
                            (b * newSet.rgbArr[p1x][p1y].blueC > b * newSet.rgbArr[maxIB][maxJB].blueC)) ) {
                        maxIB = p1x;
                        maxJB = p1y;
                        fstB = false;
                    }
                    if ((newSet.rgbArr[p1x][p1y].belonging == b) && ((fstR == true) ||
                            (b * newSet.rgbArr[p1x][p1y].redC > b * newSet.rgbArr[maxIR][maxJR].redC))) {
                        maxIR = p1x;
                        maxJR = p1y;
                        fstR = false;
                    }
                    if ((newSet.rgbArr[p1x][p1y].belonging == b) && ((fstG == true) ||
                            (b * newSet.rgbArr[p1x][p1y].greenC > b * newSet.rgbArr[maxIG][maxJG].greenC))) {
                        maxIG = p1x;
                        maxJG = p1y;
                        fstG = false;
                    }
                }
            }
        Point point1 = new Point(newSet.rgbArr[maxIB][maxJB].blueC, newSet.rgbArr[maxIB][maxJB].greenC, newSet.rgbArr[maxIB][maxJB].redC);
        Point point2 = new Point(newSet.rgbArr[maxIR][maxJR].blueC, newSet.rgbArr[maxIR][maxJR].greenC, newSet.rgbArr[maxIR][maxJR].redC);
        Point point3 = new Point(newSet.rgbArr[maxIG][maxJG].blueC, newSet.rgbArr[maxIG][maxJG].greenC, newSet.rgbArr[maxIG][maxJG].redC);
        Plane plane = new Plane();
        plane = HyperPlane.getPlane(point1, point2, point3);
        for (int p4x = 0; p4x < imageWidth; p4x++)
            for (int p4y = 0; p4y < imageHeight; p4y++) {
                if ((newSet.rgbArr[p4x][p4y].belonging == b) && (newSet.rgbArr[p4x][p4y].isItSV == true)) {
                    Point point4 = new Point(newSet.rgbArr[p4x][p4y].blueC, newSet.rgbArr[p4x][p4y].greenC, newSet.rgbArr[p4x][p4y].redC);
                    if (b * HyperPlane.pointPosition(point4, plane) > b * E)
                        newSet.rgbArr[p4x][p4y].isItSV = false;
                }
            }
        newSet.rgbArr[maxIB][maxJB].isItSV = true;
        newSet.rgbArr[maxIR][maxJR].isItSV = true;
        newSet.rgbArr[maxIG][maxJG].isItSV = true;

    }

    public void optimizationSVM3() {
        int koef = 3;
        int prored = 0, k_minus = 0, k_plus = 0;
        for (int p1x = 0; p1x < imageWidth; p1x++)
            for (int p1y = 0; p1y < imageHeight; p1y++) {
                if (newSet.rgbArr[p1x][p1y].isItSV == true) {
                    if (newSet.rgbArr[p1x][p1y].belonging == 1) {
                        if ((k_plus > 2) && (prored % koef != 0))
                            newSet.rgbArr[p1x][p1y].isItSV = false;
                        k_plus++;
                    } else if (newSet.rgbArr[p1x][p1y].belonging == -1) {
                        if ((k_minus > 2) && (prored % koef != 0))
                            newSet.rgbArr[p1x][p1y].isItSV = false;
                        k_minus++;
                    }
                }
                prored++;
            }
    }

    public void optimizationSVM4(){
        double l1 = arrangeDistance(1), l2 = arrangeDistance(-1);
        double r1 = l1 - (l1 - (255 - l2))/2, r2 = 255 - r1;
        for (int p1x = 0; p1x < imageWidth; p1x++)
            for (int p1y = 0; p1y < imageHeight; p1y++) {
                if (newSet.rgbArr[p1x][p1y].isItSV == true) {
                    Point point1 = new Point(newSet.rgbArr[p1x][p1y].blueC, newSet.rgbArr[p1x][p1y].greenC, newSet.rgbArr[p1x][p1y].redC);
                    if (newSet.rgbArr[p1x][p1y].belonging == 1) {
                        Point point2 = new Point(0, 0, 0);
                        if (HyperPlane.pointToPointDistance(point1, point2) > r1)
                            newSet.rgbArr[p1x][p1y].isItSV = false;
                    } else {
                        Point point2 = new Point(255, 255, 255);
                        if (HyperPlane.pointToPointDistance(point1, point2) > r2)
                            newSet.rgbArr[p1x][p1y].isItSV = false;
                    }
                }
            }
    }

    public double arrangeDistance(int b) {
        double arrangeKvadr = 0;
        int v = 0, n = kolSVP(b);
        if (b == -1)
            v = 255;
        Point point1 = new Point(v, v, v);
        for (int p1x = 0; p1x < imageWidth; p1x++)
            for (int p1y = 0; p1y < imageHeight; p1y++) {
                if (newSet.rgbArr[p1x][p1y].belonging == b) {
                    Point point2 = new Point(newSet.rgbArr[p1x][p1y].blueC, newSet.rgbArr[p1x][p1y].greenC, newSet.rgbArr[p1x][p1y].redC);
                    arrangeKvadr += Math.pow(HyperPlane.pointToPointDistance(point1, point2), 2) / n;
                }
            }

        return Math.pow(arrangeKvadr,0.5);
    }

    public void createMathSet(int k) {
        int p = 0;
        mathSet = new MathSet(k, 3);
        int imageWidth = imagebw.getWidth(this);
        int imageHeight = imagebw.getHeight(this);
        for (int i = 0; i < imageWidth; i++)
            for (int j = 0; j < imageHeight; j++)
                if (newSet.rgbArr[i][j].isItSV == true) {
                    mathSet.setArr[p].x[0] = newSet.rgbArr[i][j].blueC;
                    mathSet.setArr[p].x[1] = newSet.rgbArr[i][j].redC;
                    mathSet.setArr[p].x[2] = newSet.rgbArr[i][j].greenC;
                    mathSet.setArr[p].y = newSet.rgbArr[i][j].belonging;
                    p++;
                }
    }

    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
        g.drawImage(imagebw, image.getWidth(this), 0, null);
    }
}
