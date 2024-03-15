package dao;
import datasource.PostgresDBJpaConnection;
import entity.Settings;
import jakarta.persistence.EntityManager;

public class SettingsDao {
    public void persist(Settings settings) {
        EntityManager em = PostgresDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(settings);
        em.getTransaction().commit();
    }

    public Settings update(Settings settings) {
        EntityManager em = PostgresDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(settings);
        em.getTransaction().commit();
        return settings;
    }

    public void delete(Settings settings) {
        EntityManager em = PostgresDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(settings);
        em.getTransaction().commit();
    }

    public Settings find(int id) {
        EntityManager em = PostgresDBJpaConnection.getInstance();
        return em.find(Settings.class,  id);
    }
}
