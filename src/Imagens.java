import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class Imagens {
    private final Path pasta;
    private final int intervalo;
    private int numeroPasso = 0;

    public Imagens(Path pasta, int intervalo) throws IOException {
        if (intervalo < 1) {
            throw new IllegalArgumentException("O intervalo deve ser maior que zero.");
        }
        this.pasta = pasta;
        this.intervalo = intervalo;
        Files.createDirectories(pasta);
    }

    public static BufferedImage abrir(String caminho) throws IOException {
        Path arquivo = Path.of(caminho);
        if (!Files.isRegularFile(arquivo) || !Files.isReadable(arquivo)) {
            throw new IOException("Arquivo inexistente ou sem permissão de leitura.");
        }
        BufferedImage imagem = ImageIO.read(arquivo.toFile());
        if (imagem == null) throw new IOException("Formato de imagem não reconhecido.");
        // Uma imagem com paleta pode arredondar a nova cor. ARGB guarda a cor exata.
        BufferedImage copia = new BufferedImage(imagem.getWidth(), imagem.getHeight(),
                                                BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < imagem.getHeight(); y++) {
            for (int x = 0; x < imagem.getWidth(); x++) {
                copia.setRGB(x, y, imagem.getRGB(x, y));
            }
        }
        return copia;
    }

    public void salvar(BufferedImage imagem, String nome) throws IOException {
        // Todas as saídas são PNG, mesmo que a entrada seja BMP ou JPEG.
        if (!ImageIO.write(imagem, "png", pasta.resolve(nome).toFile())) {
            throw new IOException("Não foi possível salvar a imagem PNG.");
        }
    }

    public void registrarPasso(BufferedImage imagem, int pintados) throws IOException {
        // Salva também o primeiro pixel, para haver uma etapa em regiões pequenas.
        if (pintados == 1 || pintados % intervalo == 0) {
            numeroPasso++;
            salvar(imagem, String.format("passo_%04d.png", numeroPasso));
        }
    }
}
