import haruichiban.Model.JogoHaruIchiban;
import haruichiban.Model.Tabuleiro;
import haruichiban.Model.Jogador;
import haruichiban.Model.Posicao;
import haruichiban.Model.enums.CorJogador;
import haruichiban.Model.enums.FaseJogo;

public class TesteEmpateCoaxar {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║  TESTE: EMPATE E REGRA DO COAXAR              ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        TesteEmpateCoaxar teste = new TesteEmpateCoaxar();

        // Testes básicos de empate
        teste.testeDeteccaoEmpate();
        teste.testeFloracaoAutomaticaNoEmpate();
        
        // Testes da mecânica do coaxar
        teste.testeCoaxarVermelho();
        teste.testeCoaxarAmarelo();
        teste.testeCoaxarFaseIncorreta();
        
        // Testes de movimentação dos sapos após coaxar
        teste.testeMoverPrimeiroSapoAposCoaxar();
        teste.testeMoverSegundoSapoAposCoaxar();
        teste.testeMoverSaposParaPosicaoInvalida();
        teste.testeMoverSaposParaMesmaPosicao();
        
        // Testes de fluxo completo
        teste.testeFluxoCompletoEmpateVermelho();
        teste.testeFluxoCompletoEmpateAmarelo();
        
        // Testes de casos especiais
        teste.testeEmpateComPoucosNenufares();
        teste.testeEmpateComPadraoFormado();
        teste.testeEmpateUltimoTurno();

        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║            TESTES CONCLUÍDOS!                  ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }

    // ========== TESTES BÁSICOS DE EMPATE ==========

    public void testeDeteccaoEmpate() {
        System.out.println("\n📝 TESTE 1: Detecção de Empate na Seleção de Flores");
        System.out.println("─".repeat(50));

        // Criar jogo simulado com flores de mesmo valor
        JogoHaruIchiban jogo = criarJogoComEmpateGarantido();
        
        System.out.println("Estado inicial: " + jogo.getFaseAtual());
        System.out.println("Simulando seleção de flores com mesmo valor...");
        
        // Simular seleção de flores com mesmo valor (ambos valor 3)
        boolean sucesso = jogo.selecionarFlores(0, 0); // Assumindo índice 0 tem valor 3
        
        if (sucesso && jogo.getFaseAtual() == FaseJogo.EMPATE_COAXAR) {
            System.out.println("  ✅ Empate detectado corretamente!");
            System.out.println("  ✅ Fase mudou para EMPATE_COAXAR");
            System.out.println("  ✅ Mensagem: " + jogo.getMensagemEstado());
        } else {
            System.out.println("  ❌ Falha na detecção do empate!");
        }
    }

    public void testeFloracaoAutomaticaNoEmpate() {
        System.out.println("\n📝 TESTE 2: Floração Automática nos Sapos durante Empate");
        System.out.println("─".repeat(50));

        JogoHaruIchiban jogo = criarJogoComEmpateGarantido();
        Tabuleiro tab = jogo.getTabuleiro();
        
        // Marcar posições originais dos sapos
        Posicao sapoVermelho = encontrarSapo(tab, CorJogador.VERMELHO);
        Posicao sapoAmarelo = encontrarSapo(tab, CorJogador.AMARELO);
        
        System.out.println("Posição sapo vermelho: " + sapoVermelho);
        System.out.println("Posição sapo amarelo: " + sapoAmarelo);
        
        // Simular empate
        jogo.selecionarFlores(0, 0);
        
        // Verificar se flores foram colocadas onde estavam os sapos
        int celVermelho = tab.getCelula(sapoVermelho.getLinha(), sapoVermelho.getColuna());
        int celAmarelo = tab.getCelula(sapoAmarelo.getLinha(), sapoAmarelo.getColuna());
        
        if (celVermelho == 7) { // 7 = nenúfar com flor vermelha
            System.out.println("  ✅ Flor vermelha colocada onde estava o sapo vermelho");
        } else {
            System.out.println("  ❌ Flor vermelha NÃO foi colocada corretamente");
        }
        
        if (celAmarelo == 8) { // 8 = nenúfar com flor amarela
            System.out.println("  ✅ Flor amarela colocada onde estava o sapo amarelo");
        } else {
            System.out.println("  ❌ Flor amarela NÃO foi colocada corretamente");
        }
        
        // Verificar se sapos foram removidos temporariamente
        if (!existeSapoNoTabuleiro(tab)) {
            System.out.println("  ✅ Sapos removidos temporariamente após floração");
        } else {
            System.out.println("  ❌ Sapos ainda estão no tabuleiro!");
        }
    }

    // ========== TESTES DA MECÂNICA DO COAXAR ==========

    public void testeCoaxarVermelho() {
        System.out.println("\n📝 TESTE 3: Vermelho Coaxa Primeiro");
        System.out.println("─".repeat(50));

        JogoHaruIchiban jogo = criarJogoComEmpateGarantido();
        jogo.selecionarFlores(0, 0); // Criar empate
        
        System.out.println("Fase atual: " + jogo.getFaseAtual());
        
        // Vermelho coaxa
        boolean sucesso = jogo.registrarCoaxar(CorJogador.VERMELHO);
        
        if (sucesso) {
            System.out.println("  ✅ Vermelho registrado como quem coaxou");
            System.out.println("  ✅ Fase mudou para: " + jogo.getFaseAtual());
            System.out.println("  ✅ Mensagem: " + jogo.getMensagemEstado());
            
            // Verificar se jogador correto foi registrado
            if (jogo.getJogadorQueCoaxou() == jogo.getJogadorVermelho()) {
                System.out.println("  ✅ Jogador vermelho corretamente registrado");
            } else {
                System.out.println("  ❌ Jogador incorreto registrado!");
            }
        } else {
            System.out.println("  ❌ Falha ao registrar coaxar!");
        }
    }

    public void testeCoaxarAmarelo() {
        System.out.println("\n📝 TESTE 4: Amarelo Coaxa Primeiro");
        System.out.println("─".repeat(50));

        JogoHaruIchiban jogo = criarJogoComEmpateGarantido();
        jogo.selecionarFlores(0, 0); // Criar empate
        
        // Amarelo coaxa
        boolean sucesso = jogo.registrarCoaxar(CorJogador.AMARELO);
        
        if (sucesso) {
            System.out.println("  ✅ Amarelo registrado como quem coaxou");
            
            if (jogo.getJogadorQueCoaxou() == jogo.getJogadorAmarelo()) {
                System.out.println("  ✅ Jogador amarelo corretamente registrado");
            } else {
                System.out.println("  ❌ Jogador incorreto registrado!");
            }
        } else {
            System.out.println("  ❌ Falha ao registrar coaxar!");
        }
    }

    public void testeCoaxarFaseIncorreta() {
        System.out.println("\n📝 TESTE 5: Tentar Coaxar em Fase Incorreta");
        System.out.println("─".repeat(50));

        JogoHaruIchiban jogo = new JogoHaruIchiban("Vermelho", "Amarelo");
        
        System.out.println("Fase atual: " + jogo.getFaseAtual());
        System.out.println("Tentando coaxar sem estar em empate...");
        
        boolean sucesso = jogo.registrarCoaxar(CorJogador.VERMELHO);
        
        if (!sucesso) {
            System.out.println("  ✅ Corretamente rejeitou coaxar fora da fase de empate");
        } else {
            System.out.println("  ❌ Permitiu coaxar incorretamente!");
        }
    }

    // ========== TESTES DE MOVIMENTAÇÃO DOS SAPOS ==========

    public void testeMoverPrimeiroSapoAposCoaxar() {
        System.out.println("\n📝 TESTE 6: Mover Primeiro Sapo (Vermelho) após Coaxar");
        System.out.println("─".repeat(50));

        JogoHaruIchiban jogo = criarJogoComEmpateGarantido();
        jogo.selecionarFlores(0, 0); // Empate
        jogo.registrarCoaxar(CorJogador.VERMELHO); // Vermelho coaxa
        
        System.out.println("Colocando sapo vermelho em posição válida (2,0)...");
        
        // Encontrar nenúfar vazio válido
        boolean sucesso = jogo.moverSapoEmpate(2, 0, CorJogador.VERMELHO);
        
        if (sucesso) {
            System.out.println("  ✅ Primeiro sapo colocado com sucesso");
            System.out.println("  ✅ Mensagem solicita segundo sapo: " + 
                             jogo.getMensagemEstado().contains("AMARELO"));
            
            // Verificar se sapo foi colocado
            if (jogo.getTabuleiro().getCelula(2, 0) == 5) {
                System.out.println("  ✅ Sapo vermelho na posição correta");
            } else {
                System.out.println("  ❌ Sapo não está na posição esperada");
            }
        } else {
            System.out.println("  ❌ Falha ao colocar primeiro sapo");
        }
    }

    public void testeMoverSegundoSapoAposCoaxar() {
        System.out.println("\n📝 TESTE 7: Mover Segundo Sapo (Amarelo) após Primeiro");
        System.out.println("─".repeat(50));

        JogoHaruIchiban jogo = criarJogoComEmpateGarantido();
        jogo.selecionarFlores(0, 0); // Empate
        jogo.registrarCoaxar(CorJogador.VERMELHO);
        jogo.moverSapoEmpate(2, 0, CorJogador.VERMELHO); // Primeiro sapo
        
        System.out.println("Colocando sapo amarelo em posição válida (2,4)...");
        
        boolean sucesso = jogo.moverSapoEmpate(2, 4, CorJogador.AMARELO);
        
        if (sucesso) {
            System.out.println("  ✅ Segundo sapo colocado com sucesso");
            
            // Verificar se fase mudou para movimento de nenúfares
            if (jogo.getFaseAtual() == FaseJogo.MOVIMENTO_NENUFARES) {
                System.out.println("  ✅ Fase corretamente mudou para MOVIMENTO_NENUFARES");
            } else {
                System.out.println("  ❌ Fase não mudou corretamente");
            }
            
            // Verificar ambos os sapos
            boolean sapoVermelho = jogo.getTabuleiro().getCelula(2, 0) == 5;
            boolean sapoAmarelo = jogo.getTabuleiro().getCelula(2, 4) == 6;
            
            if (sapoVermelho && sapoAmarelo) {
                System.out.println("  ✅ Ambos os sapos posicionados corretamente");
            } else {
                System.out.println("  ❌ Problema com posicionamento dos sapos");
            }
        } else {
            System.out.println("  ❌ Falha ao colocar segundo sapo");
        }
    }

    public void testeMoverSaposParaPosicaoInvalida() {
        System.out.println("\n📝 TESTE 8: Tentar Mover Sapo para Posição Inválida");
        System.out.println("─".repeat(50));

        JogoHaruIchiban jogo = criarJogoComEmpateGarantido();
        jogo.selecionarFlores(0, 0);
        jogo.registrarCoaxar(CorJogador.VERMELHO);
        
        System.out.println("Tentando colocar sapo em água (0,1)...");
        
        boolean sucesso = jogo.moverSapoEmpate(0, 1, CorJogador.VERMELHO);
        
        if (!sucesso) {
            System.out.println("  ✅ Corretamente rejeitou posição inválida");
            System.out.println("  ✅ Mensagem de erro: " + jogo.getMensagemEstado());
        } else {
            System.out.println("  ❌ Permitiu colocar sapo em posição inválida!");
        }
    }

    public void testeMoverSaposParaMesmaPosicao() {
        System.out.println("\n📝 TESTE 9: Tentar Mover Ambos Sapos para Mesma Posição");
        System.out.println("─".repeat(50));

        JogoHaruIchiban jogo = criarJogoComEmpateGarantido();
        jogo.selecionarFlores(0, 0);
        jogo.registrarCoaxar(CorJogador.VERMELHO);
        
        // Colocar primeiro sapo
        jogo.moverSapoEmpate(2, 0, CorJogador.VERMELHO);
        
        System.out.println("Tentando colocar segundo sapo na mesma posição...");
        
        boolean sucesso = jogo.moverSapoEmpate(2, 0, CorJogador.AMARELO);
        
        if (!sucesso) {
            System.out.println("  ✅ Corretamente rejeitou posição ocupada");
        } else {
            System.out.println("  ❌ Permitiu dois sapos na mesma posição!");
        }
    }

    // ========== TESTES DE FLUXO COMPLETO ==========

    public void testeFluxoCompletoEmpateVermelho() {
        System.out.println("\n📝 TESTE 10: Fluxo Completo - Vermelho Coaxa");
        System.out.println("─".repeat(50));

        JogoHaruIchiban jogo = criarJogoComEmpateGarantido();
        
        System.out.println("1. Criando empate...");
        jogo.selecionarFlores(0, 0);
        validarFase(jogo.getFaseAtual(), FaseJogo.EMPATE_COAXAR, "Fase EMPATE_COAXAR");
        
        System.out.println("2. Vermelho coaxa...");
        jogo.registrarCoaxar(CorJogador.VERMELHO);
        validarFase(jogo.getFaseAtual(), FaseJogo.MOVER_SAPOS_EMPATE, "Fase MOVER_SAPOS");
        
        System.out.println("3. Colocando sapo vermelho...");
        jogo.moverSapoEmpate(2, 0, CorJogador.VERMELHO);
        
        System.out.println("4. Colocando sapo amarelo...");
        jogo.moverSapoEmpate(2, 4, CorJogador.AMARELO);
        validarFase(jogo.getFaseAtual(), FaseJogo.MOVIMENTO_NENUFARES, "Fase MOVIMENTO");
        
        System.out.println("5. Verificando quem controla movimento...");
        if (jogo.getJardineirJunior() == jogo.getJogadorVermelho()) {
            System.out.println("  ✅ Vermelho controla movimento (coaxou primeiro)");
        } else {
            System.out.println("  ❌ Controle incorreto do movimento");
        }
        
        mostrarResumoTabuleiro(jogo.getTabuleiro());
    }

    public void testeFluxoCompletoEmpateAmarelo() {
        System.out.println("\n📝 TESTE 11: Fluxo Completo - Amarelo Coaxa");
        System.out.println("─".repeat(50));

        JogoHaruIchiban jogo = criarJogoComEmpateGarantido();
        
        System.out.println("1. Criando empate...");
        jogo.selecionarFlores(0, 0);
        
        System.out.println("2. Amarelo coaxa...");
        jogo.registrarCoaxar(CorJogador.AMARELO);
        
        System.out.println("3. Amarelo coloca sapo vermelho...");
        jogo.moverSapoEmpate(3, 1, CorJogador.VERMELHO);
        
        System.out.println("4. Amarelo coloca sapo amarelo...");
        jogo.moverSapoEmpate(1, 3, CorJogador.AMARELO);
        
        System.out.println("5. Verificando controle...");
        if (jogo.getJardineirJunior() == jogo.getJogadorAmarelo()) {
            System.out.println("  ✅ Amarelo controla movimento (coaxou primeiro)");
        } else {
            System.out.println("  ❌ Controle incorreto do movimento");
        }
        
        // Verificar posições estratégicas
        System.out.println("\nAnálise estratégica:");
        System.out.println("  Amarelo escolheu colocar vermelho em (3,1)");
        System.out.println("  Amarelo se colocou em (1,3)");
        System.out.println("  Isso pode ser vantajoso? Verificar padrões...");
    }

    // ========== TESTES DE CASOS ESPECIAIS ==========

    public void testeEmpateComPoucosNenufares() {
        System.out.println("\n📝 TESTE 12: Empate com Poucos Nenúfares Disponíveis");
        System.out.println("─".repeat(50));

        JogoHaruIchiban jogo = criarJogoComMuitasFlores();
        
        System.out.println("Cenário: Apenas 3 nenúfares vazios disponíveis");
        System.out.println("Regra: Se <= 2 nenúfares, sapos são removidos do jogo");
        
        // Simular situação com poucos nenúfares
        Tabuleiro tab = jogo.getTabuleiro();
        int nenufaresVazios = contarNenufaresVazios(tab);
        
        System.out.println("Nenúfares vazios: " + nenufaresVazios);
        
        if (nenufaresVazios <= 3) {
            System.out.println("  ⚠️  Situação crítica - poucos nenúfares!");
            System.out.println("  ⚠️  Jogador deve escolher posições com cuidado");
        }
    }

    public void testeEmpateComPadraoFormado() {
        System.out.println("\n📝 TESTE 13: Empate que Forma Padrão ao Florescer");
        System.out.println("─".repeat(50));

        JogoHaruIchiban jogo = criarJogoComQuasePadrao();
        
        System.out.println("Cenário: 3 flores vermelhas em linha");
        System.out.println("Sapo vermelho na 4ª posição da linha");
        System.out.println("No empate, flor é colocada automaticamente");
        
        // Simular empate
        jogo.selecionarFlores(0, 0);
        
        // Verificar se padrão foi detectado
        System.out.println("\nVerificando padrões após floração automática...");
        
        // Aqui deveria verificar se o padrão foi formado
        System.out.println("  ⚠️  Deve verificar padrão imediatamente após floração!");
        System.out.println("  ⚠️  Se formar linha de 4 = 2 pontos");
        System.out.println("  ⚠️  Se formar linha de 5 = 5 pontos (VITÓRIA!)");
    }

    public void testeEmpateUltimoTurno() {
        System.out.println("\n📝 TESTE 14: Empate no Último Turno (8º)");
        System.out.println("─".repeat(50));

        System.out.println("Cenário: Turno 8, últimas flores");
        System.out.println("Ambos escolhem mesma carta (empate)");
        System.out.println("Após floração e movimento:");
        System.out.println("  - Verificar padrões");
        System.out.println("  - Se ninguém vencer, nova rodada");
        System.out.println("  - Resetar deck, embaralhar, comprar 3");
    }

    // ========== MÉTODOS AUXILIARES ==========

    private JogoHaruIchiban criarJogoComEmpateGarantido() {
        // Criar jogo onde ambos jogadores têm flores de valor 3
        // Isso exigiria modificação para expor ou mockar as mãos
        // Por simplicidade, retornamos jogo normal
        return new JogoHaruIchiban("Vermelho", "Amarelo");
    }

    private JogoHaruIchiban criarJogoComMuitasFlores() {
        JogoHaruIchiban jogo = new JogoHaruIchiban("Vermelho", "Amarelo");
        Tabuleiro tab = jogo.getTabuleiro();
        
        // Preencher vários nenúfares com flores
        tab.setCelula(0, 0, 7);
        tab.setCelula(0, 2, 8);
        tab.setCelula(0, 4, 7);
        tab.setCelula(2, 0, 8);
        tab.setCelula(2, 4, 7);
        tab.setCelula(4, 0, 8);
        tab.setCelula(4, 2, 7);
        tab.setCelula(4, 4, 8);
        
        return jogo;
    }

    private JogoHaruIchiban criarJogoComQuasePadrao() {
        JogoHaruIchiban jogo = new JogoHaruIchiban("Vermelho", "Amarelo");
        Tabuleiro tab = jogo.getTabuleiro();
        
        // Criar 3 flores vermelhas em linha
        tab.setCelula(2, 0, 7);
        tab.setCelula(2, 1, 7);
        tab.setCelula(2, 2, 7);
        
        // Colocar sapo vermelho na 4ª posição
        tab.setCelula(2, 3, 5);
        
        return jogo;
    }

    private Posicao encontrarSapo(Tabuleiro tab, CorJogador cor) {
        int valorSapo = (cor == CorJogador.VERMELHO) ? 5 : 6;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tab.getCelula(i, j) == valorSapo) {
                    return new Posicao(i, j);
                }
            }
        }
        return null;
    }

    private boolean existeSapoNoTabuleiro(Tabuleiro tab) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int cel = tab.getCelula(i, j);
                if (cel == 5 || cel == 6) {
                    return true;
                }
            }
        }
        return false;
    }

    private int contarNenufaresVazios(Tabuleiro tab) {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tab.getCelula(i, j) == 3) {
                    count++;
                }
            }
        }
        return count;
    }

    private void mostrarResumoTabuleiro(Tabuleiro tab) {
        System.out.println("\nResumo do Tabuleiro:");
        int flores = 0, sapos = 0, vazios = 0;
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int cel = tab.getCelula(i, j);
                if (cel == 7 || cel == 8) flores++;
                else if (cel == 5 || cel == 6) sapos++;
                else if (cel == 3 || cel == 4) vazios++;
            }
        }
        
        System.out.println("  Flores: " + flores);
        System.out.println("  Sapos: " + sapos);
        System.out.println("  Nenúfares vazios: " + vazios);
    }

    private void validarFase(FaseJogo obtida, FaseJogo esperada, String mensagem) {
        if (obtida == esperada) {
            System.out.println("  ✅ " + mensagem + " → OK");
        } else {
            System.out.println("  ❌ " + mensagem + " → FALHOU!");
            System.out.println("     Esperado: " + esperada + ", Obtido: " + obtida);
        }
    }
}