package br.edu.fatec.todo.controller;

import br.edu.fatec.todo.dto.TarefaRequest;
import br.edu.fatec.todo.model.Tarefa;
import br.edu.fatec.todo.repository.TarefaRepository;
import br.edu.fatec.todo.service.TarefaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest   // liga o sistema completo
@Transactional    // desfaz tudo no banco ao final de cada teste
class TarefaControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TarefaService service;

    @Autowired
    private TarefaRepository repository;

    private MockMvc mockMvc;

    @BeforeEach
    void configurar() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("POST /tarefas deve criar tarefa e retornar 201")
    void deveCriarTarefa() throws Exception {
        String json = """
                {
                  "nome": "Tarefa de integracao",
                  "descricao": "Criada pelo teste"
                }
                """;

        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Tarefa de integracao"))
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    @DisplayName("POST /tarefas sem nome deve retornar 400")
    void naoDeveCriarSemNome() throws Exception {
        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nome\": \"\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").exists());
    }

    @Test
    @DisplayName("PUT /tarefas/{id} deve alterar a tarefa")
    void deveAlterarTarefa() throws Exception {
        Tarefa existente = service.criar(new TarefaRequest("Original", null, null, null));

        String json = """
                {
                  "nome": "Alterada",
                  "status": "CONCLUIDA"
                }
                """;

        mockMvc.perform(put("/tarefas/" + existente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Alterada"))
                .andExpect(jsonPath("$.status").value("CONCLUIDA"));
    }

    @Test
    @DisplayName("DELETE /tarefas/{id} deve apagar a tarefa e retornar 204")
    void deveDeletarTarefa() throws Exception {
        Tarefa existente = service.criar(new TarefaRequest("Para apagar", null, null, null));

        mockMvc.perform(delete("/tarefas/" + existente.getId()))
                .andExpect(status().isNoContent());

        assertFalse(repository.existsById(existente.getId()));
    }

    @Test
    @DisplayName("GET /tarefas/{id} inexistente deve retornar 404")
    void deveRetornar404() throws Exception {
        mockMvc.perform(get("/tarefas/999999"))
                .andExpect(status().isNotFound());
    }
}