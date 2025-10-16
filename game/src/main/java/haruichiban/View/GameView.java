package haruichiban.View;

import haruichiban.Model.JogoHaruIchiban;

public interface GameView {
    void atualizarTela(JogoHaruIchiban jogo);
    void mostrarErro(String mensagem);
    void mostrarMensagem(String mensagem);
}
