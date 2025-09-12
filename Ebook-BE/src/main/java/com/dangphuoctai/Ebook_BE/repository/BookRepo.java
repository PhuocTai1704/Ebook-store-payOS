package com.dangphuoctai.Ebook_BE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangphuoctai.Ebook_BE.entity.Book;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

}
