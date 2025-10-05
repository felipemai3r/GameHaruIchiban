package haruichiban.Controller;


import haruichiban.Model.JogoHaruIchiban;
import haruichiban.View.GameView;

public class GameController {
    private JogoHaruIchiban jogo;
    private GameView view;
    
    public GameController(GameView view) {
        this.view = view;
    }
    
    public void iniciarNovoJogo(String nomeVermelho, String nomeAmarelo) {
        jogo = new JogoHaruIchiban(nomeVermelho, nomeAmarelo);
        view.atualizarTela(jogo);
    }
    
    public void selecionarFlores(int indiceVermelho, int indiceAmarelo) {
        if (jogo.selecionarFlores(indiceVermelho, indiceAmarelo)) {
            view.atualizarTela(jogo);
        } else {
            view.mostrarErro("Não foi possível selecionar as flores!");
        }
    }
    
    public void executarFloracaoJunior() {
        if (jogo.executarFloracaoJunior()) {
            view.atualizarTela(jogo);
        } else {
            view.mostrarErro("Não foi possível executar a floração!");
        }
    }
    
    public void executarFloracaoSenior(int linha, int coluna) {
        if (jogo.executarFloracaoSenior(linha, coluna)) {
            view.atualizarTela(jogo);
        } else {
            view.mostrarErro(jogo.getMensagemEstado());
        }
    }
    
    public void moverSapo(int linhaOrig, int colunaOrig, int linhaDest, int colunaDest) {
        if (jogo.moverSapo(linhaOrig, colunaOrig, linhaDest, colunaDest)) {
            view.atualizarTela(jogo);
        } else {
            view.mostrarErro("Não foi possível mover o sapo!");
        }
    }
    
    public void moverNenufar(int linhaOrig, int colunaOrig, char direcao) {

        if (jogo.executarMovimentoNenufar(linhaOrig, colunaOrig, direcao)) {
            view.atualizarTela(jogo);
        } else {
            view.mostrarErro(jogo.getMensagemEstado());
        }
    }
    
    public void selecionarNenufarEscuro(int linha, int coluna) {
        if (jogo.selecionarNenufarEscuro(linha, coluna)) {
            view.atualizarTela(jogo);
        } else {
            view.mostrarErro(jogo.getMensagemEstado());
        }
    }
    
    public JogoHaruIchiban getJogo() {
        return jogo;
    }
}