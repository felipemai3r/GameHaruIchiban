package haruichiban.Controller;

import haruichiban.Model.Jogador;
import haruichiban.Model.JogoHaruIchiban;
import haruichiban.Model.enums.CorJogador;
import haruichiban.View.GameView;

public class GameController {
    private final GameView view;
    private JogoHaruIchiban jogo;

    public GameController(GameView view) {
        this.view = view;
    }

    // ---------- ciclo principal ----------
    public void iniciarNovoJogo(String nomeVermelho, String nomeAmarelo) {
        this.jogo = new JogoHaruIchiban(nomeVermelho, nomeAmarelo);
        view.atualizarTela(jogo);
    }

    public void selecionarFlores(int idxV, int idxA) {
        if (jogo.selecionarFlores(idxV, idxA)) {
            view.atualizarTela(jogo);
        } else {
            view.mostrarErro(jogo.getMensagemEstado());
        }
    }

    public void executarFloracaoJunior() {
        if (jogo.executarFloracaoJunior()) view.atualizarTela(jogo);
        else view.mostrarErro(jogo.getMensagemEstado());
    }

    public void executarFloracaoSenior(int linha, int coluna) {
        if (jogo.executarFloracaoSenior(linha, coluna)) view.atualizarTela(jogo);
        else view.mostrarErro(jogo.getMensagemEstado());
    }

    public void selecionarNenufarEscuro(int linha, int coluna) {
        if (jogo.selecionarNenufarEscuro(linha, coluna)) view.atualizarTela(jogo);
        else view.mostrarErro(jogo.getMensagemEstado());
    }

    // ---------- movimento de nenúfar (por direção) ----------
    public void moverNenufarDirecao(int linhaOrig, int colunaOrig, char direcao) {
        if (jogo.executarMovimentoNenufar(linhaOrig, colunaOrig, direcao)) {
            view.atualizarTela(jogo);
        } else {
            view.mostrarErro(jogo.getMensagemEstado());
        }
    }

    // ---------- sapos (modo normal e empate) ----------
    public void moverSapo(int lo, int co, int ld, int cd) {
        if (jogo.moverSapo(lo, co, ld, cd)) view.atualizarTela(jogo);
        else view.mostrarErro("Movimento de sapo inválido.");
    }

    public void registrarCoaxar(CorJogador cor) {
        if (jogo.registrarCoaxar(cor)) view.atualizarTela(jogo);
        else view.mostrarErro(jogo.getMensagemEstado());
    }

    public void moverSapoEmpate(int linha, int coluna, CorJogador corSapo) {
        if (jogo.moverSapoEmpate(linha, coluna, corSapo)) view.atualizarTela(jogo);
        else view.mostrarErro(jogo.getMensagemEstado());
    }

    // ---------- nova rodada ----------
    public void iniciarNovaRodada() {
        jogo.iniciarNovaRodada(); // preserva pontuação; reseta mãos/nenúfares/sapos conforme backend
        view.atualizarTela(jogo);
    }

    // ---------- util ----------
    public JogoHaruIchiban getJogo() { return jogo; }
    public Jogador getJogadorVermelho() { return jogo.getJogadorVermelho(); }
    public Jogador getJogadorAmarelo() { return jogo.getJogadorAmarelo(); }
}
