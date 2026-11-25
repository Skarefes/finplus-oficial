CREATE TABLE lancamento_fixo (
                                 id          BIGSERIAL PRIMARY KEY,
                                 usuario_id  BIGINT        NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
                                 nome        VARCHAR(100)  NOT NULL,
                                 valor       NUMERIC(12,2) NOT NULL,
                                 tipo        VARCHAR(20)   NOT NULL,
                                 categoria   VARCHAR(20)   NOT NULL,
                                 dia_do_mes  INTEGER       NOT NULL CHECK (dia_do_mes BETWEEN 1 AND 28),
                                 gerado_ate  DATE          NOT NULL  -- último mês (dia 1) que já foi gerado
);

ALTER TABLE financeiro
    ADD COLUMN fixo_id          BIGINT REFERENCES lancamento_fixo(id) ON DELETE SET NULL,
    ADD COLUMN competencia_fixo DATE;

-- rede de segurança: o mesmo fixo nunca gera duas vezes o mesmo mês
CREATE UNIQUE INDEX uq_financeiro_fixo_competencia ON financeiro (fixo_id, competencia_fixo);