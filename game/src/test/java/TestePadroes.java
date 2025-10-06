import haruichiban.Model.Tabuleiro;
import haruichiban.Model.ResultadoPadrao;
import haruichiban.Model.enums.CorJogador;

public class TestePadroes {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║  TESTE DE DETECÇÃO DE PADRÕES - HARU ICHIBAN  ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        TestePadroes teste = new TestePadroes();
        
        teste.testeQuadrado2x2Vermelho();
        teste.testeQuadrado2x2Amarelo();
        teste.testeLinhaHorizontal4Vermelho();
        teste.testeLinhaHorizontal5Amarelo();
        teste.testeLinhaVertical4Vermelho();
        teste.testeLinhaVertical5Amarelo();
        teste.testeDiagonalPrincipal4();
        teste.testeDiagonalSecundaria5();
        teste.testeNenhumPadrao();
        teste.testePadroesMultiplos();
        
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║            TODOS OS TESTES CONCLUÍDOS!         ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }
    
    // ========== TESTES DE QUADRADO ==========
    
    public void testeQuadrado2x2Vermelho() {
        System.out.println("\n📝 TESTE 1: Quadrado 2x2 - Vermelho");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        limparTabuleiro(t);
        
        // Criar quadrado vermelho (valor 7) em (0,0)
        t.setCelula(0, 0, 7);
        t.setCelula(0, 1, 7);
        t.setCelula(1, 0, 7);
        t.setCelula(1, 1, 7);
        
        mostrarTabuleiro(t);
        
        ResultadoPadrao resultado = t.verificarPadroes();
        
        validarResultado(resultado.isEncontrou(), true, "Deveria encontrar padrão");
        validarCor(resultado.getCorVencedor(), CorJogador.VERMELHO, "Cor deveria ser VERMELHO");
        validarPontos(resultado.getPontuacao(), 1, "Pontuação deveria ser 1");
    }
    
    public void testeQuadrado2x2Amarelo() {
        System.out.println("\n📝 TESTE 2: Quadrado 2x2 - Amarelo");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        limparTabuleiro(t);
        
        // Criar quadrado amarelo (valor 8) em (2,2)
        t.setCelula(2, 2, 8);
        t.setCelula(2, 3, 8);
        t.setCelula(3, 2, 8);
        t.setCelula(3, 3, 8);
        
        mostrarTabuleiro(t);
        
        ResultadoPadrao resultado = t.verificarPadroes();
        
        validarResultado(resultado.isEncontrou(), true, "Deveria encontrar padrão");
        validarCor(resultado.getCorVencedor(), CorJogador.AMARELO, "Cor deveria ser AMARELO");
        validarPontos(resultado.getPontuacao(), 1, "Pontuação deveria ser 1");
    }
    
    // ========== TESTES DE LINHA HORIZONTAL ==========
    
    public void testeLinhaHorizontal4Vermelho() {
        System.out.println("\n📝 TESTE 3: Linha Horizontal 4 - Vermelho");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        limparTabuleiro(t);
        
        // Criar linha horizontal de 4 na linha 2
        t.setCelula(2, 0, 7);
        t.setCelula(2, 1, 7);
        t.setCelula(2, 2, 7);
        t.setCelula(2, 3, 7);
        
        mostrarTabuleiro(t);
        
        ResultadoPadrao resultado = t.verificarPadroes();
        
        validarResultado(resultado.isEncontrou(), true, "Deveria encontrar padrão");
        validarCor(resultado.getCorVencedor(), CorJogador.VERMELHO, "Cor deveria ser VERMELHO");
        validarPontos(resultado.getPontuacao(), 2, "Pontuação deveria ser 2");
    }
    
    public void testeLinhaHorizontal5Amarelo() {
        System.out.println("\n📝 TESTE 4: Linha Horizontal 5 - Amarelo (VITÓRIA!)");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        limparTabuleiro(t);
        
        // Criar linha horizontal de 5 na linha 1
        t.setCelula(1, 0, 8);
        t.setCelula(1, 1, 8);
        t.setCelula(1, 2, 8);
        t.setCelula(1, 3, 8);
        t.setCelula(1, 4, 8);
        
        mostrarTabuleiro(t);
        
        ResultadoPadrao resultado = t.verificarPadroes();
        
        validarResultado(resultado.isEncontrou(), true, "Deveria encontrar padrão");
        validarCor(resultado.getCorVencedor(), CorJogador.AMARELO, "Cor deveria ser AMARELO");
        validarPontos(resultado.getPontuacao(), 5, "Pontuação deveria ser 5 (VITÓRIA!)");
    }
    
    // ========== TESTES DE LINHA VERTICAL ==========
    
    public void testeLinhaVertical4Vermelho() {
        System.out.println("\n📝 TESTE 5: Linha Vertical 4 - Vermelho");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        limparTabuleiro(t);
        
        // Criar linha vertical de 4 na coluna 3
        t.setCelula(0, 3, 7);
        t.setCelula(1, 3, 7);
        t.setCelula(2, 3, 7);
        t.setCelula(3, 3, 7);
        
        mostrarTabuleiro(t);
        
        ResultadoPadrao resultado = t.verificarPadroes();
        
        validarResultado(resultado.isEncontrou(), true, "Deveria encontrar padrão");
        validarCor(resultado.getCorVencedor(), CorJogador.VERMELHO, "Cor deveria ser VERMELHO");
        validarPontos(resultado.getPontuacao(), 2, "Pontuação deveria ser 2");
    }
    
    public void testeLinhaVertical5Amarelo() {
        System.out.println("\n📝 TESTE 6: Linha Vertical 5 - Amarelo (VITÓRIA!)");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        limparTabuleiro(t);
        
        // Criar linha vertical de 5 na coluna 2
        t.setCelula(0, 2, 8);
        t.setCelula(1, 2, 8);
        t.setCelula(2, 2, 8);
        t.setCelula(3, 2, 8);
        t.setCelula(4, 2, 8);
        
        mostrarTabuleiro(t);
        
        ResultadoPadrao resultado = t.verificarPadroes();
        
        validarResultado(resultado.isEncontrou(), true, "Deveria encontrar padrão");
        validarCor(resultado.getCorVencedor(), CorJogador.AMARELO, "Cor deveria ser AMARELO");
        validarPontos(resultado.getPontuacao(), 5, "Pontuação deveria ser 5 (VITÓRIA!)");
    }
    
    // ========== TESTES DE DIAGONAL ==========
    
    public void testeDiagonalPrincipal4() {
        System.out.println("\n📝 TESTE 7: Diagonal Principal 4 - Vermelho");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        limparTabuleiro(t);
        
        // Criar diagonal de 4 (↘)
        t.setCelula(0, 0, 7);
        t.setCelula(1, 1, 7);
        t.setCelula(2, 2, 7);
        t.setCelula(3, 3, 7);
        
        mostrarTabuleiro(t);
        
        ResultadoPadrao resultado = t.verificarPadroes();
        
        validarResultado(resultado.isEncontrou(), true, "Deveria encontrar padrão");
        validarCor(resultado.getCorVencedor(), CorJogador.VERMELHO, "Cor deveria ser VERMELHO");
        validarPontos(resultado.getPontuacao(), 3, "Pontuação deveria ser 3");
    }
    
    public void testeDiagonalSecundaria5() {
        System.out.println("\n📝 TESTE 8: Diagonal Secundária 5 - Amarelo (VITÓRIA!)");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        limparTabuleiro(t);
        
        // Criar diagonal de 5 (↙)
        t.setCelula(0, 4, 8);
        t.setCelula(1, 3, 8);
        t.setCelula(2, 2, 8);
        t.setCelula(3, 1, 8);
        t.setCelula(4, 0, 8);
        
        mostrarTabuleiro(t);
        
        ResultadoPadrao resultado = t.verificarPadroes();
        
        validarResultado(resultado.isEncontrou(), true, "Deveria encontrar padrão");
        validarCor(resultado.getCorVencedor(), CorJogador.AMARELO, "Cor deveria ser AMARELO");
        validarPontos(resultado.getPontuacao(), 5, "Pontuação deveria ser 5 (VITÓRIA!)");
    }
    
    // ========== TESTES NEGATIVOS ==========
    
    public void testeNenhumPadrao() {
        System.out.println("\n📝 TESTE 9: Nenhum Padrão");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        limparTabuleiro(t);
        
        // Flores esparsas, sem padrão
        t.setCelula(0, 0, 7);
        t.setCelula(0, 2, 8);
        t.setCelula(2, 1, 7);
        t.setCelula(3, 4, 8);
        
        mostrarTabuleiro(t);
        
        ResultadoPadrao resultado = t.verificarPadroes();
        
        validarResultado(resultado.isEncontrou(), false, "NÃO deveria encontrar padrão");
    }
    
    public void testePadroesMultiplos() {
        System.out.println("\n📝 TESTE 10: Padrões Múltiplos (deve retornar maior pontuação)");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        limparTabuleiro(t);
        
        // Criar quadrado 2x2 (1 ponto) E linha de 4 (2 pontos)
        // Deve retornar a linha de 4
        t.setCelula(0, 0, 7);
        t.setCelula(0, 1, 7);
        t.setCelula(1, 0, 7);
        t.setCelula(1, 1, 7); // Quadrado
        
        t.setCelula(2, 0, 8);
        t.setCelula(2, 1, 8);
        t.setCelula(2, 2, 8);
        t.setCelula(2, 3, 8); // Linha de 4
        
        mostrarTabuleiro(t);
        
        ResultadoPadrao resultado = t.verificarPadroes();
        
        validarResultado(resultado.isEncontrou(), true, "Deveria encontrar padrão");
        validarPontos(resultado.getPontuacao(), 2, "Deveria retornar maior pontuação (2)");
    }
    
    // ========== MÉTODOS AUXILIARES ==========
    
    private void limparTabuleiro(Tabuleiro t) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                t.setCelula(i, j, 0);
            }
        }
    }
    
    private void mostrarTabuleiro(Tabuleiro t) {
        System.out.println("Tabuleiro:");
        for (int i = 0; i < 5; i++) {
            System.out.print("  ");
            for (int j = 0; j < 5; j++) {
                int valor = t.getCelula(i, j);
                String simbolo = valor == 7 ? "🔴" : valor == 8 ? "🟡" : "⬜";
                System.out.print(simbolo + " ");
            }
            System.out.println();
        }
    }
    
    private void validarResultado(boolean obtido, boolean esperado, String mensagem) {
        if (obtido == esperado) {
            System.out.println("  ✅ " + mensagem + " → OK");
        } else {
            System.out.println("  ❌ " + mensagem + " → FALHOU!");
            System.out.println("     Esperado: " + esperado + ", Obtido: " + obtido);
        }
    }
    
    private void validarCor(CorJogador obtida, CorJogador esperada, String mensagem) {
        if (obtida == esperada) {
            System.out.println("  ✅ " + mensagem + " → OK");
        } else {
            System.out.println("  ❌ " + mensagem + " → FALHOU!");
            System.out.println("     Esperado: " + esperada + ", Obtido: " + obtida);
        }
    }
    
    private void validarPontos(int obtido, int esperado, String mensagem) {
        if (obtido == esperado) {
            System.out.println("  ✅ " + mensagem + " → OK");
        } else {
            System.out.println("  ❌ " + mensagem + " → FALHOU!");
            System.out.println("     Esperado: " + esperado + ", Obtido: " + obtido);
        }
    }
}