package dao;

import interfaces.ICRUD;
import modelos.Cliente;
import util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao implements ICRUD<Cliente, Integer> {

    @Override
    public Cliente salvar(Cliente cliente) {
        String sql = "INSERT INTO cliente " +
                "(cpf, nome, email, rua, numero, bairro, cep, cidade, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            stmt.setString(1, cliente.getCpf());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getRua());
            stmt.setInt(5, cliente.getNumero());
            stmt.setString(6, cliente.getBairro());
            stmt.setString(7, cliente.getCep());
            stmt.setString(8, cliente.getCidade());
            stmt.setString(9, cliente.getEstado());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setId(rs.getInt(1));
                    }
                }
            }

            System.out.println("Cliente salvo com sucesso! ID: " + cliente.getId());
            return cliente;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar cliente: " + e.getMessage());
            throw new RuntimeException(
                    "Erro ao salvar cliente no banco de dados.",
                    e
            );
        }
    }

    @Override
    public void deletar(Integer id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Cliente deletado com sucesso.");
            } else {
                System.out.println("Nenhum cliente encontrado com o ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao deletar cliente: " + e.getMessage());
            throw new RuntimeException(
                    "Erro ao deletar cliente no banco de dados.",
                    e
            );
        }
    }

    @Override
    public void alterar(Cliente cliente) {
        String sql = "UPDATE cliente SET " +
                "cpf = ?, nome = ?, email = ?, rua = ?, numero = ?, " +
                "bairro = ?, cep = ?, cidade = ?, estado = ? " +
                "WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, cliente.getCpf());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getRua());
            stmt.setInt(5, cliente.getNumero());
            stmt.setString(6, cliente.getBairro());
            stmt.setString(7, cliente.getCep());
            stmt.setString(8, cliente.getCidade());
            stmt.setString(9, cliente.getEstado());
            stmt.setInt(10, cliente.getId());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Cliente alterado com sucesso.");
            } else {
                System.out.println("Nenhum cliente encontrado com o ID: "
                        + cliente.getId());
            }

        } catch (SQLException e) {
            System.err.println("Erro ao alterar cliente: " + e.getMessage());
            throw new RuntimeException(
                    "Erro ao alterar cliente no banco de dados.",
                    e
            );
        }
    }

    @Override
    public Cliente consultar(Integer id) {
        String sql = "SELECT id, cpf, nome, email, rua, numero, bairro, " +
                "cep, cidade, estado FROM cliente WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();

                    cliente.setId(rs.getInt("id"));
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setRua(rs.getString("rua"));
                    cliente.setNumero(rs.getInt("numero"));
                    cliente.setBairro(rs.getString("bairro"));
                    cliente.setCep(rs.getString("cep"));
                    cliente.setCidade(rs.getString("cidade"));
                    cliente.setEstado(rs.getString("estado"));

                    return cliente;
                }

                return null;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao consultar cliente: " + e.getMessage());
            throw new RuntimeException(
                    "Erro ao consultar cliente no banco de dados.",
                    e
            );
        }
    }

    @Override
    public List<Cliente> consultar() {
        String sql = "SELECT id, cpf, nome, email, rua, numero, bairro, " +
                "cep, cidade, estado FROM cliente ORDER BY id";

        List<Cliente> clientes = new ArrayList<>();

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Cliente cliente = new Cliente();

                cliente.setId(rs.getInt("id"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setNome(rs.getString("nome"));
                cliente.setEmail(rs.getString("email"));
                cliente.setRua(rs.getString("rua"));
                cliente.setNumero(rs.getInt("numero"));
                cliente.setBairro(rs.getString("bairro"));
                cliente.setCep(rs.getString("cep"));
                cliente.setCidade(rs.getString("cidade"));
                cliente.setEstado(rs.getString("estado"));

                clientes.add(cliente);
            }

            return clientes;

        } catch (SQLException e) {
            System.err.println("Erro ao consultar clientes: " + e.getMessage());
            throw new RuntimeException(
                    "Erro ao consultar clientes no banco de dados.",
                    e
            );
        }
    }
}