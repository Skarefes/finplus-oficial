package com.yama.finplus.repository;

import com.yama.finplus.domain.cartao.Parcela;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ParcelaRepository extends JpaRepository<Parcela, Long> {

    List<Parcela> findAllByFinanceiro_IdAndFinanceiro_Usuario_Email(
            Long idFinanceiro, String email
    );

    @Query("""
    SELECT p FROM Parcela p
    JOIN FETCH p.financeiro f
    WHERE f.usuario.email = :email
      AND p.competencia = :mes
    ORDER BY f.data DESC, p.numeroParcela
    """)
    List<Parcela> listarPorCompetencia(@Param("email") String email, @Param("mes") LocalDate mes);

    // soma só o que já "chegou": parcelas do mês atual para trás
    @Query("""
    SELECT SUM(p.valorParcela) FROM Parcela p
    WHERE p.financeiro.tipo = :tipo
      AND p.financeiro.usuario.email = :email
      AND p.competencia <= :ate
    """)
    BigDecimal somarAteCompetencia(@Param("tipo") TipoMovimentacao tipo,
                                   @Param("email") String email,
                                   @Param("ate") LocalDate ate);
}
