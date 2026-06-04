<p align="center">
  <img src="app/Icon/AraraFinal.png" alt="YourLife Logo" width="140" />
</p>

<h1 align="center">YourLife</h1>

<p align="center">
  Rede social nativa para Android, desenvolvida em Kotlin com arquitetura MVVM e integrada a uma API REST hospedada na Vercel.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform" />
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Language" />
  <img src="https://img.shields.io/badge/Min%20SDK-24-informational?style=for-the-badge" alt="Min SDK" />
  <img src="https://img.shields.io/badge/Target%20SDK-34-informational?style=for-the-badge" alt="Target SDK" />
  <img src="https://img.shields.io/badge/Architecture-MVVM-blueviolet?style=for-the-badge" alt="Architecture" />
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License" />
</p>

---

## Indice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Stack Tecnologica](#stack-tecnologica)
- [Estrutura de Diretorios](#estrutura-de-diretorios)
- [Pre-requisitos](#pre-requisitos)
- [Instalacao e Execucao](#instalacao-e-execucao)
- [Endpoints da API](#endpoints-da-api)
- [Autor](#autor)

---

## Sobre o Projeto

**YourLife** e uma rede social mobile-first para Android, onde usuarios podem se conectar, compartilhar postagens, trocar mensagens privadas, gerenciar amizades e receber conselhos. O aplicativo consome uma API REST hospedada na Vercel e segue o padrao de arquitetura MVVM (Model-View-ViewModel), garantindo separacao de responsabilidades, testabilidade e manutencao facilitada.

---

## Funcionalidades

| Modulo | Descricao |
| :--- | :--- |
| **Autenticacao** | Registro e login com autenticacao via token JWT |
| **Feed** | Timeline de postagens dos amigos com pull-to-refresh |
| **Postagens** | Criacao de posts, curtidas e sistema de comentarios |
| **Perfil** | Visualizacao e edicao de perfil do usuario |
| **Amigos** | Envio, aceitacao e rejeicao de solicitacoes de amizade |
| **Mensagens** | Chat privado em tempo real entre usuarios |
| **Notificacoes** | Central de notificacoes com marcacao de leitura |
| **Conselhos** | Feed de conselhos com filtragem por categoria |

---

## Arquitetura

O projeto segue a arquitetura **MVVM** (Model-View-ViewModel) com camadas bem definidas:

```
┌─────────────────────────────────────────────────┐
│                    VIEW LAYER                   │
│  Activities / Fragments / Adapters / XML Layout │
├─────────────────────────────────────────────────┤
│                 VIEWMODEL LAYER                 │
│   AuthViewModel  |  FeedViewModel  |  ...       │
│        LiveData  |  Coroutines                  │
├─────────────────────────────────────────────────┤
│                REPOSITORY LAYER                 │
│            YourLifeRepository                   │
├─────────────────────────────────────────────────┤
│                  DATA LAYER                     │
│     ApiService (Retrofit)  |  Data Models       │
│     RetrofitInstance        |  TokenManager      │
└─────────────────────────────────────────────────┘
```

**Fluxo de dados:**
- **View** observa `LiveData` exposta pelo **ViewModel**
- **ViewModel** aciona o **Repository** via Coroutines
- **Repository** realiza chamadas HTTP ao backend via **Retrofit**
- Respostas sao encapsuladas em `Resource<T>` (Loading, Success, Error)

---

## Stack Tecnologica

| Categoria | Tecnologia | Versao |
| :--- | :--- | :---: |
| Linguagem | Kotlin | 2.2.x |
| Build System | Gradle (Kotlin DSL) | 8.13 |
| UI Framework | Android Views + ViewBinding + DataBinding | -- |
| Networking | Retrofit 2 + OkHttp 4 | 2.9 / 4.12 |
| Serializacao | Gson | 2.10 |
| Assincronismo | Kotlin Coroutines | 1.7 |
| Lifecycle | ViewModel + LiveData | 2.6 |
| Navegacao | Jetpack Navigation Component | 2.7 |
| Banco Local | Room Database | 2.6 |
| Imagens | Coil | 2.5 |
| UI Components | Material Design 3 | 1.10 |
| Testes | JUnit 4 + Espresso | 4.13 / 3.5 |

---

## Estrutura de Diretorios

```
YourLifeApp/
├── app/
│   ├── Icon/
│   │   └── AraraFinal.png                   # Icone do aplicativo
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/yourlife/
│   │   │   │   ├── MainActivity.kt          # Tela de login / registro
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/               # Modelos de dados (User, Post, Comment, etc.)
│   │   │   │   │   ├── remote/              # ApiService e RetrofitInstance
│   │   │   │   │   └── repository/          # YourLifeRepository
│   │   │   │   ├── ui/
│   │   │   │   │   ├── advice/              # Tela de conselhos
│   │   │   │   │   ├── auth/                # ViewModel de autenticacao
│   │   │   │   │   ├── comment/             # Tela de comentarios
│   │   │   │   │   ├── feed/                # Feed de postagens
│   │   │   │   │   ├── friends/             # Gestao de amizades
│   │   │   │   │   ├── mail/                # Mensagens privadas
│   │   │   │   │   ├── notifications/       # Central de notificacoes
│   │   │   │   │   └── profile/             # Perfil do usuario
│   │   │   │   └── util/                    # Utilitarios (Token, Network, Date, Resource)
│   │   │   ├── res/                         # Layouts XML, drawables, menus, valores
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/                     # Testes de instrumentacao
│   │   └── test/                            # Testes unitarios
│   └── build.gradle.kts                     # Configuracao do modulo app
├── gradle/
│   └── libs.versions.toml                   # Catalogo de versoes (Version Catalog)
├── build.gradle.kts                         # Configuracao raiz do projeto
├── settings.gradle.kts                      # Definicao de modulos
├── gradlew / gradlew.bat                    # Wrapper do Gradle
└── README.md
```

---

## Pre-requisitos

Antes de compilar o projeto, certifique-se de ter instalado:

| Ferramenta | Versao Minima |
| :--- | :---: |
| Android Studio | Hedgehog (2023.1.1) ou superior |
| JDK | 11 |
| Android SDK | API 34 |
| Gradle | 8.x (via wrapper incluso) |

---

## Instalacao e Execucao

**1. Clone o repositorio**

```bash
git clone https://github.com/ThyagoToledo/YourLifeApp.git
cd YourLifeApp
```

**2. Abra no Android Studio**

- Abra o Android Studio
- Selecione `File > Open` e navegue ate o diretorio clonado
- Aguarde o Gradle sincronizar as dependencias

**3. Configure o ambiente**

- Certifique-se de que o Android SDK 34 esta instalado via SDK Manager
- Crie ou selecione um emulador com API 24+ ou conecte um dispositivo fisico

**4. Execute o aplicativo**

- Clique em `Run > Run 'app'` ou pressione `Shift + F10`
- O app sera compilado e instalado no dispositivo/emulador selecionado

**5. Configuracao opcional (servidor local)**

Para testes com backend local, edite o campo `BASE_URL` em `app/build.gradle.kts`:

```kotlin
// Em buildTypes > debug, descomente e ajuste:
buildConfigField("String", "BASE_URL", "\"http://SEU_IP_LOCAL:3000/api/\"")
```

---

## Endpoints da API

Base URL: `https://your-life-gamma.vercel.app/api/`

<details>
<summary><b>Autenticacao</b></summary>

| Metodo | Rota | Descricao |
| :---: | :--- | :--- |
| `POST` | `/auth/register` | Registrar novo usuario |
| `POST` | `/auth/login` | Autenticar usuario |

</details>

<details>
<summary><b>Usuarios</b></summary>

| Metodo | Rota | Descricao |
| :---: | :--- | :--- |
| `GET` | `/users/me` | Obter perfil do usuario autenticado |
| `GET` | `/users/{id}` | Obter perfil por ID |
| `GET` | `/users/{id}/posts` | Listar postagens do usuario |
| `GET` | `/users/{id}/friends` | Listar amigos do usuario |
| `PUT` | `/users/me` | Atualizar perfil |
| `GET` | `/users/search/{query}` | Pesquisar usuarios |

</details>

<details>
<summary><b>Feed e Postagens</b></summary>

| Metodo | Rota | Descricao |
| :---: | :--- | :--- |
| `GET` | `/feed` | Obter feed de postagens |
| `POST` | `/posts` | Criar nova postagem |
| `POST` | `/posts/{id}/like` | Curtir postagem |
| `DELETE` | `/posts/{id}/like` | Remover curtida |
| `GET` | `/posts/{id}/comments` | Listar comentarios |
| `POST` | `/posts/{id}/comments` | Adicionar comentario |

</details>

<details>
<summary><b>Amigos</b></summary>

| Metodo | Rota | Descricao |
| :---: | :--- | :--- |
| `GET` | `/friends` | Listar amigos |
| `GET` | `/friends/requests` | Listar solicitacoes pendentes |
| `GET` | `/friends/status/{userId}` | Verificar status da amizade |
| `POST` | `/friends/request` | Enviar solicitacao de amizade |
| `PUT` | `/friends/accept/{requesterId}` | Aceitar solicitacao |
| `DELETE` | `/friends/reject/{requesterId}` | Rejeitar solicitacao |
| `DELETE` | `/friends/{id}` | Remover amigo |

</details>

<details>
<summary><b>Mensagens</b></summary>

| Metodo | Rota | Descricao |
| :---: | :--- | :--- |
| `GET` | `/messages/conversations` | Listar conversas |
| `GET` | `/messages/{userId}` | Obter mensagens com usuario |
| `POST` | `/messages` | Enviar mensagem |
| `PUT` | `/messages/{userId}/read` | Marcar como lida |

</details>

<details>
<summary><b>Notificacoes</b></summary>

| Metodo | Rota | Descricao |
| :---: | :--- | :--- |
| `GET` | `/notifications` | Listar notificacoes |
| `PUT` | `/notifications/{id}/read` | Marcar como lida |

</details>

<details>
<summary><b>Conselhos</b></summary>

| Metodo | Rota | Descricao |
| :---: | :--- | :--- |
| `GET` | `/advices` | Listar conselhos (filtro por categoria opcional) |
| `POST` | `/advices` | Criar novo conselho |

</details>

---

## Autor

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/ThyagoToledo">
        <img src="https://github.com/ThyagoToledo.png" width="100px;" alt="Thyago Toledo"/>
        <br />
        <sub><b>Thyago Toledo</b></sub>
      </a>
    </td>
  </tr>
</table>

---

<p align="center">
  Feito com dedicacao por <a href="https://github.com/ThyagoToledo">Thyago Toledo</a>
</p>
