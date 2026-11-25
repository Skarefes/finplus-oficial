ALTER TABLE parcelas ADD COLUMN competencia DATE;

-- parcelas antigas: mês da compra + (número da parcela - 1) meses
UPDATE parcelas p
SET competencia = (date_trunc('month', f.data) + (p.numero_parcela - 1) * INTERVAL '1 month')::date
FROM financeiro f
WHERE f.id = p.financeiro_id;

ALTER TABLE parcelas ALTER COLUMN competencia SET NOT NULL;

CREATE INDEX idx_parcelas_competencia ON parcelas (competencia);