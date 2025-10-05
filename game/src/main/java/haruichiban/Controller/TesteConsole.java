package haruichiban.Controller;
import haruichiban.Model.*;
import haruichiban.Model.enums.FaseJogo;
import java.util.Scanner;
import java.util.List;

public class TesteConsole {
    private JogoHaruIchiban jogo;
    private Scanner scanner;
    
    public TesteConsole() {
        scanner = new Scanner(System.in);
    }
    
    public void iniciar() {
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║      HARU ICHIBAN - TESTE         ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        
        System.out.print("Nome do Jogador VERMELHO: ");
        String nomeVermelho = scanner.nextLine();
        
        System.out.print("Nome do Jogador AMARELO: ");
        String nomeAmarelo = scanner.nextLine();
        
        jogo = new JogoHaruIchiban(nomeVermelho, nomeAmarelo);
        
        System.out.println("\n✓ Jogo iniciado!\n");
        
        loopPrincipal();
    }
    
    private void loopPrincipal() {
        while (!jogo.isJogoFinalizado()) {
            mostrarEstadoJogo();
            
            FaseJogo fase = jogo.getFaseAtual();
            
            switch (fase) {
                case SELECAO_FLOR:
                    faseSelecaoFlor();
                    break;
                    
                case FLORACAO_JUNIOR:
                    faseFloracaoJunior();
                    break;
                    
                case FLORACAO_SENIOR:
                    faseFloracaoSenior();
                    break;
                    
                case MOVIMENTO_NENUFARES:
                    faseMovimentoNenufares();
                    break;
                    
                case SELECAO_NENUFAR_ESCURO:
                    faseSelecaoNenufarEscuro();
                    break;
                    
                case FIM_RODADA:
                    fimDeRodada();
                    break;
            }
        }
        
        mostrarVencedor();
    }
    
    private void mostrarEstadoJogo() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TURNO: " + jogo.getTurnoAtual());
        System.out.println("FASE: " + jogo.getFaseAtual().getDescricao());
        System.out.println("STATUS: " + jogo.getMensagemEstado());
        System.out.println("=".repeat(60));
        
        // Mostrar pontuação
        System.out.println("\n📊 PLACAR:");
        System.out.println("  " + jogo.getJogadorVermelho().getNome() + " (VERMELHO): " + 
                          jogo.getJogadorVermelho().getPontuacao() + " pontos");
        System.out.println("  " + jogo.getJogadorAmarelo().getNome() + " (AMARELO): " + 
                          jogo.getJogadorAmarelo().getPontuacao() + " pontos");
        
        // Mostrar tabuleiro
        System.out.println("\n🎮 TABULEIRO:");
        jogo.getTabuleiro().verTabuleiro();
        
        System.out.println("\nLEGENDA:");
        System.out.println("  0=vazio | 1=flor🔴 | 2=flor🟡 | 3=nenúfar⚪");
        System.out.println("  4=nenúfar⚫ | 5=sapo🔴 | 6=sapo🟡 | 7=flor🔴 | 8=flor🟡");
    }
    
    private void faseSelecaoFlor() {
        System.out.println("\n🌸 SELEÇÃO DE FLORES");
        
        // Mostrar mão do jogador vermelho
        System.out.println("\n" + jogo.getJogadorVermelho().getNome() + " (VERMELHO):");
        mostrarMao(jogo.getJogadorVermelho());
        System.out.print("Escolha uma flor (0, 1 ou 2): ");
        int escolhaVermelho = lerInteiro(0, 2);
        
        // Mostrar mão do jogador amarelo
        System.out.println("\n" + jogo.getJogadorAmarelo().getNome() + " (AMARELO):");
        mostrarMao(jogo.getJogadorAmarelo());
        System.out.print("Escolha uma flor (0, 1 ou 2): ");
        int escolhaAmarelo = lerInteiro(0, 2);
        
        if (!jogo.selecionarFlores(escolhaVermelho, escolhaAmarelo)) {
            System.out.println("❌ Erro ao selecionar flores!");
        } else {
            System.out.println("\n✓ Flores selecionadas!");
            if (jogo.getJardineirJunior() != null) {
                System.out.println("  🌱 Jardineiro Júnior: " + jogo.getJardineirJunior().getNome() + 
                                 " (valor " + jogo.getJardineirJunior().getFlorSelecionada().getValor() + ")");
                System.out.println("  🌳 Jardineiro Sênior: " + jogo.getJardineirSenior().getNome() + 
                                 " (valor " + jogo.getJardineirSenior().getFlorSelecionada().getValor() + ")");
            }
        }
        
        pausar();
    }
    
    private void mostrarMao(Jogador jogador) {
        List<Flor> mao = jogador.getMao();
        for (int i = 0; i < mao.size(); i++) {
            System.out.println("  [" + i + "] Flor valor: " + mao.get(i).getValor());
        }
    }
    
    private void faseFloracaoJunior() {
        System.out.println("\n🌱 FLORAÇÃO DO JARDINEIRO JÚNIOR");
        System.out.println("Floração automática no nenúfar escuro...");
        
        Posicao escuro = jogo.getTabuleiro().encontrarNenufarEscuro();
        if (escuro != null) {
            System.out.println("Nenúfar escuro está em: (" + escuro.getLinha() + ", " + escuro.getColuna() + ")");
            
            // Verificar se tem sapo
            if (jogo.getTabuleiro().temSapo(escuro.getLinha(), escuro.getColuna())) {
                System.out.println("⚠️  Há um sapo no nenúfar escuro!");
                moverSapoInterativo();
            }
        }
        
        if (!jogo.executarFloracaoJunior()) {
            System.out.println("❌ Erro na floração júnior!");
        } else {
            System.out.println("✓ Floração júnior executada!");
        }
        
        pausar();
    }
    
    private void faseFloracaoSenior() {
        System.out.println("\n🌳 FLORAÇÃO DO JARDINEIRO SÊNIOR");
        System.out.println(jogo.getJardineirSenior().getNome() + " escolhe onde colocar sua flor.");
        
        System.out.print("Linha (0-4): ");
        int linha = lerInteiro(0, 4);
        
        System.out.print("Coluna (0-4): ");
        int coluna = lerInteiro(0, 4);
        
        // Verificar se tem sapo
        if (jogo.getTabuleiro().temSapo(linha, coluna)) {
            System.out.println("⚠️  Há um sapo nesta posição!");
            moverSapoInterativo();
        }
        
        if (!jogo.executarFloracaoSenior(linha, coluna)) {
            System.out.println("❌ " + jogo.getMensagemEstado());
            System.out.println("Tente novamente...");
        } else {
            System.out.println("✓ Floração sênior executada!");
        }
        
        pausar();
    }
    
    private void faseMovimentoNenufares() {
        System.out.println("\n💨 MOVIMENTO DOS NENÚFARES");
        System.out.println(jogo.getJardineirJunior().getNome() + " move um nenúfar.");
        System.out.println("(Movimento de 1 casa horizontal ou vertical)");
        
        System.out.println("\nPosição ORIGEM:");
        System.out.print("  Linha (0-4): ");
        int linhaOrig = lerInteiro(0, 4);
        System.out.print("  Coluna (0-4): ");
        int colunaOrig = lerInteiro(0, 4);
        
        System.out.println("\nDireção de DESTINO:");
        System.out.print("  Direção (F=frente, T=tras, E=esquerda, D=direita): ");
        char direcao = scanner.nextLine().toUpperCase().charAt(0);
        
        if (!jogo.executarMovimentoNenufar(linhaOrig, colunaOrig, direcao)) {
            System.out.println("❌ " + jogo.getMensagemEstado());
            System.out.println("Tente novamente...");
        } else {
            System.out.println("✓ Nenúfar movido!");
        }
        
        pausar();
    }
    
    private void faseSelecaoNenufarEscuro() {
        System.out.println("\n⚫ SELEÇÃO DO NENÚFAR ESCURO");
        System.out.println(jogo.getJardineirSenior().getNome() + " seleciona o próximo nenúfar escuro.");
        
        System.out.print("Linha (0-4): ");
        int linha = lerInteiro(0, 4);
        
        System.out.print("Coluna (0-4): ");
        int coluna = lerInteiro(0, 4);
        
        // Verificar se tem sapo
        if (jogo.getTabuleiro().temSapo(linha, coluna)) {
            System.out.println("⚠️  Há um sapo nesta posição!");
            moverSapoInterativo();
        }
        
        if (!jogo.selecionarNenufarEscuro(linha, coluna)) {
            System.out.println("❌ " + jogo.getMensagemEstado());
            System.out.println("Tente novamente...");
        } else {
            System.out.println("✓ Nenúfar escuro selecionado!");
        }
        
        pausar();
    }
    
    private void moverSapoInterativo() {
        System.out.println("\n🐸 Mover sapo para nenúfar vazio:");
        System.out.print("  Linha destino (0-4): ");
        int linhaDest = lerInteiro(0, 4);
        System.out.print("  Coluna destino (0-4): ");
        int colunaDest = lerInteiro(0, 4);
        
        // A origem do sapo precisa ser identificada - por simplicidade, 
        // você pode passar as coordenadas ou buscar automaticamente
        System.out.println("(Implementar lógica de movimento de sapo aqui)");
    }
    
    private void fimDeRodada() {
        System.out.println("\n🏁 FIM DE RODADA!");
        System.out.println(jogo.getMensagemEstado());
        
        System.out.print("\nPressione ENTER para iniciar nova rodada...");
        scanner.nextLine();
        
        jogo.iniciarNovaRodada();
    }
    
    private void mostrarVencedor() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎉 FIM DE JOGO! 🎉");
        System.out.println("=".repeat(60));
        
        if (jogo.getJogadorVermelho().venceu()) {
            System.out.println("\n👑 " + jogo.getJogadorVermelho().getNome() + 
                             " é o JARDINEIRO IMPERIAL! 👑");
        } else if (jogo.getJogadorAmarelo().venceu()) {
            System.out.println("\n👑 " + jogo.getJogadorAmarelo().getNome() + 
                             " é o JARDINEIRO IMPERIAL! 👑");
        }
        
        System.out.println("\nPLACAR FINAL:");
        System.out.println("  " + jogo.getJogadorVermelho().getNome() + ": " + 
                          jogo.getJogadorVermelho().getPontuacao() + " pontos");
        System.out.println("  " + jogo.getJogadorAmarelo().getNome() + ": " + 
                          jogo.getJogadorAmarelo().getPontuacao() + " pontos");
    }
    
    private int lerInteiro(int min, int max) {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine());
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.print("Valor deve estar entre " + min + " e " + max + ". Tente novamente: ");
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número: ");
            }
        }
    }
    
    private void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    // Método main para executar
    public static void main(String[] args) {
        TesteConsole teste = new TesteConsole();
        teste.iniciar();
    }
}