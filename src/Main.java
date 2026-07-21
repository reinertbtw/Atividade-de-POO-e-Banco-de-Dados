import dao.ClienteDao;
import dao.ProdutoDao;
import dao.PedidoDao;
import modelos.Cliente;
import modelos.Produto;
import modelos.Pedido;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ProdutoDao produtoDao = new ProdutoDao();
        ClienteDao clienteDao = new ClienteDao();
        PedidoDao pedidoDao = new PedidoDao();

        int opcao;

        do {
            exibirMenuPrincipal();
            opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1:
                        menuProdutos(produtoDao);
                        break;

                    case 2:
                        menuClientes(clienteDao);
                        break;

                    case 3:
                        menuPedidos(pedidoDao, clienteDao, produtoDao);
                        break;

                    case 0:
                        System.out.println("Programa encerrado.");
                        break;

                    default:
                        System.out.println("Opção inválida.");
                        pausar();
                }

            } catch (RuntimeException e) {
                System.err.println("Erro: " + e.getMessage());
                pausar();
            }

        } while (opcao != 0);

        scanner.close();
    }

    // =========================================================
    // MENU PRINCIPAL
    // =========================================================

    private static void exibirMenuPrincipal() {
        System.out.println("\n==================================");
        System.out.println("       SISTEMA DE LOJA");
        System.out.println("==================================");
        System.out.println("1 - Menu de Produtos");
        System.out.println("2 - Menu de Clientes");
        System.out.println("3 - Menu de Pedidos");
        System.out.println("0 - Sair");
        System.out.println("==================================");
    }

    // =========================================================
    // MENU DE PRODUTOS
    // =========================================================

    private static void menuProdutos(ProdutoDao produtoDao) {
        int opcao;

        do {
            System.out.println("\n==================================");
            System.out.println("       MENU DE PRODUTOS");
            System.out.println("==================================");
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Listar produtos");
            System.out.println("3 - Consultar produto por ID");
            System.out.println("4 - Alterar produto");
            System.out.println("5 - Deletar produto");
            System.out.println("0 - Voltar");
            System.out.println("==================================");

            opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1:
                        cadastrarProduto(produtoDao);
                        break;

                    case 2:
                        listarProdutos(produtoDao);
                        break;

                    case 3:
                        consultarProduto(produtoDao);
                        break;

                    case 4:
                        alterarProduto(produtoDao);
                        break;

                    case 5:
                        deletarProduto(produtoDao);
                        break;

                    case 0:
                        System.out.println("Voltando ao menu principal...");
                        break;

                    default:
                        System.out.println("Opção inválida.");
                }

            } catch (RuntimeException e) {
                System.err.println("Erro: " + e.getMessage());
            }

            if (opcao != 0) {
                pausar();
            }

        } while (opcao != 0);
    }

    private static void cadastrarProduto(ProdutoDao produtoDao) {
        System.out.println("\n--- CADASTRAR PRODUTO ---");

        String nome = lerTexto("Nome: ");
        double preco = lerDoubleNaoNegativo("Preço: ");
        int estoque = lerInteiroNaoNegativo("Estoque: ");

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setEstoque(estoque);

        Produto produtoSalvo = produtoDao.salvar(produto);

        System.out.println("Produto cadastrado com sucesso!");
        System.out.println(produtoSalvo);
    }

    private static void listarProdutos(ProdutoDao produtoDao) {
        System.out.println("\n--- LISTA DE PRODUTOS ---");

        List<Produto> produtos = produtoDao.consultar();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        for (Produto produto : produtos) {
            System.out.println(produto);
        }
    }

    private static void consultarProduto(ProdutoDao produtoDao) {
        System.out.println("\n--- CONSULTAR PRODUTO ---");

        int id = lerInteiro("Digite o ID do produto: ");

        Produto produto = produtoDao.consultar(id);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
        } else {
            System.out.println("Produto encontrado:");
            System.out.println(produto);
        }
    }

    private static void alterarProduto(ProdutoDao produtoDao) {
        System.out.println("\n--- ALTERAR PRODUTO ---");

        int id = lerInteiro("Digite o ID do produto: ");

        Produto produto = produtoDao.consultar(id);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.println("Produto atual:");
        System.out.println(produto);

        String nome = lerTexto("Novo nome: ");
        double preco = lerDoubleNaoNegativo("Novo preço: ");
        int estoque = lerInteiroNaoNegativo("Novo estoque: ");

        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setEstoque(estoque);

        produtoDao.alterar(produto);

        System.out.println("Produto alterado com sucesso!");
        System.out.println(produtoDao.consultar(id));
    }

    private static void deletarProduto(ProdutoDao produtoDao) {
        System.out.println("\n--- DELETAR PRODUTO ---");

        int id = lerInteiro("Digite o ID do produto: ");

        Produto produto = produtoDao.consultar(id);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.println("Produto que será deletado:");
        System.out.println(produto);

        String confirmacao = lerTexto(
                "Confirma a exclusão? Digite S para sim ou N para não: "
        );

        if (confirmacao.equalsIgnoreCase("s")) {
            produtoDao.deletar(id);
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    // =========================================================
    // MENU DE CLIENTES
    // =========================================================

    private static void menuClientes(ClienteDao clienteDao) {
        int opcao;

        do {
            System.out.println("\n==================================");
            System.out.println("       MENU DE CLIENTES");
            System.out.println("==================================");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Listar clientes");
            System.out.println("3 - Consultar cliente por ID");
            System.out.println("4 - Alterar cliente");
            System.out.println("5 - Deletar cliente");
            System.out.println("0 - Voltar");
            System.out.println("==================================");

            opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1:
                        cadastrarCliente(clienteDao);
                        break;

                    case 2:
                        listarClientes(clienteDao);
                        break;

                    case 3:
                        consultarCliente(clienteDao);
                        break;

                    case 4:
                        alterarCliente(clienteDao);
                        break;

                    case 5:
                        deletarCliente(clienteDao);
                        break;

                    case 0:
                        System.out.println("Voltando ao menu principal...");
                        break;

                    default:
                        System.out.println("Opção inválida.");
                }

            } catch (RuntimeException e) {
                System.err.println("Erro: " + e.getMessage());
            }

            if (opcao != 0) {
                pausar();
            }

        } while (opcao != 0);
    }

    private static void cadastrarCliente(ClienteDao clienteDao) {
        System.out.println("\n--- CADASTRAR CLIENTE ---");

        Cliente cliente = new Cliente();

        cliente.setCpf(lerTexto("CPF: "));
        cliente.setNome(lerTexto("Nome: "));
        cliente.setEmail(lerTexto("E-mail: "));
        cliente.setRua(lerTexto("Rua: "));
        cliente.setNumero(lerInteiroNaoNegativo("Número: "));
        cliente.setBairro(lerTexto("Bairro: "));
        cliente.setCep(lerTexto("CEP: "));
        cliente.setCidade(lerTexto("Cidade: "));
        cliente.setEstado(lerEstado());

        Cliente clienteSalvo = clienteDao.salvar(cliente);

        System.out.println("Cliente cadastrado com sucesso!");
        System.out.println(clienteSalvo);
    }

    private static void listarClientes(ClienteDao clienteDao) {
        System.out.println("\n--- LISTA DE CLIENTES ---");

        List<Cliente> clientes = clienteDao.consultar();

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
    }

    private static void consultarCliente(ClienteDao clienteDao) {
        System.out.println("\n--- CONSULTAR CLIENTE ---");

        int id = lerInteiro("Digite o ID do cliente: ");

        Cliente cliente = clienteDao.consultar(id);

        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
        } else {
            System.out.println("Cliente encontrado:");
            System.out.println(cliente);
        }
    }

    private static void alterarCliente(ClienteDao clienteDao) {
        System.out.println("\n--- ALTERAR CLIENTE ---");

        int id = lerInteiro("Digite o ID do cliente: ");

        Cliente cliente = clienteDao.consultar(id);

        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        System.out.println("Cliente atual:");
        System.out.println(cliente);

        cliente.setCpf(lerTexto("Novo CPF: "));
        cliente.setNome(lerTexto("Novo nome: "));
        cliente.setEmail(lerTexto("Novo e-mail: "));
        cliente.setRua(lerTexto("Nova rua: "));
        cliente.setNumero(lerInteiroNaoNegativo("Novo número: "));
        cliente.setBairro(lerTexto("Novo bairro: "));
        cliente.setCep(lerTexto("Novo CEP: "));
        cliente.setCidade(lerTexto("Nova cidade: "));
        cliente.setEstado(lerEstado());

        clienteDao.alterar(cliente);

        System.out.println("Cliente alterado com sucesso!");
        System.out.println(clienteDao.consultar(id));
    }

    private static void deletarCliente(ClienteDao clienteDao) {
        System.out.println("\n--- DELETAR CLIENTE ---");

        int id = lerInteiro("Digite o ID do cliente: ");

        Cliente cliente = clienteDao.consultar(id);

        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        System.out.println("Cliente que será deletado:");
        System.out.println(cliente);

        String confirmacao = lerTexto(
                "Confirma a exclusão? Digite S para sim ou N para não: "
        );

        if (confirmacao.equalsIgnoreCase("s")) {
            clienteDao.deletar(id);
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    // =========================================================
// MENU DE PEDIDOS
// =========================================================

    private static void menuPedidos(
            PedidoDao pedidoDao,
            ClienteDao clienteDao,
            ProdutoDao produtoDao
    ) {
        int opcao;

        do {
            System.out.println("\n==================================");
            System.out.println("       MENU DE PEDIDOS");
            System.out.println("==================================");
            System.out.println("1 - Criar pedido");
            System.out.println("2 - Listar pedidos");
            System.out.println("3 - Consultar pedido por ID");
            System.out.println("4 - Deletar pedido");
            System.out.println("0 - Voltar");
            System.out.println("==================================");

            opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1:
                        criarPedido(pedidoDao, clienteDao, produtoDao);
                        break;

                    case 2:
                        listarPedidos(pedidoDao);
                        break;

                    case 3:
                        consultarPedido(pedidoDao);
                        break;

                    case 4:
                        deletarPedido(pedidoDao);
                        break;

                    case 0:
                        System.out.println("Voltando ao menu principal...");
                        break;

                    default:
                        System.out.println("Opção inválida.");
                }

            } catch (RuntimeException e) {
                System.err.println("Erro: " + e.getMessage());
            }

            if (opcao != 0) {
                pausar();
            }

        } while (opcao != 0);
    }

// =========================================================
// CRIAÇÃO DO PEDIDO E CARRINHO
// =========================================================

    private static void criarPedido(
            PedidoDao pedidoDao,
            ClienteDao clienteDao,
            ProdutoDao produtoDao
    ) {
        System.out.println("\n--- CRIAR PEDIDO ---");

        int idCliente = lerInteiro("Digite o ID do cliente: ");

        Cliente cliente = clienteDao.consultar(idCliente);

        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        int opcao;

        do {
            System.out.println("\n----------------------------------");
            System.out.println("CLIENTE: " + cliente.getNome());
            System.out.println("DATA: " + pedido.getData());
            System.out.println("STATUS: " + pedido.getStatus());
            System.out.println("----------------------------------");
            System.out.println("1 - Adicionar produto ao carrinho");
            System.out.println("2 - Remover produto do carrinho");
            System.out.println("3 - Visualizar carrinho");
            System.out.println("4 - Finalizar pedido");
            System.out.println("0 - Cancelar pedido");
            System.out.println("----------------------------------");

            opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1:
                        adicionarProdutoAoCarrinho(pedido, produtoDao);
                        break;

                    case 2:
                        removerProdutoDoCarrinho(pedido);
                        break;

                    case 3:
                        mostrarCarrinho(pedido);
                        break;

                    case 4:
                        pedido.finalizarPedido();
                        pedidoDao.salvar(pedido);

                        System.out.println(
                                "Pedido finalizado e salvo com sucesso!"
                        );

                        return;

                    case 0:
                        System.out.println("Pedido cancelado.");
                        return;

                    default:
                        System.out.println("Opção inválida.");
                }

            } catch (RuntimeException e) {
                // Se o salvamento falhar depois da finalização,
                // permite continuar trabalhando com o pedido.
                pedido.setStatus(Pedido.ABERTO);
                System.err.println("Erro: " + e.getMessage());
            }

        } while (opcao != 0);
    }

    private static void adicionarProdutoAoCarrinho(
            Pedido pedido,
            ProdutoDao produtoDao
    ) {
        System.out.println("\n--- ADICIONAR PRODUTO ---");

        int idProduto = lerInteiro("Digite o ID do produto: ");

        Produto produto = produtoDao.consultar(idProduto);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        pedido.adicionarNoCarrinho(produto);

        System.out.println("Produto adicionado ao carrinho:");
        System.out.println(produto);
    }

    private static void removerProdutoDoCarrinho(Pedido pedido) {
        System.out.println("\n--- REMOVER PRODUTO ---");

        if (pedido.getProdutos().isEmpty()) {
            System.out.println("O carrinho está vazio.");
            return;
        }

        mostrarCarrinho(pedido);

        int idProduto = lerInteiro(
                "Digite o ID do produto que deseja remover: "
        );

        boolean removido = pedido.removerDoCarrinho(idProduto);

        if (removido) {
            System.out.println("Produto removido do carrinho.");
        } else {
            System.out.println(
                    "Esse produto não foi encontrado no carrinho."
            );
        }
    }

    private static void mostrarCarrinho(Pedido pedido) {
        System.out.println("\n--- CARRINHO ---");

        if (pedido.getProdutos().isEmpty()) {
            System.out.println("O carrinho está vazio.");
            return;
        }

        int numeroItem = 1;

        for (Produto produto : pedido.getProdutos()) {
            System.out.println(
                    numeroItem +
                            " - ID: " + produto.getId() +
                            " | Nome: " + produto.getNome() +
                            " | Preço: R$ " + produto.getPreco()
            );

            numeroItem++;
        }

        System.out.println(
                "Total de itens: " + pedido.getProdutos().size()
        );
    }

// =========================================================
// CONSULTA E EXCLUSÃO DE PEDIDOS
// =========================================================

    private static void listarPedidos(PedidoDao pedidoDao) {
        System.out.println("\n--- LISTA DE PEDIDOS ---");

        List<Pedido> pedidos = pedidoDao.consultar();

        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }

        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
        }
    }

    private static void consultarPedido(PedidoDao pedidoDao) {
        System.out.println("\n--- CONSULTAR PEDIDO ---");

        int id = lerInteiro("Digite o ID do pedido: ");

        Pedido pedido = pedidoDao.consultar(id);

        if (pedido == null) {
            System.out.println("Pedido não encontrado.");
        } else {
            System.out.println("Pedido encontrado:");
            System.out.println(pedido);
        }
    }

    private static void deletarPedido(PedidoDao pedidoDao) {
        System.out.println("\n--- DELETAR PEDIDO ---");

        int id = lerInteiro("Digite o ID do pedido: ");

        Pedido pedido = pedidoDao.consultar(id);

        if (pedido == null) {
            System.out.println("Pedido não encontrado.");
            return;
        }

        System.out.println("Pedido que será deletado:");
        System.out.println(pedido);

        String confirmacao = lerTexto(
                "Confirma a exclusão? Digite S para sim ou N para não: "
        );

        if (confirmacao.equalsIgnoreCase("s")) {
            pedidoDao.deletar(id);
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    // =========================================================
    // MÉTODOS AUXILIARES DE ENTRADA
    // =========================================================

    private static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();

            if (!valor.isEmpty()) {
                return valor;
            }

            System.out.println("O campo não pode ficar vazio.");
        }
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Integer.parseInt(scanner.nextLine().trim());

            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private static int lerInteiroNaoNegativo(String mensagem) {
        while (true) {
            int valor = lerInteiro(mensagem);

            if (valor >= 0) {
                return valor;
            }

            System.out.println("O valor não pode ser negativo.");
        }
    }

    private static double lerDoubleNaoNegativo(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);

                String valor = scanner.nextLine()
                        .trim()
                        .replace(",", ".");

                double numero = Double.parseDouble(valor);

                if (numero >= 0) {
                    return numero;
                }

                System.out.println("O valor não pode ser negativo.");

            } catch (NumberFormatException e) {
                System.out.println("Digite um preço válido.");
            }
        }
    }

    private static String lerEstado() {
        while (true) {
            String estado = lerTexto("Estado (UF): ").toUpperCase();

            if (estado.length() == 2) {
                return estado;
            }

            System.out.println("Digite a sigla do estado com 2 letras. Exemplo: SP");
        }
    }

    private static void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
}