package com.yama.finplus.repository;

import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import com.yama.finplus.domain.financeiro.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {
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
}
