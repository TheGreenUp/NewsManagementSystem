package ru.green.nca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.green.nca.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News,Integer> {
}
