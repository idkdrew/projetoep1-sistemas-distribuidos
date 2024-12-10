package server;

import controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import repository.UserRepository;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 20340;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final UserController userController;

    static {
        UserRepository userRepository = new UserRepository();
        userController = new UserController(userRepository); // Inicializa o controller
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado na porta " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            System.out.println("Cliente conectado: " + clientSocket.getRemoteSocketAddress());

            // Lê e processa mensagens do cliente até que a conexão seja fechada
            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                processClientMessage(clientMessage, writer);
            }

        } catch (IOException e) {
            System.err.println("Erro na comunicação com o cliente: " + e.getMessage());
        } finally {
            // No bloco finally, sinaliza quando o cliente foi desconectado
            System.out.println("Cliente desconectado: " + clientSocket.getRemoteSocketAddress());
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar o socket do cliente: " + e.getMessage());
            }
        }
    }

    private static void processClientMessage(String clientMessage, PrintWriter writer) {
        try {
            // Chama o UserController para tratar a requisição
            String response = userController.handleRequest(clientMessage);

            // Envia a resposta para o cliente
            writer.println(response);

        } catch (Exception e) {
            e.printStackTrace();
            writer.println("{\"status\":500,\"operacao\":\"error\",\"mensagem\":\"Erro interno ao processar a requisição.\"}");
        }
    }
}
