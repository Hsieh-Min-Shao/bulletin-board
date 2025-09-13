package com.example.bulletin.dao;

import com.example.bulletin.entity.AnnFile;

import java.util.List;

public interface AnnFileDao {
    void save(AnnFile annFile);

    List<AnnFile> findAllByAnnId(Long id);

    AnnFile findByAnnIdAndFileId(Long annId,Long fileId);

    void delete(AnnFile annFile);

    void deleteByAnnIdAndFileId(Long annId,Long fileId);

}
