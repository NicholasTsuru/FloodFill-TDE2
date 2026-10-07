import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class GerarExemplos {
    public static void main(String[] args) throws Exception {
        Files.createDirectories(Path.of("exemplos"));
        BufferedImage imagem = new BufferedImage(20, 16, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 20; x++) {
                int cor = Color.WHITE.getRGB();
                if (x == 0 || x == 19 || y == 0 || y == 15 || x == 10 || (y == 8 && x < 10)) {
                    cor = Color.BLACK.getRGB();
                } else if (x >= 13 && x <= 16 && y >= 3 && y <= 6) {
                    cor = Color.BLUE.getRGB();
                }
                imagem.setRGB(x, y, cor);
            }
        }
        ImageIO.write(imagem, "png", Path.of("exemplos/regioes.png").toFile());
        BufferedImage diagonal = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                diagonal.setRGB(x, y, x == y ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
            }
        }
        ImageIO.write(diagonal, "png", Path.of("exemplos/diagonal.png").toFile());
        System.out.println("Imagens de exemplo geradas.");
    }
}
