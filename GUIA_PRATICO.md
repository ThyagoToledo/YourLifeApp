# 💡 GUIA PRÁTICO - Como Usar o Código YourLife Android

**Data:** 04/11/2025  
**Objetivo:** Guia prático com exemplos reais de código funcionando

---

## 🎯 COMO FUNCIONA A ARQUITETURA

### **Fluxo de Dados (MVVM)**

```
┌─────────────┐
│   Activity  │  1. Usuário interage com a UI
│  Fragment   │
└─────┬───────┘
      │
      │ 2. Chama função do ViewModel
      ▼
┌─────────────┐
│  ViewModel  │  3. Faz lógica de negócio
└─────┬───────┘
      │
      │ 4. Chama Repository
      ▼
┌─────────────┐
│ Repository  │  5. Faz chamada API
└─────┬───────┘
      │
      │ 6. Usa Retrofit/ApiService
      ▼
┌─────────────┐
│   Backend   │  7. Retorna dados JSON
│     API     │
└─────┬───────┘
      │
      │ 8. Response processada
      ▼
┌─────────────┐
│  Resource   │  9. Encapsula estado
│   <Data>    │     (Success/Error/Loading)
└─────┬───────┘
      │
      │ 10. LiveData notifica
      ▼
┌─────────────┐
│     UI      │  11. Atualiza interface
└─────────────┘
```

---

## 📝 EXEMPLOS PRÁTICOS

### **1. COMO FAZER LOGIN**

**MainActivity.kt - Implementação Real:**
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar ViewModel
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        setupLoginButton()
        observeLoginState()
    }

    private fun setupLoginButton() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Validação
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Chamar ViewModel
            authViewModel.login(email, password)
        }
    }

    private fun observeLoginState() {
        authViewModel.loginState.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Mostrar loading
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnLogin.isEnabled = false
                }
                is Resource.Success -> {
                    // Login bem-sucedido
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Login realizado!", Toast.LENGTH_SHORT).show()
                    
                    // Navegar para FeedActivity
                    startActivity(Intent(this, FeedActivity::class.java))
                    finish()
                }
                is Resource.Error -> {
                    // Erro no login
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
```

**AuthViewModel.kt - Lógica de Login:**
```kotlin
class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = YourLifeRepository()
    
    private val _loginState = MutableLiveData<Resource<AuthResponse>>()
    val loginState: LiveData<Resource<AuthResponse>> = _loginState

    fun login(email: String, password: String) {
        // Verificar conectividade
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            _loginState.value = Resource.Error("Sem conexão com a internet")
            return
        }

        viewModelScope.launch {
            // Estado de Loading
            _loginState.value = Resource.Loading()

            // Chamar Repository
            val result = repository.login(email, password)
            
            // Processar resultado
            when (result) {
                is Resource.Success -> {
                    // Salvar token
                    result.data?.token?.let { token ->
                        TokenManager.saveToken(getApplication(), token)
                        
                        // Salvar info do usuário
                        result.data.user?.let { user ->
                            TokenManager.saveUserInfo(
                                getApplication(),
                                user.id,
                                user.name,
                                user.email
                            )
                        }
                    }
                }
                else -> {}
            }
            
            _loginState.value = result
        }
    }
}
```

---

### **2. COMO CARREGAR O FEED DE POSTS**

**FeedActivity.kt - Implementação Real:**
```kotlin
class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var viewModel: FeedViewModel
    private lateinit var adapter: FeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FeedViewModel::class.java]

        setupRecyclerView()
        setupSwipeRefresh()
        observeFeedState()
        
        // Carregar feed inicial
        viewModel.loadFeed()
    }

    private fun setupRecyclerView() {
        adapter = FeedAdapter(
            onLikeClick = { post -> viewModel.likePost(post.id) },
            onCommentClick = { post -> openComments(post) }
        )
        
        binding.rvFeed.apply {
            layoutManager = LinearLayoutManager(this@FeedActivity)
            adapter = this@FeedActivity.adapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadFeed()
        }
    }

    private fun observeFeedState() {
        viewModel.feedState.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is Resource.Success -> {
                    binding.swipeRefresh.isRefreshing = false
                    resource.data?.let { posts ->
                        adapter.submitList(posts)
                    }
                }
                is Resource.Error -> {
                    binding.swipeRefresh.isRefreshing = false
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
```

**FeedViewModel.kt - Lógica do Feed:**
```kotlin
class FeedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = YourLifeRepository()
    
    private val _feedState = MutableLiveData<Resource<List<Post>>>()
    val feedState: LiveData<Resource<List<Post>>> = _feedState

    fun loadFeed() {
        viewModelScope.launch {
            _feedState.value = Resource.Loading()

            val token = TokenManager.getToken(getApplication())
            if (token.isNullOrEmpty()) {
                _feedState.value = Resource.Error("Não autenticado")
                return@launch
            }

            val result = repository.getFeed(token)
            _feedState.value = result
        }
    }

    fun likePost(postId: Int) {
        viewModelScope.launch {
            val token = TokenManager.getToken(getApplication()) ?: return@launch
            
            val result = repository.likePost(token, postId)
            
            if (result is Resource.Success) {
                // Recarregar feed para atualizar contadores
                loadFeed()
            }
        }
    }
}
```

**FeedAdapter.kt - Adapter do RecyclerView:**
```kotlin
class FeedAdapter(
    private val onLikeClick: (Post) -> Unit,
    private val onCommentClick: (Post) -> Unit
) : ListAdapter<Post, FeedAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.apply {
                // Avatar do usuário
                ivAvatar.load(post.userAvatar) {
                    crossfade(true)
                    placeholder(R.drawable.ic_avatar_placeholder)
                    transformations(CircleCropTransformation())
                }

                // Informações do post
                tvUserName.text = post.userName
                tvTimestamp.text = DateUtils.formatTimeAgo(post.createdAt)
                tvContent.text = post.content
                tvLikesCount.text = "${post.likesCount} curtidas"
                tvCommentsCount.text = "${post.commentsCount} comentários"

                // Ícone de curtida
                val likeIcon = if (post.isLiked) {
                    R.drawable.ic_heart_filled
                } else {
                    R.drawable.ic_heart_outline
                }
                btnLike.setIconResource(likeIcon)

                // Listeners
                btnLike.setOnClickListener { onLikeClick(post) }
                btnComment.setOnClickListener { onCommentClick(post) }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }
}
```

---

### **3. COMO CRIAR UM NOVO POST**

**FeedActivity.kt - Criar Post:**
```kotlin
class FeedActivity : AppCompatActivity() {
    // ... código anterior ...

    private fun setupCreatePost() {
        binding.btnPost.setOnClickListener {
            val content = binding.etNewPost.text.toString().trim()

            if (content.isEmpty()) {
                Toast.makeText(this, "Escreva algo!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createPost(content)
        }

        // Observar estado de criação
        viewModel.createPostState.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.btnPost.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.btnPost.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    binding.etNewPost.text?.clear()
                    Toast.makeText(this, "Post criado!", Toast.LENGTH_SHORT).show()
                    
                    // Feed é recarregado automaticamente no ViewModel
                }
                is Resource.Error -> {
                    binding.btnPost.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
```

---

### **4. COMO GERENCIAR AMIZADES**

**FriendsFragment.kt - Solicitações de Amizade:**
```kotlin
class FriendsFragment : Fragment() {
    private lateinit var binding: FragmentFriendsBinding
    private lateinit var requestAdapter: FriendRequestAdapter
    private lateinit var friendsAdapter: FriendAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFriendRequestsRecyclerView()
        setupFriendsRecyclerView()
    }

    private fun setupFriendRequestsRecyclerView() {
        requestAdapter = FriendRequestAdapter(
            onAccept = { request ->
                // Aceitar solicitação
                viewModel.acceptFriendRequest(request.requesterId)
            },
            onReject = { request ->
                // Rejeitar solicitação
                viewModel.rejectFriendRequest(request.requesterId)
            }
        )

        binding.rvFriendRequests.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = requestAdapter
        }
    }

    private fun setupFriendsRecyclerView() {
        friendsAdapter = FriendAdapter(
            onFriendClick = { friend ->
                // Abrir perfil do amigo
                openProfile(friend.id)
            }
        )

        binding.rvFriends.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = friendsAdapter
        }
    }
}
```

**FriendRequestAdapter.kt - Implementação Real:**
```kotlin
class FriendRequestAdapter(
    private val onAccept: (FriendRequest) -> Unit,
    private val onReject: (FriendRequest) -> Unit
) : ListAdapter<FriendRequest, FriendRequestAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFriendRequestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemFriendRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(friendRequest: FriendRequest) {
            binding.tvName.text = friendRequest.name
            binding.ivAvatar.load(friendRequest.avatar)
            
            binding.btnAccept.setOnClickListener {
                onAccept(friendRequest)
            }
            
            binding.btnReject.setOnClickListener {
                onReject(friendRequest)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FriendRequest>() {
        override fun areItemsTheSame(oldItem: FriendRequest, newItem: FriendRequest) =
            oldItem.requestId == newItem.requestId

        override fun areContentsTheSame(oldItem: FriendRequest, newItem: FriendRequest) =
            oldItem == newItem
    }
}
```

---

### **5. COMO FUNCIONA O REPOSITORY**

**YourLifeRepository.kt - Camada de Dados:**
```kotlin
class YourLifeRepository {
    private val api = RetrofitInstance.api

    // Template de função padrão
    suspend fun getFeed(token: String): Resource<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                // Fazer chamada API
                val response = api.getFeed("Bearer $token")
                
                // Processar resposta
                if (response.isSuccessful) {
                    response.body()?.let {
                        Resource.Success(it)
                    } ?: Resource.Error("Resposta vazia")
                } else {
                    Resource.Error("Erro ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error("Erro de rede: ${e.message}")
            }
        }
    }

    suspend fun createPost(token: String, content: String): Resource<CreatePostResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = CreatePostRequest(content)
                val response = api.createPost("Bearer $token", request)
                
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro: ${e.message}")
            }
        }
    }

    suspend fun likePost(token: String, postId: Int): Resource<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.likePost("Bearer $token", postId)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error("Erro: ${e.message}")
            }
        }
    }

    // Função auxiliar para processar responses
    private fun <T> handleResponse(response: Response<T>): Resource<T> {
        return if (response.isSuccessful) {
            response.body()?.let {
                Resource.Success(it)
            } ?: Resource.Error("Resposta vazia")
        } else {
            Resource.Error("Erro ${response.code()}")
        }
    }
}
```

---

### **6. COMO FUNCIONA O TOKEN MANAGER**

**TokenManager.kt - Gerenciamento de Sessão:**
```kotlin
object TokenManager {
    private const val PREF_NAME = "yourlife_prefs"
    private const val KEY_TOKEN = "auth_token"

    // Salvar token após login
    fun saveToken(context: Context, token: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_TOKEN, token)
            .apply()
    }

    // Recuperar token para chamadas API
    fun getToken(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)
    }

    // Verificar se está logado
    fun hasToken(context: Context): Boolean {
        return !getToken(context).isNullOrEmpty()
    }

    // Fazer logout
    fun clearToken(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}
```

**Uso no ViewModel:**
```kotlin
// Verificar autenticação
val token = TokenManager.getToken(getApplication())
if (token.isNullOrEmpty()) {
    _feedState.value = Resource.Error("Não autenticado")
    return@launch
}

// Usar token na chamada API
val result = repository.getFeed(token)
```

---

### **7. COMO FUNCIONA O RESOURCE (Estados)**

**Resource.kt - Sealed Class:**
```kotlin
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}
```

**Uso na UI:**
```kotlin
viewModel.feedState.observe(this) { resource ->
    when (resource) {
        is Resource.Loading -> {
            // Mostrar loading spinner
            showLoading()
        }
        is Resource.Success -> {
            // Exibir dados
            hideLoading()
            resource.data?.let { posts ->
                displayPosts(posts)
            }
        }
        is Resource.Error -> {
            // Mostrar erro
            hideLoading()
            showError(resource.message ?: "Erro desconhecido")
        }
    }
}
```

---

### **8. COMO FUNCIONA A API (RETROFIT)**

**RetrofitInstance.kt - Configuração:**
```kotlin
object RetrofitInstance {
    private const val BASE_URL = "https://your-life-gamma.vercel.app/api/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
```

**ApiService.kt - Endpoints:**
```kotlin
interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("feed")
    suspend fun getFeed(@Header("Authorization") token: String): Response<List<Post>>

    @POST("posts")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Body request: CreatePostRequest
    ): Response<CreatePostResponse>

    @POST("posts/{id}/like")
    suspend fun likePost(
        @Header("Authorization") token: String,
        @Path("id") postId: Int
    ): Response<ApiResponse>
}
```

---

## 🚀 FLUXO COMPLETO DE UMA REQUISIÇÃO

### **Exemplo: Curtir um Post**

**1. Usuário clica no botão de curtir:**
```kotlin
// FeedAdapter.kt
btnLike.setOnClickListener {
    onLikeClick(post) // Callback para Activity
}
```

**2. Activity/Fragment chama ViewModel:**
```kotlin
// FeedActivity.kt
val adapter = FeedAdapter(
    onLikeClick = { post ->
        viewModel.likePost(post.id) // ✅ Chamada ao ViewModel
    }
)
```

**3. ViewModel processa e chama Repository:**
```kotlin
// FeedViewModel.kt
fun likePost(postId: Int) {
    viewModelScope.launch {
        _likeState.value = Resource.Loading()
        
        val token = TokenManager.getToken(getApplication())
        if (token == null) {
            _likeState.value = Resource.Error("Não autenticado")
            return@launch
        }
        
        val result = repository.likePost(token, postId) // ✅ Chamada ao Repository
        _likeState.value = result
        
        if (result is Resource.Success) {
            loadFeed() // Recarregar feed
        }
    }
}
```

**4. Repository faz chamada API:**
```kotlin
// YourLifeRepository.kt
suspend fun likePost(token: String, postId: Int): Resource<ApiResponse> {
    return withContext(Dispatchers.IO) {
        try {
            val response = api.likePost("Bearer $token", postId) // ✅ Chamada HTTP
            handleResponse(response)
        } catch (e: Exception) {
            Resource.Error("Erro: ${e.message}")
        }
    }
}
```

**5. Retrofit/OkHttp faz requisição HTTP:**
```kotlin
// ApiService.kt (Interface Retrofit)
@POST("posts/{id}/like")
suspend fun likePost(
    @Header("Authorization") token: String,
    @Path("id") postId: Int
): Response<ApiResponse>

// ✅ HTTP POST: https://your-life-gamma.vercel.app/api/posts/123/like
// Header: Authorization: Bearer eyJhbGc...
```

**6. Backend processa e retorna JSON:**
```json
{
  "success": true,
  "message": "Post curtido com sucesso"
}
```

**7. Retrofit converte JSON para Kotlin:**
```kotlin
data class ApiResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String
)
```

**8. Repository encapsula em Resource:**
```kotlin
return Resource.Success(apiResponse)
```

**9. ViewModel emite LiveData:**
```kotlin
_likeState.value = result
```

**10. UI observa e atualiza:**
```kotlin
viewModel.likeState.observe(this) { resource ->
    when (resource) {
        is Resource.Success -> {
            Toast.makeText(this, "Post curtido!", Toast.LENGTH_SHORT).show()
            // Feed é recarregado automaticamente
        }
        is Resource.Error -> {
            Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
        }
        is Resource.Loading -> {
            // Mostrar loading se necessário
        }
    }
}
```

---

## 📚 RESUMO DOS CONCEITOS

### **MVVM (Model-View-ViewModel)**
- **Model:** Data classes + Repository + API
- **View:** Activities + Fragments + XML Layouts
- **ViewModel:** Lógica de negócio + LiveData

### **Repository Pattern**
- Camada intermediária entre ViewModel e API
- Centraliza lógica de acesso a dados
- Facilita testes e manutenção

### **LiveData + Observer**
- Observação reativa de dados
- Lifecycle-aware (respeita ciclo de vida)
- Atualizações automáticas da UI

### **Coroutines**
- Operações assíncronas simplificadas
- Substituem callbacks complexos
- `suspend fun` + `viewModelScope.launch`

### **Sealed Classes (Resource)**
- Representam estados mutuamente exclusivos
- Type-safe (seguro em tempo de compilação)
- Facilita tratamento de estados na UI

---

## 🎓 BOAS PRÁTICAS IMPLEMENTADAS

✅ **Separação de responsabilidades** (cada classe tem um propósito)  
✅ **Single Responsibility Principle**  
✅ **Dependency Injection** (manual, preparado para Hilt/Dagger)  
✅ **Error Handling** robusto  
✅ **Loading States** em todas as operações assíncronas  
✅ **Token Management** centralizado  
✅ **Network Check** antes de requisições  
✅ **Comentários KDoc** em funções públicas  
✅ **Naming Conventions** seguindo padrão Kotlin  
✅ **ViewBinding** evitando findViewById  

---

**🎯 Conclusão:** O código está bem estruturado, seguindo as melhores práticas do Android moderno, e pronto para expansão!

