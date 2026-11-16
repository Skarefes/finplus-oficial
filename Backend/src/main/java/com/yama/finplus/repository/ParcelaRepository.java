package com.yama.finplus.repository;

import com.yama.finplus.domain.cartao.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParcelaRepository extends JpaRepository<Parcela, Long> {

    List<Parcela> findAllByFinanceiro_IdAndFinanceiro_Usuario_Email(
            Long idFinanceiro, String email
    );
}
