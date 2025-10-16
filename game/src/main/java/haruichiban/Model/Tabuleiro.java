package haruichiban.Model;

import java.util.ArrayList;
import java.util.List;

import haruichiban.Model.enums.CorJogador;
import haruichiban.Model.enums.TipoPadrao;

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
        if (valor == 5)
            return CorJogador.VERMELHO;
        if (valor == 6)
            return CorJogador.AMARELO;
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

    /**
     * VERSÃO CORRETA DO "VENTO DA PRIMAVERA"
     *
     * Move um único nenúfar 1 casa OU empurra o bloco contíguo de nenúfares exatamente
     * 1 casa na direção indicada, desde que exista uma célula de água (0) imediatamente
     * APÓS a ponta do bloco. Nunca desliza até a borda.
     */
    public boolean moverNenufar(int linhaOrigem, int colunaOrigem, char direcaoMovimento) {
        int dr = 0, dc = 0;
        switch (direcaoMovimento) {
            case 'D' -> { dr = 0; dc = +1; }  // direita
            case 'E' -> { dr = 0; dc = -1; }  // esquerda
            case 'C' -> { dr = -1; dc = 0; }  // cima
            case 'B' -> { dr = +1; dc = 0; }  // baixo
            default -> { return false; }
        }

        if (!posicaoValida(linhaOrigem, colunaOrigem)) return false;
        // origem não pode ser água
        if (getCelula(linhaOrigem, colunaOrigem) == 0) return false;

        int nr = linhaOrigem + dr, nc = colunaOrigem + dc;
        if (!posicaoValida(nr, nc)) return false;

        // 1) Se a casa vizinha imediata é água -> move só 1 casa
        if (getCelula(nr, nc) == 0) {
            int val = getCelula(linhaOrigem, colunaOrigem);
            setCelula(nr, nc, val);
            setCelula(linhaOrigem, colunaOrigem, 0);
            return true;
        }

        // 2) Caso contrário, há um bloco contíguo: encontrar a "ponta" (última não-água)
        int tailR = linhaOrigem, tailC = colunaOrigem;
        int rr = nr, cc = nc;
        while (posicaoValida(rr, cc) && getCelula(rr, cc) != 0) {
            tailR = rr; tailC = cc;
            rr += dr; cc += dc;
        }

        // rr,cc é a primeira célula APÓS o bloco (pode ser fora da grade ou água)
        if (!posicaoValida(rr, cc)) return false;           // empurraria para fora -> inválido
        if (getCelula(rr, cc) != 0) return false;           // não tem água logo após o bloco -> inválido

        // 3) Shift do bloco inteirinho 1 casa (da ponta para trás)
        int curR = tailR, curC = tailC;
        setCelula(rr, cc, getCelula(curR, curC));           // ponta -> água
        while (!(curR == linhaOrigem && curC == colunaOrigem)) {
            int prevR = curR - dr, prevC = curC - dc;
            setCelula(curR, curC, getCelula(prevR, prevC)); // anterior -> atual
            curR = prevR; curC = prevC;
        }
        setCelula(linhaOrigem, colunaOrigem, 0);            // libera origem
        return true;
    }

    // ---------- utilidades já existentes ----------
    private boolean existeAguaAdiante(int r, int c, int dr, int dc) {
        int rr = r + dr, cc = c + dc;
        while (posicaoValida(rr, cc)) {
            if (getCelula(rr, cc) == 0) return true;
            rr += dr; cc += dc;
        }
        return false;
    }

    private void empurrarUmPasso(int r, int c, int dr, int dc) {
        int nr = r + dr, nc = c + dc;
        if (!posicaoValida(nr, nc)) return; // segurança

        if (getCelula(nr, nc) != 0) {
            empurrarUmPasso(nr, nc, dr, dc);
        }

        int atual = getCelula(r, c);
        setCelula(nr, nc, atual);
        setCelula(r, c, 0);
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

    public void limparFlores() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int valor = tabuleiro[i][j];
                if (valor == 7 || valor == 8) {
                    tabuleiro[i][j] = 3; // Volta para nenúfar claro
                }
            }
        }
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

    public ResultadoPadrao encontrouQuadradoCompleto() {
        ResultadoPadrao resultado = new ResultadoPadrao();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int valor = tabuleiro[i][j];
                if ((valor == 7 || valor == 8) &&
                        valor == tabuleiro[i][j + 1] &&
                        valor == tabuleiro[i + 1][j] &&
                        valor == tabuleiro[i + 1][j + 1]) {

                    CorJogador cor = (valor == 7) ? CorJogador.VERMELHO : CorJogador.AMARELO;
                    resultado.setEncontrou(true);
                    resultado.setCorVencedor(cor);
                    resultado.setTipo(TipoPadrao.QUADRADO_2X2);
                    resultado.setPontuacao(1);
                    return resultado;
                }
            }
        }
        return resultado;
    }

    public ResultadoPadrao encontrarLinhaHorizontal() {
        ResultadoPadrao resultado = new ResultadoPadrao();
        // Linha de 5 flores (5 pontos)
        for (int i = 0; i < 5; i++) {
            int valor = tabuleiro[i][0];
            if ((valor == 7 || valor == 8) &&
                    valor == tabuleiro[i][1] &&
                    valor == tabuleiro[i][2] &&
                    valor == tabuleiro[i][3] &&
                    valor == tabuleiro[i][4]) {

                CorJogador cor = (valor == 7) ? CorJogador.VERMELHO : CorJogador.AMARELO;
                resultado.setEncontrou(true);
                resultado.setCorVencedor(cor);
                resultado.setTipo(TipoPadrao.LINHA_5);
                resultado.setPontuacao(5);
                return resultado;
            }
        }

        // Linha de 4 flores - posição 0-3 (2 pontos)
        for (int i = 0; i < 5; i++) {
            int valor = tabuleiro[i][0];
            if ((valor == 7 || valor == 8) &&
                    valor == tabuleiro[i][1] &&
                    valor == tabuleiro[i][2] &&
                    valor == tabuleiro[i][3]) {

                CorJogador cor = (valor == 7) ? CorJogador.VERMELHO : CorJogador.AMARELO;

                resultado.setEncontrou(true);
                resultado.setCorVencedor(cor);
                resultado.setTipo(TipoPadrao.LINHA_HORIZONTAL_4);
                resultado.setPontuacao(2);
                return resultado;
            }
        }

        // Linha de 4 flores - posição 1-4 (2 pontos)
        for (int i = 0; i < 5; i++) {
            int valor = tabuleiro[i][1];
            if ((valor == 7 || valor == 8) &&
                    valor == tabuleiro[i][2] &&
                    valor == tabuleiro[i][3] &&
                    valor == tabuleiro[i][4]) {

                CorJogador cor = (valor == 7) ? CorJogador.VERMELHO : CorJogador.AMARELO;

                resultado.setEncontrou(true);
                resultado.setCorVencedor(cor);
                resultado.setTipo(TipoPadrao.LINHA_HORIZONTAL_4);
                resultado.setPontuacao(2);
                return resultado;
            }
        }

        return resultado;
    }

    public ResultadoPadrao encontrarLinhaVertical() {
        ResultadoPadrao resultado = new ResultadoPadrao();
        // Linha de 5 flores (5 pontos)
        for (int i = 0; i < 5; i++) {
            int valor = tabuleiro[0][i];
            if ((valor == 7 || valor == 8) &&
                    valor == tabuleiro[1][i] &&
                    valor == tabuleiro[2][i] &&
                    valor == tabuleiro[3][i] &&
                    valor == tabuleiro[4][i]) {

                CorJogador cor = (valor == 7) ? CorJogador.VERMELHO : CorJogador.AMARELO;
                resultado.setEncontrou(true);
                resultado.setCorVencedor(cor);
                resultado.setTipo(TipoPadrao.LINHA_5);
                resultado.setPontuacao(5);
                return resultado;
            }
        }

        // Linha de 4 flores - posição 0-3 (2 pontos)
        for (int i = 0; i < 5; i++) {
            int valor = tabuleiro[0][i];
            if ((valor == 7 || valor == 8) &&
                    valor == tabuleiro[1][i] &&
                    valor == tabuleiro[2][i] &&
                    valor == tabuleiro[3][i]) {

                CorJogador cor = (valor == 7) ? CorJogador.VERMELHO : CorJogador.AMARELO;
                resultado.setEncontrou(true);
                resultado.setCorVencedor(cor);
                resultado.setTipo(TipoPadrao.LINHA_VERTICAL_4);
                resultado.setPontuacao(2);
                return resultado;
            }
        }

        // Linha de 4 flores - posição 1-4 (2 pontos)
        for (int i = 0; i < 5; i++) {
            int valor = tabuleiro[1][i];
            if ((valor == 7 || valor == 8) &&
                    valor == tabuleiro[2][i] &&
                    valor == tabuleiro[3][i] &&
                    valor == tabuleiro[4][i]) {

                CorJogador cor = (valor == 7) ? CorJogador.VERMELHO : CorJogador.AMARELO;

                resultado.setEncontrou(true);
                resultado.setCorVencedor(cor);
                resultado.setTipo(TipoPadrao.LINHA_VERTICAL_4);
                resultado.setPontuacao(2);
                return resultado;
            }
        }

        return resultado;
    }

    public static ResultadoPadrao nenhumPadrao() {
        return new ResultadoPadrao(false, null, null);
    }

    public ResultadoPadrao encontrarDiagonal() {
        ResultadoPadrao resultado = new ResultadoPadrao();
        // Diagonal principal ↘ - 5 flores
        int valor = tabuleiro[0][0];
        if ((valor == 7 || valor == 8) &&
            valor == tabuleiro[1][1] &&
            valor == tabuleiro[2][2] &&
            valor == tabuleiro[3][3] &&
            valor == tabuleiro[4][4]) {

            CorJogador cor = (valor == 7) ? CorJogador.VERMELHO : CorJogador.AMARELO;

            resultado.setEncontrou(true);
            resultado.setCorVencedor(cor);
            resultado.setTipo(TipoPadrao.LINHA_5);
            resultado.setPontuacao(5);
            return resultado;
        }

        // Diagonal secundária ↙ - 5 flores
        valor = tabuleiro[0][4];
        if ((valor == 7 || valor == 8) &&
            valor == tabuleiro[1][3] &&
            valor == tabuleiro[2][2] &&
            valor == tabuleiro[3][1] &&
            valor == tabuleiro[4][0]) {

            CorJogador cor = (valor == 7) ? CorJogador.VERMELHO : CorJogador.AMARELO;
            resultado.setEncontrou(true);
            resultado.setCorVencedor(cor);
            resultado.setTipo(TipoPadrao.LINHA_5);
            resultado.setPontuacao(5);
            return resultado;
        }

        // Diagonal principal ↘ - 4 flores (0,0 a 3,3)
        valor = tabuleiro[0][0];
        if ((valor == 7 || valor == 8) &&
            valor == tabuleiro[1][1] &&
            valor == tabuleiro[2][2] &&
            valor == tabuleiro[3][3]) {

            CorJogador cor = (valor == 7) ? CorJogador.VERMELHO : CorJogador.AMARELO;
            resultado.setEncontrou(true);
            resultado.setCorVencedor(cor);
            resultado.setTipo(TipoPadrao.DIAGONAL_4);
            resultado.setPontuacao(3);
            return resultado;
        }

        // Diagonal principal ↘ - 4 flores (1,1 a 4,4)
        valor = tabuleiro[1][1];
        if ((valor == 7 || valor == 8) &&
            valor == tabuleiro[2][2] &&
            valor == tabuleiro[3][3] &&
            valor == tabuleiro[4][4]) {

            CorJogador cor = (valor == 7) ? CorJogador.VERMELHO : CorJogador.AMARELO;
            resultado.setEncontrou(true);
            resultado.setCorVencedor(cor);
            resultado.setTipo(TipoPadrao.DIAGONAL_4);
            resultado.setPontuacao(3);
            return resultado;
        }

        // Diagonal secundária ↙ - 4 flores (0,4 a 3,1)
        valor = tabuleiro[0][4];
        if ((valor == 7 || valor == 8) &&
            valor == tabuleiro[1][3] &&
            valor == tabuleiro[2][2] &&
            valor == tabuleiro[3][1]) {

            CorJogador cor = (valor == 7) ? CorJogador.VERMELHO : CorJogador.AMARELO;
            resultado.setEncontrou(true);
            resultado.setCorVencedor(cor);
            resultado.setTipo(TipoPadrao.DIAGONAL_4);
            resultado.setPontuacao(3);
            return resultado;
        }

        // Diagonal secundária ↙ - 4 flores (1,3 a 4,0)
        valor = tabuleiro[1][3];
        if ((valor == 7 || valor == 8) &&
            valor == tabuleiro[2][2] &&
            valor == tabuleiro[3][1] &&
            valor == tabuleiro[4][0]) {

            CorJogador cor = (valor == 7) ? CorJogador.VERMELHO : CorJogador.AMARELO;
            resultado.setEncontrou(true);
            resultado.setCorVencedor(cor);
            resultado.setTipo(TipoPadrao.DIAGONAL_4);
            resultado.setPontuacao(3);
            return resultado;
        }

        return resultado;
    }

    public ResultadoPadrao verificarPadroes() {
        // Prioridade: Linha de 5 (5 pts) > Diagonal 4 (3 pts) > Linha 4 (2 pts) > Quadrado (1 pt)

        // Verificar linhas de 5 (horizontal e vertical)
        ResultadoPadrao resultado = encontrarLinhaHorizontal();
        if (resultado.isEncontrou() && resultado.getPontuacao() == 5) {
            return resultado;
        }

        resultado = encontrarLinhaVertical();
        if (resultado.isEncontrou() && resultado.getPontuacao() == 5) {
            return resultado;
        }

        // Verificar diagonal de 5
        resultado = encontrarDiagonal();
        if (resultado.isEncontrou() && resultado.getPontuacao() == 5) {
            return resultado;
        }

        // Verificar diagonal de 4 (3 pontos)
        resultado = encontrarDiagonal();
        if (resultado.isEncontrou() && resultado.getPontuacao() == 3) {
            return resultado;
        }

        // Verificar linhas de 4 (2 pontos)
        resultado = encontrarLinhaHorizontal();
        if (resultado.isEncontrou() && resultado.getPontuacao() == 2) {
            return resultado;
        }

        resultado = encontrarLinhaVertical();
        if (resultado.isEncontrou() && resultado.getPontuacao() == 2) {
            return resultado;
        }

        // Verificar quadrado (1 ponto)
        resultado = encontrouQuadradoCompleto();
        if (resultado.isEncontrou()) {
            return resultado;
        }

        // Nenhum padrão encontrado
        return new ResultadoPadrao();
    }

    // ---------- NOVO: usado para evitar travamento na seleção do escuro ----------
    public boolean existeNenufarClaroVazio() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tabuleiro[i][j] == 3) return true;
            }
        }
        return false;
    }
}
