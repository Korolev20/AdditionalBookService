package com.example.second.SecondApp.SecondWebAPI.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;



@Entity
@Table(name = "accountingofbooks")
public class Library {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int bookId;

    @Column(name = "date_taken")
    private LocalDateTime takenBook;

    @Column(name = "date_return")
    private LocalDateTime returnBook;


    public Library(int bookId, LocalDateTime takenBook, LocalDateTime returnBook) {
        this.bookId = bookId;
        this.takenBook = takenBook;
        this.returnBook = returnBook;
    }

    public Library() {

    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getTakenBook() {
        return takenBook;
    }

    public void setTakenBook(LocalDateTime takenBook) {
        this.takenBook = takenBook;
    }

    public LocalDateTime getReturnBook() {
        return returnBook;
    }

    public void setReturnBook(LocalDateTime returnBook) {
        this.returnBook = returnBook;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}