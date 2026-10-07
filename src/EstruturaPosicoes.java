// A mesma operação pode guardar posições em pilha ou em fila.
public interface EstruturaPosicoes {
    void adicionar(Posicao posicao);
    Posicao retirar();
    boolean estaVazia();
}
