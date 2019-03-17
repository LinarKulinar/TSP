package matrix;

import java.io.*;
import java.util.Random;

public class Matrix {
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
     * Метод складывает значения две матрицы и вызвращает результат
     *
     * @param a матрица a
     * @param b матрица b
     * @return матрица a+b, определяемая по правилам матричного сложения
     */
    public static Matrix addTwoMatrix(Matrix a, Matrix b) {
        if (a.getSizeRow() != b.getSizeRow() || a.getSizeColumn() != b.getSizeColumn()) {
            throw new IllegalArgumentException("Складывать матрицы надо совпадающих рахмерностей"); //Проверить правильность выбора ошибки
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
        File file = new File(fileName);//создали объект типа "Файл" в окружении
        FileWriter outFile = null;//создали поток
        try {
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
            System.err.println(e.getMessage());
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
                    throw new IOException("Некорректный формат входных данных. (type2)");
                }
                for (int j = 0; j < m; j++) {
                    a.setElement(Double.parseDouble(str[j]), i, j);//кладем значение в массив
                }
            }
            if (br.ready()) {
                throw new IOException("Некорректный формат входных данных. (type3)");
            }
            return a;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static void writeMatrixToStream(Matrix m, DataOutputStream out) {
        //Оказывается, не надо в поток писать пробелы, чтобы потом парсить. Потоки умнее
        try {
            out.writeUTF(m.getSizeRow() + "");//число строк записываемой матрицы
            out.writeUTF(m.getSizeColumn() + "");//число столбцов
            for (int i = 0; i < m.getSizeRow(); i++) {
                for (int j = 0; j < m.getSizeColumn(); j++) {
                    out.writeUTF(m.getElement(i, j) + ""); // записывем m[i][j] элемент матрицы, приведенной к стрингу
                }
                //out.writeUTF("");//Перенесем на следующую строку когда все элементы в строке уже записаны
            }
            //out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static Matrix readFromStream(DataInputStream in) {
        try {
            int n = Integer.parseInt(in.readUTF());
            int m = Integer.parseInt(in.readUTF());
            Matrix a = new Matrix(n, m);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    int value = Integer.parseInt(in.readUTF()); //Считываем строку
                    a.setElement(i, j, value);//кладем значение в массив
                }
            }
            return a;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

}