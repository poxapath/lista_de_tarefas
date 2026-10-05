package br.edu.fatec.todo.service;

import br.edu.fatec.todo.dto.TarefaRequest;
import br.edu.fatec.todo.exception.TarefaNaoEncontradaException;
import br.edu.fatec.todo.model.Tarefa;
import br.edu.fatec.todo.repository.TarefaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository repository;

    public TarefaService(TarefaRepository repository) {
        this.repository = repository;
    }

    // listar tarefas
    @Transactional(readOnly = true)
    public List<Tarefa> listar() {
        return repository.findAll();
    }

    // buscar tarefa pelo id
    @Transactional(readOnly = true)
    public Tarefa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));
    }

    // cirar nova tarefa
    @Transactional
    public Tarefa criar(TarefaRequest dados) {
        Tarefa tarefa = new Tarefa();
        tarefa.setNome(dados.nome());
        tarefa.setDescricao(dados.descricao());
        tarefa.setStatus(dados.status()); // se vier vazio, vira PENDENTE sozinho
        tarefa.setObservacoes(dados.observacoes());
        return repository.save(tarefa);
    }

    // alterar tarefa existente
    @Transactional
    public Tarefa atualizar(Long id, TarefaRequest dados) {
        Tarefa tarefa = buscarPorId(id); // se não existir, já dispara o erro

        tarefa.setNome(dados.nome());
        tarefa.setDescricao(dados.descricao());
        tarefa.setObservacoes(dados.observacoes());
        if (dados.status() != null) {
            tarefa.setStatus(dados.status());
        }
        return repository.save(tarefa);
    }

    // deletar tarefa
    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new TarefaNaoEncontradaException(id);
        }
        repository.deleteById(id);
    }
}