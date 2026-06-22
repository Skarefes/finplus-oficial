-- Primeiro derruba a tabela que foi criada errada manualmente se existir

DROP table if exists cartao;

-- Depois recria ela do zero com o tipo certo

CREATE TABLE cartao(
    id bigserial primary key,
    proprietario varchar(100),
    banco varchar(100),
    final_cartao varchar(4)
)

-- O final_cartao ficou com varchar 255
-- Irei alterar manualmente com um novo V2