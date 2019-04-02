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
        try {
            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту.
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать сообщения.
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            Matrix a = Matrix.readFromStream(in);
            log.info("Удачно считана матрица A");
            Matrix b = Matrix.readFromStream(in);
            log.info("Удачно считана матрица B");
            Matrix c = Matrix.addTwoMatrix(a, b);
            log.info("Удачно перемножили две матрицы");
            Matrix.writeMatrixToStream(c, out);
            out.flush();
            log.info("Удачно записали результат в поток\n------------------------------------------------");
        } catch (IOException e) {
            log.warning(e.getMessage() + "\nНе удалось произвести вычисления на сервере");
            System.err.println("Не удалось произвести вычисления на сервере");
        }

    }
}
