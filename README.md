# Flood Fill com pilha e fila próprias

Integrantes: **Nicholas**.

Programa em Java, sem bibliotecas externas. Usa apenas as classes padrão do Java para
abrir e salvar imagens. A pilha e a fila são implementadas aqui com nós encadeados.
O preenchimento é iterativo, sem recursão, e considera cima, baixo, esquerda e direita.

## Compilar e executar

Instale **JDK 17 ou superior**. Verifique `java -version` e `javac -version`.
Abra o terminal **na pasta deste projeto**, onde estão `src` e este README.

```sh
mkdir bin
javac -encoding UTF-8 --release 17 -d bin src/*.java
java -cp bin Main
```

Se `bin` já existe, pule `mkdir bin`. Os mesmos comandos servem para os terminais
usuais do Windows, Linux e macOS. Este projeto tem seu próprio `Main` e sua própria `bin`.
Não compile junto com o projeto Morse.

## Demonstração simples pelo menu

1. Digite `3` e informe `exemplos/regioes.png`.
2. Digite `4`, informe X = `1` e Y = `1`. X é a coluna; Y é a linha. Ambos começam em zero.
3. Digite `1` para usar a pilha.
4. Digite `FF0000` para vermelho e `1` para salvar uma imagem por pixel pintado.
5. Confira a mensagem `Pixels pintados: 63` e a pasta indicada em `saidas`.
6. Digite `2`, informe a mesma cor e intervalo para usar a fila na mesma imagem original.
7. Compare as etapas: a ordem muda, mas os dois `final.png` são iguais.
8. Digite `0` para encerrar.

| Opção | Ação |
|---|---|
| 1 | Executar com pilha |
| 2 | Executar com fila |
| 3 | Escolher imagem |
| 4 | Escolher coordenada de início |
| 0 | Encerrar |

Escolha a imagem e a coordenada antes de executar. Uma imagem nova exige uma nova
coordenada. Cada execução reabre o arquivo original e cria outra pasta em `saidas`,
sem sobrescrever resultados anteriores. O arquivo de entrada não é alterado.

## Cores, imagens e etapas

Digite a cor com **seis dígitos hexadecimais, sem `#`**:

| Cor | Código |
|---|---|
| Vermelho | `FF0000` |
| Verde | `00FF00` |
| Azul | `0000FF` |
| Preto | `000000` |
| Branco | `FFFFFF` |

Entradas PNG, BMP e JPEG podem ser lidas pelo Java; prefira PNG ou BMP com cores sólidas.
JPEG pode ter pequenas diferenças de cor devido à compressão. Pixels são comparados
pela cor exata, incluindo transparência; a nova cor digitada é opaca.

**Todas as saídas são PNG** durante a execução:

- `original.png`: imagem antes da alteração.
- `passo_0001.png`, `passo_0002.png` etc.: etapas numeradas.
- `final.png`: resultado do preenchimento.

O primeiro pixel sempre é registrado. Depois, o intervalo escolhido define a frequência
das imagens. Para uma imagem grande, use por exemplo `100` para reduzir arquivos e tempo.
Se a nova cor já for a original, o programa retorna zero pixels e salva apenas original
e final, sem etapas de mudança. Abra os PNGs no visualizador de imagens para acompanhar.

`exemplos/regioes.png` tem 20 × 16 pixels e paredes pretas; amplie no visualizador.
`exemplos/diagonal.png` tem três pixels brancos ligados apenas pela diagonal: em
X = 0, Y = 0, só um pixel deve ser pintado.

## Como o código funciona

- `Posicao`: guarda linha e coluna.
- `NoPosicao`: guarda uma posição e uma referência para o próximo nó.
- `PilhaPosicoes`: insere e retira no topo; o último que entra sai primeiro.
- `FilaPosicoes`: insere no fim e retira no início; o primeiro que entra sai primeiro.
- `EstruturaPosicoes`: define as três operações comuns: adicionar, retirar e verificar vazio.
- `FloodFill`: usa essas operações em um laço; tem entradas `comPilha` e `comFila`.
- `Imagens`: lê imagens, cria uma cópia com cores exatas e salva os PNGs.
- `Main`: organiza o menu e valida as entradas.



As imagens de exemplo já acompanham o projeto. Se quiser recriá-las:

```sh
java -cp bin GerarExemplos
```

Esse comando sobrescreve somente `exemplos/regioes.png` e `exemplos/diagonal.png`.

