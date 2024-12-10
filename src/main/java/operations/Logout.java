package operations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Logout {
    private String token;


    public Logout() {
    }

    // Construtor
    public Logout(String token) {
        this.token = token;
    }

    // Getter e Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
