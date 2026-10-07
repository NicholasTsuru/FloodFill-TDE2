public class FilaPosicoes implements EstruturaPosicoes {
    private NoPosicao inicio;
    private NoPosicao fim;

    public void adicionar(Posicao posicao) {
        NoPosicao novo = new NoPosicao(posicao);
        if (estaVazia()) {
            inicio = novo;
        } else {
            fim.proximo = novo;
        }
        fim = novo;
    }

    public Posicao retirar() {
        if (estaVazia()) throw new IllegalStateException("A fila está vazia.");
        Posicao posicao = inicio.posicao;
        inicio = inicio.proximo;
        if (inicio == null) fim = null;
        return posicao; // Sai primeiro a primeira posição que entrou (FIFO).
    }

    public boolean estaVazia() {
        return inicio == null;
    }
}
