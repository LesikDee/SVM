package dema;

import dema.AlgebraModule.LagrangSolution;
import dema.AlgebraModule.MathSet;
import dema.GeometryModule.*;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Tests {
    @Test
    public void test1() {
        Point p1 = new Point(1, -2, 0);
        Point p2 = new Point(2, 0, -1);
        Point p3 = new Point(0, -1, 2);
        Plane pl1 = new Plane();
        pl1 = HyperPlane.getPlane(p1, p2, p3);
        //System.out.println("%d %d %d %d ",pl1.A,pl1.B, pl1.C, pl1.D);
        System.out.println(pl1.A + " " + pl1.B + " " + pl1.C + " " + pl1.D);
    }

    @Test
    public void test2() {
        int kol = 4;
        MathSet a = new MathSet(kol,2);
        a.setArr[0].x[0] = 1; a.setArr[0].x[1] = 1; a.setArr[0].y = 1;
        a.setArr[2].x[0] = 1; a.setArr[2].x[1] = 2; a.setArr[2].y = -1;
        a.setArr[1].x[0] = 2; a.setArr[1].x[1] = 3; a.setArr[1].y = -1;
        a.setArr[3].x[0] = 3; a.setArr[3].x[1] = 1; a.setArr[3].y = 1;
        LagrangSolution solve = new LagrangSolution(a);
        solve.maker();
        for (int i = 0; i < solve.kol; i++) {
            for (int j = 0; j < solve.kol; j++)
                System.out.print(solve.matrixEq.mainPath[i][j] + " ");
            System.out.println(solve.matrixEq.rightPath[i]);
        }
    }
    @Test
    public void test3() {
        int kol = 5;
        MathSet a = new MathSet(kol,3);
        a.setArr[0].x[0] = 17; a.setArr[0].x[1] = 69;a.setArr[0].x[2] = 45; a.setArr[0].y = 1;
        a.setArr[1].x[0] = 18; a.setArr[1].x[1] = 63; a.setArr[1].x[2] = 50;a.setArr[1].y = 1;
        a.setArr[2].x[0] = 128; a.setArr[2].x[1] = 185;a.setArr[2].x[2] = 172; a.setArr[2].y = -1;
        a.setArr[3].x[0] = 118; a.setArr[3].x[1] = 222;a.setArr[3].x[2] = 204; a.setArr[3].y = -1;
        a.setArr[4].x[0] = 145; a.setArr[4].x[1] = 203;a.setArr[4].x[2] = 167; a.setArr[4].y = -1;
        LagrangSolution solve = new LagrangSolution(a);
        solve.maker();
        for (int i = 0; i < solve.kol; i++) {
            for (int j = 0; j < solve.kol; j++)
                System.out.print(solve.matrixEq.mainPath[i][j] + " ");
            System.out.println(solve.matrixEq.rightPath[i]);
        }
    }
}