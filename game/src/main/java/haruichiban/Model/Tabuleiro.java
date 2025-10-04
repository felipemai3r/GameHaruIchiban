package haruichiban.Model;

import java.util.ArrayList;
import java.util.List;
import haruichiban.Model.enums.CorJogador;

public class Tabuleiro {
    /**
     * LEGENDA DO TABULEIRO:
     * 0 - vazio (fora da lagoa)
     * 1 - flor vermelha
     * 2 - flor amarela
     * 3 - nenúfar claro (sem flor)
     * 4 - nenúfar escuro (sem flor)
     * 5 - sapo vermelho
     * 6 - sapo amarelo
     * 7 - nenúfar claro com flor vermelha
     * 8 - nenúfar claro com flor amarela
     */
    private int[][] tabuleiro = new int[5][5];
    
    public void iniciarTabuleiro() {
        // Limpar tudo primeiro
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tabuleiro[i][j] = 0;
            }
        }
        
        // Configuração inicial
        tabuleiro[0][0] = 3;
        tabuleiro[0][2] = 3;
        tabuleiro[0][4] = 3;

        tabuleiro[1][1] = 3;
        tabuleiro[1][2] = 5; // sapo vermelho
        tabuleiro[1][3] = 4; // nenúfar escuro (único inicial)

        tabuleiro[2][0] = 3;
        tabuleiro[2][1] = 3;
        tabuleiro[2][3] = 3;
        tabuleiro[2][4] = 3;

        tabuleiro[3][1] = 3;
        tabuleiro[3][2] = 3;
        tabuleiro[3][3] = 6; // sapo amarelo

        tabuleiro[4][0] = 3;
        tabuleiro[4][2] = 3;
        tabuleiro[4][4] = 3;
    }
    
    public int getCelula(int linha, int coluna) {
        if (posicaoValida(linha, coluna)) {
            return this.tabuleiro[linha][coluna];
        }
        return -1;
    }
    
    public void setCelula(int linha, int coluna, int valor) {
        if (posicaoValida(linha, coluna)) {
            this.tabuleiro[linha][coluna] = valor;
        }
    }
    
    public boolean posicaoValida(int linha, int coluna) {
        return linha >= 0 && linha < 5 && coluna >= 0 && coluna < 5;
    }
    
    public void verTabuleiro() {
        System.out.println("=== TABULEIRO ===");
        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro[i].length; j++) {
                System.out.print(tabuleiro[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("================");
    }
    
    public Posicao encontrarNenufarEscuro() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tabuleiro[i][j] == 4) {
                    return new Posicao(i, j);
                }
            }
        }
        return null;
    }
    
    public boolean podeColocarFlor(int linha, int coluna) {
        int valor = getCelula(linha, coluna);
        return valor == 3 || valor == 4 || valor == 5 || valor == 6;
    }

    // Coloca a flor e transforma o nenúfar em florido
    public boolean colocarFlor(int linha, int coluna, Flor flor) {
        if (!podeColocarFlor(linha, coluna)) {
            return false;
        }
        
        int novoValor = (flor.getCor() == CorJogador.VERMELHO) ? 7 : 8;
        setCelula(linha, coluna, novoValor);
        
        return true;
    }
    
    public boolean temSapo(int linha, int coluna) {
        int valor = getCelula(linha, coluna);
        return valor == 5 || valor == 6;
    }
    
    public CorJogador getCorSapo(int linha, int coluna) {
        int valor = getCelula(linha, coluna);
        if (valor == 5) return CorJogador.VERMELHO;
        if (valor == 6) return CorJogador.AMARELO;
        return null;
    }
    
    public boolean moverSapo(int linhaOrigem, int colunaOrigem, 
                            int linhaDestino, int colunaDestino) {
        if (!temSapo(linhaOrigem, colunaOrigem)) {
            return false;
        }
        
        int valorDestino = getCelula(linhaDestino, colunaDestino);
        if (valorDestino != 3) {
            return false;
        }
        
        int valorSapo = getCelula(linhaOrigem, colunaOrigem);
        setCelula(linhaDestino, colunaDestino, valorSapo);
        setCelula(linhaOrigem, colunaOrigem, 3);
        
        return true;
    }
    
    public boolean moverNenufar(int linhaOrigem, int colunaOrigem,
                               int linhaDestino, int colunaDestino) {
        int diffLinha = Math.abs(linhaDestino - linhaOrigem);
        int diffColuna = Math.abs(colunaDestino - colunaOrigem);
        
        if (!((diffLinha == 1 && diffColuna == 0) || 
              (diffLinha == 0 && diffColuna == 1))) {
            return false;
        }
        
        if (!posicaoValida(linhaDestino, colunaDestino)) {
            return false;
        }
        
        int temp = tabuleiro[linhaOrigem][colunaOrigem];
        tabuleiro[linhaOrigem][colunaOrigem] = tabuleiro[linhaDestino][colunaDestino];
        tabuleiro[linhaDestino][colunaDestino] = temp;
        
        return true;
    }
    
    public void virarTodosNenufaresParaClaro() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tabuleiro[i][j] == 4) {
                    tabuleiro[i][j] = 3;
                }
            }
        }
    }
    
    public int contarNenufaresNaoFlorescidos() {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int valor = tabuleiro[i][j];
                if (valor == 3 || valor == 4 || valor == 5 || valor == 6) {
                    count++;
                }
            }
        }
        return count;
    }
    
    public List<Posicao> encontrarSapos() {
        List<Posicao> sapos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (temSapo(i, j)) {
                    sapos.add(new Posicao(i, j));
                }
            }
        }
        return sapos;
    }
    
    public void removerSapos() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (temSapo(i, j)) {
                    setCelula(i, j, 3);
                }
            }
        }
    }
    
    public void limparFlores() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int valor = tabuleiro[i][j];
                if (valor == 7 || valor == 8) {
                    tabuleiro[i][j] = 3;
                }
            }
        }
    }
}
