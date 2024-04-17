package dao;
import datasource.MariaDBJpaConnection;
import entity.Settings;
import jakarta.persistence.EntityManager;

public class SettingsDao {
    public void persist(Settings settings) {
        if(find(1) != null){
            settings.setId(1);
            update(settings);
            return;
        }
        EntityManager em = MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(settings);
        em.getTransaction().commit();
    }

    public Settings update(Settings settings) {
        EntityManager em = MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(settings);
        em.getTransaction().commit();
        return settings;
    }

    public Settings find(int id) {
        EntityManager em = MariaDBJpaConnection.getInstance();
        return em.find(Settings.class,  id);
    }
}
