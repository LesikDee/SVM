package dema.AlgebraModule;

import dema.AlgebraModule.MatrixEq;

/**
 * Created by Администратор on 17.11.2016.
 */
public class MatrixOperations {
    static int  kol;
    static MatrixEq matrixEq;
    static double E = 0.00001;
    public static void copyMatrix(MatrixEq m1, MatrixEq m2){
        for (int i = 0; i < kol; i++) {
            for (int j = 0; j < kol; j++)
                m1.mainPath[i][j] = m2.mainPath[i][j];
            m1.rightPath[i] =m2.rightPath[i];
        }
    }
    public static boolean[]rangMatrDeterm(MatrixEq matr){
        kol = matr.rightPath.length;
        boolean rangMatr[] = new boolean[kol];
        matrixEq = new MatrixEq(kol);
        copyMatrix(matrixEq, matr);
        int minF;
        double otn , suppEl;
        for (int i = 0; i < kol; i++) {
            rangMatr[i] = true;
            if (Math.abs(matrixEq.mainPath[i][i]) < E) {
                minF = minFinder(i);
                if (minF == -1) {
                    rangMatr[i] = false;
                } else {
                    changer(i, minF);
                }
            }
            if (rangMatr[i]) {
                otn = 1 / matrixEq.mainPath[i][i];
                for (int thisx = i; thisx < kol; thisx++)
                    matrixEq.mainPath[i][thisx] *= otn;
                for (int thisy = 0; thisy < kol; thisy++) {
                    if (thisy != i) {
                        suppEl = matrixEq.mainPath[thisy][i];
                        for (int thisx = i; thisx < kol; thisx++) {
                            matrixEq.mainPath[thisy][thisx] -= suppEl * matrixEq.mainPath[i][thisx];
                        }
                    }
                }
            }
        }
        return rangMatr;
    }
    public static double[] toSolveSystem(MatrixEq matr) {
        kol = matr.rightPath.length;
        matrixEq = new MatrixEq(kol);
        copyMatrix(matrixEq, matr);
        int minF;
        double otn , suppEl;
        for (int i = 0; i < kol; i++) {
            if (matrixEq.mainPath[i][i] == 0) {
                minF = minFinder(i);
                if( minF == -1){
                    System.out.println("Error");
                    return null;
                }
                changer(i, minFinder(i));
            }
            otn = 1 / matrixEq.mainPath[i][i];
            for (int thisx = i; thisx < kol; thisx++)
                matrixEq.mainPath[i][thisx] *= otn;
            matrixEq.rightPath[i] *= otn;
            for (int thisy = 0; thisy < kol; thisy++) {
                if (thisy != i) {
                    suppEl = matrixEq.mainPath[thisy][i];
                    for (int thisx = i; thisx < kol; thisx++) {
                        matrixEq.mainPath[thisy][thisx] -= suppEl * matrixEq.mainPath[i][thisx];
                    }
                    matrixEq.rightPath[thisy] -= suppEl * matrixEq.rightPath[i];
                }
            }
        }
        return matrixEq.rightPath;
    }

    public static int minFinder(int st) {
        int minn, i = st, j;
        double minv;
        while ((i < kol) && (Math.abs(matrixEq.mainPath[i][st]) < E))
            i++;
        if (i == kol)
            return -1;
        else {
            minv = Math.abs(matrixEq.mainPath[i][st]);
            minn = i;
            for (j = i; j < kol; j++)
                if ((Math.abs(matrixEq.mainPath[j][st]) < minv) && (matrixEq.mainPath[j][st] != 0)) {
                    minv = Math.abs(matrixEq.mainPath[j][st]);
                    minn = j;
                }
            return minn;
        }
    }
    public static double scalarMult(int[] x1, int[] x2) {
        double sum = 0;
        for (int i = 0; i < x1.length; i++)
            sum += x1[i] * x2[i];
        return sum;
    }
    public static double scalarMult(double[] x1, int[] x2) {
        double sum = 0;
        for (int i = 0; i < x1.length; i++)
            sum += x1[i] * x2[i];
        return sum;
    }
    public static void changer(int n1, int n2) {
        int i;
        double b;
        for (i = n1; i < kol; i++) {
            b = matrixEq.mainPath[n1][i];
            matrixEq.mainPath[n1][i] = matrixEq.mainPath[n2][i];
            matrixEq.mainPath[n2][i] = b;
        }
        b = matrixEq.rightPath[n1];
        matrixEq.rightPath[n1] = matrixEq.rightPath[n2];
        matrixEq.rightPath[n2] = b;
    }
}
