package server;

import matrix.Matrix;

import java.io.*;
import java.net.Socket;

public class Server extends Thread {
    public static final int PORT = 4579;
    private Socket socket;

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
            Matrix a = Matrix.readFromStream(in);//читаем
            Matrix b = Matrix.readFromStream(in);//читаем
            Matrix c = Matrix.addTwoMatrix(a, b);//перемножаем
            Matrix.writeMatrixToStream(c, out);//пишем в поток
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
