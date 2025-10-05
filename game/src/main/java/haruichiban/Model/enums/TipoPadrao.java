package haruichiban.Model.enums;

public enum TipoPadrao {
    QUADRADO_2X2(1, "Quadrado 2x2"),
    LINHA_HORIZONTAL_4(2, "Linha Horizontal de 4"),
    LINHA_VERTICAL_4(2, "Linha Vertical de 4"),
    DIAGONAL_4(3, "Diagonal de 4"),
    LINHA_5(5, "Linha de 5 flores"),
    NENHUM(0, "Nenhum padrão");
    
    private final int pontos;
    private final String descricao;
    
    TipoPadrao(int pontos, String descricao) {
        this.pontos = pontos;
        this.descricao = descricao;
    }
    
    public int getPontos() { 
        return pontos; 
    }
    
    public String getDescricao() { 
        return descricao; 
    }
}
