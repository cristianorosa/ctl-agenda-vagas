# ctl-agenda-vagas
Controle de Agendamentos de Vagas

Sistema exemplo desenvolvido com JSF, Prime Faces 14 e Spring Boot 3.4.0.

Arquitetur:
xhtml -> Controller -> Service -> ServiceImpl -> Repository -> Entity

- O xhtml é a camada de interfcae do usuário que recebe as interações.
- Quando o sistema for iniciado, a primeira camada que será sera responável por receber os eventos das interações do usuário será o nosso controller. 
- A camada do service será uma interface em que as classes que o assinarem devem ser responsáveis por implementar.
- O serviceImpl é a implamentação da interface service e é aqui que ficam todas as regras de negocio do sistema.
- O reposotory será a camada que interage com a base de dados.
- Entity é a represntação dos dados que serão gerenciados.

Siga os passos para executar:
1. Para iniciá-lo, faça checkout do projeto em pasta local:
  - git clone https://github.com/cristianorosa/ctl-agenda-vagas
2. Empacote a versão com maven:
  - mvn package
3. Rode o sistema com o camondo:
  - java -jar -Dserver.port=9494 target/ctl-agenda-vagas-0.0.1-SNAPSHOT.jar
4. Acesse no browser:
  - http://localhost:9494/

Requisitos:
Java 17, Maven, Git

