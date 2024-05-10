package dao;
import datasource.MariaDBJpaConnection;
import entity.Settings;
import jakarta.persistence.EntityManager;
import view.SettingsSingleton;

/**
 * DAO class for Settings entity
 */
public class SettingsDao {
    private static SettingsSingleton settingsSingleton = SettingsSingleton.getInstance();
    /**
     * Persists settings to the database
     * @param settings settings to be persisted
     */
    public void persist(Settings settings) {
        if(find(1) != null){
            settings.setId(1);
            update(settings);
            return;
        }
        EntityManager em = MariaDBJpaConnection.getInstance();
        if(em == null){
            return;
        }
        em.getTransaction().begin();
        em.persist(settings);
        em.getTransaction().commit();
    }
    /**
     * Updates settings in the database
     * @param settings settings to be updated
     * @return updated settings
     */
    public Settings update(Settings settings) {
        EntityManager em = MariaDBJpaConnection.getInstance();
        if(em == null){
            return settingsSingleton.getSettings();
        }
        em.getTransaction().begin();
        em.merge(settings);
        em.getTransaction().commit();
        return settings;
    }
    /**
     * Finds settings in the database
     * @param id id of the settings to be found
     * @return settings
     */
    public Settings find(int id) {
        EntityManager em = MariaDBJpaConnection.getInstance();
        if(em == null){
            return settingsSingleton.getSettings();
        }
        return em.find(Settings.class,  id);
    }
}
