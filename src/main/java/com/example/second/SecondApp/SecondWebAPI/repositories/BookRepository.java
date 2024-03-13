package com.example.second.SecondApp.SecondWebAPI.repositories;

import com.example.second.SecondApp.SecondWebAPI.models.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Library, Integer> {

    Library findByBookId(int bookId);
    List <Library> findAllByTakenBookIsNull();
}