package com.example.bulletin.dao.impl;

import com.example.bulletin.dao.AnnFileDao;
import com.example.bulletin.entity.AnnFile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnnFileDaoImpl implements AnnFileDao {

    @Autowired
    private SessionFactory sf;

    private Session s() {
        return sf.getCurrentSession();
    }

    @Override
    public void save(AnnFile annFile) {
        s().merge(annFile);
    }

    @Override
    public List<AnnFile> findAllByAnnId(Long annId) {
        return s().createQuery(
                        "from AnnFile f where f.announceId = :annId order by f.id ",
                        AnnFile.class)
                .setParameter("annId", annId)
                .getResultList();
    }

    @Override
    public AnnFile findByAnnIdAndFileId(Long annId, Long fileId) {
        return s().createQuery(
                        "from AnnFile f where f.id = :fileId and f.announceId = :annId", AnnFile.class)
                .setParameter("fileId", fileId)
                .setParameter("annId", annId)
                .uniqueResult();
    }

    @Override
    public void delete(AnnFile annFile) {
        if (annFile != null) s().delete(annFile);
    }

    @Override
    public void deleteByAnnIdAndFileId(Long annId, Long fileId) {
        AnnFile annFile = s().createQuery(
                        "from AnnFile f where f.id = :fileId and f.announceId = :annId", AnnFile.class)
                .setParameter("fileId", fileId)
                .setParameter("annId", annId)
                .uniqueResult();
        if (annFile != null) s().delete(annFile);
    }

}

