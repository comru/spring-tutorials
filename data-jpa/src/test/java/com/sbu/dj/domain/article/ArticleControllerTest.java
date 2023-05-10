package com.sbu.dj.domain.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.sbu.dj.IntegrationTest;
import com.sbu.dj.domain.article.dto.ArticleNew;
import com.sbu.dj.domain.article.dto.ArticleUpdate;
import com.sbu.dj.domain.comment.dto.CommentNew;
import com.sbu.dj.domain.tag.TagRepository;
import com.sbu.dj.domain.user.UserDto;
import com.sbu.dj.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@IntegrationTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ArticleControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(ArticleControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private String jamesToken;

    private TestInfo testInfo;
    private String simpsonToken;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        this.testInfo = testInfo;

        UserDto.LoginUser jamesLoginRequest = new UserDto.LoginUser("james@gmail.com", "1234");
        jamesToken = "Token " + userService.login(jamesLoginRequest).token();

        UserDto.LoginUser simpsonLoginRequest = new UserDto.LoginUser("simpson@gmail.com", "1234");
        simpsonToken = "Token " + userService.login(simpsonLoginRequest).token();

        logTestName();
    }

    private void logTestName() {
        logger.info("Run '" + testInfo.getDisplayName() + "' test");
    }

    @Test
    void createArticle() throws Exception {
        // given
        ArticleNew request =
                new ArticleNew("Test Article", "Test description", "Test body", Set.of("test", "performance", "sample", "java"));

        // when
        ResultActions resultActions = mockMvc.perform(post("/articles")
                .header("Authorization", jamesToken)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.article.slug").value("test-article"))
                .andExpect(jsonPath("$.article.title").value("Test Article"))
                .andExpect(jsonPath("$.article.description").value("Test description"))
                .andExpect(jsonPath("$.article.body").value("Test body"))
                .andExpect(jsonPath("$.article.tagList", containsInAnyOrder("test", "performance", "sample", "java")))
                .andExpect(jsonPath("$.article.author.username").value("james"))
                .andDo(print());
    }

    @Test
    void getSingleArticle() throws Exception {
        mockMvc.perform(get("/articles/{slug}", "effective-java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.title").value("Effective Java"))
                .andExpect(jsonPath("$.article.author.username").value("james"))
                .andExpect(jsonPath("$.article.favorited").value(false))
                .andExpect(jsonPath("$.article.favoritesCount").value(3))
                .andExpect(jsonPath("$.article.tagList", containsInAnyOrder("java", "properties")))
                .andDo(print());
    }

    @Test
    void getArticles() throws Exception {
        mockMvc.perform(get("/articles")
                        .param("tag", "java")
                        .param("author", "james")
                        .param("favorited", "simpson"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articlesCount").value(1))
                .andExpect(jsonPath("$.articles[0].title").value("Effective Java"))
                .andExpect(jsonPath("$.articles[0].author.username").value("james"))
                .andExpect(jsonPath("$.articles[0].favorited").value(false))
                .andExpect(jsonPath("$.articles[0].favoritesCount").value(3))
                .andExpect(jsonPath("$.articles[0].tagList[0]").value("java"))
                .andDo(print());
    }

    @Test
    void getFeedArticles() throws Exception {
        mockMvc.perform(get("/articles/feed").header("Authorization", simpsonToken))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.articlesCount").value(1),
                        jsonPath("$.articles", hasSize(1)),
                        jsonPath("$.articles[0].author.username").value("james"),
                        jsonPath("$.articles[0].author.following").value(true),
                        jsonPath("$.articles[0].title").value("Effective Java"),
                        jsonPath("$.articles[0].author.username").value("james"),
                        jsonPath("$.articles[0].favorited").value(true),
                        jsonPath("$.articles[0].favoritesCount").value(3),
                        jsonPath("$.articles[0].tagList[0]").value("java")
                )
                .andDo(print());
    }

    @Test
    public void updateArticle() throws Exception {
        // given
        // - create a test article
        ArticleNew createRequest =
                new ArticleNew("Test Article", "Test description", "Test body", Set.of("test", "sample"));

        // - get the slug of the article
        Map<String, Object> articleResponce = JsonPath.parse(mockMvc.perform(post("/articles")
                                .header("Authorization", jamesToken)
                                .content(objectMapper.writeValueAsString(Map.of("article", createRequest)))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .json();

        Map<String, Object> articleMap = (Map<String, Object>) articleResponce.get("article");
        Object slug = articleMap.get("slug");
        // when
        // - update the article
        ArticleUpdate updateRequest =
                new ArticleUpdate("Updated Title", "Updated description", "Updated body");
        ResultActions resultActions = mockMvc.perform(put("/articles/{slug}", slug)
                .header("Authorization", jamesToken)
                .content(objectMapper.writeValueAsString(Map.of("article", updateRequest)))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article.slug").value("updated-title"))
                .andExpect(jsonPath("$.article.title").value("Updated Title"))
                .andExpect(jsonPath("$.article.description").value("Updated description"))
                .andExpect(jsonPath("$.article.body").value("Updated body"))
                .andExpect(jsonPath("$.article.createdAt").value(articleMap.get("createdAt")))
//                .andExpect(jsonPath("$.article.updatedAt").value(not(articleMap.get("updatedAt"))))
                .andDo(print());
    }

    @Test
    public void deleteArticle() throws Exception {
        // given
        // - create a test article
        ArticleNew createRequest =
                new ArticleNew("Test Article", "Test description", "Test body", Set.of("test", "sample"));

        // - get the slug of the article
        String slug = JsonPath.parse(mockMvc.perform(post("/articles")
                                .header("Authorization", jamesToken)
                                .content(objectMapper.writeValueAsString(Map.of("article", createRequest)))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .read("$.article.slug");

        // when
        ResultActions resultActions =
                mockMvc.perform(delete("/articles/{slug}", slug).header("Authorization", jamesToken));

        // then
        resultActions.andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void createComment() throws Exception {
        // given
        // - create a test article
        ArticleNew createRequest =
                new ArticleNew("Test Article", "Test description", "Test body", Set.of("test", "sample"));

        // - get the slug of the article by james
        String slug = JsonPath.parse(mockMvc.perform(post("/articles")
                                .header("Authorization", jamesToken)
                                .content(objectMapper.writeValueAsString(Map.of("article", createRequest)))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString())
                .read("$.article.slug");

        // - create a comment by simpson
        CommentNew request = new CommentNew("Test comment");

        // when
        ResultActions resultActions = mockMvc.perform(post("/articles/{slug}/comments", slug)
                .header("Authorization", simpsonToken)
                .content(objectMapper.writeValueAsString(Map.of("comment", request)))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment.body").value("Test comment"))
                .andExpect(jsonPath("$.comment.author.username").value("simpson"))
                .andDo(print());
    }
}
