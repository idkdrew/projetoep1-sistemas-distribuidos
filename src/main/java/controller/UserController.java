package controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import operations.Login;
import operations.Logout;
import operations.SignInUser;
import repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

public class UserController {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public String handleRequest(String jsonRequest) {
        try {
            // Deserializar JSON genérico para identificar a operação
            var requestNode = objectMapper.readTree(jsonRequest);
            System.out.println("JSON recebido:" + jsonRequest);
            String operation = requestNode.get("operacao").asText();

            switch (operation) {
                case "cadastrarUsuario":
                    return handleSignUp(jsonRequest);

                case "login":
                    return handleLogin(jsonRequest);

                case "logout":
                    return handleLogout(jsonRequest);

                default:
                    return createErrorResponse(401, operation, "Operacao não encontrada.");
            }
        } catch (Exception ex) {
            return createErrorResponse(500, "error", "Erro inesperado no servidor.");
        }
    }

    private String handleSignUp(String jsonRequest) {
        try {
            // Deserializar para SignInUse
            SignInUser request = objectMapper.readValue(jsonRequest, SignInUser.class);

            // Validação de campos
            if (!isRaValid(request.getRa()) || !isSenhaValid(request.getSenha()) || !isNomeValid(request.getNome())){
                String cadastrarErro = createErrorResponse(401, "cadastrarUsuario", "Os campos recebidos nao sao validos.");
                System.out.println("JSON enviado:" + cadastrarErro);
                return cadastrarErro;
            }

            // Verificar se o usuário já existe
            if (userRepository.findByRa(request.getRa()).isPresent()) {
                String cadastrarErro = createErrorResponse(401, "cadastrarUsuario", "Nao foi possível cadastrar pois o usuário informado já existe.");
                System.out.println("JSON enviado: " + cadastrarErro);
                return cadastrarErro;
            }

            // Criar e salvar o novo usuário
            User user = new User(request.getRa(), request.getSenha(), request.getNome(), false);
            userRepository.registerUser(user);

            String cadastrarSucesso = createSuccessResponse(201, "cadastrarUsuario", "Cadastro realizado com sucesso.");
            System.out.println("JSON enviado:" + cadastrarSucesso);
            return cadastrarSucesso;
        } catch (IOException e) {
            String cadastrarErro = createErrorResponse(401, "cadastrarUsuario", "Nao foi possível ler o JSON recebido.");
            System.out.println("JSON enviado: " + cadastrarErro);
            return cadastrarErro;
        } catch (RuntimeException e){
            String cadastrarErro = createErrorResponse(401, "cadastrarUsuario", "O servidor nao conseguiu se conectar ao banco de dados");
            System.out.println("JSON enviado: " + cadastrarErro);
            return cadastrarErro;
        }
    }

    private String handleLogin(String jsonRequest) {
        try {
            Login request = objectMapper.readValue(jsonRequest, Login.class);

            if (!isRaValid(request.getRa()) || !isSenhaValid(request.getSenha())) {
                String cadastraErro = createErrorResponse(401, "login", "Os campos recebidos nao sao validos.");
                System.out.println("JSON enviado: " + cadastraErro);
                return cadastraErro;
            }

            Optional<User> userOpt = userRepository.findByRa(request.getRa());

            if (userOpt.isEmpty() || !userOpt.get().getSenha().equals(request.getSenha())) {
                String cadastraErro = createErrorResponse(401, "login", "Credenciais invalidas.");
                System.out.println("JSON enviado: " + cadastraErro);
                return cadastraErro;
            }

            String cadastrarSucesso = objectMapper.writeValueAsString(new Response(200, userOpt.get().getRa()));
            System.out.println("JSON enviado: " + cadastrarSucesso);
            return cadastrarSucesso;
        } catch (IOException e) {
            String cadastrarErro = createErrorResponse(401, "login", "Nao foi possivel ler o json recebido.");
            System.out.println("JSON enviado: " + cadastrarErro);
            return cadastrarErro;
        } catch (RuntimeException ex) {
            String cadastrarErro = createErrorResponse(401, "login", "O servidor nao conseguiu se conectar com o banco de dados.");
            System.out.println("JSON enviado: " + cadastrarErro);
            return cadastrarErro;
        }
    }

    private String handleLogout(String jsonRequest) {
        try {
            Logout request = objectMapper.readValue(jsonRequest, Logout.class);
            String cadastrarSucesso = objectMapper.writeValueAsString(new Response(200));
            System.out.println("JSON enviado: " + cadastrarSucesso);
            return cadastrarSucesso;
        } catch (IOException e) {
            String cadastrarErro = createErrorResponse(401, "logout", "Não foi possível ler o JSON recebido.");
            System.out.println("JSON enviado: " + cadastrarErro);
            return cadastrarErro;
        } catch (Exception ex) {
            String cadastrarErro = createErrorResponse(500, "logout", "Erro inesperado ao realizar logout.");
            System.out.println("JSON enviado: " + cadastrarErro);
            return cadastrarErro;
        }
    }

    private String createSuccessResponse(int status, String operation, String message) {
        try {
            return objectMapper.writeValueAsString(new Response(status, operation, message));
        } catch (Exception e) {
            return createErrorResponse(500, "response", "Erro ao criar resposta de sucesso.");
        }
    }

    private String createErrorResponse(int status, String operation, String message) {
        try {
            return objectMapper.writeValueAsString(new Response(status, operation, message));
        } catch (Exception e) {
            return "{\"status\":500,\"operacao\":\"error\",\"mensagem\":\"Erro ao criar resposta de erro.\"}";
        }
    }

    private boolean isRaValid(String ra) {
        return ra != null && ra.matches("\\d{7}");
    }

    private boolean isSenhaValid(String senha) {
        return senha != null && senha.matches("[a-zA-Z]{8,20}");
    }

    private boolean isNomeValid(String nome) {
        return nome != null && nome.matches("[A-Z ]{1,50}");
    }

    // Classe interna para resposta genérica
    static class Response {
        private int status;
        private String operacao;
        private String mensagem;
        private String token;

        public Response(int status) {
            this.status = status;
        }

        public Response(int status, String token) {
            this.status = status;
            this.token = token;
        }

        public Response(int status, String operacao, String mensagem) {
            this.status = status;
            this.operacao = operacao;
            this.mensagem = mensagem;
        }

        public Response(int status, String operacao, String mensagem, String token) {
            this.status = status;
            this.operacao = operacao;
            this.mensagem = mensagem;
            this.token = token;
        }

        // Getters e setters para serialização JSON
        public int getStatus() {
            return status;
        }

        public String getOperacao() {
            return operacao;
        }

        public String getMensagem() {
            return mensagem;
        }

        public String getToken() {
            return token;
        }
    }
}
