package model;

public class User {
    private String ra;
    private String senha;  // Alteração aqui
    private String nome;
    private boolean isAdmin;

    public User(String ra, String senha, String nome, boolean isAdmin) {
        this.ra = ra;
        this.senha = senha;
        this.nome = nome;
        this.isAdmin = isAdmin;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
