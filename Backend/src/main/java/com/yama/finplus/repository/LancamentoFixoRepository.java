package com.yama.finplus.repository;

import com.yama.finplus.domain.financeiro.fixos.LancamentoFixo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LancamentoFixoRepository extends JpaRepository<LancamentoFixo, Long> {

    List<LancamentoFixo> findAllByUsuario_Email(String email);

    Optional<LancamentoFixo> findByIdAndUsuario_Email(Long id, String email);

    // fixos que ainda não foram gerados até o mês pedido
    List<LancamentoFixo> findAllByUsuario_EmailAndGeradoAteBefore(String email, LocalDate mes);

}
