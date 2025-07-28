# Pré-Desafio Cryptopals - Processo Seletivo LabSEC

Este repositório contém a minha solução para o Pré-Desafio do processo seletivo da LabSEC, que consiste nos exercícios do primeiro set do Cryptopals.

**Autor:** Arthur de Farias Salmoria

## Estrutura do Projeto

O projeto foi desenvolvido em Java utilizando o IntelliJ IDEA e está estruturado como um projeto Maven.

- **`/src/main/java/org/example/`**: Contém todos os códigos em Java.
- **`/explicacoes/`**: Contém arquivos `.txt`, um para cada exercício, detalhando o raciocínio e o algoritmo utilizado.
- **`pom.xml`**: Arquivo de configuração do Maven, que inclui as dependências do projeto.
- **`CifraHex.txt` e `textoCifrado.txt`**: Arquivos de entrada utilizados por alguns dos exercícios.

## Dependências

O projeto utiliza a biblioteca **BouncyCastle** para algumas das operações criptográficas. A dependência já está configurada no arquivo `pom.xml`. O Maven fará o download automaticamente.

## Observações

Minha experiência prévia com os conceitos de criptografia foi fundamental para estruturar os algoritmos, embora o desenvolvimento em Java para algoritmos criptográficos com a biblioteca BouncyCastle tenha sido uma certa novidade e um ótimo aprendizado. Todo esse processo me deixa com cada vez mais vontade de ingressar no laboratório.