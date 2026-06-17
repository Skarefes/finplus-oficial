package com.yama.finplus.repository;

import com.yama.finplus.domain.Financeiro;
import com.yama.finplus.domain.enums.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {
    List<Financeiro> findByTipo(TipoMovimentacao tipo);

}
