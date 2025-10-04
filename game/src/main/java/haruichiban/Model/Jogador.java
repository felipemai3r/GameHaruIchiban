package haruichiban.Model;


import haruichiban.Model.enums.CorJogador;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jogador {
    private String nome;
    private CorJogador cor;
    private List<Flor> mao;
    private List<Flor> deck;
    private Flor florSelecionada;
    private int pontuacao;
    
    public Jogador(String nome, CorJogador cor) {
        this.nome = nome;
        this.cor = cor;
        this.pontuacao = 0;
        this.mao = new ArrayList<>();
        this.deck = new ArrayList<>();
        inicializarDeck();
        comprarMao();
    }
    
    private void inicializarDeck() {
        deck.clear();
        for (int i = 1; i <= 8; i++) {
            deck.add(new Flor(i, cor));
        }
        Collections.shuffle(deck);
    }
    
    private void comprarMao() {
        mao.clear();
        for (int i = 0; i < 3 && !deck.isEmpty(); i++) {
            mao.add(deck.remove(0));
        }
    }
    
    public boolean selecionarFlor(int indice) {
        if (indice >= 0 && indice < mao.size()) {
            florSelecionada = mao.get(indice);
            return true;
        }
        return false;
    }
    
    public void usarFlorSelecionada() {
        if (florSelecionada != null) {
            mao.remove(florSelecionada);
            
            if (!deck.isEmpty() && mao.size() < 3) {
                mao.add(deck.remove(0));
            }
            
            florSelecionada = null;
        }
    }
    
    public void resetarParaNovaRodada() {
        // Resetar deck completo apenas no início de nova rodada
        inicializarDeck();
        comprarMao();
        florSelecionada = null;
    }
    
    public boolean temFloresDisponiveis() {
        return !mao.isEmpty() || !deck.isEmpty();
    }
    
    public int getFloresRestantes() {
        return mao.size() + deck.size();
    }
    
    public void adicionarPontos(int pontos) {
        this.pontuacao += pontos;
    }
    
    public boolean venceu() {
        return pontuacao >= 5;
    }
    
    // Getters
    public String getNome() { return nome; }
    public CorJogador getCor() { return cor; }
    public List<Flor> getMao() { return new ArrayList<>(mao); }
    public Flor getFlorSelecionada() { return florSelecionada; }
    public int getPontuacao() { return pontuacao; }
    public int getTamanhoMao() { return mao.size(); }
}
