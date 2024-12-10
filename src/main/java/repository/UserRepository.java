package repository;

import model.User;

import java.sql.*;
import java.util.Optional;

public class UserRepository {
    private final String url = "jdbc:postgresql://localhost:5432/sistemasDistribuidos";
    private final String user = "postgres";
    private final String password = "123";

    public Optional<User> findByRa(String ra) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM users WHERE ra = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, ra);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getString("ra"),
                            rs.getString("senha"),
                            rs.getString("nome"),
                            rs.getBoolean("isAdmin")));
                }
            }
        } catch (SQLException e) {
            // Lançar RuntimeException com mensagem específica
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return Optional.empty();
    }

    public void registerUser(User user) {
        try (Connection connection = DriverManager.getConnection(url, this.user, this.password)) {
            String query = "INSERT INTO users (ra, senha, nome, isAdmin) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, user.getRa());
                stmt.setString(2, user.getSenha());
                stmt.setString(3, user.getNome());
                stmt.setBoolean(4, user.isAdmin());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            // Lançar RuntimeException com mensagem específica
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    public boolean authenticateUser(String ra, String password) {
        try {
            Optional<User> user = findByRa(ra);
            return user.isPresent() && user.get().getSenha().equals(password);
        } catch (RuntimeException e) {
            // Propagar a RuntimeException com detalhes do erro
            throw new RuntimeException("Erro ao autenticar usuário: " + e.getMessage());
        }
    }
}
