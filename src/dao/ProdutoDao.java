package dao;

import java.util.List;

import interfaces.ICRUD;
import modelos.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.ConnectionFactory;

public class ProdutoDao implements ICRUD<Produto, Integer> {

    @Override
    public Produto salvar(Produto produto) {
        String sql = "insert into produto (nome,preco,estoque) values (?,?,?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; // Para pegar o ID gerado

        try {
            // 1. Obter uma conexão com o banco de dados
            conn = ConnectionFactory.getConnection();

            // 2. Preparar a declaração SQL
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // 3. Definir os parâmetros da declaração SQL
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());

            // 4. Executar a inserção
            int rowsAffected = stmt.executeUpdate();

            // 5. Verificar se a inserção foi bem-sucedida e obter o ID gerado
            if (rowsAffected > 0) ;
            {
                rs = stmt.getGeneratedKeys(); // Obtém o ResultSet com as chaves geradas
                if (rs.next()) {
                    // Se houver um ID gerado, atribui-o ao objeto Produto
                    produto.setId(rs.getInt(1));
                }
            }
            System.out.println("Produto salvo com sucesso! ID: " + produto.getId());
            return produto; // Retorna o produto com o ID atualizado

        } catch (SQLException e) {
            // Em caso de erro, imprime a pilha de exceção
            System.err.println("Erro ao salvar produto: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar produto no banco de dados.", e);
        } finally {
            // 6. Fechar os recursos (ResultSet, PreparedStatement, Connection)
            // É importante fechar na ordem inversa da abertura para evitar vazamento de recursos
            ConnectionFactory.closeConnection(rs); // Método auxiliar para fechar ResultSet
            ConnectionFactory.closeConnection(stmt); // Método auxiliar para fechar PreparedStatement
            ConnectionFactory.closeConnection(conn); // Método auxiliar para fechar Connection
        }
    }

    @Override
    public void deletar(Integer id) {

    }

    @Override
    public void alterar(Produto obj) {

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
    public List<Produto> consultar() {
        return null;
    }

}
