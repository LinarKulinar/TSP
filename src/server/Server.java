package server;

import matrix.Matrix;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class Server extends Thread {
    public static final int PORT = 4579;
    private Socket socket;
    private static final Logger log = Logger.getLogger(Server.class.getName());

    public Server(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        DataOutputStream out = null;
        try {
            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту.
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать сообщения.
            DataInputStream in = new DataInputStream(sin);
            out = new DataOutputStream(sout);

            Matrix a = Matrix.readFromStream(in);
            log.info("Удачно считана матрица A");
            Matrix b = Matrix.readFromStream(in);
            log.info("Удачно считана матрица B");
            Matrix c = Matrix.addTwoMatrix(a, b);
            log.info("Удачно перемножили две матрицы");
            out.writeUTF("\n");//в случае корректного вывода матрицы, данные начинаются с пустой строки
            Matrix.writeMatrixToStream(c, out);
            out.flush();
            log.info("Удачно записали результат в поток\n------------------------------------------------");
        } catch (IOException e) {//проблемесы с сервером
            System.err.println("Не удалось произвести вычисления на сервере");
            log.warning(e.getMessage() + "\nНе удалось произвести вычисления на сервере\n------------------------------------------------");
        } catch (IllegalArgumentException e) {//было успешное подключение, но произошла ошибка в вычислениях
            try {
                out.writeUTF(e.getMessage());
                log.warning(e.getMessage() + ". Матрица С не была возвращена клиенту." + "\n------------------------------------------------");
            } catch (IOException e1) {
                System.err.println("Не удалось передать ошибку клиенту");
                log.warning(e.getMessage() + "\nНе удалось передать ошибку клиенту\n------------------------------------------------");
            }
        }
    }
}
