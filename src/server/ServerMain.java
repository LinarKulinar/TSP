package server;//package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerMain {
    private static final Logger log = Logger.getLogger(ServerMain.class.getName());

    public static void main(String[] args) {
        log.info("Готов к параллельной обработке данных. Жду клиента.");
        try (ServerSocket ss = new ServerSocket(Server.PORT)) {
            while (true) {
                //раздаем сокеты
                Socket socket = ss.accept();
                log.info("Я получил задание для обработки");
                Server server = new Server(socket);
                server.start();
                log.info("Запустил отдельный процесс для обработки");
            }
        } catch (IOException e) {
            log.warning(e.getMessage() + "\nНе удалось создать сокет");
            System.err.println("Не удалось создать сокет");
        }
    }
}
