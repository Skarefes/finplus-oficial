package com.yama.finplus.domain.resumo;

import com.yama.finplus.domain.cartao.ParcelaService;
import com.yama.finplus.domain.financeiro.DadosResumoFinanceiro;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import com.yama.finplus.domain.usuario.DadosTeto;
import com.yama.finplus.domain.usuario.Usuario;
import com.yama.finplus.repository.FinanceiroRepository;
import com.yama.finplus.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ResumoFincanceiroService {

    final private FinanceiroRepository financeiroRepository;
    private final UsuarioRepository usuarioRepository;

    public ResumoFincanceiroService(FinanceiroRepository financeiroRepository, ParcelaService parcelaService, UsuarioRepository usuarioRepository) {
        this.financeiroRepository = financeiroRepository;

        this.usuarioRepository = usuarioRepository;
    }

    //Funcao que vai somar as receitas e despesas totais, para cada usuario
    public DadosResumoFinanceiro somaTipos(String email) {
        BigDecimal receitas = Optional.ofNullable(financeiroRepository
                .somarPorTipoEUsuario(TipoMovimentacao.RECEITA, email)).orElse(BigDecimal.ZERO);

        BigDecimal despesas = Optional.ofNullable(financeiroRepository
                .somarPorTipoEUsuario(TipoMovimentacao.DESPESA, email)).orElse(BigDecimal.ZERO);

        return new DadosResumoFinanceiro(receitas, despesas);
    }

    //Funcao que vai analisar o saldo total entre receita e despesas, para cada usuario
    public BigDecimal calcularSaldoTotal(String email) {
        DadosResumoFinanceiro resumo =  this.somaTipos(email);
        return resumo.totalReceita().subtract(resumo.totalDespesa());
    }

    public DadosTeto buscarTeto(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
        return new DadosTeto(usuario.getTetoMensal());
    }

    @Transactional
    public DadosTeto definirTeto(DadosTeto dados, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
        usuario.setTetoMensal(dados.teto()); // sem save(): o @Transactional já grava a alteração
        return new DadosTeto(usuario.getTetoMensal());
    }

}
