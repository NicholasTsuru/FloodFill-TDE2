import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class TestesFloodFill {
    private static int verificacoes = 0;
    private static final int BRANCO = Color.WHITE.getRGB();
    private static final int PRETO = Color.BLACK.getRGB();
    private static final int VERMELHO = Color.RED.getRGB();

    private static void conferir(boolean condicao, String mensagem) {
        if (!condicao) throw new AssertionError(mensagem);
        verificacoes++;
    }

    private static BufferedImage regiao() {
        BufferedImage imagem = new BufferedImage(7, 5, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 7; x++) {
                boolean parede = x == 0 || x == 6 || y == 0 || y == 4 || x == 3;
                imagem.setRGB(x, y, parede ? PRETO : BRANCO);
            }
        }
        return imagem;
    }

    private static boolean iguais(BufferedImage a, BufferedImage b) {
        if (a.getWidth() != b.getWidth() || a.getHeight() != b.getHeight()) return false;
        for (int y = 0; y < a.getHeight(); y++) {
            for (int x = 0; x < a.getWidth(); x++) {
                if (a.getRGB(x, y) != b.getRGB(x, y)) return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        PilhaPosicoes pilha = new PilhaPosicoes();
        FilaPosicoes fila = new FilaPosicoes();
        for (int i = 1; i <= 3; i++) {
            pilha.adicionar(new Posicao(i, 0));
            fila.adicionar(new Posicao(i, 0));
        }
        for (int i = 1; i <= 3; i++) {
            conferir(pilha.retirar().linha == 4 - i, "A pilha deve retirar em ordem inversa.");
            conferir(fila.retirar().linha == i, "A fila deve retirar em ordem de chegada.");
        }
        conferir(pilha.estaVazia() && fila.estaVazia(), "As estruturas deveriam estar vazias.");
        fila.adicionar(new Posicao(9, 9));
        conferir(fila.retirar().linha == 9 && fila.estaVazia(), "A fila deve poder ser reutilizada.");
        try {
            pilha.retirar();
            throw new AssertionError("Retirada de pilha vazia deveria falhar.");
        } catch (IllegalStateException e) { verificacoes++; }
        try {
            fila.retirar();
            throw new AssertionError("Retirada de fila vazia deveria falhar.");
        } catch (IllegalStateException e) { verificacoes++; }

        Path pasta = Files.createTempDirectory("testes-flood-");
        BufferedImage a = regiao();
        BufferedImage b = regiao();
        Path pastaPilha = pasta.resolve("pilha");
        Path pastaFila = pasta.resolve("fila");
        conferir(FloodFill.comPilha(a, 1, 1, VERMELHO, new Imagens(pastaPilha, 1)) == 6,
                 "Pilha deve pintar seis pixels.");
        conferir(FloodFill.comFila(b, 1, 1, VERMELHO, new Imagens(pastaFila, 1)) == 6,
                 "Fila deve pintar seis pixels.");
        conferir(iguais(a, b), "Pilha e fila devem terminar com a mesma imagem.");
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 7; x++) {
                int esperado = x >= 1 && x <= 2 && y >= 1 && y <= 3 ? VERMELHO : regiao().getRGB(x, y);
                conferir(a.getRGB(x, y) == esperado, "Somente a região conectada pode mudar.");
            }
        }
        conferir(iguais(ImageIO.read(pastaPilha.resolve("original.png").toFile()), regiao()),
                 "A original salva deve preservar a imagem de entrada.");
        conferir(iguais(ImageIO.read(pastaPilha.resolve("final.png").toFile()), a),
                 "A final salva deve corresponder ao resultado.");
        conferir(!iguais(ImageIO.read(pastaPilha.resolve("passo_0002.png").toFile()),
                        ImageIO.read(pastaFila.resolve("passo_0002.png").toFile())),
                 "As etapas devem mostrar ordens diferentes de preenchimento.");
        conferir(Files.exists(pastaFila.resolve("passo_0006.png")), "As etapas precisam ser numeradas.");

        BufferedImage diagonal = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        diagonal.setRGB(0, 0, BRANCO); diagonal.setRGB(1, 1, BRANCO);
        diagonal.setRGB(1, 0, PRETO); diagonal.setRGB(0, 1, PRETO);
        conferir(FloodFill.comFila(diagonal, 0, 0, VERMELHO, new Imagens(pasta.resolve("diagonal"), 1)) == 1,
                 "Uma ligação só pela diagonal não deve ser preenchida.");
        conferir(diagonal.getRGB(1, 1) == BRANCO, "A diagonal precisa continuar branca.");
        BufferedImage igual = regiao();
        Path semMudanca = pasta.resolve("mesma-cor");
        conferir(FloodFill.comPilha(igual, 1, 1, BRANCO, new Imagens(semMudanca, 1)) == 0,
                 "Cor igual deve encerrar sem entrar no laço.");
        conferir(iguais(igual, regiao()) && !Files.exists(semMudanca.resolve("passo_0001.png")),
                 "Cor igual não deve criar etapas de alteração.");
        for (int[] posicao : new int[][]{{-1, 0}, {0, -1}, {5, 0}, {0, 7}}) {
            try {
                FloodFill.comFila(regiao(), posicao[0], posicao[1], VERMELHO,
                                  new Imagens(pasta.resolve("invalido"), 1));
                throw new AssertionError("Coordenada fora da imagem deveria falhar.");
            } catch (IllegalArgumentException e) { verificacoes++; }
        }
        try {
            new Imagens(pasta.resolve("intervalo"), 0);
            throw new AssertionError("Intervalo zero deveria falhar.");
        } catch (IllegalArgumentException e) { verificacoes++; }
        try {
            Imagens.abrir(pasta.resolve("inexistente.png").toString());
            throw new AssertionError("Arquivo inexistente deveria falhar.");
        } catch (IOException e) { verificacoes++; }
        Path invalido = pasta.resolve("invalido.txt");
        Files.writeString(invalido, "isto não é uma imagem");
        try {
            Imagens.abrir(invalido.toString());
            throw new AssertionError("Arquivo inválido deveria falhar.");
        } catch (IOException e) { verificacoes++; }
        // Paleta: teste de uma cor que não pertence à paleta da imagem de entrada.
        BufferedImage paleta = new BufferedImage(2, 2, BufferedImage.TYPE_BYTE_BINARY);
        Path arquivoPaleta = pasta.resolve("paleta.png");
        ImageIO.write(paleta, "png", arquivoPaleta.toFile());
        BufferedImage aberta = Imagens.abrir(arquivoPaleta.toString());
        conferir(FloodFill.comPilha(aberta, 0, 0, VERMELHO, new Imagens(pasta.resolve("paleta"), 1)) == 4,
                 "A cor nova deve ser guardada mesmo se a entrada tinha paleta.");
        conferir(aberta.getRGB(0, 0) == VERMELHO, "A cor escolhida precisa ser exata.");

        BufferedImage grande = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
        conferir(FloodFill.comPilha(grande, 0, 0, VERMELHO, new Imagens(pasta.resolve("grande"), 1000)) == 3600,
                 "O laço deve preencher imagens maiores sem recursão.");
        conferir(Files.exists(pasta.resolve("grande/passo_0004.png")),
                 "Intervalo maior deve salvar primeiro pixel e quadros a cada mil pixels.");
        System.out.println("Flood Fill: " + verificacoes + " verificações passaram.");
    }
}
