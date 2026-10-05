package br.edu.fatec.todo.service;

import br.edu.fatec.todo.dto.TarefaRequest;
import br.edu.fatec.todo.exception.TarefaNaoEncontradaException;
import br.edu.fatec.todo.model.StatusTarefa;
import br.edu.fatec.todo.model.Tarefa;
import br.edu.fatec.todo.repository.TarefaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository repository;

    @InjectMocks
    private TarefaService service;

    @Test
    @DisplayName("Deve criar uma tarefa com os dados informados")
    void deveCriarTarefa() {

        when(repository.save(any(Tarefa.class))).thenAnswer(inv -> inv.getArgument(0));
        TarefaRequest dados = new TarefaRequest("Estudar", "Testes unitarios", null, "Obs");


        Tarefa criada = service.criar(dados);


        assertEquals("Estudar", criada.getNome());
        assertEquals("Testes unitarios", criada.getDescricao());
        verify(repository).save(any(Tarefa.class)); // confere se o save foi chamado
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar tarefa que não existe")
    void deveLancarErroAoBuscarInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TarefaNaoEncontradaException.class, () -> service.buscarPorId(99L));
    }

    @Test
    @DisplayName("Deve alterar os dados de uma tarefa existente")
    void deveAtualizarTarefa() {
        Tarefa existente = new Tarefa();
        existente.setId(1L);
        existente.setNome("Nome antigo");
        existente.setStatus(StatusTarefa.PENDENTE);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Tarefa.class))).thenAnswer(inv -> inv.getArgument(0));

        TarefaRequest novosDados = new TarefaRequest("Nome novo", "Desc", StatusTarefa.CONCLUIDA, null);
        Tarefa atualizada = service.atualizar(1L, novosDados);

        assertEquals("Nome novo", atualizada.getNome());
        assertEquals(StatusTarefa.CONCLUIDA, atualizada.getStatus());
    }

    @Test
    @DisplayName("Deve deletar uma tarefa existente")
    void deveDeletarTarefa() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deletar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Não deve deletar tarefa que não existe")
    void naoDeveDeletarInexistente() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(TarefaNaoEncontradaException.class, () -> service.deletar(99L));
        verify(repository, never()).deleteById(any()); // garante que NÃO tentou apagar
    }
}