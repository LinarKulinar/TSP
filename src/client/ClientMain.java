package client;//package client;

import matrix.Matrix;
import server.Server;

import java.io.*;
import java.net.Socket;

public class ClientMain {

    public static void main(String[] args) {
        args = new String[3];
        args[0] = "client";
        args[1] = "A";
        args[2] = "B";

        if (args.length != 3) {
            System.out.println("Введите имена файлов верно");
            return;
        }
        Matrix A = new Matrix(5, 4);
        A.fillRandMatrix();
        Matrix B = new Matrix(5, 5);
        B.fillRandMatrix();
        /*Matrix.writeMatrixToFile(A, args[0]);
        Matrix.writeMatrixToFile(B, args[1]);
        Matrix a = Matrix.readFromFile(args[0]);
        Matrix b = Matrix.readFromFile(args[1]);
        if (a == null || b == null) {
            System.err.println("Матрицы не считаны");
            return;
        }*/
        Matrix a = Matrix.readFromFile("A.txt");
        Matrix b = Matrix.readFromFile("B.txt");

        //создаем сокет клиента, с потоками для чтения и записи
        try {
            Socket socket = new Socket("127.0.0.1", Server.PORT);

            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом.
             InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            Matrix.writeMatrixToStream(a, out);
            Matrix.writeMatrixToStream(b, out);
            out.flush();
            Matrix c = Matrix.readFromStream(in);
            Matrix.writeMatrixToFile(c, "C.txt");

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

/*
        ObjectOutputStream oos = new ObjectOutputStream(socketClient.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socketClient.getInputStream()){
            oos.writeObject(a);
            oos.writeObject(b);

            Object o = ois.readObject();
            //если полученный объект принадлежит классу матриц, то записываем его в файл
            if (o instanceof Matrix) {
                Matrix c = (Matrix) o;
                Matrix.writeToFile(args[2], c);
            } else if (o instanceof IllegalArgumentException) {
                IllegalArgumentException e = (IllegalArgumentException) o;
                System.out.println(e.getMessage());
            } else {
                System.out.println("Получена не матрица");
            }*/


    }
}
