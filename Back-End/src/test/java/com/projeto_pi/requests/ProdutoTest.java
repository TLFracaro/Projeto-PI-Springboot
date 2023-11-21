package com.projeto_pi.requests;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class ProdutoTest {

//     // Entidade responsável pelas requisições
//     @Autowired
//     private MockMvc mockMvc;

//     // Entidade de serialização JSON
//     @Autowired
//     private ObjectMapper mapper;

//     // Entidade com as variáveis de ambiente
//     private static final Dotenv dotenv = Dotenv.configure().load();

//     // Entidades dos produtos
//     private ProdutoDto produtoDto1, produtoDto2;

//     // Entidades de usuário
//     private UsuarioDto usuarioAdmin, usuarioCommon;

//     // Entidade com os arquivos de imagem da requisição
//     private final List<MockMultipartFile> imagens = new ArrayList<>();

//     // Secret do JWT para decodificar o token
//     private static final String secret = Objects.requireNonNull(dotenv.get("JWT_SECRET"));

//     @BeforeAll
//     public static void loadDotenv() {
//         // Configuração das variáveis de ambiente
//         System.setProperty("spring.datasource.url", Objects.requireNonNull(dotenv.get("DATABASE_URL")));
//         System.setProperty("spring.datasource.username", Objects.requireNonNull(dotenv.get("DATABASE_USERNAME")));
//         System.setProperty("spring.datasource.password", Objects.requireNonNull(dotenv.get("DATABASE_PASSWORD")));
//     }

//     @BeforeEach
//     public void setup() {

//         // Criação do primeiro produto
//         produtoDto1 = new ProdutoDto("produto 1",
//                 "Camisetas",
//                 "Quicksilver",
//                 BigDecimal.valueOf(120.99),
//                 "Camisetas da Quicksilver",
//                 "something",
//                 BigDecimal.valueOf(130),
//                 List.of(new VariacaoDto("GG", "Vermelha", 2)));

//         // Criação do segundo produto
//         produtoDto2 = new ProdutoDto("produto 2",
//                 "Calças",
//                 "Quicksilver",
//                 BigDecimal.valueOf(120.99),
//                 "Camisetas da Quicksilver",
//                 "something",
//                 BigDecimal.valueOf(130),
//                 List.of(new VariacaoDto("M", "Azul", 10)));

//         // Usuário com permissões diferentes
//         usuarioAdmin = new UsuarioDto("77369780007", "Kimitaro Atiro", "teste1@gmail.com", "123123", Privilegio.ADMIN);
//         usuarioCommon = new UsuarioDto("05306836011", "Mataro Kishimoto", "teste2@gmail.com", "123123", Privilegio.USUARIO);

//         // Carregar a imagem do primeiro produto na memória
//         File file = new File("src/test/imagens/camiseta.webp");
//         try (FileInputStream stream = new FileInputStream(file)) {
//             imagens.add(new MockMultipartFile("imagens", file.getName(), "image/webp", stream));
//         } catch (IOException e) {
//             throw new RuntimeException(e);
//         }

//         // Carregar a imagem do segundo produto na memória
//         file = new File("src/test/imagens/calca.jpg");
//         try (FileInputStream stream = new FileInputStream(file)) {
//             imagens.add(new MockMultipartFile("imagens", file.getName(), "image/jpeg", stream));
//         } catch (IOException e) {
//             throw new RuntimeException(e);
//         }

//         // Carregar a imagem que será atualizada
//         file = new File("src/test/imagens/camiseta2.webp");
//         try (FileInputStream stream = new FileInputStream(file)) {
//             imagens.add(new MockMultipartFile("imagens", file.getName(), "image/webp", stream));
//         } catch (IOException e) {
//             throw new RuntimeException(e);
//         }

//         // Configuração o deserilizador de JSON
//         mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//         mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
//         mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
//     }

//     /*
//      * Para facilitar o entendimento da aplicação, serão executados dois testes cada um
//      * com um usuário que possui permissões diferentes seguindo o mesmo fluxo da aplicação,
//      * na qual seguirá o seguinte padrão:
//      *
//      * --> Dois usuários serão cadastrados, com permissões de acesso diferente (ADMIN e USUARIO).
//      * --> Ambos usuários serão AUTENTICADOS e irão receber um TOKEN para poder acessar as outras rotas.
//      * --> Para ter acesso a qualquer rota que não seja de autenticação, o token é requisitado, caso contrário
//      *     o retorno será um HTTP status code (403) FORBIDDEN.
//      * --> As rotas que cadastram, atualizam e deletam produtos, só podem ser acessadas pelo ADMIN.
//      * --> As rotas que listão e trazem um único produto, podem ser acessadas por qualquer um que esteja AUTENTICADO.
//      * --> As rotas relacionadas aos usuários, só podem ser acessadas por usuários com mesmo ID da autenticação
//      *
//      * -->> PS: Conforme os testes são feitos, outros comentários com explicações e expectativas
//      *       de resultados serão feitos para melhor compreensão.
//      * */


//     @Test
//     @Transactional
//     public void testAppFlowUserAdmin() throws Exception {

//         // Requisição para cadastro de usuário (ADMIN)
//         MvcResult result = mockMvc.perform(MockMvcRequestBuilders
//                 .post("/auth/registrar")
//                 .content(mapper.writeValueAsString(usuarioAdmin))
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .characterEncoding(StandardCharsets.UTF_8)).andReturn();

//         // Resultado da requisição
//         MockHttpServletResponse response = result.getResponse();

//         // Verifica se houve sucesso na requisição HTTP status code (200) OK
//         assertEquals(HttpStatus.OK.value(), response.getStatus());

//         // Extrai o resultado a resposta da requisição
//         JsonNode node = mapper.readTree(response.getContentAsString());

//         // Extrai o token do usuário administrador (ADMIN)
//         String token = node.get("token").asText();

//         // Cabeçalho com o token de autenticação
//         HttpHeaders headers = new HttpHeaders();
//         headers.add("Authorization", "Bearer " + token);

//         // Requisição para cadastro de produto (POST)
//         result = mockMvc.perform(MockMvcRequestBuilders
//                 .multipart(HttpMethod.POST, "/produto")
//                 .file(imagens.get(0))
//                 .file(imagens.get(1))
//                 .param("json", mapper.writeValueAsString(produtoDto1))
//                 .headers(headers)
//                 .contentType(MediaType.MULTIPART_FORM_DATA)
//                 .characterEncoding(StandardCharsets.UTF_8)).andReturn();

//         // Resultado da requisição
//         response = result.getResponse();

//         // Verifica se houve sucesso na requisição HTTP status code (201) CREATED
//         assertEquals(HttpStatus.CREATED.value(), response.getStatus());

//         // Produto que foi criado
//         Produto produto1 = mapper.readValue(response.getContentAsString(), Produto.class);

//         // Espera um tempo para o servidor lidar com as imagens
//         Thread.sleep(1500);

//         // Requisição para procurar um produto (GET)
//         result = mockMvc.perform(MockMvcRequestBuilders
//                 .get("/produto")
//                 .param("produtoId", produto1.getProdutoId().toString())
//                 .headers(headers)
//                 .characterEncoding(StandardCharsets.UTF_8)).andReturn();

//         // Resultado da requisição
//         response = result.getResponse();

//         // Verifica se houve sucesso na requisição HTTP status code (200) OK
//         assertEquals(HttpStatus.OK.value(), response.getStatus());

//         // Verifica se o produto é o mesmo que foi cadastrado
//         assertEquals(produto1, mapper.readValue(response.getContentAsString(), Produto.class));


//         // Requisição para atualizar o produto (PUT)
//         result = mockMvc.perform(MockMvcRequestBuilders
//                 .multipart(HttpMethod.PUT, "/produto")
//                 .file(imagens.getLast())
//                 .param("produtoId", produto1.getProdutoId().toString())
//                 .param("json", mapper.writeValueAsString(produtoDto2))
//                 .contentType(MediaType.MULTIPART_FORM_DATA)
//                 .headers(headers)
//                 .characterEncoding(StandardCharsets.UTF_8)).andReturn();

//         // Resultado da requisição
//         response = result.getResponse();

//         // Verifica se houve sucesso na requisição HTTP status code (200) OK
//         assertEquals(HttpStatus.OK.value(), response.getStatus());

//         // Pega o produto atualizado
//         Produto produto2 = mapper.readValue(response.getContentAsString(), Produto.class);

//         // Verifica se o produto foi realmente atualizado
//         assertNotEquals(produto1, produto2);

//         // Espera um tempo para o servidor lidar com as imagens
//         Thread.sleep(1500);

//         // Requisição para excluir um produto (DELETE)
//         result = mockMvc.perform(MockMvcRequestBuilders
//                 .delete("/produto")
//                 .param("produtoId", produto2.getProdutoId().toString())
//                 .headers(headers)
//                 .characterEncoding(StandardCharsets.UTF_8)).andReturn();


//         // Resultado da requisição
//         response = result.getResponse();

//         // Verifica se houve sucesso na requisição HTTP status code (200) OK
//         assertEquals(HttpStatus.OK.value(), response.getStatus());

//         // Espera um tempo para o servidor lidar com as imagens
//         Thread.sleep(1500);


//         // Requisição de listagem de produtos (GET)
//         result = mockMvc.perform(MockMvcRequestBuilders
//                 .get("/produto/todos")
//                 .characterEncoding(StandardCharsets.UTF_8)
//                 .headers(headers)).andReturn();

//         // Resultado da requisição
//         response = result.getResponse();

//         // Como o único produto foi apagado, a verificação é se a lista está vazia
//         // HTTP status code (404) NOT_FOUND
//         assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
//     }

//     @Test
//     @Transactional
//     public void testAppFlowUserCommon() throws Exception {

//         // Requisição para cadastro de usuário (USUARIO)
//         MvcResult result = mockMvc.perform(MockMvcRequestBuilders
//                 .post("/auth/registrar")
//                 .content(mapper.writeValueAsString(usuarioCommon))
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .characterEncoding(StandardCharsets.UTF_8)).andReturn();

//         // Resultado da requisição
//         MockHttpServletResponse response = result.getResponse();

//         // Verifica se houve sucesso na requisição HTTP status code (200) OK
//         assertEquals(HttpStatus.OK.value(), response.getStatus());

//         // Extrai o resultado a resposta da requisição
//         JsonNode node = mapper.readTree(response.getContentAsString());

//         // Lógica para extrair o token do JSON, decodificar e extrair o ID do usuário (USUARIO)
//         String token = node.get("token").asText();

//         Algorithm algorithm = Algorithm.HMAC256(secret);
//         JWTVerifier verifier = JWT.require(algorithm).build();
//         DecodedJWT decodedJWT = verifier.verify(token);

//         String userId = decodedJWT.getClaim("usuarioId").asString();

//         // Cabeçalho com o token de autenticação
//         HttpHeaders headers = new HttpHeaders();
//         headers.add("Authorization", "Bearer " + token);


//         //Este usuário não possui a permissão para cadastrar, atualizar e deletar produtos,
//         //então iremos verificar se ele não tem acesso a um desses recursos
//         // Requisição para cadastro de produto (POST)
//         result = mockMvc.perform(MockMvcRequestBuilders
//                 .multipart(HttpMethod.POST, "/produto")
//                 .file(imagens.get(0))
//                 .file(imagens.get(1))
//                 .param("json", mapper.writeValueAsString(produtoDto1))
//                 .headers(headers)
//                 .contentType(MediaType.MULTIPART_FORM_DATA)
//                 .characterEncoding(StandardCharsets.UTF_8)).andReturn();

//         // Resultado da requisição
//         response = result.getResponse();

//         // Verifica se o usuário não foi autorizado a fazer a requisição
//         // HTTP status code (403) FORBIDDEN
//         assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());


//         // Este usuário pode ver os produtos que estão cadastrados 
//         // Requisição de listagem de produtos (GET)
//         result = mockMvc.perform(MockMvcRequestBuilders
//                 .get("/produto/todos")
//                 .characterEncoding(StandardCharsets.UTF_8)
//                 .headers(headers)).andReturn();

//         // Resultado da requisição
//         response = result.getResponse();

//         // Como não há produtos cadastrados, ele irá receber um HTTP status code (404) NOT_FOUND
//         assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());


//         // Qualquer usuário pode ver, atualizar ou deletar o seu cadastro,
//         // no entanto, existe uma verificação se o usuário que esta solicitando o recurso,
//         // é o mesmo que esta autenticado

//         // Requisição para ver o cadastro do usuário (GET)
//         result = mockMvc.perform(MockMvcRequestBuilders
//                 .get("/usuario")
//                 .param("usuarioId", userId)
//                 .headers(headers)
//                 .characterEncoding(StandardCharsets.UTF_8)).andReturn();

//         // Resultado da requisição
//         response = result.getResponse();

//         // Verifica se houve sucesso na requisição HTTP status code (200) OK
//         assertEquals(HttpStatus.OK.value(), response.getStatus());

//         // Dados do usuário (A senha é criptografada por segurança)
//         Usuario usuario = mapper.readValue(response.getContentAsString(), Usuario.class);

//         // Verifica se a senha esta realmente criptografada
//         assertNotEquals(usuarioCommon.senha(), usuario.getSenha());

//         // Por último, iremos excluir o usuário e verificar se ele realmente foi excluído

//         // Requisição para excluir o usuário (DELETE)
//         result = mockMvc.perform(MockMvcRequestBuilders
//                 .delete("/usuario")
//                 .param("usuarioId", userId)
//                 .headers(headers)
//                 .characterEncoding(StandardCharsets.UTF_8)).andReturn();

//         // Resultado da requisição
//         response = result.getResponse();

//         // Verifica se houve sucesso na requisição HTTP status code (200) OK
//         assertEquals(HttpStatus.OK.value(), response.getStatus());


//         // Requisição para ver o usuário foi realmente excluído (GET)
//         assertThrows(NoSuchElementException.class, () ->
//                 mockMvc.perform(MockMvcRequestBuilders
//                         .get("/usuario")
//                         .param("usuarioId", userId)
//                         .headers(headers)
//                         .characterEncoding(StandardCharsets.UTF_8)).andReturn());
//     }
}
