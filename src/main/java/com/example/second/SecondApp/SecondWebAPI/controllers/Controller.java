package com.example.second.SecondApp.SecondWebAPI.controllers;

import com.example.second.SecondApp.SecondWebAPI.exceptions.BookErrorResponse;
import com.example.second.SecondApp.SecondWebAPI.exceptions.BookNotFoundException;
import com.example.second.SecondApp.SecondWebAPI.exceptions.NoFreeBooksException;
import com.example.second.SecondApp.SecondWebAPI.models.Library;

import com.example.second.SecondApp.SecondWebAPI.services.ServiceLibrary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/freeBook")
@Tag(name = "BookFreeController", description = "Controller for working with book status")
public class Controller {
    private final ServiceLibrary bookService;


    @Autowired
    public Controller(ServiceLibrary bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/createAccountingOfBooks")
    @Operation(summary = "Creating a book")
    public Library createBook(@RequestBody int id) {
        Library newBook = new Library();
        newBook.setBookId(id);
        bookService.createBook(newBook);
        return newBook;

    }

    @PostMapping("/updateStatusBook")
    @Operation(summary = "Changing the book status to “busy”")
    public ResponseEntity<HttpStatus> addTime(@RequestBody Library library) {
        Library newLibrary = bookService.findIdBook(library.getBookId());

        if (newLibrary.getTakenBook() == null) {
            library.setTakenBook(library.getTakenBook());
            library.setReturnBook(library.getReturnBook());
            bookService.updateBook(newLibrary.getId(), library);
        } else {
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PostMapping("/resetTimeBook")
    @Operation(summary = "Changing the book status to “free”")
    public Library resetTime(@RequestBody Library library) {
        Library newLibrary = bookService.findIdBook(library.getBookId());
        bookService.resetTime(newLibrary.getId(), library);
        return newLibrary;
    }


    @GetMapping("/findAllFreeBooks")
    @Operation(summary = "Find All free books")
    public List<Library> getAllFreeBook() {
        return bookService.findAllFreeBooks();
    }

    @PostMapping("updateBookById/{id}")
    @Operation(summary = "Changing books")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id, @RequestBody Library library) {
        bookService.findById(id);
        bookService.updateBook(id, library);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<BookErrorResponse> handlerException(NoFreeBooksException e) {
        BookErrorResponse response = new BookErrorResponse(
                "Свободные книги не найдены",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<BookErrorResponse> handlerException(BookNotFoundException e) {
        BookErrorResponse response = new BookErrorResponse(
                "Book not found",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


}
