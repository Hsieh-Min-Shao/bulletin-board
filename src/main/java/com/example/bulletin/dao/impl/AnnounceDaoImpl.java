package com.example.bulletin.dao.impl;

import com.example.bulletin.dao.AnnounceDao;
import com.example.bulletin.entity.Announce;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnnounceDaoImpl implements AnnounceDao {

    @Autowired
    private SessionFactory sf;

    private Session s() {
        return sf.getCurrentSession();
    }

    @Override
    public List<Announce> findAll() {
        return s().createQuery("from Announce order by publishDate desc", Announce.class).list();
    }

    @Override
    public List<Announce> findAllByPage(int page) {
        int size = 3;
        int beginPage = (page - 1) * size;
        return s().createQuery("from Announce order by publishDate desc", Announce.class)
                .setFirstResult(beginPage).setMaxResults(size).list();
    }

    @Override
    public long countAll() {
        return s().createQuery("select count(a.id) from Announce a", Long.class)
                .uniqueResult();
    }

    @Override
    public Announce findById(Long id) {
        return s().get(Announce.class, id);
    }

    @Override
    public void create(Announce announce) {
        s().save(announce);
    }

    @Override
    public void update(Announce announce) {
        s().merge(announce);
    }

    @Override
    public void deleteById(Long id) {
        Announce a = s().get(Announce.class, id);
        if (a != null) s().delete(a);
    }
}
