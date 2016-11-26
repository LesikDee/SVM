package dema.AlgebraModule;

/**
 * Created by Администратор on 22.11.2016.
 */
public class MatrixEq{
    public double mainPath[][];
    public double rightPath[];
    public MatrixEq(int kolOfColumn){
        mainPath = new double[kolOfColumn][kolOfColumn];
        rightPath = new double[kolOfColumn];
    }
}