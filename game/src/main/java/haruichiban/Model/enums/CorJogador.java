package haruichiban.Model.enums;

/**
 * Enumeração para representar as cores dos jogadores.
 */
public enum CorJogador {
    VERMELHO(1, "Vermelho"), 
    AMARELO(2, "Amarelo");
    
    private final int id;
    private final String nome;
    
    CorJogador(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    public int getId() { 
        return id; 
    }
    
    public String getNome() { 
        return nome; 
    }
}
