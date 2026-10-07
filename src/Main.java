import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        String caminho = null;
        int linha = -1;
        int coluna = -1;
        while (true) {
            System.out.println("\nFLOOD FILL");
            System.out.println("Imagem: " + (caminho == null ? "não escolhida" : caminho));
            System.out.println("1 - Executar com pilha");
            System.out.println("2 - Executar com fila");
            System.out.println("3 - Escolher imagem");
            System.out.println("4 - Escolher coordenada de início");
            System.out.println("0 - Encerrar");
            if (!entrada.hasNextLine()) return;
            String opcao = entrada.nextLine().trim();
            if (opcao.equals("0")) return;
            try {
                switch (opcao) {
                    case "3": {
                        System.out.print("Caminho da imagem (ex.: exemplos/regioes.png): ");
                        if (!entrada.hasNextLine()) return;
                        String novoCaminho = entrada.nextLine();
                        BufferedImage imagem = Imagens.abrir(novoCaminho);
                        caminho = novoCaminho;
                        linha = -1;
                        coluna = -1;
                        System.out.println("Imagem aberta: " + imagem.getWidth() + " x " + imagem.getHeight());
                        System.out.println("Escolha novamente a coordenada pela opção 4.");
                        break;
                    }
                    case "4": {
                        if (caminho == null) {
                            System.out.println("Escolha uma imagem primeiro (opção 3).");
                            break;
                        }
                        BufferedImage imagem = Imagens.abrir(caminho);
                        System.out.print("X (coluna, de 0 a " + (imagem.getWidth() - 1) + "): ");
                        if (!entrada.hasNextLine()) return;
                        int x = Integer.parseInt(entrada.nextLine().trim());
                        System.out.print("Y (linha, de 0 a " + (imagem.getHeight() - 1) + "): ");
                        if (!entrada.hasNextLine()) return;
                        int y = Integer.parseInt(entrada.nextLine().trim());
                        if (x < 0 || x >= imagem.getWidth() || y < 0 || y >= imagem.getHeight()) {
                            throw new IllegalArgumentException("Coordenada fora da imagem.");
                        }
                        coluna = x;
                        linha = y;
                        System.out.println("Coordenada escolhida: X=" + coluna + ", Y=" + linha);
                        break;
                    }
                    case "1":
                    case "2": {
                        if (caminho == null || linha < 0 || coluna < 0) {
                            System.out.println("Escolha a imagem (3) e a coordenada (4) antes de executar.");
                            break;
                        }
                        System.out.print("Nova cor em hexadecimal, ex.: FF0000 para vermelho: ");
                        if (!entrada.hasNextLine()) return;
                        String cor = entrada.nextLine().trim();
                        if (!cor.matches("[0-9a-fA-F]{6}")) {
                            throw new IllegalArgumentException("A cor precisa ter seis dígitos hexadecimais, sem #.");
                        }
                        int novaCor = new Color(Integer.parseInt(cor, 16)).getRGB();
                        System.out.print("Salvar a cada quantos pixels? (1 mostra todos): ");
                        if (!entrada.hasNextLine()) return;
                        int intervalo = Integer.parseInt(entrada.nextLine().trim());
                        if (intervalo < 1) throw new IllegalArgumentException("Digite um intervalo maior que zero.");
                        // Reabre a original: pilha e fila podem ser comparadas no mesmo menu.
                        BufferedImage imagem = Imagens.abrir(caminho);
                        String metodo = opcao.equals("1") ? "pilha" : "fila";
                        Files.createDirectories(Path.of("saidas"));
                        Path pasta = Files.createTempDirectory(Path.of("saidas"), metodo + "_");
                        Imagens registro = new Imagens(pasta, intervalo);
                        int pintados;
                        if (opcao.equals("1")) {
                            pintados = FloodFill.comPilha(imagem, linha, coluna, novaCor, registro);
                        } else {
                            pintados = FloodFill.comFila(imagem, linha, coluna, novaCor, registro);
                        }
                        System.out.println("Pixels pintados: " + pintados);
                        if (pintados == 0) System.out.println("A nova cor já era a cor inicial; nada foi alterado.");
                        System.out.println("Imagens salvas em: " + pasta);
                        break;
                    }
                    default:
                        System.out.println("Opção inválida. Digite um número do menu.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Erro ao abrir ou salvar imagem: " + e.getMessage());
            }
        }
    }
}
