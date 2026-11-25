package com.yama.finplus.domain.financeiro.fixos;

import com.yama.finplus.domain.cartao.ParcelaService;
import com.yama.finplus.domain.financeiro.Financeiro;
import com.yama.finplus.domain.financeiro.enums.FormaPagamento;
import com.yama.finplus.domain.usuario.Usuario;
import com.yama.finplus.repository.FinanceiroRepository;
import com.yama.finplus.repository.LancamentoFixoRepository;
import com.yama.finplus.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class FixoService {

    private static final int MESES_A_FRENTE = 12;

    private final LancamentoFixoRepository fixoRepository;
    private final FinanceiroRepository financeiroRepository;
    private final UsuarioRepository usuarioRepository;
    private final ParcelaService parcelaService;

    public FixoService(LancamentoFixoRepository fixoRepository, FinanceiroRepository financeiroRepository,
                       UsuarioRepository usuarioRepository, ParcelaService parcelaService) {
        this.fixoRepository = fixoRepository;
        this.financeiroRepository = financeiroRepository;
        this.usuarioRepository = usuarioRepository;
        this.parcelaService = parcelaService;
    }

    public List<DadosDetalhamentoFixo> listar(String email) {
        return fixoRepository.findAllByUsuario_Email(email).stream()
                .map(DadosDetalhamentoFixo::new).toList();
    }

    @Transactional
    public DadosDetalhamentoFixo criar(DadosCadastroFixo dados, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));

        var fixo = new LancamentoFixo();
        fixo.setUsuario(usuario);
        preencher(fixo, dados);
        // "já gerado até o mês passado": o mês atual é gerado logo abaixo
        fixo.setGeradoAte(YearMonth.now().minusMonths(1).atDay(1));
        fixoRepository.save(fixo);

        gerarMesesPendentes(fixo, YearMonth.now());
        return new DadosDetalhamentoFixo(fixo);
    }

    @Transactional
    public DadosDetalhamentoFixo atualizar(Long id, DadosCadastroFixo dados, String email) {
        var fixo = fixoRepository.findByIdAndUsuario_Email(id, email).orElseThrow();
        preencher(fixo, dados);

        // reflete nos lançamentos do mês atual em diante; os meses passados ficam como estão
        LocalDate mesAtual = YearMonth.now().atDay(1);
        for (Financeiro f : financeiroRepository.findAllByFixoAndCompetenciaFixoGreaterThanEqual(fixo, mesAtual)) {
            f.setNome(fixo.getNome());
            f.setValor(fixo.getValor());
            f.setTipo(fixo.getTipo());
            f.setCategoria(fixo.getCategoria());
            f.setData(f.getCompetenciaFixo().withDayOfMonth(fixo.getDiaDoMes()).atStartOfDay());
            // o valor que aparece no mês vem da parcela (a única), então ela acompanha
            f.getParcelas().forEach(p -> p.setValorParcela(fixo.getValor()));
        }
        return new DadosDetalhamentoFixo(fixo);
    }

    @Transactional
    public void excluir(Long id, String email) {
        var fixo = fixoRepository.findByIdAndUsuario_Email(id, email).orElseThrow();

        // tira o fixo do mês atual e dos próximos; o histórico dos meses passados fica
        LocalDate mesAtual = YearMonth.now().atDay(1);
        financeiroRepository.deleteAll(
                financeiroRepository.findAllByFixoAndCompetenciaFixoGreaterThanEqual(fixo, mesAtual));

        fixoRepository.delete(fixo);
    }

    // chamado ao abrir um mês: gera o que falta e não mexe no que já existe
    @Transactional
    public void garantirMes(String email, YearMonth alvo) {
        // não cria lançamentos a perder de vista
        if (alvo.isAfter(YearMonth.now().plusMonths(MESES_A_FRENTE))) {
            return;
        }
        for (var fixo : fixoRepository.findAllByUsuario_EmailAndGeradoAteBefore(email, alvo.atDay(1))) {
            gerarMesesPendentes(fixo, alvo);
        }
    }

    private void gerarMesesPendentes(LancamentoFixo fixo, YearMonth ate) {
        YearMonth proximo = YearMonth.from(fixo.getGeradoAte()).plusMonths(1);
        while (!proximo.isAfter(ate)) { // também preenche meses pulados no meio
            criarLancamento(fixo, proximo);
            proximo = proximo.plusMonths(1);
        }
        fixo.setGeradoAte(ate.atDay(1));
    }

    private void criarLancamento(LancamentoFixo fixo, YearMonth mes) {
        var financeiro = new Financeiro();
        financeiro.setUsuario(fixo.getUsuario());
        financeiro.setNome(fixo.getNome());
        financeiro.setValor(fixo.getValor());
        financeiro.setTipo(fixo.getTipo());
        financeiro.setCategoria(fixo.getCategoria());
        financeiro.setFormaPagamento(FormaPagamento.PIX); // o usuário pode ajustar no mês, se quiser
        financeiro.setData(mes.atDay(fixo.getDiaDoMes()).atStartOfDay());
        financeiro.setFixo(fixo);
        financeiro.setCompetenciaFixo(mes.atDay(1));
        financeiroRepository.save(financeiro);

        parcelaService.gerarParcelas(financeiro, 1); // toda transação tem 1 parcela, como as outras
    }

    private void preencher(LancamentoFixo fixo, DadosCadastroFixo dados) {
        fixo.setNome(dados.nome());
        fixo.setValor(dados.valor());
        fixo.setTipo(dados.tipo());
        fixo.setCategoria(dados.categoria());
        fixo.setDiaDoMes(dados.diaDoMes());
    }
}
