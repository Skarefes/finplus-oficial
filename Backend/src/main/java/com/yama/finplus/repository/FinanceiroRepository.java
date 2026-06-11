package com.yama.finplus.repository;

import com.yama.finplus.domain.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {
}
