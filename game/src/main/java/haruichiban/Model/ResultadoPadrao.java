package haruichiban.Model;

import haruichiban.Model.enums.CorJogador;
import haruichiban.Model.enums.TipoPadrao;

public class ResultadoPadrao {
    private boolean encontrou;
    private CorJogador corVencedor;
    private int pontuacao;
    private TipoPadrao tipo;


    public ResultadoPadrao(boolean encontrou, CorJogador corVencedor, TipoPadrao tipo) {
        this.encontrou = encontrou;
        this.corVencedor = corVencedor;
        this.tipo = tipo;
        this.pontuacao = tipo != null ? tipo.getPontos() : 0;
    }

    
    public boolean isEncontrou() {
        return encontrou;
    }

    public void setEncontrou(boolean encontrou) {
        this.encontrou = encontrou;
    }
    public CorJogador getCorVencedor() {
        return corVencedor;
    }
    public void setCorVencedor(CorJogador corVencedor) {
        this.corVencedor = corVencedor;
    }
    public int getPontuacao() {
        return pontuacao;
    }
    
    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }
    
    public TipoPadrao getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoPadrao tipo) {
        this.tipo = tipo;
    }

}
