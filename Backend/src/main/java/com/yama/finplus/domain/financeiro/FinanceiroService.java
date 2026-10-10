package com.yama.finplus.domain.financeiro;

import com.yama.finplus.domain.cartao.ParcelaService;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import com.yama.finplus.domain.usuario.Usuario;
import com.yama.finplus.infra.exceptions.FormaPagamentoNaoAutorizadaException;
import com.yama.finplus.repository.FinanceiroRepository;
import com.yama.finplus.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FinanceiroService {

    private final FinanceiroRepository financeiroRepository;
    private final ParcelaService parcelaService;
    private final UsuarioRepository usuarioRepository;

    public FinanceiroService(FinanceiroRepository financeiroRepository, ParcelaService parcelaService, UsuarioRepository usuarioRepository) {
        this.financeiroRepository = financeiroRepository;
        this.parcelaService = parcelaService;
        this.usuarioRepository = usuarioRepository;
    }

    //Funcao para colocar as transicoes de gastos e ganhos
    @Transactional
    public DadosDetalhamentoFinanceiro registrar(DadosCadastroFinanceiro dados, String email) {

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Usuario não encontrado"));

        Integer quantidade = dados.quantidadeParcelas();

        //Se a quantidade for maior que 1 e a forma de pagamento nao permitir parcelamento, gera uma excecao
        if (dados.quantidadeParcelasFeitas() > 1 && !dados.formaPagamento().permiteParcelamento()){
            throw new FormaPagamentoNaoAutorizadaException("A forma de pagamento não permite parcelamento");
        }

        var financeiro = new Financeiro(dados);

        //Vincula a transacao ao usuario autenticado
        financeiro.setUsuario(usuario);

        financeiroRepository.save(financeiro);

        //Toda transação tera uma parcela, mesmo nao precisando, ajudando na logica futura
        parcelaService.gerarParcelas(financeiro, quantidade);
        return new DadosDetalhamentoFinanceiro(financeiro);
    }

    //Função para pegar todos os dados
    public List<DadosDetalhamentoFinanceiro> listarTudo(String email) {
        return financeiroRepository.findAllByUsuario_Email(email).stream()
                .map(DadosDetalhamentoFinanceiro::new).toList();
    }

    //Função para filtrar a lista do tipo e enviar conforme requisitado pelo URL
    public List<DadosDetalhamentoFinanceiro> listarPorTipo(TipoMovimentacao tipo, String email) {
        return financeiroRepository.findByTipoAndUsuario_Email(tipo, email).stream()
                .map(DadosDetalhamentoFinanceiro::new).toList();
    }

    @Transactional
    //Funcao para editar um item
    public DadosDetalhamentoFinanceiro editarDados(Long id, DadosAtualizacaoFinanceiro dados, String email) {
        //identificador fincaneiro ele pega o repository do Financeiro que ja e o objeto pra poder editar
        var identificadorFinanceiro = financeiroRepository.findByIdAndUsuario_Email(id, email).orElseThrow();
        identificadorFinanceiro.atualizarDados(dados);
        //retorna um novo DTO com os novos dados
        return new DadosDetalhamentoFinanceiro(identificadorFinanceiro);
    }

    //Funcao que deleta um item
    @Transactional
    public void removerDados(Long id, String email) {
        var financeiro = financeiroRepository
                .findByIdAndUsuario_Email(id, email).orElseThrow();

        financeiroRepository.delete(financeiro);
    }

}
