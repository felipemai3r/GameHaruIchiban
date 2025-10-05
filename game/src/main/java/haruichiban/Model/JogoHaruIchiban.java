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
            mensagemEstado = "EMPATE! Floração automática nos sapos.";
            floracaoAutomaticaEmpate();
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

        faseAtual = FaseJogo.MOVIMENTO_NENUFARES;
        mensagemEstado = "Quem 'croou' primeiro move os nenúfares.";
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

        if (!tabuleiro.moverNenufar(linhaOrig, colunaOrig,direcao)) {
            mensagemEstado = "Movimento inválido!";
            return false;
        }

        faseAtual = FaseJogo.SELECAO_NENUFAR_ESCURO;
        mensagemEstado = jardineirSenior.getNome() + " seleciona o próximo nenúfar escuro.";

        return true;
    }

    public boolean selecionarNenufarEscuro(int linha, int coluna) {
        if (faseAtual != FaseJogo.SELECAO_NENUFAR_ESCURO)
            return false;

        if (!tabuleiro.podeColocarFlor(linha, coluna)) {
            mensagemEstado = "Não pode selecionar esta posição!";
            return false;
        }

        tabuleiro.virarTodosNenufaresParaClaro();
        tabuleiro.setCelula(linha, coluna, 4);

        if (tabuleiro.contarNenufaresNaoFlorescidos() <= 2) {
            tabuleiro.removerSapos();
            mensagemEstado = "Apenas 2 nenúfares restantes! Sapos removidos.";
        }

        // TODO: Verificar padrões geométricos aqui
        // Se formar padrão, finalizarRodada()

        // Verificar se rodada acabou (8 turnos = 8 flores por jogador)
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
}