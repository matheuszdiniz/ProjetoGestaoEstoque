import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection connection;

    public Database(String dbUrl) throws SQLException {
        connection = DriverManager.getConnection(dbUrl);
        createTable();
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS produtos (nome TEXT PRIMARY KEY, quantidade INTEGER)";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
    }

    public void addProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, quantidade) VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, produto.getNome());
        pstmt.setInt(2, produto.getQuantidade());
        pstmt.executeUpdate();
    }

    public void updateProduto(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET quantidade = ? WHERE nome = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, produto.getQuantidade());
        pstmt.setString(2, produto.getNome());
        pstmt.executeUpdate();
    }

    public void deleteProduto(String nome) throws SQLException {
        String sql = "DELETE FROM produtos WHERE nome = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, nome);
        pstmt.executeUpdate();
    }

    public List<Produto> getAllProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String nome = rs.getString("nome");
            int quantidade = rs.getInt("quantidade");
            produtos.add(new Produto(nome, quantidade));
        }
        return produtos;
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
