package br.com.starlog.dao;

import br.com.starlog.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoDAOSqlite implements ProdutoDAO {

    private static final String URL_CONEXAO = "jdbc:sqlite:starlog.db";

    public ProdutoDAOSqlite() {
        String ddl = "CREATE TABLE IF NOT EXISTS produtos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "preco REAL NOT NULL, "
                + "estoque INTEGER NOT NULL"
                + ");";

        try (Connection conn = conectar();
            Statement stmt = conn.createStatement()) {
            stmt.execute(ddl);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela produtos: " + e.getMessage(), e);
        }
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL_CONEXAO);
    }

    @Override
    public void salvar(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?);";

        try (Connection conn = conectar();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar produto: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        String sql = "SELECT id, nome, preco, estoque FROM produtos WHERE id = ?;";

        try (Connection conn = conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearLinha(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Produto> listarTodos() {
        String sql = "SELECT id, nome, preco, estoque FROM produtos ORDER BY nome ASC;";
        List<Produto> lista = new ArrayList<>();

        try (Connection conn = conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearLinha(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public boolean atualizar(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, estoque = ? WHERE id = ?;";

        try (Connection conn = conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());
            stmt.setLong(4, produto.getId());

            int afetadas = stmt.executeUpdate();
            return afetadas > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean excluir(Long id) {
        String sql = "DELETE FROM produtos WHERE id = ?;";

        try (Connection conn = conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int afetadas = stmt.executeUpdate();
            return afetadas > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir produto: " + e.getMessage(), e);
        }
    }

    private Produto mapearLinha(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getDouble("preco"),
                rs.getInt("estoque")
        );
    }
}