package com.yama.finplus.repository;

import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import com.yama.finplus.domain.financeiro.Financeiro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {
    @EntityGraph(attributePaths = "parcelas")
    List<Financeiro> findAllByUsuario_Email(String email);

    List<Financeiro> findByTipoAndUsuario_Email(
            TipoMovimentacao tipo,
            String email
    );

    Optional<Financeiro> findByIdAndUsuario_Email(
            Long id,
            String email
    );

    @Query("""
        SELECT SUM(f.valor)
        FROM Financeiro f
        WHERE f.tipo = :tipo
          AND f.usuario.email = :email
        """)
    BigDecimal somarPorTipoEUsuario(
            @Param("tipo") TipoMovimentacao tipo,
            @Param("email") String email
    );

    @EntityGraph(attributePaths = "parcelas")
    @Query("""
    select f from Financeiro f 
    where f.usuario.email = :email and f.data >= :inicio and f.data < :fim 
    order by f.data desc
    """)
    List<Financeiro> listarPorPeriodo(@Param("email") String email,
                                      @Param("inicio") LocalDateTime inicio,
                                      @Param("fim") LocalDateTime fim);
}
