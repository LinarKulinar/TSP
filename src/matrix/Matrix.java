package matrix;

import java.io.*;
import java.util.Random;
import java.util.logging.Logger;

public class Matrix {

    private static final Logger log = Logger.getLogger(Matrix.class.getName());
    private double[][] matrix; //private - чтобы никто не видел

    /**
     * конструктор, принимаюший параметром матрицу
     */
    public Matrix(double matrix[][]) {
        this.matrix = matrix;
    }

    /**
     * Конструктор
     *
     * @param line   строки
     * @param column столбцы
     */
    public Matrix(int line, int column) {
        matrix = new double[line][column];
    }

    /**
     * Возвращает число строк в матрице
     */
    public int getSizeRow() {
        return matrix.length;
    }

    /**
     * Возвращает число столбцов
     */
    public int getSizeColumn() {
        return matrix[0].length;
    }


    /**
     * Заполняет массив случайными значениями
     */
    public void fillRandMatrix() {
        Random rnd = new Random();
        double m[][] = new double[getSizeRow()][getSizeColumn()];
        for (int i = 0; i < getSizeRow(); i++) {
            for (int j = 0; j < getSizeColumn(); j++) {
                matrix[i][j] = rnd.nextDouble();
            }
        }
    }

    /**
     * Записываем новое значение элемента матрицы
     *
     * @param value записываемое значение
     * @param i     номер строки
     * @param j     номер столбца
     */

    public void setElement(double value, int i, int j) {
        if (i >= getSizeRow() || j >= getSizeColumn()) {
            throw new ArrayIndexOutOfBoundsException("Вышли за границы массива");
        }
        matrix[i][j] = value;
    }

    /**
     * Получаем значение элемента матрицы
     *
     * @param i номер строки
     * @param j номер столбца
     * @return значение элемента матрицы matrix[i][j]
     */

    public double getElement(int i, int j) {
        if (i >= getSizeRow() || j >= getSizeColumn()) {
            throw new ArrayIndexOutOfBoundsException("Вышли за границы массива");
        }
        return matrix[i][j];
    }

    /**
     * Метод умножает две матрицы и вызвращает результат
     *
     * @param a матрица a
     * @param b матрица b
     * @return матрица a*b, определяемая по правилам матричного умножения
     */
    public static Matrix multTwoMatrix(Matrix a, Matrix b) {
        if (a.getSizeColumn() != b.getSizeRow()) {
            throw new IllegalArgumentException("Умножать матрицы надо подходящих размерностей"); //Проверить правильность выбора ошибки
        }
        Matrix c = new Matrix(a.getSizeRow(), b.getSizeColumn());
        for (int i = 0; i < a.getSizeRow(); i++) {
            for (int j = 0; j < b.getSizeColumn(); j++) {
                for (int k = 0; k < a.getSizeColumn(); k++) {
                    c.setElement(a.matrix[i][k] + b.matrix[k][j], i, j);
                }
            }
        }
        return c;
    }

    /**
     * Метод умножает две матрицы и вызвращает результат
     *
     * @param a матрица a
     * @param b матрица b
     * @return матрица a+b, определяемая по правилам матричного сложения
     */
    public static Matrix addTwoMatrix(Matrix a, Matrix b) {
        if (a.getSizeRow() != b.getSizeRow() || a.getSizeColumn() != b.getSizeColumn()) {
            throw new IllegalArgumentException("Складывать матрицы надо совпадающих размерностей"); //Проверить правильность выбора ошибки
        }
        Matrix c = new Matrix(a.getSizeRow(), a.getSizeColumn());
        for (int i = 0; i < a.getSizeRow(); i++) {
            for (int j = 0; j < a.getSizeColumn(); j++) {
                c.setElement(a.matrix[i][j] + b.matrix[i][j], i, j);
            }
        }
        return c;
    }


    /**
     * Функция, записывающая матрицу в файл
     *
     * @param m        исходная матрица
     * @param fileName путь к файлу, в который будем записывать
     */
    public static void writeMatrixToFile(Matrix m, String fileName) {
        try {
            File file = new File(fileName);//создали объект типа "Файл" в окружении
            FileWriter outFile = null;//создали поток
            outFile = new FileWriter(file);
            outFile.write(m.getSizeRow() + " ");//число строк записываемой матрицы
            outFile.write(m.getSizeColumn() + "\n");//число столбцов
            for (int i = 0; i < m.getSizeRow(); i++) {
                for (int j = 0; j < m.getSizeColumn(); j++) {
                    outFile.write(m.getElement(i, j) + " "); // записывем m[i][j] элемент матрицы, приведенной к стрингу\
                }
                outFile.write("\n");//Перенесем на следующую строку когда все элементы в строке уже записаны
            }
            outFile.flush();
            outFile.close();//закрываем поток!
        } catch (IOException e) {
            log.warning(e.getMessage() + "\nНе удалось записать матрицу в файл");
            System.err.println("Не удалось записать матрицу в файл");
        }
    }

    /**
     * Функция считывающая матрицу из файла
     *
     * @param fileName файл, из которого будем считывать
     * @return считанная матрица
     */
    public static Matrix readFromFile(String fileName) {
        //создаем поток для чтения файла
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            //считываем из br строку, преобразуем из строчного типа в целочисленный
            String[] str1 = br.readLine().split(" ");
            int n = Integer.parseInt(str1[0]);
            int m = Integer.parseInt(str1[1]);
            if (str1.length != 2) {
                throw new IOException("Некорректный формат входных данных. (type1)");
            }
            Matrix a = new Matrix(n, m);
            for (int i = 0; i < n; i++) {
                String[] str = br.readLine().split(" "); //Считывае строку и пихаем массив с разделителем - пробелом
                if (str.length != m) {
                    throw new IOException("Неверное число чисел в i = " + i + "строке");
                }
                for (int j = 0; j < m; j++) {
                    a.setElement(Double.parseDouble(str[j]), i, j);//кладем значение в массив
                }
            }
            if (br.ready()) {
                throw new IOException("Записано слишком много данных");
            }
            return a;
        } catch (IOException e) {
            log.warning(e.getMessage() + "\nНе удалось промзвести чтение из файла");
            System.err.println("Не удалось промзвести чтение из файла");
            return null;
        }
    }

    /**
     * Функция, записывающая матрицу в поток типа @DataOutputStream
     *
     * @param m   - исходная матрица
     * @param out - поток, в который будет производиться запись
     */
    public static void writeMatrixToStream(Matrix m, DataOutputStream out) {
        //Оказывается, не надо в поток писать пробелы, чтобы потом парсить. Потоки умнее
        try {
            out.writeUTF(m.getSizeRow() + "");//число строк записываемой матрицы
            out.writeUTF(m.getSizeColumn() + "");//число столбцов
            for (int i = 0; i < m.getSizeRow(); i++) {
                for (int j = 0; j < m.getSizeColumn(); j++) {
                    out.writeUTF(m.getElement(i, j) + ""); // записывем m[i][j] элемент матрицы, приведенной к стрингу
                }
            }
            out.flush();
        } catch (IOException e) {
            log.warning(e.getMessage() + "\nНе удалось записать матрицу в поток");
            System.err.println("Не удалось записать матрицу в поток");
        }
    }

    /**
     * Функция, читающая из потока матрицу.
     *
     * @param in - исходный поток, из которого будет производиться чтение.
     * @return - прочтенная матрица. В случае некорректного ввода возвращает null.
     */

    public static Matrix readFromStream(DataInputStream in) {
        try {
            int n = Integer.parseInt(in.readUTF());
            int m = Integer.parseInt(in.readUTF());
            Matrix a = new Matrix(n, m);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    double value = Double.parseDouble(in.readUTF()); //Считываем строку
                    a.setElement(value, i, j);//кладем значение в массив
                }
            }
            return a;
        } catch (IOException | NumberFormatException e) {
            log.warning(e.getMessage() + "\nБыли считаны некорректные данные. Произошла ошибка");
            System.err.println("Были считаны некорректные данные. Произошла ошибка");
            return null;
        }
    }
}