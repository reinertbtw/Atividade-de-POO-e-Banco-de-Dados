-- Cria o banco de dados 'loja_poo'
CREATE DATABASE loja_poo;

-- Seleciona o banco de dados 'loja_poo' para uso
USE loja_poo;

-- Cria a tabela 'produto'
CREATE TABLE produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL,
    estoque INT NOT NULL
) ENGINE = InnoDB;

-- Cria a tabela 'cliente'
CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    rua VARCHAR(150) NOT NULL,
    numero INT NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado CHAR(2) NOT NULL
) ENGINE = InnoDB;

-- Cria a tabela 'pedido'
CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    data_pedido DATE NOT NULL,
    status VARCHAR(20) NOT NULL,

    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES cliente(id)
) ENGINE = InnoDB;

-- Cria a tabela 'pedido_produto' (tabela de junção para o relacionamento N:N entre pedido e produto)
CREATE TABLE pedido_produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    produto_id INT NOT NULL,

    CONSTRAINT fk_pedido_produto_pedido
        FOREIGN KEY (pedido_id)
        REFERENCES pedido(id)
        ON DELETE CASCADE, -- Se um pedido for deletado, seus itens em pedido_produto também serão

    CONSTRAINT fk_pedido_produto_produto
        FOREIGN KEY (produto_id)
        REFERENCES produto(id)
) ENGINE = InnoDB;

SELECT * FROM cliente;
SELECT * FROM produto;
SELECT * FROM pedido;
