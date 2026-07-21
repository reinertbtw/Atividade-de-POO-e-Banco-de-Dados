package dao;

import java.util.List;
import java.util.ArrayList;

import interfaces.ICRUD;
import modelos.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.ConnectionFactory;

public class ProdutoDao implements ICRUD<Produto, Integer> { // Corrigido: <Produto, Integer>

    @Override
    public Produto salvar(Produto produto) {
        String sql = "insert into produto (nome,preco,estoque) values (?,?,?)";
        // Usando try-with-resources para garantir o fechamento automático dos recursos
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());

            int rowsAffected = stmt.executeUpdate();

            // Corrigido: Removido o ponto e vírgula extra
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) { // try-with-resources para ResultSet
                    if (rs.next()) {
                        produto.setId(rs.getInt(1));
                    }
                }
            }
            System.out.println("Produto salvo com sucesso! ID: " + produto.getId());
            return produto;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar produto: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar produto no banco de dados.", e);
        }
        // O bloco finally não é mais necessário para fechar Connection, PreparedStatement e ResultSet
        // pois o try-with-resources já faz isso automaticamente.
    }

    @Override // Corrigido: Adicionado o '@'
    public void deletar(Integer id) {
        String sql = "DELETE FROM produto WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Produto deletado com sucesso.");
            } else {
                System.out.println("Nenhum produto encontrado com o ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao deletar produto: " + e.getMessage());
            throw new RuntimeException("Erro ao deletar produto no banco de dados.", e);
        }
    }

    @Override
    public void alterar(Produto produto) {
        String sql = """
                UPDATE produto
                SET nome = ?, preco = ?, estoque = ?
                WHERE id = ?
                """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());
            stmt.setInt(4, produto.getId());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Produto alterado com sucesso.");
            } else {
                System.out.println("Nenhum produto encontrado com o ID: " + produto.getId());
            }

        } catch (SQLException e) {
            System.err.println("Erro ao alterar produto: " + e.getMessage());
            throw new RuntimeException("Erro ao alterar produto no banco de dados.", e);
        }
    }

    @Override
    public Produto consultar(Integer id) {
        String sql = "SELECT id, nome, preco, estoque FROM produto WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto();

                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setPreco(rs.getDouble("preco"));
                    produto.setEstoque(rs.getInt("estoque"));

                    return produto;
                }

                return null;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao consultar produto: " + e.getMessage());
            throw new RuntimeException("Erro ao consultar produto no banco de dados.", e);
        }
    }

    @Override
    public List<Produto> consultar() { // Corrigido: <Produto>
        String sql = "SELECT id, nome, preco, estoque FROM produto ORDER BY id";

        List<Produto> produtos = new ArrayList<>(); // Corrigido: <Produto> e <>

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Produto produto = new Produto();

                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setEstoque(rs.getInt("estoque"));

                produtos.add(produto);
            }

            return produtos;

        } catch (SQLException e) {
            System.err.println("Erro ao consultar produtos: " + e.getMessage());
            throw new RuntimeException("Erro ao consultar produtos no banco de dados.", e);
        }
    }
}