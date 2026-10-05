CREATE TABLE tarefa (
    id               BIGSERIAL PRIMARY KEY,
    nome             VARCHAR(100) NOT NULL,
    descricao        TEXT,
    status           VARCHAR(20) NOT NULL DEFAULT 'PENDENTE'
                     CHECK (status IN ('PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDA')),
    observacoes      TEXT,
    data_criacao     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);