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

    @Column(nullable = false)
    private String originalName; // 原檔名
    @Column(nullable = false)
    private String storedName;   // UUID_原檔名（已消毒）

}
