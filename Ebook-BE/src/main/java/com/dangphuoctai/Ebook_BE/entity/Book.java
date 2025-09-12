package com.dangphuoctai.Ebook_BE.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false, unique = true)
    private String title;
    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private int discount;

    @Column(nullable = false, unique = true)
    private String fileUrl;
    @Column(nullable = false)
    private String fileFormat;
    @Column(nullable = false)
    private int fileSize;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();
}
