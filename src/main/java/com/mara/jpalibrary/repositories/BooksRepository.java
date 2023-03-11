package com.mara.jpalibrary.repositories;

import com.mara.jpalibrary.models.Book;
import com.mara.jpalibrary.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findAllByOwnerId(int id);
    List<Book> findByTitleContaining(String title);
}
