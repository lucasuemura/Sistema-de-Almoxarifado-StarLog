package br.com.starlog;

import br.com.starlog.dao.ProdutoDAO;
import br.com.starlog.dao.ProdutoDAOSqlite;
import br.com.starlog.model.Produto;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProdutoDAO dao = new ProdutoDAOSqlite();

        // 1. [Create]
        System.out.println("=== 1. [CREATE] Cadastrando Produtos ===");
        Produto teclado = new Produto("Teclado Mecânico RGB", 350.00, 8);
        Produto mousepad = new Produto("Mousepad Speed XL", 89.90, 20);

        dao.salvar(teclado);
        dao.salvar(mousepad);

        System.out.println("Produto 1 salvo com ID: " + teclado.getId());
        System.out.println("Produto 2 salvo com ID: " + mousepad.getId());

        // 2. [Read]
        System.out.println("\n=== 2. [READ] Listagem Inicial ===");
        imprimirTabela(dao.listarTodos());

        // 3. [Update]
        System.out.println("\n=== 3. [UPDATE] Atualizando Teclado ===");
        teclado.setEstoque(15);
        teclado.setPreco(289.90);
        boolean atualizado = dao.atualizar(teclado);
        System.out.println("Atualização confirmada (true/false): " + atualizado);
        dao.buscarPorId(teclado.getId()).ifPresent(System.out::println);

        // 4. [Delete]
        System.out.println("\n=== 4. [DELETE] Removendo Itens ===");
        boolean excluiuMousepad = dao.excluir(mousepad.getId());
        System.out.println("Mousepad removido? " + excluiuMousepad);

        boolean excluiuInexistente = dao.excluir(888L);
        if (!excluiuInexistente) {
            System.out.println("ID 888 não encontrado no banco (retorno: false). Operação segura.");
        }

        // 5. [Final State]
        System.out.println("\n=== 5. [FINAL STATE] Estado Final do Banco ===");
        List<Produto> restantes = dao.listarTodos();
        imprimirTabela(restantes);
        System.out.println("Total de itens restantes: " + restantes.size());
    }

    private static void imprimirTabela(List<Produto> produtos) {
        System.out.println("+------+---------------------------+--------------+-------------+");
        System.out.println("| ID   | NOME                      | PREÇO        | ESTOQUE     |");
        System.out.println("+------+---------------------------+--------------+-------------+");
        for (Produto p : produtos) {
            System.out.println(p);
        }
        System.out.println("+------+---------------------------+--------------+-------------+");
    }
}