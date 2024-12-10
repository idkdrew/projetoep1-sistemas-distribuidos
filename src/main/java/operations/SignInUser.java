package operations;

import model.User;
import repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Modelo de dados para a operação "cadastrarUsuario"
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignInUser {
    private String ra;
    private String senha;
    private String nome;

    // Construtor padrão (necessário para o Jackson)
    public SignInUser() {
    }

    // Construtor opcional para criar instâncias rapidamente
    public SignInUser(String ra, String senha, String nome) {
        this.ra = ra;
        this.senha = senha;
        this.nome = nome;
    }

    // Getters e setters
    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    //Método para registrar o usuário
    public static String registerUser(String ra, String senha, String nome, boolean isAdmin) {
        // Cria um novo usuário
        User user = new User(ra, senha, nome, isAdmin);

        // Chama o método registerUser da classe UserRepository
        UserRepository userRepository = new UserRepository();
        userRepository.registerUser(user);  // Registra o usuário no banco

        // Retorna uma resposta com o status de sucesso
        return "{\"status\": 201, \"action\": \"cadastrarUsuario\", \"message\": \"Cadastro realizado com sucesso.\"}";
    }
}

