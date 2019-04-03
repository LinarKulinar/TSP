package client;

import matrix.Matrix;
import server.Server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientMain {
    private static Logger log = Logger.getLogger(ClientMain.class.getName());

    public static void main(String[] args) {
        //вот эту хрень надо убрать, и из консоли запускать
        args = new String[3];
        args[0] = "A.txt";
        args[1] = "B.txt";
        args[2] = "C.txt";

        if (args.length != 3) {
            log.warning("Введено не три параметра");
            System.err.println("Введите имена файлов верно");
            return;
        }
        Matrix A = new Matrix(50, 50);
        A.fillRandMatrix();
        log.info("Сгенерировали матрицу A");
        Matrix B = new Matrix(50, 50);
        B.fillRandMatrix();
        log.info("Сгенерировали матрицу B");
        Matrix.writeMatrixToFile(A, args[0]);
        Matrix.writeMatrixToFile(B, args[1]);
        A = Matrix.readFromFile(args[0]);
        B = Matrix.readFromFile(args[1]);


        //создаем сокет клиента, с потоками для чтения и записи
        try {
            Socket socket = new Socket("127.0.0.1", Server.PORT);

            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом.
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);
            Matrix.writeMatrixToStream(A, out);
            log.info("Записали в поток матрицу A");
            Matrix.writeMatrixToStream(B, out);
            log.info("Записали в поток матрицу B");
            out.flush();
            log.info("Обе матрицы успешно отправлены");

            checkAndWriteData(in, out, args[2]);

        } catch (IOException e) {
            log.warning("Не удалось подключиться к серверу");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Метод проверяет (согласно договоренности с классом Server.java) ошибка пришла или матрица.
     * Затем метод записывает матрицу (или сообщение об ошибке) в файл fileName
     *
     * @throws IOException если будет ошибка IO (от метода readUTF())
     */

    public static void checkAndWriteData(DataInputStream in, DataOutputStream out, String fileName) throws IOException {
        String messsageErr = in.readUTF();
        if (!messsageErr.equals("\n")) {//Если лежит непустая строка ошибок
            log.warning("Матрица С не получена из-за ошибок вычисления на сервере. " + messsageErr);
            System.out.println(messsageErr + "\nПопытайтесь снова");
            //Дальше пишем в файл fileName
            File file = new File(fileName);
            FileWriter outFile = new FileWriter(file);
            ;//создали поток
            outFile.write(messsageErr + "\nПопытайтесь снова");
            outFile.close();

        } else {//Если лежит матрица
            Matrix c = Matrix.readFromStream(in);
            log.info("Матрица с результатами вычисления успешно считалась");
            Matrix.writeMatrixToFile(c, fileName);
            log.info("Матрица С успешно записана в файл " + fileName);
        }
    }

}
