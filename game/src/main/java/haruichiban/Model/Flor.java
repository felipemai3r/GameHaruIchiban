package haruichiban.Model;

import haruichiban.Model.enums.CorJogador;

public class Flor {
    private int valor; // 1 a 8
    private CorJogador cor;

    public Flor(int valor, CorJogador cor) {
        this.valor = valor;
        this.cor = cor;
    }

    public int getValor() {
        return valor;
    }

    public CorJogador getCor() {
        return cor;
    }

    @Override
    public String toString() {
        return cor.getNome() + " " + valor;
    }
}
