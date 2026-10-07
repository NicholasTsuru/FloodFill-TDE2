public class PilhaPosicoes implements EstruturaPosicoes {
    private NoPosicao topo;

    public void adicionar(Posicao posicao) {
        NoPosicao novo = new NoPosicao(posicao);
        novo.proximo = topo;
        topo = novo;
    }

    public Posicao retirar() {
        if (estaVazia()) throw new IllegalStateException("A pilha está vazia.");
        Posicao posicao = topo.posicao;
        topo = topo.proximo;
        return posicao; // Sai primeiro a última posição que entrou (LIFO).
    }

    public boolean estaVazia() {
        return topo == null;
    }
}
