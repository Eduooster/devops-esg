# 🌱 Projeto - Cidades ESG Inteligentes

Sistema desenvolvido com foco em gestão de pontos de coleta e integração com boas práticas de DevOps, incluindo CI/CD, containerização e automação de deploy.

---

## 🚀 Como executar localmente com Docker

### 1. Clonar o repositório
```bash
git clone <https://github.com/Eduooster/devops-esg>
cd esg =
```


## Subir o ambiente com Docker Compose

```bash
docker-compose up --build
```

## Acessar a aplicação

```http
http://localhost:8080
```

##  O que será iniciado:
- Aplicação Spring Boot
- Banco de dados PostgreSQL (container)
- Rede interna Docker entre os serviços
- Volume para persistência do banco

## 🔁 Pipeline CI/CD

O projeto utiliza GitHub Actions para automação do fluxo de integração e entrega contínua.

## ⚙️ Ferramentas utilizadas:
- GitHub Actions
- Maven
- Spring Boot
- H2 (ambiente de testes)

## 📌 Etapas do pipeline:
- Checkout do código
- Setup do Java 17
- Cache do Maven
- Build do projeto
- Execução dos testes automatizados
- Deploy para staging (branch develop)
- Deploy para produção (branch main)

## 🧪 Ambiente de testes:
- Utiliza banco em memória (H2)

- Profile test ativado no pipeline

## 📷 Pipeline executando com sucesso

<img width="616" height="663" alt="image" src="https://github.com/user-attachments/assets/dbe0a056-c18a-448b-9929-94fa730022c2" />

## 🐳 Containerização

O projeto utiliza Docker para garantir portabilidade e padronização do ambiente.


# Build stage

```dockerfile
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 📦 Estratégia adotada:
- Multi-stage build (Maven + JDK runtime)
- Imagem leve para execução
- Separação entre build e runtime
- Exposição da porta 8080

## 📷 Containers rodando


<img width="1605" height="277" alt="image" src="https://github.com/user-attachments/assets/a83f62c5-a5af-4833-a28a-2855150c0a84" />

## ☁️ Orquestração com Docker Compose

O sistema é composto por:

- Serviço da aplicação (Spring Boot)
- Serviço de banco de dados (PostgreSQL)
- Rede interna entre containers
- Volume para persistência de dados

## 📷 Docker Compose em execução
<img width="760" height="147" alt="image" src="https://github.com/user-attachments/assets/2239c0f5-9e35-429a-b70e-e780e5ebd1f6" />


## 🧪 Ambiente de Testes
- Utiliza profile test
- Banco em memória (H2)
- Execução automática no CI/CD

## 📷 Testes passando no pipeline

[inserir imagem - aba de testes do GitHub Actions]


🔧 Tecnologias utilizadas
- Java 17
- Spring Boot
- Maven
- Docker
- Docker Compose
- PostgreSQL
- 2 Database
- gitHub Actions
- JUnit
- Oracle

## 📊 Arquitetura do sistema

<img width="953" height="1142" alt="mermaid-diagram" src="https://github.com/user-attachments/assets/41a1195a-594a-4e30-9e87-554b8d0bccbc" />



## 🚀 Resultado final

### O projeto demonstra uma arquitetura completa de DevOps com:

- Integração contínua
- Entrega contínua
- ontainerização
- Orquestração de serviços
- Ambientes separados (dev/test/prod)

