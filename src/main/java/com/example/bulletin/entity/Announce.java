package com.example.bulletin.entity;

import com.example.bulletin.config.validator.AnnValidator;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AnnValidator
@Data
@Entity
@Table(name = "Announce")
public class Announce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "標題不能空白")
    @Size(max = 100, message = "標題長度不可超過 100")
    private String title;
    private String publisher;

    @NotNull(message = "發佈日期必填")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "publish_date")
    private LocalDate publishDate;

    @NotNull(message = "截止日期必填")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "due_date")
    private LocalDate dueDate;

    @NotBlank(message = "內容必填")
    @Lob
    private String content;
}
