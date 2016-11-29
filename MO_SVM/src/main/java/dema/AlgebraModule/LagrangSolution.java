package dema.AlgebraModule;
import dema.GeometryModule.*;



public class LagrangSolution {
    public int kol;
    public int kolNegativeRoot = 0;
    public MatrixEq matrixEq;
    public MatrixEq subsidiaryMatrix;
    public double mainArrRoots[], rootsArr[];
    public double maxRootsArr[];
    public boolean helpTrueSvmArr[], firstCase = true;
    public boolean[] svmBows;
    public double F, maxF = 0, b;
    public double[] W;
    public MathSet mathset;

    private void makeSVMSystem(MatrixEq possibleMatrixEq) {
        int kolTrueSvm = 0, x, y = 0;
        for (int i = 0; i < svmBows.length; i++)
            if (svmBows[i])
                kolTrueSvm++;
        kol = kolTrueSvm;
        matrixEq = new MatrixEq(kolTrueSvm);
        for (int i = 0; i < svmBows.length; i++) {
            x = 0;
            for (int j = 0; j < svmBows.length; j++) {
                if ((svmBows[i] != false) && (svmBows[j] != false)) {
                    matrixEq.mainPath[y][x++] = possibleMatrixEq.mainPath[i][j];
                }
            }
            if (svmBows[i] != false) {
                matrixEq.rightPath[y] = possibleMatrixEq.rightPath[i];
                y++;
            }
        }


    }

    public LagrangSolution(MathSet thisSet) {
        kol = thisSet.setArr.length + 1;
        MatrixEq possibleMatrixEq = new MatrixEq(kol);
        mathset = thisSet;
        for (int i = 0; i < kol - 1; i++) {
            for (int j = 0; j < kol - 1; j++) {
                possibleMatrixEq.mainPath[i][j] = -1 * thisSet.setArr[i].y * thisSet.setArr[j].y *
                        MatrixOperations.scalarMult(thisSet.setArr[i].x, thisSet.setArr[j].x);
            }
            possibleMatrixEq.rightPath[i] = -1;
        }
        for (int i = 0; i < kol - 1; i++) {
            possibleMatrixEq.mainPath[kol - 1][i] = thisSet.setArr[i].y;
            possibleMatrixEq.mainPath[i][kol - 1] = thisSet.setArr[i].y;
        }
        possibleMatrixEq.rightPath[kol - 1] = 0;
        svmBows = MatrixOperations.rangMatrDeterm(possibleMatrixEq);
        makeSVMSystem(possibleMatrixEq);

    }

    public void KolNegativeRoot() {
        for (int i = 0; i < kol - 1; i++)
            if (mainArrRoots[i] < 0) {
                kolNegativeRoot++;
                helpTrueSvmArr[i] = false;
            } else
                helpTrueSvmArr[i] = true;

        //helpTrueSvmArr[kol - 1] = true;
    }

    public void writeRow(int rowsKol, int y) {
        int j = 0;
        for (int i = 0; i < kol - 1; i++) {
            if (helpTrueSvmArr[i] == true)
                subsidiaryMatrix.mainPath[rowsKol][j++] = matrixEq.mainPath[y][i];
        }
        subsidiaryMatrix.mainPath[rowsKol][j] = matrixEq.mainPath[y][kol - 1];
        subsidiaryMatrix.rightPath[rowsKol] = matrixEq.rightPath[y];
    }

    boolean areAllElmsPositive(double[] x) {
        boolean arePositive = true;
        for (int i = 0; i < x.length - 1; i++) {
            if (x[i] < 0) {
                arePositive = false;
                break;
            }
        }
        return arePositive;
    }

    public double toCalculateF(double[] arrRoots) {
        int y = 0, x;
        double fValue = 0, intermediateV;
        for (int i = 0; i < kol - 1; i++) {
            if (helpTrueSvmArr[i] == true)
                fValue += arrRoots[y];
            x = 0;
            for (int j = i; j < kol - 1; j++) {
                if ((helpTrueSvmArr[i] == true) && (helpTrueSvmArr[j] == true)) {
                    intermediateV = arrRoots[y] * arrRoots[x++] * matrixEq.mainPath[i][j];
                    if (i == j) intermediateV *= 0.5;
                    fValue += intermediateV;
                }
            }
            if (helpTrueSvmArr[i] == true)
                y++;
        }
        return fValue;
    }

    public void findMaxF(int rowsKol, int y) {
        if ((y != kol - 1) && (rowsKol < kol - kolNegativeRoot - 1)) {
            writeRow(rowsKol, y);
            if (rowsKol == kol - kolNegativeRoot - 2) {
                rootsArr = MatrixOperations.toSolveSystem(subsidiaryMatrix);
                if (areAllElmsPositive(rootsArr)) {
                    F = toCalculateF(rootsArr);
                    if ((firstCase) || (F > maxF)) {
                        maxRootsArr = rootsArr;
                        maxF = F;
                        firstCase = false;
                    }
                }
                rowsKol--;

            }
            findMaxF(rowsKol + 1, y + 1);
        }
        if (y < kolNegativeRoot)
            findMaxF(rowsKol, y + 1);
    }

    public void HyperplaneCalculate() {
        int x = 0;
        W = new double[mathset.setArr[0].x.length];
        for (int i = 0; i < kol - 1; i++) {
            for (int j = 0; j < mathset.setArr[0].x.length; j++) {
                if (helpTrueSvmArr[i])
                    W[j] += maxRootsArr[x] * mathset.setArr[i].x[j] * mathset.setArr[i].y;
            }
            if (helpTrueSvmArr[i])
                x++;
        }
    }
    public int firstTrueRoot(){
        int k=0, i=0;
        while((helpTrueSvmArr[i] == false) || (svmBows[i] == false)){
            k++;
            i++;
        }
        return k;
    }

    public void maker() {
        mainArrRoots = new double[matrixEq.rightPath.length];
        mainArrRoots = MatrixOperations.toSolveSystem(matrixEq);
        if (mainArrRoots != null) {
            helpTrueSvmArr = new boolean[kol - 1];
            KolNegativeRoot();
            rootsArr = new double[kol - kolNegativeRoot];
            maxRootsArr = new double[kol - kolNegativeRoot];
            subsidiaryMatrix = new MatrixEq(kol - kolNegativeRoot);
            writeRow(kol - kolNegativeRoot - 1, kol - 1);
            findMaxF(0, 0);
            System.out.println(toCalculateF(maxRootsArr));
            HyperplaneCalculate();
            b = mathset.setArr[firstTrueRoot()].y -
                    MatrixOperations.scalarMult(W, mathset.setArr[firstTrueRoot()].x);

        }
    }
}


