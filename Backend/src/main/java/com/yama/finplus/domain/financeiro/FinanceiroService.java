package com.yama.finplus.domain.financeiro;

import com.yama.finplus.domain.cartao.DadosLancamentoMensal;
import com.yama.finplus.domain.cartao.Parcela;
import com.yama.finplus.domain.cartao.ParcelaService;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import com.yama.finplus.domain.financeiro.fixos.FixoService;
import com.yama.finplus.domain.usuario.Usuario;
import com.yama.finplus.infra.exceptions.FormaPagamentoNaoAutorizadaException;
import com.yama.finplus.infra.exceptions.TransacaoParceladaExpcetion;
import com.yama.finplus.repository.FinanceiroRepository;
import com.yama.finplus.repository.ParcelaRepository;
import com.yama.finplus.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class FinanceiroService {

    private final FinanceiroRepository financeiroRepository;
    private final ParcelaService parcelaService;
    private final UsuarioRepository usuarioRepository;
    private final FixoService fixoService;
    private final ParcelaRepository parcelaRepository;

    public FinanceiroService(FinanceiroRepository financeiroRepository, ParcelaService parcelaService, UsuarioRepository usuarioRepository, FixoService fixoService, ParcelaRepository parcelaRepository) {
        this.financeiroRepository = financeiroRepository;
        this.parcelaService = parcelaService;
        this.usuarioRepository = usuarioRepository;
        this.fixoService = fixoService;
        this.parcelaRepository = parcelaRepository;
    }

    //Funcao para colocar as transicoes de gastos e ganhos
    @Transactional
    public DadosDetalhamentoFinanceiro registrar(DadosCadastroFinanceiro dados, String email) {

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Usuario não encontrado"));

        //Integer quantidade = dados.quantidadeParcelas();
        Integer quantidade = dados.quantidadeParcelasFeitas();

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

    //@Transactional
    //Funcao para editar um item
    //public DadosDetalhamentoFinanceiro editarDados(Long id, DadosAtualizacaoFinanceiro dados, String email) {
       //identificador fincaneiro ele pega o repository do Financeiro que ja e o objeto pra poder editar
        //var identificadorFinanceiro = financeiroRepository.findByIdAndUsuario_Email(id, email).orElseThrow();
       // identificadorFinanceiro.atualizarDados(dados);
        //retorna um novo DTO com os novos dados
       // return new DadosDetalhamentoFinanceiro(identificadorFinanceiro);
    //}

    //Funcao que deleta um item
    @Transactional
    public void removerDados(Long id, String email) {
        var financeiro = financeiroRepository
                .findByIdAndUsuario_Email(id, email).orElseThrow();

        financeiroRepository.delete(financeiro);
    }


    // o mes agora é montado pelas parcelas
    @Transactional
    public List<DadosLancamentoMensal> listarPorMes(int mes, int ano, String email){
        YearMonth periodoMesAno = YearMonth.of(ano, mes);
        fixoService.garantirMes(email, periodoMesAno);

        return parcelaRepository.listarPorCompetencia(email, periodoMesAno.atDay(1)).stream()
                .map(DadosLancamentoMensal::new).toList();
    }

    @Transactional
    public DadosDetalhamentoFinanceiro editarDados(Long id, DadosAtualizacaoFinanceiro dados, String email) {
        var identificadorFinanceiro = financeiroRepository.findByIdAndUsuario_Email(id, email).orElseThrow();

        if (identificadorFinanceiro.getParcelas().size() > 1) {
            throw new TransacaoParceladaExpcetion("Compra parcelada não pode ser editada. Exclua e cadastre novamente.");
        }

        identificadorFinanceiro.atualizarDados(dados);

        // a lista do mês agora lê da PARCELA, então a única parcela acompanha valor e data
        for (Parcela p : identificadorFinanceiro.getParcelas()) {
            p.setValorParcela(identificadorFinanceiro.getValor());
            p.setCompetencia(identificadorFinanceiro.getData().toLocalDate().withDayOfMonth(1));
        }
        return new DadosDetalhamentoFinanceiro(identificadorFinanceiro);
    }
}
