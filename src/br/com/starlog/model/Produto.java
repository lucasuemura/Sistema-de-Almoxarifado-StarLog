package br.com.starlog.model;

public class Produto {
    private Long id;
    private String nome;
    private double preco;
    private int estoque;

    // Construtor de negócio (sem ID)
    public Produto(String nome, double preco, int estoque) {
        if (nome == null || nome.trim().length() < 3) {
            throw new IllegalArgumentException("O nome do produto deve possuir ao menos 3 caracteres.");
        }
        setPreco(preco);
        setEstoque(estoque);
        this.nome = nome.trim();
    }

    // Construtor de persistência (com ID)
    public Produto(Long id, String nome, double preco, int estoque) {
        this(nome, preco, estoque);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco <= 0.0) {
            throw new IllegalArgumentException("O preço deve ser maior que 0.0.");
        }
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        if (estoque < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }
        this.estoque = estoque;
    }

    @Override
    public String toString() {
        return String.format("| %-4d | %-25s | R$ %9.2f | %-7d un. |", id, nome, preco, estoque);
    }
}