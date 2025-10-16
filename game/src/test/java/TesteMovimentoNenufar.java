

import haruichiban.Model.Tabuleiro;

public class TesteMovimentoNenufar {
    
    public static void main(String[] args) {
        TesteMovimentoNenufar teste = new TesteMovimentoNenufar();
        
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║  TESTE DE MOVIMENTO DE NENÚFARES - HARU ICHIBAN ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");
        
        // Executar todos os testes
        teste.testeMovimentoSimplesDireita();
        teste.testeMovimentoSimplesEsquerda();
        teste.testeMovimentoSimplesFrente();
        teste.testeMovimentoSimplesTras();
        
        teste.testeEmpurrarDoisNenufaresDireita();
        teste.testeEmpurrarTresNenufaresDireita();
        teste.testeEmpurrarGrupoEsquerda();
        teste.testeEmpurrarGrupoFrente();
        teste.testeEmpurrarGrupoTras();
        
        teste.testeMovimentoInvalidoForaDoTabuleiro();
        teste.testeMovimentoInvalidoEmpurraForaDireita();
        teste.testeMovimentoInvalidoEmpurraForaEsquerda();
        teste.testeMovimentoInvalidoEmpurraForaFrente();
        teste.testeMovimentoInvalidoEmpurraForaTras();
        
        teste.testeMovimentoComSapo();
        teste.testeMovimentoComFlores();
        teste.testeMovimentoNenufarEscuro();
        
        teste.testeCasoComplexo();
        
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║            TODOS OS TESTES CONCLUÍDOS!          ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }
    
    // ========== TESTES DE MOVIMENTO SIMPLES ==========
    
    public void testeMovimentoSimplesDireita() {
        System.out.println("\n📝 TESTE 1: Movimento Simples para Direita");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        // Colocar um nenúfar na posição (2,1) e espaço vazio em (2,2)
        t.setCelula(2, 1, 3); // nenúfar claro
        t.setCelula(2, 2, 0); // vazio
        
        System.out.println("ANTES:");
        mostrarLinha(t, 2);
        
        boolean sucesso = t.moverNenufar(2, 1, 'D');
        
        System.out.println("\nDEPOIS:");
        mostrarLinha(t, 2);
        
        validarResultado(sucesso, true, "Movimento deveria ter sucesso");
        validarCelula(t, 2, 1, 0, "Posição origem deveria estar vazia");
        validarCelula(t, 2, 2, 3, "Posição destino deveria ter nenúfar");
    }
    
    public void testeMovimentoSimplesEsquerda() {
        System.out.println("\n📝 TESTE 2: Movimento Simples para Esquerda");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        t.setCelula(2, 3, 3); // nenúfar
        t.setCelula(2, 2, 0); // vazio
        
        System.out.println("ANTES:");
        mostrarLinha(t, 2);
        
        boolean sucesso = t.moverNenufar(2, 3, 'E');
        
        System.out.println("\nDEPOIS:");
        mostrarLinha(t, 2);
        
        validarResultado(sucesso, true, "Movimento deveria ter sucesso");
        validarCelula(t, 2, 3, 0, "Posição origem deveria estar vazia");
        validarCelula(t, 2, 2, 3, "Posição destino deveria ter nenúfar");
    }
    
    public void testeMovimentoSimplesFrente() {
        System.out.println("\n📝 TESTE 3: Movimento Simples para Frente (Cima)");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        t.setCelula(3, 2, 3); // nenúfar
        t.setCelula(2, 2, 0); // vazio
        
        System.out.println("ANTES:");
        mostrarColuna(t, 2);
        
        boolean sucesso = t.moverNenufar(3, 2, 'C');
        
        System.out.println("\nDEPOIS:");
        mostrarColuna(t, 2);
        
        validarResultado(sucesso, true, "Movimento deveria ter sucesso");
        validarCelula(t, 3, 2, 0, "Posição origem deveria estar vazia");
        validarCelula(t, 2, 2, 3, "Posição destino deveria ter nenúfar");
    }
    
    public void testeMovimentoSimplesTras() {
        System.out.println("\n📝 TESTE 4: Movimento Simples para Trás (Baixo)");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        t.setCelula(1, 2, 3); // nenúfar
        t.setCelula(2, 2, 0); // vazio
        
        System.out.println("ANTES:");
        mostrarColuna(t, 2);
        
        boolean sucesso = t.moverNenufar(1, 2, 'B');
        
        System.out.println("\nDEPOIS:");
        mostrarColuna(t, 2);
        
        validarResultado(sucesso, true, "Movimento deveria ter sucesso");
        validarCelula(t, 1, 2, 0, "Posição origem deveria estar vazia");
        validarCelula(t, 2, 2, 3, "Posição destino deveria ter nenúfar");
    }
    
    // ========== TESTES DE EMPURRAR GRUPOS ==========
    
    public void testeEmpurrarDoisNenufaresDireita() {
        System.out.println("\n📝 TESTE 5: Empurrar DOIS Nenúfares para Direita");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        // [3][3][0][0][0]
        t.setCelula(2, 0, 3);
        t.setCelula(2, 1, 3);
        t.setCelula(2, 2, 0);
        
        System.out.println("ANTES:");
        mostrarLinha(t, 2);
        
        boolean sucesso = t.moverNenufar(2, 0, 'D');
        
        System.out.println("\nDEPOIS:");
        mostrarLinha(t, 2);
        System.out.println("Esperado: [0][3][3][0][0]");
        
        validarResultado(sucesso, true, "Movimento deveria empurrar com sucesso");
        validarCelula(t, 2, 0, 0, "Posição (2,0) deveria estar vazia");
        validarCelula(t, 2, 1, 3, "Posição (2,1) deveria ter nenúfar");
        validarCelula(t, 2, 2, 3, "Posição (2,2) deveria ter nenúfar");
    }
    
    public void testeEmpurrarTresNenufaresDireita() {
        System.out.println("\n📝 TESTE 6: Empurrar TRÊS Nenúfares para Direita");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        // [3][3][3][0][0]
        t.setCelula(2, 0, 3);
        t.setCelula(2, 1, 3);
        t.setCelula(2, 2, 3);
        t.setCelula(2, 3, 0);
        
        System.out.println("ANTES:");
        mostrarLinha(t, 2);
        
        boolean sucesso = t.moverNenufar(2, 0, 'D');
        
        System.out.println("\nDEPOIS:");
        mostrarLinha(t, 2);
        System.out.println("Esperado: [0][3][3][3][0]");
        
        validarResultado(sucesso, true, "Movimento deveria empurrar 3 nenúfares");
        validarCelula(t, 2, 0, 0, "Posição (2,0) deveria estar vazia");
        validarCelula(t, 2, 1, 3, "Posição (2,1) deveria ter nenúfar");
        validarCelula(t, 2, 2, 3, "Posição (2,2) deveria ter nenúfar");
        validarCelula(t, 2, 3, 3, "Posição (2,3) deveria ter nenúfar");
    }
    
    public void testeEmpurrarGrupoEsquerda() {
        System.out.println("\n📝 TESTE 7: Empurrar Grupo para Esquerda");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        // [0][0][3][3][3]
        t.setCelula(2, 2, 3);
        t.setCelula(2, 3, 3);
        t.setCelula(2, 4, 3);
        t.setCelula(2, 1, 0);
        
        System.out.println("ANTES:");
        mostrarLinha(t, 2);
        
        boolean sucesso = t.moverNenufar(2, 4, 'E');
        
        System.out.println("\nDEPOIS:");
        mostrarLinha(t, 2);
        System.out.println("Esperado: [0][3][3][3][0]");
        
        validarResultado(sucesso, true, "Movimento deveria empurrar para esquerda");
    }
    
    public void testeEmpurrarGrupoFrente() {
        System.out.println("\n📝 TESTE 8: Empurrar Grupo para Frente (Cima)");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        t.setCelula(1, 2, 0);
        t.setCelula(2, 2, 3);
        t.setCelula(3, 2, 3);
        
        System.out.println("ANTES:");
        mostrarColuna(t, 2);
        
        boolean sucesso = t.moverNenufar(3, 2, 'C');
        
        System.out.println("\nDEPOIS:");
        mostrarColuna(t, 2);
        
        validarResultado(sucesso, true, "Movimento deveria empurrar para frente");
    }
    
    public void testeEmpurrarGrupoTras() {
        System.out.println("\n📝 TESTE 9: Empurrar Grupo para Trás (Baixo)");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        t.setCelula(1, 2, 3);
        t.setCelula(2, 2, 3);
        t.setCelula(3, 2, 0);
        
        System.out.println("ANTES:");
        mostrarColuna(t, 2);
        
        boolean sucesso = t.moverNenufar(1, 2, 'B');
        
        System.out.println("\nDEPOIS:");
        mostrarColuna(t, 2);
        
        validarResultado(sucesso, true, "Movimento deveria empurrar para trás");
    }
    
    // ========== TESTES DE MOVIMENTOS INVÁLIDOS ==========
    
    public void testeMovimentoInvalidoForaDoTabuleiro() {
        System.out.println("\n📝 TESTE 10: Movimento Inválido - Fora do Tabuleiro");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        t.setCelula(2, 4, 3); // nenúfar na borda direita
        
        System.out.println("ANTES:");
        mostrarLinha(t, 2);
        
        boolean sucesso = t.moverNenufar(2, 4, 'D'); // tentar mover para direita
        
        System.out.println("\nDEPOIS:");
        mostrarLinha(t, 2);
        
        validarResultado(sucesso, false, "Movimento deveria FALHAR (fora do tabuleiro)");
        validarCelula(t, 2, 4, 3, "Nenúfar deveria permanecer na posição original");
    }
    
    public void testeMovimentoInvalidoEmpurraForaDireita() {
        System.out.println("\n📝 TESTE 11: Movimento Inválido - Empurra para Fora (Direita)");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        // [0][0][3][3][3] - último nenúfar na borda
        t.setCelula(2, 2, 3);
        t.setCelula(2, 3, 3);
        t.setCelula(2, 4, 3);
        
        System.out.println("ANTES:");
        mostrarLinha(t, 2);
        
        boolean sucesso = t.moverNenufar(2, 2, 'D'); // empurraria (2,4) para fora
        
        System.out.println("\nDEPOIS:");
        mostrarLinha(t, 2);
        
        validarResultado(sucesso, false, "Movimento deveria FALHAR (empurra para fora)");
    }
    
    public void testeMovimentoInvalidoEmpurraForaEsquerda() {
        System.out.println("\n📝 TESTE 12: Movimento Inválido - Empurra para Fora (Esquerda)");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        // [3][3][3][0][0]
        t.setCelula(2, 0, 3);
        t.setCelula(2, 1, 3);
        t.setCelula(2, 2, 3);
        
        System.out.println("ANTES:");
        mostrarLinha(t, 2);
        
        boolean sucesso = t.moverNenufar(2, 2, 'E');
        
        System.out.println("\nDEPOIS:");
        mostrarLinha(t, 2);
        
        validarResultado(sucesso, false, "Movimento deveria FALHAR");
    }
    
    public void testeMovimentoInvalidoEmpurraForaFrente() {
        System.out.println("\n📝 TESTE 13: Movimento Inválido - Empurra para Fora (Frente/Cima)");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        t.setCelula(0, 2, 3);
        t.setCelula(1, 2, 3);
        t.setCelula(2, 2, 3);
        
        System.out.println("ANTES:");
        mostrarColuna(t, 2);
        
        boolean sucesso = t.moverNenufar(2, 2, 'C');
        
        System.out.println("\nDEPOIS:");
        mostrarColuna(t, 2);
        
        validarResultado(sucesso, false, "Movimento deveria FALHAR");
    }
    
    public void testeMovimentoInvalidoEmpurraForaTras() {
        System.out.println("\n📝 TESTE 14: Movimento Inválido - Empurra para Fora (Trás/Baixo)");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        t.setCelula(2, 2, 3);
        t.setCelula(3, 2, 3);
        t.setCelula(4, 2, 3);
        
        System.out.println("ANTES:");
        mostrarColuna(t, 2);
        
        boolean sucesso = t.moverNenufar(2, 2, 'B');
        
        System.out.println("\nDEPOIS:");
        mostrarColuna(t, 2);
        
        validarResultado(sucesso, false, "Movimento deveria FALHAR");
    }
    
    // ========== TESTES COM ELEMENTOS DO JOGO ==========
    
    public void testeMovimentoComSapo() {
        System.out.println("\n📝 TESTE 15: Movimento com Sapo");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        // [5][3][0] - sapo vermelho + nenúfar + vazio
        t.setCelula(2, 0, 5); // sapo vermelho
        t.setCelula(2, 1, 3); // nenúfar
        t.setCelula(2, 2, 0); // vazio
        
        System.out.println("ANTES:");
        mostrarLinha(t, 2);
        System.out.println("Legenda: 5=sapo vermelho, 3=nenúfar, 0=vazio");
        
        boolean sucesso = t.moverNenufar(2, 0, 'D');
        
        System.out.println("\nDEPOIS:");
        mostrarLinha(t, 2);
        
        validarResultado(sucesso, true, "Sapo deveria ser empurrado");
    }
    
    public void testeMovimentoComFlores() {
        System.out.println("\n📝 TESTE 16: Movimento com Flores");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        // [7][8][0] - flor vermelha + flor amarela + vazio
        t.setCelula(2, 0, 7); // nenúfar com flor vermelha
        t.setCelula(2, 1, 8); // nenúfar com flor amarela
        t.setCelula(2, 2, 0); // vazio
        
        System.out.println("ANTES:");
        mostrarLinha(t, 2);
        System.out.println("Legenda: 7=flor vermelha, 8=flor amarela");
        
        boolean sucesso = t.moverNenufar(2, 0, 'D');
        
        System.out.println("\nDEPOIS:");
        mostrarLinha(t, 2);
        
        validarResultado(sucesso, true, "Flores deveriam ser empurradas juntas");
    }
    
    public void testeMovimentoNenufarEscuro() {
        System.out.println("\n📝 TESTE 17: Movimento de Nenúfar Escuro");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        // [4][3][0] - nenúfar escuro + nenúfar claro + vazio
        t.setCelula(2, 0, 4); // nenúfar escuro
        t.setCelula(2, 1, 3); // nenúfar claro
        t.setCelula(2, 2, 0); // vazio
        
        System.out.println("ANTES:");
        mostrarLinha(t, 2);
        System.out.println("Legenda: 4=nenúfar escuro, 3=nenúfar claro");
        
        boolean sucesso = t.moverNenufar(2, 0, 'D');
        
        System.out.println("\nDEPOIS:");
        mostrarLinha(t, 2);
        
        validarResultado(sucesso, true, "Nenúfar escuro deveria mover normalmente");
        validarCelula(t, 2, 1, 4, "Nenúfar escuro deveria estar em (2,1)");
    }
    
    public void testeCasoComplexo() {
        System.out.println("\n📝 TESTE 18: Caso Complexo - Cadeia Mista");
        System.out.println("─".repeat(50));
        
        Tabuleiro t = new Tabuleiro();
        configurarTabuleiroSimples(t);
        
        // [4][7][5][3][0] - escuro + flor + sapo + nenúfar + vazio
        t.setCelula(2, 0, 4); // nenúfar escuro
        t.setCelula(2, 1, 7); // flor vermelha
        t.setCelula(2, 2, 5); // sapo vermelho
        t.setCelula(2, 3, 3); // nenúfar claro
        t.setCelula(2, 4, 0); // vazio
        
        System.out.println("ANTES:");
        mostrarLinha(t, 2);
        System.out.println("Legenda: 4=escuro, 7=flor🔴, 5=sapo🔴, 3=nenúfar");
        
        boolean sucesso = t.moverNenufar(2, 0, 'D');
        
        System.out.println("\nDEPOIS:");
        mostrarLinha(t, 2);
        System.out.println("Esperado: [0][4][7][5][3]");
        
        validarResultado(sucesso, true, "Cadeia mista deveria mover corretamente");
        validarCelula(t, 2, 0, 0, "Posição inicial deveria estar vazia");
        validarCelula(t, 2, 1, 4, "Nenúfar escuro em (2,1)");
        validarCelula(t, 2, 2, 7, "Flor vermelha em (2,2)");
        validarCelula(t, 2, 3, 5, "Sapo vermelho em (2,3)");
        validarCelula(t, 2, 4, 3, "Nenúfar claro em (2,4)");
    }
    
    // ========== MÉTODOS AUXILIARES ==========
    
    private void configurarTabuleiroSimples(Tabuleiro t) {
        // Limpar tudo
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                t.setCelula(i, j, 0);
            }
        }
    }
    
    private void mostrarLinha(Tabuleiro t, int linha) {
        System.out.print("  Linha " + linha + ": [");
        for (int j = 0; j < 5; j++) {
            System.out.print(t.getCelula(linha, j));
            if (j < 4) System.out.print("][");
        }
        System.out.println("]");
    }
    
    private void mostrarColuna(Tabuleiro t, int coluna) {
        System.out.println("  Coluna " + coluna + ":");
        for (int i = 0; i < 5; i++) {
            System.out.println("    Linha " + i + ": [" + t.getCelula(i, coluna) + "]");
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
    
    private void validarCelula(Tabuleiro t, int linha, int coluna, int valorEsperado, String mensagem) {
        int valorObtido = t.getCelula(linha, coluna);
        if (valorObtido == valorEsperado) {
            System.out.println("  ✅ " + mensagem + " → OK");
        } else {
            System.out.println("  ❌ " + mensagem + " → FALHOU!");
            System.out.println("     Esperado: " + valorEsperado + ", Obtido: " + valorObtido);
        }
    }
}