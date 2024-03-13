package com.example.second.SecondApp.SecondWebAPI.services;

import com.example.second.SecondApp.SecondWebAPI.exceptions.BookNotFoundException;
import com.example.second.SecondApp.SecondWebAPI.exceptions.NoFreeBooksException;
import com.example.second.SecondApp.SecondWebAPI.repositories.BookRepository;
import com.example.second.SecondApp.SecondWebAPI.models.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServiceLibrary {
    private final BookRepository bookRepository;

    @Autowired
    public ServiceLibrary(BookRepository peopleRepository) {
        this.bookRepository = peopleRepository;
    }

    public void createBook(Library book) {
        book.setTakenBook(null);
        book.setReturnBook(null);
        bookRepository.save(book);
    }

    public Library findIdBook(int id) {
        Library library = bookRepository.findByBookId(id);
        if (library == null) {
            throw new BookNotFoundException();
        } else {
            return library;
        }
    }
    public Optional<Library> findById(int id) {
        Optional<Library> library = bookRepository.findById(id);
        if (library.isEmpty()) {
            throw new BookNotFoundException();
        } else {
            return library;
        }
    }

    public void updateBook(int id, Library book) {
        book.setId(id);
        bookRepository.save(book);
    }

    public void resetTime(int id, Library book) {
        book.setId(id);
        book.setTakenBook(null);
        book.setReturnBook(null);
        bookRepository.save(book);
    }

    public List<Library> findAllFreeBooks() {
        List<Library> foundBook = bookRepository.findAllByTakenBookIsNull();
        if (foundBook.isEmpty()) {
            throw new NoFreeBooksException();
        } else {
            return foundBook;
        }
    }
}
