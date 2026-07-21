package modelos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    public static final String ABERTO = "ABERTO";
    public static final String FINALIZADO = "FINALIZADO";

    private Integer id;
    private Cliente cliente;
    private LocalDate data;
    private String status;
    private List<Produto> produtos;

    public Pedido() {
        this.data = LocalDate.now();
        this.status = ABERTO;
        this.produtos = new ArrayList<>();
    }

    public void adicionarNoCarrinho(Produto produto) {
        if (!ABERTO.equalsIgnoreCase(status)) {
            throw new IllegalStateException(
                    "Não é possível adicionar produtos a um pedido finalizado."
            );
        }

        if (produto == null) {
            throw new IllegalArgumentException(
                    "O produto não pode ser nulo."
            );
        }

        if (produto.getId() == null || produto.getId() <= 0) {
            throw new IllegalArgumentException(
                    "O produto precisa estar cadastrado no banco."
            );
        }

        if (produto.getEstoque() <= 0) {
            throw new IllegalArgumentException(
                    "O produto não possui estoque disponível."
            );
        }

        int quantidadeNoCarrinho = 0;

        for (Produto item : produtos) {
            if (item.getId() != null &&
                    item.getId().equals(produto.getId())) {
                quantidadeNoCarrinho++;
            }
        }

        if (quantidadeNoCarrinho >= produto.getEstoque()) {
            throw new IllegalArgumentException(
                    "A quantidade solicitada ultrapassa o estoque disponível."
            );
        }

        produtos.add(produto);
    }

    public boolean removerDoCarrinho(Integer idProduto) {
        if (!ABERTO.equalsIgnoreCase(status)) {
            throw new IllegalStateException(
                    "Não é possível remover produtos de um pedido finalizado."
            );
        }

        if (idProduto == null) {
            return false;
        }

        for (int i = 0; i < produtos.size(); i++) {
            Produto produto = produtos.get(i);

            if (produto.getId() != null &&
                        produto.getId().equals(idProduto)) {
                produtos.remove(i);
                return true;
            }
        }

        return false;
    }

    public void finalizarPedido() {
        if (cliente == null || cliente.getId() == null) {
            throw new IllegalStateException(
                    "O pedido precisa possuir um cliente."
            );
        }

        if (produtos.isEmpty()) {
            throw new IllegalStateException(
                    "Não é possível finalizar um pedido sem produtos."
            );
        }

        if (!ABERTO.equalsIgnoreCase(status)) {
            throw new IllegalStateException(
                    "Este pedido já foi finalizado."
            );
        }

        status = FINALIZADO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        if (produtos == null) {
            this.produtos = new ArrayList<>();
        } else {
            this.produtos = new ArrayList<>(produtos);
        }
    }

    @Override
    public String toString() {
        String clienteInfo = cliente == null
                ? "null"
                : cliente.getId() + " - " + cliente.getNome();

        return "Pedido{" +
                "id=" + id +
                ", cliente=" + clienteInfo +
                ", data=" + data +
                ", status='" + status + '\'' +
                ", produtos=" + produtos +
                '}';
    }
}