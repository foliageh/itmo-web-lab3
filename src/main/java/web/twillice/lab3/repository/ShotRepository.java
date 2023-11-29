package web.twillice.lab3.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import web.twillice.lab3.model.Shot;

import java.io.Serializable;
import java.util.List;

@Stateless
public class ShotRepository implements Serializable {
    @PersistenceContext
    private EntityManager db;

    public Shot create(Shot shot) {
        Shot newShot = new Shot(shot);
        db.persist(newShot);
        return newShot;
    }

    public List<Shot> findAll() {
        return db.createQuery("from Shot", Shot.class).getResultList();
    }
}
