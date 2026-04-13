# Sistema de Locação de Veículos - Projeto Unicamp (FT)

Este projeto foi desenvolvido como parte das atividades da disciplina de **Engenharia de Software**. O objetivo principal foi realizar a transição da modelagem UML para a implementação de código funcional em Java, simulando um sistema real de aluguel de frotas.

## 🚀 Funcionalidades

O sistema permite gerenciar o ciclo completo de uma locação:
- **Gestão de Frota:** Cadastro e manutenção de Marcas, Modelos e Automóveis (Placa, RENAVAM, Chassi, etc.).
- **Gestão de Clientes:** Cadastro completo com validação de dados básicos.
- **Operações de Locação:** - Registro de retirada de veículos.
    - Registro de devolução com atualização de quilometragem.
    - Cálculo automático do valor total baseado em diárias (mínimo de 1 diária).
- **Status em Tempo Real:** Controle de veículos disponíveis ou locados.

## 🛠️ Tecnologias e Diferenciais

Este projeto atingiu o nível **Avançado** proposto na atividade, implementando:

1.  **Interface Gráfica (GUI):** Desenvolvida em Java Swing, oferecendo uma experiência de usuário intuitiva com abas organizadas e tabelas interativas.
2.  **Persistência de Dados:** Implementação de persistência via arquivos CSV, garantindo que os dados de clientes, veículos e locações sejam preservados após o encerramento da aplicação.
3.  **Lógica de Negócio Robusta:** Uso de APIs modernas do Java (como `java.time`) para cálculos precisos de períodos de locação.

## ⚠️ Observações sobre o Desenvolvimento

Embora o sistema seja funcional e cumpra todos os requisitos de negócio e os extras avançados:
* **Arquitetura de Software:** O foco da atividade foi a tradução do modelo para o código e a entrega das funcionalidades. Portanto, a arquitetura interna (padrões de projeto e desacoplamento) ainda carece de uma elaboração mais profunda e refatoração para seguir padrões como MVC ou Clean Architecture de forma rigorosa.
* **Próximos Passos:** Em seguida, o projeto passará por uma refatoração estrutural com o objetivo de implementar uma arquitetura mais rigorosa. Pretende-se migrar o código para o padrão MVC (Model-View-Controller) ou Clean Architecture, visando o desacoplamento total entre a interface gráfica (Swing), a lógica de persistência (CSV) e as regras de negócio. Essa evolução permitirá uma melhor testabilidade e escalabilidade do sistema.

## 👥 Equipe
* André Luiz Clemente de Oliveira
* Fellipe Duarte Santos
* Heitor Roberto Mesquita De Souza
* Arthur Luiz Lopes Araujo
* Nicholas Cardoso Alencar
* Felipe Balzani Inserti
* Guilherme Piques
* Caio De Jesus Lima
* Lorena Testa Avelar
* Arthur Besseler Baldini
* Maria Luiza Gomes Tavares
