package com.example.bulletin.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Announce")
public class Announce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String publisher;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    @Lob
    private String content;

}
