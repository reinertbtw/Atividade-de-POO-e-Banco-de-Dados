import dao.ProdutoDao;
import modelos.Produto;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ProdutoDao produtoDao = new ProdutoDao();
        int opcao;

        do {
            exibirMenu();
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
                        System.out.println("Programa encerrado.");
                        break;

                    default:
                        System.out.println("Opção inválida.");
                }

            } catch (RuntimeException e) {
                System.err.println("Erro: " + e.getMessage());
            }

            if (opcao != 0) {
                System.out.println("\nPressione ENTER para continuar...");
                scanner.nextLine();
            }

        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n========== MENU DE PRODUTOS ==========");
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Listar produtos");
        System.out.println("3 - Consultar produto por ID");
        System.out.println("4 - Alterar produto");
        System.out.println("5 - Deletar produto");
        System.out.println("0 - Sair");
        System.out.println("======================================");
    }

    private static void cadastrarProduto(ProdutoDao produtoDao) {
        System.out.println("\n--- CADASTRAR PRODUTO ---");

        String nome = lerTexto("Nome: ");
        double preco = lerDouble("Preço: ");
        int estoque = lerInteiro("Estoque: ");

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
        double preco = lerDouble("Novo preço: ");
        int estoque = lerInteiro("Novo estoque: ");

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

        String confirmacao = lerTexto("Confirma a exclusão? Digite S para sim ou N para não: ");

        if (confirmacao.equalsIgnoreCase("s")) {
            produtoDao.deletar(id);
            System.out.println("Produto deletado com sucesso.");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

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

    private static double lerDouble(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);

                String valor = scanner.nextLine()
                        .trim()
                        .replace(",", ".");

                double numero = Double.parseDouble(valor);

                if (numero < 0) {
                    System.out.println("O valor não pode ser negativo.");
                } else {
                    return numero;
                }

            } catch (NumberFormatException e) {
                System.out.println("Digite um preço válido.");
            }
        }
    }
}