package operations;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Login {
    private String ra;
    private String senha;


    public Login() {
    }


    // Construtor
    public Login(String ra, String senha) {
        this.ra = ra;
        this.senha = senha;
    }

    // Getters e Setters
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
}
