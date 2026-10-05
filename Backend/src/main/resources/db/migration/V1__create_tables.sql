Create table usuario(
    id Bigserial primary key,
    username VARCHAR(255) not null unique,
    email varchar(255) not null unique,
    senha varchar(255) not null
);

CREATE TABLE cartao (
                        id BIGSERIAL PRIMARY KEY,
                        proprietario VARCHAR(255),
                        banco VARCHAR(255),
                        final_cartao VARCHAR(4)
);

CREATE TABLE financeiro (
                            id BIGSERIAL PRIMARY KEY,
                            nome VARCHAR(255),
                            valor NUMERIC(19, 2),
                            descricao VARCHAR(255),
                            data TIMESTAMP,
                            tipo VARCHAR(50),
                            categoria VARCHAR(50),
                            forma_pagamento VARCHAR(50),

                            cartao_id BIGINT,
                            usuario_id BIGINT NOT NULL,

                            CONSTRAINT fk_financeiro_cartao
                                FOREIGN KEY (cartao_id)
                                    REFERENCES cartao(id),

                            CONSTRAINT fk_financeiro_usuario
                                FOREIGN KEY (usuario_id)
                                    REFERENCES usuario(id)
);

CREATE TABLE parcelas (
                          id BIGSERIAL PRIMARY KEY,
                          numero_parcela INTEGER,
                          total_parcelas INTEGER,
                          valor_parcela NUMERIC(19, 2),
                          status_pagamento VARCHAR(50),

                          financeiro_id BIGINT NOT NULL,

                          CONSTRAINT fk_parcela_financeiro
                              FOREIGN KEY (financeiro_id)
                                  REFERENCES financeiro(id)
);