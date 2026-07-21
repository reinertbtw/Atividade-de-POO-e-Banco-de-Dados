import modelos.Produto;
import dao.ProdutoDao; // Importar a classe ProdutoDao

public class Main {
    public static void main(String[] args) {
        // 1. Criar um objeto Produto
        Produto novoProduto = new Produto();
        novoProduto.setNome("Camiseta Java");
        novoProduto.setPreco(49.99);
        novoProduto.setEstoque(100);

        // 2. Criar uma instância do ProdutoDao
        ProdutoDao produtoDao = new ProdutoDao();

        // Variável para armazenar o produto salvo, inicializada como null
        Produto produtoSalvo = null;

        // 3. Chamar o método salvar para persistir o produto no banco
        try {
            produtoSalvo = produtoDao.salvar(novoProduto); // Salva o produto e atribui à variável
            System.out.println("Produto salvo com sucesso no banco de dados:");
            System.out.println(produtoSalvo); // Imprime o produto com o ID gerado

            // 4. Testar o método consultar(Integer id)
            if (produtoSalvo != null && produtoSalvo.getId() != 0) { // Verifica se o produto foi salvo e tem ID
                Produto produtoConsultado = produtoDao.consultar(produtoSalvo.getId());

                System.out.println("\n--- Teste de Consulta por ID ---");
                if (produtoConsultado != null) {
                    System.out.println("Produto consultado com sucesso:");
                    System.out.println(produtoConsultado);
                } else {
                    System.out.println("Produto com ID " + produtoSalvo.getId() + " não encontrado.");
                }
            } else {
                System.out.println("Não foi possível consultar o produto, pois ele não foi salvo ou não possui ID.");
            }

        } catch (RuntimeException e) {
            System.err.println("Erro ao tentar salvar ou consultar o produto: " + e.getMessage());
            e.printStackTrace(); // Imprime o stack trace completo para depuração
        }
    }
}