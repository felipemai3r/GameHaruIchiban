package haruichiban.Model;

public class Posicao {
    private int linha;
    private int coluna;
    
    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }
    
    public int getLinha() { 
        return linha; 
    }
    
    public int getColuna() { 
        return coluna; 
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Posicao)) return false;
        Posicao p = (Posicao) obj;
        return this.linha == p.linha && this.coluna == p.coluna;
    }
    
    @Override
    public int hashCode() {
        return linha * 31 + coluna;
    }
    
    @Override
    public String toString() {
        return "(" + linha + ", " + coluna + ")";
    }
}
