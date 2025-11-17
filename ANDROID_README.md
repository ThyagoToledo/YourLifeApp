# 📱 YourLife - Aplicativo Android

## 🎯 Visão Geral

Aplicativo Android nativo desenvolvido em **Kotlin** que consome a API REST do projeto YourLife. Integração completa com o back-end Node.js + PostgreSQL hospedado no Vercel.

---

## 🏗️ Arquitetura

### Stack Tecnológico
- **Linguagem**: Kotlin
- **Arquitetura**: MVVM (Model-View-ViewModel)
- **API**: Retrofit 2.9.0
- **Async**: Kotlin Coroutines
- **UI**: Material Design 3 + ViewBinding
- **Imagens**: Coil
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

### Estrutura do Projeto

```
app/src/main/java/com/example/yourlife/
├── data/
│   ├── model/              # Modelos de dados (DTOs)
│   │   ├── User.kt
│   │   ├── Post.kt
│   │   ├── Message.kt
│   │   └── Friend.kt
│   ├── remote/             # Camada de rede
│   │   ├── ApiService.kt
│   │   └── RetrofitInstance.kt
│   └── repository/         # Repositórios
│       └── YourLifeRepository.kt
├── ui/                     # Interface do usuário
│   ├── auth/
│   │   └── AuthViewModel.kt
│   └── feed/
│       └── FeedViewModel.kt
├── util/                   # Utilitários
│   ├── Resource.kt
│   ├── TokenManager.kt
│   └── DateUtils.kt
└── MainActivity.kt
```

---

## 🔌 Integração com Back-end

### Base URL
```kotlin
https://your-life-gamma.vercel.app/api/
```

### Endpoints Implementados

#### Autenticação
- `POST /auth/register` - Registro de usuário
- `POST /auth/login` - Login

#### Usuários
- `GET /users/me` - Perfil do usuário autenticado
- `GET /users/{id}` - Perfil de outro usuário
- `GET /users/search/{query}` - Buscar usuários

#### Feed & Posts
- `GET /feed` - Buscar feed de posts
- `POST /posts` - Criar post
- `POST /posts/{id}/like` - Curtir post
- `DELETE /posts/{id}/like` - Descurtir post
- `GET /posts/{id}/comments` - Listar comentários
- `POST /posts/{id}/comments` - Criar comentário

#### Amigos
- `GET /friends` - Listar amigos
- `GET /friends/requests` - Pedidos de amizade
- `POST /friends/request` - Enviar pedido
- `PUT /friends/accept/{id}` - Aceitar pedido
- `DELETE /friends/reject/{id}` - Recusar pedido

#### Mensagens
- `GET /messages/conversations` - Listar conversas
- `GET /messages/{userId}` - Mensagens com usuário
- `POST /messages` - Enviar mensagem

#### Notificações
- `GET /notifications` - Listar notificações

---

## 🚀 Como Executar

### Pré-requisitos
- Android Studio Hedgehog ou superior
- JDK 11+
- Dispositivo Android/Emulador com API 24+

### Passos

1. **Clone o repositório** (se ainda não fez):
```bash
git clone https://github.com/ThyagoToledo/YourLife.git
cd YourLife
```

2. **Abra o projeto no Android Studio**
   - File → Open → Selecione a pasta `YourLife`

3. **Sync Gradle**
   - O Android Studio fará automaticamente
   - Ou clique em "Sync Now" quando solicitado

4. **Execute o app**
   - Conecte um dispositivo Android via USB (com depuração USB ativada)
   - Ou inicie um emulador
   - Clique em "Run" (▶️) no Android Studio

---

## 🔧 Configuração

### Alterar Base URL (para desenvolvimento local)

Se você está rodando o back-end localmente, edite `app/build.gradle.kts`:

```kotlin
buildTypes {
    debug {
        buildConfigField("String", "BASE_URL", "\"http://SEU_IP_LOCAL:3000/api/\"")
    }
}
```

⚠️ **Importante**: Use o IP da sua máquina na rede local, não `localhost` ou `127.0.0.1` (o emulador não consegue acessar).

Para descobrir seu IP:
```bash
# Windows
ipconfig

# Linux/Mac
ifconfig
```

---

## 📦 Dependências Principais

```gradle
// Retrofit para chamadas HTTP
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// OkHttp para logging e interceptors
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Coroutines para operações assíncronas
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Lifecycle & ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

// Coil para carregamento de imagens
implementation("io.coil-kt:coil:2.5.0")
```

---

## 🎨 Features Implementadas

### ✅ Autenticação
- [x] Login com email/senha
- [x] Registro de novos usuários
- [x] Armazenamento seguro de token JWT
- [x] Logout

### ✅ Feed
- [x] Listagem de posts
- [x] Criar novos posts
- [x] Curtir/descurtir posts
- [x] Ver comentários
- [x] Criar comentários

### ✅ Amigos
- [x] Listar amigos
- [x] Enviar pedidos de amizade
- [x] Aceitar/recusar pedidos
- [x] Ver status de amizade

### ✅ Mensagens
- [x] Listar conversas
- [x] Ver mensagens com amigo
- [x] Enviar mensagens
- [x] Contador de não lidas

### ✅ Notificações
- [x] Listar notificações
- [x] Marcar como lida

---

## 🔐 Segurança

### Token JWT
O token é armazenado localmente usando `SharedPreferences` e enviado em todas as requisições:

```kotlin
Authorization: Bearer {token}
```

### HTTPS
Todas as comunicações com a API são feitas via HTTPS (Vercel).

---

## 📱 Compatibilidade

- **Min SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 34 (Android 14)
- **Testado em**: Android 10, 11, 12, 13, 14

---

## 🐛 Troubleshooting

### Erro: "Unable to resolve host"
- Verifique sua conexão com a internet
- Verifique se a URL da API está correta
- Se estiver usando emulador, use o IP da máquina, não localhost

### Erro: "Unauthorized" (401)
- Token expirado ou inválido
- Faça logout e login novamente

### Erro: "Network Security Policy"
Se estiver usando HTTP (desenvolvimento local), adicione no `AndroidManifest.xml`:

```xml
<application
    android:usesCleartextTraffic="true"
    ...>
```

---

## 📚 Próximos Passos

### Funcionalidades a Implementar
- [ ] Tela de perfil do usuário
- [ ] Edição de perfil (avatar, bio, interesses)
- [ ] Sistema de conselhos
- [ ] Notificações push
- [ ] Dark mode
- [ ] Cache local com Room Database
- [ ] Pull-to-refresh
- [ ] Paginação infinita no feed
- [ ] Upload de imagens nos posts

### Melhorias de UX
- [ ] Animações de transição
- [ ] Skeleton loaders
- [ ] Estados vazios (empty states)
- [ ] Tratamento de erros offline
- [ ] Retry automático

---

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está sob a licença MIT.

---

## 👨‍💻 Desenvolvedor

**Thyago Toledo**
- GitHub: [@ThyagoToledo](https://github.com/ThyagoToledo)
- Backend Repository: [YourLife](https://github.com/ThyagoToledo/YourLife)

---

## 📞 Suporte

Para reportar bugs ou sugerir melhorias, abra uma [issue no GitHub](https://github.com/ThyagoToledo/YourLife/issues).

