package haruichiban.Model;

import haruichiban.Model.enums.CorJogador;
import haruichiban.Model.enums.FaseJogo;

public class JogoHaruIchiban {
    private Tabuleiro tabuleiro;
    private Jogador jogadorVermelho;
    private Jogador jogadorAmarelo;
    private Jogador jardineirJunior;
    private Jogador jardineirSenior;
    private FaseJogo faseAtual;
    private boolean jogoFinalizado;
    private String mensagemEstado;
    private int turnoAtual;

    private Jogador jogadorQueCoaxou; // Jogador que ganhou o "coaxar"
    private boolean saposRemovidos; // Para controlar se os sapos foram removidos temporariamente
    private int saposMovidos; // Contador de sapos movidos no empate

    public JogoHaruIchiban(String nomeVermelho, String nomeAmarelo) {
        this.tabuleiro = new Tabuleiro();
        this.jogadorVermelho = new Jogador(nomeVermelho, CorJogador.VERMELHO);
        this.jogadorAmarelo = new Jogador(nomeAmarelo, CorJogador.AMARELO);
        this.faseAtual = FaseJogo.SELECAO_FLOR;
        this.jogoFinalizado = false;
        this.turnoAtual = 1;
        this.mensagemEstado = "Ambos jogadores devem selecionar uma flor";

        tabuleiro.iniciarTabuleiro();
    }

    public boolean selecionarFlores(int indiceVermelho, int indiceAmarelo) {
        if (faseAtual != FaseJogo.SELECAO_FLOR)
            return false;

        boolean v = jogadorVermelho.selecionarFlor(indiceVermelho);
        boolean a = jogadorAmarelo.selecionarFlor(indiceAmarelo);

        if (!v || !a)
            return false;

        determinarJardineiros();

        if (jardineirJunior == null) {
            // EMPATE!
            mensagemEstado = "EMPATE! Quem coaxar primeiro move ambos os sapos!";
            floracaoAutomaticaEmpate();
            faseAtual = FaseJogo.EMPATE_COAXAR; // Nova fase
            saposMovidos = 0;
        } else {
            faseAtual = FaseJogo.FLORACAO_JUNIOR;
            mensagemEstado = jardineirJunior.getNome() + " é o Jardineiro Júnior.";
        }

        return true;
    }

    private void determinarJardineiros() {
        int valorVermelho = jogadorVermelho.getFlorSelecionada().getValor();
        int valorAmarelo = jogadorAmarelo.getFlorSelecionada().getValor();

        if (valorVermelho < valorAmarelo) {
            jardineirJunior = jogadorVermelho;
            jardineirSenior = jogadorAmarelo;
        } else if (valorAmarelo < valorVermelho) {
            jardineirJunior = jogadorAmarelo;
            jardineirSenior = jogadorVermelho;
        } else {
            jardineirJunior = null;
            jardineirSenior = null;
        }
    }

    private void floracaoAutomaticaEmpate() {
        Posicao sapoVermelho = encontrarSapo(CorJogador.VERMELHO);
        if (sapoVermelho != null) {
            tabuleiro.colocarFlor(sapoVermelho.getLinha(), sapoVermelho.getColuna(),
                    jogadorVermelho.getFlorSelecionada());
            jogadorVermelho.usarFlorSelecionada();
        }

        Posicao sapoAmarelo = encontrarSapo(CorJogador.AMARELO);
        if (sapoAmarelo != null) {
            tabuleiro.colocarFlor(sapoAmarelo.getLinha(), sapoAmarelo.getColuna(),
                    jogadorAmarelo.getFlorSelecionada());
            jogadorAmarelo.usarFlorSelecionada();
        }

        // Remover os sapos temporariamente após floração
        tabuleiro.removerSapos();
        saposRemovidos = true;
    }

    public boolean registrarCoaxar(CorJogador corJogador) {
        if (faseAtual != FaseJogo.EMPATE_COAXAR) {
            return false;
        }
        
        jogadorQueCoaxou = (corJogador == CorJogador.VERMELHO) ? 
                           jogadorVermelho : jogadorAmarelo;
        
        faseAtual = FaseJogo.MOVER_SAPOS_EMPATE;
        mensagemEstado = jogadorQueCoaxou.getNome() + 
                         " coaxou primeiro! Coloque o sapo VERMELHO.";
        return true;
    }

    public boolean moverSapoEmpate(int linha, int coluna, CorJogador corSapo) {
        if (faseAtual != FaseJogo.MOVER_SAPOS_EMPATE) {
            return false;
        }
        
        // Verificar se a posição é válida (nenúfar vazio)
        if (!tabuleiro.posicaoValida(linha, coluna) || 
            tabuleiro.getCelula(linha, coluna) != 3) {
            mensagemEstado = "Posição inválida! Escolha um nenúfar vazio.";
            return false;
        }
        
        // Colocar o sapo
        int valorSapo = (corSapo == CorJogador.VERMELHO) ? 5 : 6;
        tabuleiro.setCelula(linha, coluna, valorSapo);
        saposMovidos++;
        
        if (saposMovidos == 1) {
            // Primeiro sapo colocado, pedir o segundo
            mensagemEstado = jogadorQueCoaxou.getNome() + 
                             " agora coloque o sapo AMARELO.";
            return true;
        } else if (saposMovidos == 2) {
            // Ambos os sapos colocados, continuar o jogo
            saposRemovidos = false;
            saposMovidos = 0;
            jogadorQueCoaxou = null;
            
            // Escolher aleatoriamente quem será o júnior para movimento
            // ou permitir que o jogador que coaxou escolha
            jardineirJunior = jogadorQueCoaxou; 
            jardineirSenior = (jogadorQueCoaxou == jogadorVermelho) ? 
                             jogadorAmarelo : jogadorVermelho;
            
            faseAtual = FaseJogo.MOVIMENTO_NENUFARES;
            mensagemEstado = jardineirJunior.getNome() + " move os nenúfares.";
            return true;
        }
        
        return false;
    }


    private Posicao encontrarSapo(CorJogador cor) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tabuleiro.temSapo(i, j) &&
                        tabuleiro.getCorSapo(i, j) == cor) {
                    return new Posicao(i, j);
                }
            }
        }
        return null;
    }

    public boolean executarFloracaoJunior() {
        if (faseAtual != FaseJogo.FLORACAO_JUNIOR)
            return false;

        Posicao nenufarEscuro = tabuleiro.encontrarNenufarEscuro();
        if (nenufarEscuro == null)
            return false;

        if (tabuleiro.temSapo(nenufarEscuro.getLinha(), nenufarEscuro.getColuna())) {
            mensagemEstado = "Há um sapo no nenúfar escuro. Mova-o primeiro.";
            return false;
        }

        tabuleiro.colocarFlor(nenufarEscuro.getLinha(), nenufarEscuro.getColuna(),
                jardineirJunior.getFlorSelecionada());
        jardineirJunior.usarFlorSelecionada();

        faseAtual = FaseJogo.FLORACAO_SENIOR;
        mensagemEstado = jardineirSenior.getNome() + " escolhe onde florescer.";

        return true;
    }

    public boolean executarFloracaoSenior(int linha, int coluna) {
        if (faseAtual != FaseJogo.FLORACAO_SENIOR)
            return false;

        if (!tabuleiro.podeColocarFlor(linha, coluna)) {
            mensagemEstado = "Não pode colocar flor nesta posição!";
            return false;
        }

        if (tabuleiro.temSapo(linha, coluna)) {
            mensagemEstado = "Há um sapo aqui. Mova-o primeiro.";
            return false;
        }

        tabuleiro.colocarFlor(linha, coluna, jardineirSenior.getFlorSelecionada());
        jardineirSenior.usarFlorSelecionada();

        faseAtual = FaseJogo.MOVIMENTO_NENUFARES;
        mensagemEstado = jardineirJunior.getNome() + " move um nenúfar.";

        return true;
    }

    public boolean moverSapo(int linhaOrig, int colunaOrig, int linhaDest, int colunaDest) {
        return tabuleiro.moverSapo(linhaOrig, colunaOrig, linhaDest, colunaDest);
    }

    public boolean executarMovimentoNenufar(int linhaOrig, int colunaOrig, char direcao) {
        if (faseAtual != FaseJogo.MOVIMENTO_NENUFARES)
            return false;

        if (!tabuleiro.moverNenufar(linhaOrig, colunaOrig, direcao)) {
            mensagemEstado = "Movimento inválido!";
            return false;
        }

        ResultadoPadrao resultado = tabuleiro.verificarPadroes();

        if (resultado.isEncontrou()) {
            // Encontrou um padrão! Adicionar pontos e verificar vitória
            Jogador vencedor = (resultado.getCorVencedor() == CorJogador.VERMELHO)
                    ? jogadorVermelho
                    : jogadorAmarelo;

            vencedor.adicionarPontos(resultado.getPontuacao());

            mensagemEstado = vencedor.getNome() + " marcou " + resultado.getPontuacao() +
                    " ponto(s) com " + resultado.getTipo().getDescricao() + "!";

            finalizarRodada(vencedor);
            return true;
        }

        // Se não encontrou padrão, continua o jogo normalmente
        faseAtual = FaseJogo.SELECAO_NENUFAR_ESCURO;
        mensagemEstado = jardineirSenior.getNome() + " seleciona o próximo nenúfar escuro.";

        return true;
    }

    public boolean selecionarNenufarEscuro(int linha, int coluna) {
        if (faseAtual != FaseJogo.SELECAO_NENUFAR_ESCURO) return false;
    
        if (!tabuleiro.posicaoValida(linha, coluna)) {
            mensagemEstado = "Posição inválida.";
            return false;
        }
    
        int cel = tabuleiro.getCelula(linha, coluna);
    
        // Só pode escolher NENÚFAR CLARO VAZIO (3).
        // Nunca permitir sobre água (0), sapo (5/6) ou flor (7/8).
        if (cel != 3) {
            if (cel == 5 || cel == 6) {
                mensagemEstado = "Há um sapo aqui. Mova-o primeiro para um nenúfar vazio.";
            } else if (cel == 7 || cel == 8) {
                mensagemEstado = "Já existe uma flor aqui. Selecione um nenúfar claro vazio.";
            } else {
                mensagemEstado = "Selecione um nenúfar claro vazio.";
            }
            return false;
        }
    
        // Tudo certo: marca o novo escuro
        tabuleiro.virarTodosNenufaresParaClaro();
        tabuleiro.setCelula(linha, coluna, 4);
    
        // Se restarem 2 nenúfares não floridos, remove os sapos (como estava na sua lógica)
        if (tabuleiro.contarNenufaresNaoFlorescidos() <= 2) {
            tabuleiro.removerSapos();
            mensagemEstado = "Apenas 2 nenúfares restantes! Sapos removidos.";
        }
    
        // TODO: verificar padrões geométricos e, se houver, chamar finalizarRodada(jogadorVencedor)
    
        // Verifica fim de rodada por esgotar flores
        if (!jogadorVermelho.temFloresDisponiveis() && !jogadorAmarelo.temFloresDisponiveis()) {
            finalizarRodada(null); // Ninguém marcou pontos
            return true;
        }
    
        turnoAtual++;
        faseAtual = FaseJogo.SELECAO_FLOR;
        mensagemEstado = "Turno " + turnoAtual + ": Selecionem as flores!";
    
        return true;
    }

    private void finalizarRodada(Jogador vencedorRodada) {
        if (vencedorRodada != null) {
            // Jogador marcou pontos
            // Pontuação já foi adicionada antes de chamar este método

            if (vencedorRodada.venceu()) {
                jogoFinalizado = true;
                mensagemEstado = vencedorRodada.getNome() + " é o Jardineiro Imperial!";
                return;
            }
        }

        // Preparar nova rodada
        faseAtual = FaseJogo.FIM_RODADA;
        mensagemEstado = "Fim da rodada! Preparando nova rodada...";
    }

    public void iniciarNovaRodada() {
        // Resetar jogadores
        jogadorVermelho.resetarParaNovaRodada();
        jogadorAmarelo.resetarParaNovaRodada();

        // Resetar tabuleiro
        tabuleiro.limparFlores();
        tabuleiro.virarTodosNenufaresParaClaro();

        // Recolocar sapos nas posições iniciais
        // Encontrar nenúfares com ovos (isso depende da sua lógica visual)
        // Por simplicidade, vou colocar nas posições originais
        tabuleiro.setCelula(1, 2, 5); // sapo vermelho
        tabuleiro.setCelula(3, 3, 6); // sapo amarelo
        tabuleiro.setCelula(1, 3, 4); // nenúfar escuro inicial

        turnoAtual = 1;
        jardineirJunior = null;
        jardineirSenior = null;
        faseAtual = FaseJogo.SELECAO_FLOR;
        mensagemEstado = "Nova rodada! Selecionem as flores!";
    }

    // Getters
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public Jogador getJogadorVermelho() {
        return jogadorVermelho;
    }

    public Jogador getJogadorAmarelo() {
        return jogadorAmarelo;
    }

    public Jogador getJardineirJunior() {
        return jardineirJunior;
    }

    public Jogador getJardineirSenior() {
        return jardineirSenior;
    }

    public FaseJogo getFaseAtual() {
        return faseAtual;
    }

    public boolean isJogoFinalizado() {
        return jogoFinalizado;
    }

    public String getMensagemEstado() {
        return mensagemEstado;
    }

    public int getTurnoAtual() {
        return turnoAtual;
    }
    public Jogador getJogadorQueCoaxou() {
        return jogadorQueCoaxou;
    }
}