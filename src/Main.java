import modelos.Produto;
import dao.ProdutoDao;
import java.util.List; // Importar List para o método consultar()

public class Main {
    public static void main(String[] args) {
        // 1. Criar uma instância do ProdutoDao
        ProdutoDao produtoDao = new ProdutoDao();

        // Variável para armazenar o produto salvo, inicializada como null
        Produto produtoSalvo = null;

        try {
            // --- TESTE: SALVAR PRODUTO ---
            System.out.println("--- TESTE: SALVAR PRODUTO ---");
            Produto novoProduto = new Produto();
            novoProduto.setNome("Camiseta Java");
            novoProduto.setPreco(49.99);
            novoProduto.setEstoque(100);

            produtoSalvo = produtoDao.salvar(novoProduto);
            System.out.println("Produto salvo com sucesso no banco de dados:");
            System.out.println(produtoSalvo);

            // --- TESTE: CONSULTAR PRODUTO POR ID ---
            System.out.println("\n--- TESTE: CONSULTAR PRODUTO POR ID ---");
            if (produtoSalvo != null && produtoSalvo.getId() != 0) {
                Produto produtoConsultado = produtoDao.consultar(produtoSalvo.getId());

                if (produtoConsultado != null) {
                    System.out.println("Produto consultado com sucesso:");
                    System.out.println(produtoConsultado);
                } else {
                    System.out.println("Produto com ID " + produtoSalvo.getId() + " não encontrado.");
                }
            } else {
                System.out.println("Não foi possível consultar o produto, pois ele não foi salvo ou não possui ID.");
            }

            // --- TESTE: LISTAR TODOS OS PRODUTOS ---
            System.out.println("\n--- TESTE: LISTAR TODOS OS PRODUTOS ---");
            List<Produto> produtos = produtoDao.consultar();
            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto cadastrado.");
            } else {
                System.out.println("Produtos cadastrados:");
                for (Produto produto : produtos) {
                    System.out.println(produto);
                }
            }

            // --- TESTE: ALTERAR PRODUTO ---
            System.out.println("\n--- TESTE: ALTERAR PRODUTO ---");
            if (produtoSalvo != null && produtoSalvo.getId() != 0) {
                produtoSalvo.setNome("Camiseta Java Atualizada");
                produtoSalvo.setPreco(59.90);
                produtoSalvo.setEstoque(80);

                produtoDao.alterar(produtoSalvo);

                System.out.println("Produto após alteração:");
                Produto produtoAlterado = produtoDao.consultar(produtoSalvo.getId());
                System.out.println(produtoAlterado);
            } else {
                System.out.println("Não foi possível alterar o produto, pois ele não foi salvo ou não possui ID.");
            }

            // --- TESTE: DELETAR PRODUTO ---
            System.out.println("\n--- TESTE: DELETAR PRODUTO ---");
            if (produtoSalvo != null && produtoSalvo.getId() != 0) {
                produtoDao.deletar(produtoSalvo.getId());
                // Tenta consultar para confirmar que foi deletado
                Produto produtoDeletado = produtoDao.consultar(produtoSalvo.getId());
                if (produtoDeletado == null) {
                    System.out.println("Confirmação: Produto com ID " + produtoSalvo.getId() + " não encontrado após deleção.");
                } else {
                    System.out.println("Erro: Produto com ID " + produtoSalvo.getId() + " ainda existe após deleção.");
                }
            } else {
                System.out.println("Não foi possível deletar o produto, pois ele não foi salvo ou não possui ID.");
            }

        } catch (RuntimeException e) {
            System.err.println("Ocorreu um erro durante a execução: " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace completo para depuração
        }
    }
}