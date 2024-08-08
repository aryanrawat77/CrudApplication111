package com.example.SimplestCRUDExample.repo;

import com.example.SimplestCRUDExample.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByStatus(String status, Pageable pageable);

    // Find books published after a certain date with pagination
    Page<Book> findByPublishedDateAfter(LocalDate date, Pageable pageable);

    // Find books by status and published date with pagination
    @Query("SELECT b FROM Book b WHERE b.status = :status AND b.publishedDate >= :publishedDate")
    Page<Book> findByStatusAndPublishedDateAfter(@Param("status") String status, @Param("publishedDate") LocalDate publishedDate, Pageable pageable);


}
