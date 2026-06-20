package com.yama.finplus.repository;

import com.yama.finplus.domain.Financeiro;
import com.yama.finplus.domain.enums.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {
    List<Financeiro> findByTipo(TipoMovimentacao tipo);

    //Fazer uma soma com o tipo desejado
    @Query("select sum (f.valor) from Financeiro f where f.tipo = :tipo")
    BigDecimal findByTipoAndSumTipo(@Param("tipo") TipoMovimentacao tipoMovimentacao);

}
