import matrix.Matrix;

import java.util.logging.Logger;

public class TestSpace {

    private static final Logger log = Logger.getLogger(TestSpace.class.getName());

    /**
     * Тестим вывод матрицы в файл fileName
     *
     * @param fileName - файл, куда будем выводить матрицы
     */
    public static void testOutputMatrix(String fileName) {

        double arr[][] = {{0, 1, 2, 10.0}, {1, 2, 3, 4}, {0, 10, 2, 3}}; // Инициализировал массив двумерный
        Matrix m = new Matrix(arr); //создал матрицу, заполненную как и массив arr
        Matrix.writeMatrixToFile(m, fileName);
        log.info("Successful write matrix in file!");//напишет это, если запись пройдет корректно

    }

    /**
     * Тестим работоспособность сложения матриц
     *
     * @param fileName - файл, куда произведем запись по окончании умножения
     */
    public static void testMultMatrix(Matrix m1, Matrix m2, String fileName) {
        try {
            Matrix.writeMatrixToFile(m1,"A.txt");
            Matrix.writeMatrixToFile(m2,"B.txt");

            Matrix mMult = Matrix.addTwoMatrix(m1, m2);
            Matrix.writeMatrixToFile(mMult, fileName);//обернул в try, тк может кинуть ошибку
            log.info("Successful multiplication and write matrix!");//напишет это, если умножение и запись пройдет корректно
        } catch (Exception e) {
            log.warning(e.getMessage() + "\nFail in multipicatio matrix");
            System.err.println("Fail in multipicatio matrix!");//напишет это, если пройдет некорректно, причем в stderr консоли
        }

    }

    private static void testReadFromFile(String filename) {
        Matrix m = Matrix.readFromFile(filename);
        System.out.print(m.getSizeRow() + " ");//число строк записываемой матрицы
        System.out.print(m.getSizeColumn() + "\n");//число столбцов
        for (int i = 0; i < m.getSizeRow(); i++) {
            for (int j = 0; j < m.getSizeColumn(); j++) {
                System.out.print(m.getElement(i, j) + " "); // записывем m[i][j] элемент матрицы, приведенной к стрингу
            }
            System.out.print("\n");//Перенесем на следующую строку когда все элементы в строке уже записаны
        }
        System.out.flush();
        log.info("Чтение из файла произошла успешно");
    }

    /**
     * функция для тестирования заполнения матриц рандомом
     *
     * @param fileName - файл, куда будем выводить результат
     */
    public static void testfillRandMatrix(String fileName) {
        Matrix m = new Matrix(2, 3);
        m.fillRandMatrix();
        Matrix.writeMatrixToFile(m, fileName);
    }

    /**
     * точка входа
     */
    public static void main(String[] args) {//точка входа в программу
        Matrix m1 = new Matrix(3, 4);
        m1.fillRandMatrix();
        Matrix m2 = new Matrix(4, 5);
        m2.fillRandMatrix();
        Matrix m3 = new Matrix(5, 1);

        //testOutputMatrix("B.txt");
        //testReadFromFile("A.txt");
        testMultMatrix(m1, m2, "C.txt"); // должен корректно работать
        //testMultMatrix(m2, m3, "C.txt"); // должен завалиться из-за несовпадения размерности
        //testfillRandMatrix("C.txt");


    }
}

