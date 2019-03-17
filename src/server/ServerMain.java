package server;//package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(Server.PORT)) {
            while (true){
                //раздаем сокеты
                Socket socket = ss.accept();
                Server server = new Server(socket);
                server.start();
            }
        }catch (IOException e){
            System.err.println("Не удалось создать сокет");
        }
    }
}
