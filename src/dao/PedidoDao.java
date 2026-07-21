package dao;

import interfaces.ICRUD;
import modelos.Cliente;
import modelos.Pedido;
import modelos.Produto;
import util.ConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDao implements ICRUD<Pedido, Integer> {

    private static final String SQL_CONSULTA_BASE =
            "SELECT " +
                    "pe.id AS pedido_id, " +
                    "pe.data_pedido, " +
                    "pe.status, " +
                    "c.id AS cliente_id_result, " +
                    "c.cpf AS cliente_cpf, " +
                    "c.nome AS cliente_nome, " +
                    "c.email AS cliente_email, " +
                    "c.rua AS cliente_rua, " +
                    "c.numero AS cliente_numero, " +
                    "c.bairro AS cliente_bairro, " +
                    "c.cep AS cliente_cep, " +
                    "c.cidade AS cliente_cidade, " +
                    "c.estado AS cliente_estado " +
                    "FROM pedido pe " +
                    "INNER JOIN cliente c ON c.id = pe.cliente_id ";

    @Override
    public Pedido salvar(Pedido pedido) {
        validarPedido(pedido);

        String sqlPedido =
                "INSERT INTO pedido " +
                        "(cliente_id, data_pedido, status) " +
                        "VALUES (?, ?, ?)";

        String sqlProduto =
                "INSERT INTO pedido_produto " +
                        "(pedido_id, produto_id) " +
                        "VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);

                try (
                        PreparedStatement stmtPedido = conn.prepareStatement(
                                sqlPedido,
                                Statement.RETURN_GENERATED_KEYS
                        );
                        PreparedStatement stmtProduto =
                                conn.prepareStatement(sqlProduto)
                ) {
                    stmtPedido.setInt(1, pedido.getCliente().getId());
                    stmtPedido.setDate(2, Date.valueOf(pedido.getData()));
                    stmtPedido.setString(3, pedido.getStatus());

                    stmtPedido.executeUpdate();

                    try (ResultSet rs = stmtPedido.getGeneratedKeys()) {
                        if (rs.next()) {
                            pedido.setId(rs.getInt(1));
                        } else {
                            throw new SQLException(
                                    "Não foi possível obter o ID do pedido."
                            );
                        }
                    }

                    for (Produto produto : pedido.getProdutos()) {
                        stmtProduto.setInt(1, pedido.getId());
                        stmtProduto.setInt(2, produto.getId());
                        stmtProduto.executeUpdate();
                    }
                }

                conn.commit();

                System.out.println(
                        "Pedido salvo com sucesso! ID: " + pedido.getId()
                );

                return pedido;

            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException erroRollback) {
                    e.addSuppressed(erroRollback);
                }

                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar pedido: " + e.getMessage());
            throw new RuntimeException(
                    "Erro ao salvar pedido no banco de dados.",
                    e
            );
        }
    }

    @Override
    public void deletar(Integer id) {
        validarId(id);

        String sqlItens =
                "DELETE FROM pedido_produto WHERE pedido_id = ?";

        String sqlPedido =
                "DELETE FROM pedido WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);

                try (
                        PreparedStatement stmtItens =
                                conn.prepareStatement(sqlItens);
                        PreparedStatement stmtPedido =
                                conn.prepareStatement(sqlPedido)
                ) {
                    stmtItens.setInt(1, id);
                    stmtItens.executeUpdate();

                    stmtPedido.setInt(1, id);
                    int linhasAfetadas = stmtPedido.executeUpdate();

                    if (linhasAfetadas == 0) {
                        conn.rollback();
                        System.out.println(
                                "Nenhum pedido encontrado com o ID: " + id
                        );
                        return;
                    }
                }

                conn.commit();
                System.out.println("Pedido deletado com sucesso.");

            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException erroRollback) {
                    e.addSuppressed(erroRollback);
                }

                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao deletar pedido: " + e.getMessage());
            throw new RuntimeException(
                    "Erro ao deletar pedido no banco de dados.",
                    e
            );
        }
    }

    @Override
    public void alterar(Pedido pedido) {
        validarPedido(pedido);
        validarId(pedido.getId());

        String sqlPedido =
                "UPDATE pedido " +
                        "SET cliente_id = ?, data_pedido = ?, status = ? " +
                        "WHERE id = ?";

        String sqlExcluirProdutos =
                "DELETE FROM pedido_produto WHERE pedido_id = ?";

        String sqlAdicionarProduto =
                "INSERT INTO pedido_produto " +
                        "(pedido_id, produto_id) " +
                        "VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);

                try (
                        PreparedStatement stmtPedido =
                                conn.prepareStatement(sqlPedido);
                        PreparedStatement stmtExcluirProdutos =
                                conn.prepareStatement(sqlExcluirProdutos);
                        PreparedStatement stmtAdicionarProduto =
                                conn.prepareStatement(sqlAdicionarProduto)
                ) {
                    stmtPedido.setInt(1, pedido.getCliente().getId());
                    stmtPedido.setDate(2, Date.valueOf(pedido.getData()));
                    stmtPedido.setString(3, pedido.getStatus());
                    stmtPedido.setInt(4, pedido.getId());

                    int linhasAfetadas = stmtPedido.executeUpdate();

                    if (linhasAfetadas == 0) {
                        conn.rollback();
                        System.out.println(
                                "Nenhum pedido encontrado com o ID: "
                                        + pedido.getId()
                        );
                        return;
                    }

                    stmtExcluirProdutos.setInt(1, pedido.getId());
                    stmtExcluirProdutos.executeUpdate();

                    for (Produto produto : pedido.getProdutos()) {
                        stmtAdicionarProduto.setInt(1, pedido.getId());
                        stmtAdicionarProduto.setInt(2, produto.getId());
                        stmtAdicionarProduto.executeUpdate();
                    }
                }

                conn.commit();
                System.out.println("Pedido alterado com sucesso.");

            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException erroRollback) {
                    e.addSuppressed(erroRollback);
                }

                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao alterar pedido: " + e.getMessage());
            throw new RuntimeException(
                    "Erro ao alterar pedido no banco de dados.",
                    e
            );
        }
    }

    @Override
    public Pedido consultar(Integer id) {
        validarId(id);

        String sql = SQL_CONSULTA_BASE +
                "WHERE pe.id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pedido pedido = mapearPedido(rs);
                    carregarProdutos(conn, pedido);
                    return pedido;
                }

                return null;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao consultar pedido: " + e.getMessage());
            throw new RuntimeException(
                    "Erro ao consultar pedido no banco de dados.",
                    e
            );
        }
    }

    @Override
    public List<Pedido> consultar() {
        String sql = SQL_CONSULTA_BASE +
                "ORDER BY pe.id";

        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection()) {

            /*
             * Primeiro carregamos os pedidos e fechamos o ResultSet.
             * Depois carregamos os produtos de cada pedido.
             */
            try (
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery()
            ) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }

            for (Pedido pedido : pedidos) {
                carregarProdutos(conn, pedido);
            }

            return pedidos;

        } catch (SQLException e) {
            System.err.println("Erro ao consultar pedidos: " + e.getMessage());
            throw new RuntimeException(
                    "Erro ao consultar pedidos no banco de dados.",
                    e
            );
        }
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();

        cliente.setId(rs.getInt("cliente_id_result"));
        cliente.setCpf(rs.getString("cliente_cpf"));
        cliente.setNome(rs.getString("cliente_nome"));
        cliente.setEmail(rs.getString("cliente_email"));
        cliente.setRua(rs.getString("cliente_rua"));
        cliente.setNumero(rs.getInt("cliente_numero"));
        cliente.setBairro(rs.getString("cliente_bairro"));
        cliente.setCep(rs.getString("cliente_cep"));
        cliente.setCidade(rs.getString("cliente_cidade"));
        cliente.setEstado(rs.getString("cliente_estado"));

        Pedido pedido = new Pedido();

        pedido.setId(rs.getInt("pedido_id"));
        pedido.setCliente(cliente);
        pedido.setData(rs.getDate("data_pedido").toLocalDate());
        pedido.setStatus(rs.getString("status"));

        return pedido;
    }

    private void carregarProdutos(
            Connection conn,
            Pedido pedido
    ) throws SQLException {
        String sql =
                "SELECT p.id, p.nome, p.preco, p.estoque " +
                        "FROM pedido_produto pp " +
                        "INNER JOIN produto p ON p.id = pp.produto_id " +
                        "WHERE pp.pedido_id = ? " +
                        "ORDER BY pp.id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedido.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto();

                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setPreco(rs.getDouble("preco"));
                    produto.setEstoque(rs.getInt("estoque"));

                    pedido.getProdutos().add(produto);
                }
            }
        }
    }

    private void validarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException(
                    "O pedido não pode ser nulo."
            );
        }

        if (pedido.getCliente() == null ||
                pedido.getCliente().getId() == null ||
                pedido.getCliente().getId() <= 0) {
            throw new IllegalArgumentException(
                    "O pedido precisa possuir um cliente cadastrado."
            );
        }

        if (pedido.getData() == null) {
            throw new IllegalArgumentException(
                    "A data do pedido não pode ser nula."
            );
        }

        if (pedido.getStatus() == null ||
                pedido.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "O status do pedido não pode ser vazio."
            );
        }

        if (pedido.getProdutos() == null ||
                pedido.getProdutos().isEmpty()) {
            throw new IllegalArgumentException(
                    "O pedido precisa possuir pelo menos um produto."
            );
        }

        for (Produto produto : pedido.getProdutos()) {
            if (produto == null ||
                    produto.getId() == null ||
                    produto.getId() <= 0) {
                throw new IllegalArgumentException(
                        "Todos os produtos precisam estar cadastrados."
                );
            }
        }
    }

    private void validarId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "O ID precisa ser maior que zero."
            );
        }
    }
}