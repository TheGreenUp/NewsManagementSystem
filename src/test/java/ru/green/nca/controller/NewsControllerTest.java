package ru.green.nca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.green.nca.entity.Comment;
import ru.green.nca.entity.News;
import ru.green.nca.model.NewsWithComments;
import ru.green.nca.service.NewsService;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NewsController.class)
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;
    News NEWS_1 = new News(1, "1984", "Orwell", null, null, 1, 1);

    @Test
    public void getAllNewsTest() throws Exception {
        when(newsService.getNews(eq(0), eq(10))).thenReturn(List.of(NEWS_1));
        //сверху мы описываем поведение, что будет, если передадим 0 и 10 в качестве параметров
        //а снизу мы вызываем с этими параметрами, на что получаем результат из thenReturn()
        this.mockMvc.perform(get("/api/news?page=0&size=10"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getNewsByIdTest() throws Exception {
        when(newsService.getNewsById(eq(3))).thenReturn(NEWS_1);
        this.mockMvc.perform(get("/api/news/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createNewsTest() throws Exception {
        when(newsService.createNews(NEWS_1)).thenReturn(NEWS_1);
        String newsJson = asJsonString(NEWS_1);
        this.mockMvc.perform(post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newsJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deleteNewsTest() throws Exception {
        mockMvc.perform(delete("/api/news/1"))
                .andExpect(status().isOk());
        verify(newsService).deleteNews(1);
    }

    @Test
    public void updateNewsTest() throws Exception {
        when(newsService.updateNews(eq(1), eq(NEWS_1))).thenReturn(NEWS_1);

        mockMvc.perform(put("/api/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(NEWS_1)))
                .andExpect(status().isOk());

        verify(newsService).updateNews(eq(1), eq(NEWS_1));
    }

    @Test
    public void searchNewsTest() throws Exception {
        when(newsService.searchByTitleOrText(eq("1984"), eq(0), eq(10))).thenReturn(List.of(NEWS_1));
        this.mockMvc.perform(get("/api/news/search?keyword=1984&page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(NEWS_1)))
                .andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void findNewsWithCommentsTest() throws Exception {
        Comment comment = new Comment(10,"test text",null,null,1,1);
        NewsWithComments news = new NewsWithComments(NEWS_1, List.of(comment));
        asJsonString(news);
        when(newsService.viewNewsWithComments(eq(11),eq(0),eq(10))).thenReturn(news);
        this.mockMvc.perform(get("/api/news/11/comments?page=0&size=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(asJsonString(news)));
    }
    // Метод для преобразования объекта в JSON строку
    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
