package br.edu.fatec.todo.exception;

public class TarefaNaoEncontradaException extends RuntimeException {

    public TarefaNaoEncontradaException(Long id) {
        super("Tarefa com id " + id + " não encontrada");
    }
}