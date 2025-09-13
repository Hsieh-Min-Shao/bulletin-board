package com.example.bulletin.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "AnnFile")
public class AnnFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "announce_id", nullable = false)
    private Long announceId;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "stored_name", nullable = false)
    private String storedName;
}
