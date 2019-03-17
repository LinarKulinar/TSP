package client;

import matrix.Matrix;
import server.Server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientMain {
    private static Logger log = Logger.getLogger(ClientMain.class.getName());

    public static void main(String[] args) {
        args = new String[3];
        args[0] = "client";
        args[1] = "A";
        args[2] = "B";

        if (args.length != 3) {
            System.err.println("Введите имена файлов верно");
            return;
        }
        Matrix A = new Matrix(5, 5);
        A.fillRandMatrix();
        log.info("Сгенерировали матрицу A");
        Matrix B = new Matrix(5, 5);
        B.fillRandMatrix();
        log.info("Сгенерировали матрицу B");
        /*Matrix.writeMatrixToFile(A, args[0]);
        Matrix.writeMatrixToFile(B, args[1]);
        Matrix a = Matrix.readFromFile(args[0]);
        Matrix b = Matrix.readFromFile(args[1]);
        */

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
            log.info("Обе успешно матрицы отправлены");
            Matrix c = Matrix.readFromStream(in);
            log.info("Матрица с результатами вычисления успешно считалась");
            Matrix.writeMatrixToFile(c, "C.txt");
            log.info("Матрица c успешно записана в файл");

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
