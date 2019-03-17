import matrix.Matrix;

public class TestSpace {
    static String fileName = "B.txt"; //два слеша - магия после копирования

    /**
     * Тестим вывод матрицы в файл fileName
     */
    public static void testOutputMatrix() {

        double arr[][] = {{0, 1, 2, 10.0}, {1, 2, 3, 4}, {0, 10, 2, 3}}; // Инициализировал массив двумерный
        Matrix m = new Matrix(arr); //создал матрицу, заполненную как и массив arr
        try {
            Matrix.writeMatrixToFile(m, fileName);
            System.out.println("Successful write matrix in file!");//напишет это, если запись пройдет корректно
        } catch (Exception e) {
            System.err.println("Fail write matrix in file!");//напишет это, если запись пройдет некорректно, причем в stderr консоли
        }
    }

    /**
     * Тестим работоспособность сложения матриц
     */
    public static void testAdditionMatrix(Matrix m1, Matrix m2) {
        try {
            Matrix mSum = Matrix.multTwoMatrix(m1, m2);
            Matrix.writeMatrixToFile(mSum, fileName);//обернул в try, тк может кинуть ошибку
            System.out.println("Successful addition and write matrix!");//напишет это, если сложение и запись пройдет корректно
        } catch (Exception e) {
            System.err.println("Fail!");//напишет это, если пройдет некорректно, причем в stderr консоли
        }

    }

    private static void testReadFromFile() {
        Matrix m = Matrix.readFromFile("A.txt");
        System.out.print(m.getSizeRow() + " ");//число строк записываемой матрицы
        System.out.print(m.getSizeColumn() + "\n");//число столбцов
        for (int i = 0; i < m.getSizeRow(); i++) {
            for (int j = 0; j < m.getSizeColumn(); j++) {
                System.out.print(m.getElement(i, j) + " "); // записывем m[i][j] элемент матрицы, приведенной к стрингу
            }
            System.out.print("\n");//Перенесем на следующую строку когда все элементы в строке уже записаны
        }
        System.out.flush();
    }

    public static void testfillRandMatrix(){
        Matrix m = new Matrix(2,3);
        m.fillRandMatrix();
        Matrix.writeMatrixToFile(m,fileName);
    }

    //создадим матрицы m1,m2,m3 для проверки
    static double arr1[][] = {{0, 1, 2, 10.0}, {1, 2, 3, 4}, {0, 10, 2, 3}}; // Инициализировал массив двумерный (3x4)
    static Matrix m1 = new Matrix(arr1); //создал матрицу, заполненную как и массив arr
    static double arr2[][] = {{3, 1, 2, 10.0}, {1, 2, 3, 4}, {0, 10, 2, 3}}; // Инициализировал массив двумерный (3x4)
    static Matrix m2 = new Matrix(arr2); //создал матрицу, заполненную как и массив arr
    static double arr3[][] = {{3, 1, 2}, {1, 2, 3}, {0, 10, 2}}; // Размерность (3x3) не совпадает размерностью с m1 и m2
    static Matrix m3 = new Matrix(arr3); //создал матрицу, заполненную как и массив arr


    public static void main(String[] args) {//точка входа в программу
        testOutputMatrix();
        //testReadFromFile();
        //testAdditionMatrix(m1,m2); // должен корректно работать
        //testAdditionMatrix(m2,m3); // должен завалиться из-за несовпадения размерности
        testfillRandMatrix();



    }



    //Matrix o = new Matrix();
    //Matrix sum = Matrix.multTwoMatrix(a, o);


}

