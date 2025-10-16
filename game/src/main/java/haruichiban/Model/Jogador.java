package haruichiban.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import haruichiban.Model.enums.CorJogador;

/**
 * Mantém a mão do jogador e um "estoque" de valores restantes para a rodada.
 * - Na nova rodada o estoque é {1..8}, embaralhado.
 * - Compra 3 para a mão.
 * - Ao longo dos turnos, repõe a mão até 3 usando SOMENTE os valores
 *   restantes no estoque (logo, sem repetições na rodada).
 */
public class Jogador {

    private final String nome;
    private final CorJogador cor;
    private int pontuacao;

    private final List<Flor> mao = new ArrayList<>();
    private Flor florSelecionada;

    // Valores disponíveis nesta rodada (sem repetição)
    private final List<Integer> estoqueValores = new ArrayList<>();

    public Jogador(String nome, CorJogador cor) {
        this.nome = nome;
        this.cor = cor;
        this.pontuacao = 0;
        resetarParaNovaRodada();
    }

    /** Início de rodada: recria o estoque {1..8} e compra 3. */
    public void resetarParaNovaRodada() {
        mao.clear();
        florSelecionada = null;

        estoqueValores.clear();
        for (int v = 1; v <= 8; v++) {
            estoqueValores.add(v);
        }
        Collections.shuffle(estoqueValores);

        comprarAte(3);
    }

    /** Entre turnos: completa a mão até 3 flores, sem repetir valores. */
    public void reporMaoAteTres() {
        comprarAte(3);
    }

    private void comprarAte(int limite) {
        while (mao.size() < limite && !estoqueValores.isEmpty()) {
            int valor = estoqueValores.remove(0);
            mao.add(new Flor(valor, cor));
        }
    }

    public boolean selecionarFlor(int indice) {
        if (indice < 0 || indice >= mao.size()) return false;
        florSelecionada = mao.get(indice);
        return true;
    }

    public void usarFlorSelecionada() {
        if (florSelecionada != null) {
            mao.remove(florSelecionada);
            florSelecionada = null;
        }
    }

    public boolean temFloresDisponiveis() {
        return !mao.isEmpty();
    }

    public void adicionarPontos(int p) {
        pontuacao += p;
        if (pontuacao < 0) pontuacao = 0;
        if (pontuacao > 5) pontuacao = 5;
    }

    public boolean venceu() {
        return pontuacao >= 5;
    }

    // Getters
    public String getNome() { return nome; }
    public CorJogador getCor() { return cor; }
    public int getPontuacao() { return pontuacao; }
    public List<Flor> getMao() { return Collections.unmodifiableList(mao); }
    public Flor getFlorSelecionada() { return florSelecionada; }
}
