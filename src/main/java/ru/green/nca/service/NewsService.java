package ru.green.nca.service;

import ru.green.nca.entity.News;
import ru.green.nca.model.NewsWithComments;

import java.util.List;

public interface NewsService {
    News getNewsById(int newsId);

    List<News> getNews(int page, int size);

    News createNews(News news);

    NewsWithComments viewNewsWithComments(int newsId, int commentPage, int commentSize);

    News updateNews(int newsId, News updatedNews);

    void deleteNews(int newsId);

    List<News> searchByTitleOrText(String keyword, int page, int size);

}
