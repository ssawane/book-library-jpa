package com.mara.jpalibrary.services;

import com.mara.jpalibrary.models.Book;
import com.mara.jpalibrary.models.Person;
import com.mara.jpalibrary.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(Optional<Integer> page, Optional<Integer> booksPerPage, boolean sort) {
        if (sort) {
            if (page.isPresent() && booksPerPage.isPresent())
                return booksRepository.findAll(PageRequest.of(page.get(),booksPerPage.get(),
                                Sort.by("yearOfProduction"))).getContent();
            else
                return booksRepository.findAll(Sort.by("yearOfProduction"));
        } else {
            if (page.isPresent() && booksPerPage.isPresent())
                return booksRepository.findAll(PageRequest.of(page.get(), booksPerPage.get())).getContent();
        }
        return booksRepository.findAll();
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);

        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public List<Book> findAllByOwnerId(int id) {
        return booksRepository.findAllByOwnerId(id);
    }

    public Optional<Person> getOwnerByBookId(int id) {
        Optional<Book> book = booksRepository.findById(id);
        return book.map(Book::getOwner);
    }

    @Transactional
    public void appointToPerson(Person person, int id) {
        Optional<Book> book = booksRepository.findById(id);
        book.ifPresent(value -> {
            value.setOwner(person);
            value.setOwnedAt(new Date());
        });
    }

    @Transactional
    public void releaseFromPerson(int id) {
        Optional<Book> book = booksRepository.findById(id);
        book.ifPresent(value -> {
            value.setOwner(null);
            value.setOwnedAt(null);
        });
    }

    public List<Book> findByTitleContaining(String titlePart) {
        return booksRepository.findByTitleContaining(titlePart);
    }
}
