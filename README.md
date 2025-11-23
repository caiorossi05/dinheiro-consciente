Dinheiro Consciente

Aplicação Java Web (Maven) para organização financeira pessoal, com:

Cadastro e login de usuários

Registro de receitas e despesas

Listagem de transações

Dashboard com resumo e insight financeiro semanal

Tecnologias utilizadas:

- Java 17
- Maven
- Servlet/JSP (Java EE)
- Tomcat 7 (via tomcat7-maven-plugin)
- MySQL 8
- JDBC
- BCrypt para senha
- HTML/CSS/JavaScript
- Chart.js para gráficos

Estrutura básica do projeto:

src/main/java/br/com/dinheiroconsciente

controller/ – Servlets (UsuarioController, TransacaoController)

service/ – Regras de negócio (UsuarioService, TransacaoService)

repository/ – Acesso ao banco via JDBC

model/ – Entidades (Usuario, Transacao, TipoTransacao)

util/ConnectionFactory.java – criação da conexão JDBC

src/main/webapp

login.jsp

dashboard.html

registrar_transacao.html

transacao_listagem.html

css/style.css

WEB-INF/web.xml

Pré-requisitos

Antes de rodar o projeto, é preciso ter instalado:

JDK 17

Maven

MySQL Server

1. Criar o banco de dados

Conecte no MySQL (via DBeaver ou CLI) e execute:

CREATE DATABASE IF NOT EXISTS dinheiro_consciente
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE dinheiro_consciente;

-- Tabela de usuários
CREATE TABLE IF NOT EXISTS usuario (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome    VARCHAR(100) NOT NULL,
    senha   VARCHAR(255) NOT NULL
);

-- Tabela de transações
CREATE TABLE IF NOT EXISTS transacao (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT      NOT NULL,
    tipo       VARCHAR(20) NOT NULL,          -- RECEITA ou DESPESA
    categoria  VARCHAR(100) NOT NULL,         -- ex: SALARIO, ALIMENTACAO, etc.
    descricao  VARCHAR(255),
    valor      DECIMAL(10,2) NOT NULL,
    data       DATE         NOT NULL,
    CONSTRAINT fk_transacao_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

2. Configurar a conexão com o MySQL

Edite a classe ConnectionFactory:

src/main/java/br/com/dinheiroconsciente/util/ConnectionFactory.java

(ajustando usuário e senha):

Se no MySQL o usuário não tiver senha, deixe PASSWORD = "".

3. Como rodar o projeto

No diretório raiz do projeto (onde está o pom.xml):

Fazer o build:

mvn clean install

Subir o Tomcat embutido via plugin:

mvn tomcat7:run

O Maven/Tomcat vai subir o servidor na porta 8080.
Se tudo estiver correto, aparecerá algo como:

INFO: Starting ProtocolHandler ["http-bio-8080"]

4. Acessar a aplicação

Abra o navegador e acesse:

Tela de login:
(http://localhost:8080/login.html)
