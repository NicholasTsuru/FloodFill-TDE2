import java.awt.image.BufferedImage;
import java.io.IOException;

public class FloodFill {
    public static int comPilha(BufferedImage imagem, int linha, int coluna,
                              int novaCor, Imagens registro) throws IOException {
        return preencher(imagem, linha, coluna, novaCor, new PilhaPosicoes(), registro);
    }

    public static int comFila(BufferedImage imagem, int linha, int coluna,
                             int novaCor, Imagens registro) throws IOException {
        return preencher(imagem, linha, coluna, novaCor, new FilaPosicoes(), registro);
    }

    private static int preencher(BufferedImage imagem, int linha, int coluna,
                                 int novaCor, EstruturaPosicoes estrutura,
                                 Imagens registro) throws IOException {
        if (linha < 0 || linha >= imagem.getHeight()
                || coluna < 0 || coluna >= imagem.getWidth()) {
            throw new IllegalArgumentException("Coordenada inicial fora da imagem.");
        }
        int corOriginal = imagem.getRGB(coluna, linha);
        registro.salvar(imagem, "original.png");
        if (novaCor == corOriginal) {
            registro.salvar(imagem, "final.png");
            return 0; // A mudança de cor precisa marcar os pixels visitados.
        }
        int pintados = 0;
        estrutura.adicionar(new Posicao(linha, coluna));
        while (!estrutura.estaVazia()) {
            Posicao atual = estrutura.retirar();
            int l = atual.linha;
            int c = atual.coluna;
            // Confira limites antes de acessar o pixel.
            if (l < 0 || l >= imagem.getHeight() || c < 0 || c >= imagem.getWidth()) {
                continue;
            }
            if (imagem.getRGB(c, l) != corOriginal) continue;
            imagem.setRGB(c, l, novaCor);
            pintados++;
            registro.registrarPasso(imagem, pintados);
            estrutura.adicionar(new Posicao(l - 1, c)); // cima
            estrutura.adicionar(new Posicao(l + 1, c)); // baixo
            estrutura.adicionar(new Posicao(l, c - 1)); // esquerda
            estrutura.adicionar(new Posicao(l, c + 1)); // direita
        }
        registro.salvar(imagem, "final.png");
        return pintados;
    }
}
