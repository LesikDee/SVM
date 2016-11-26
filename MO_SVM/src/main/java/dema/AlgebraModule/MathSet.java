package dema.AlgebraModule;

/**
 * Created by Администратор on 17.11.2016.
 */
public class MathSet {
    public Coordinates[] setArr;
    public MathSet(int kol,int dimKol){
        setArr = new Coordinates[kol];
        for (int j = 0; j < kol; ++j) {
            setArr[j] = new Coordinates();
            for (int i = 0; i < dimKol; ++i)
                setArr[j].x =new int[dimKol];
        }
    }
    public class Coordinates {
        public int x[] ;// from 0 to 255
        public int y; //1 or -1
    }
}
