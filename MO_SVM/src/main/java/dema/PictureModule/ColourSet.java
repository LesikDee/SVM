package dema.PictureModule;

public class ColourSet {
    MyRGB[][] rgbArr;
    public ColourSet(int width, int height){
        rgbArr = new MyRGB[width][height];
        for (int x = 0; x < width; ++x)
            for (int y = 0; y < height; ++y)
                rgbArr[x][y] = new MyRGB();
    }
    public class MyRGB {
        public int redC ;// from 0 to 255
        public int greenC;
        public int blueC ;
        public int belonging; //1 or -1
        boolean isItSV = true;
    }
}
