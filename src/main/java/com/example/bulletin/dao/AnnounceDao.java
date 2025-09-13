package com.example.bulletin.dao;

import com.example.bulletin.entity.Announce;

import java.util.List;

public interface AnnounceDao {
    List<Announce> findAll();

    List<Announce> findAllByPage(int page);

    long countAll();

    Announce findById(Long id);

    void create(Announce announce);

    void update(Announce announce);

    void deleteById(Long id);
}
