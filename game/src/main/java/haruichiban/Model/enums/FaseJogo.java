package haruichiban.Model.enums;

public enum FaseJogo {
    SELECAO_FLOR("Seleção de Flores"),
    FLORACAO_JUNIOR("Floração do Jardineiro Júnior"),
    FLORACAO_SENIOR("Floração do Jardineiro Sênior"),
    MOVIMENTO_NENUFARES("Movimento dos Nenúfares"),
    SELECAO_NENUFAR_ESCURO("Seleção do Nenúfar Escuro"),
    FIM_RODADA("Fim de Rodada");
    
    private final String descricao;
    
    FaseJogo(String descricao) { 
        this.descricao = descricao; 
    }
    
    public String getDescricao() { 
        return descricao; 
    }
}