package datasource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
/**
 * Class for creating a connection to the MariaDB database
 */
public class MariaDBJpaConnection {
    private MariaDBJpaConnection() {
    }
    /**
     * Entity manager factory
     */
    private static EntityManagerFactory emf = null;
    /**
     * Entity manager
     */
    private static EntityManager em = null;
/**
     * Returns the entity manager
     * @return entity manager
     */

    public static EntityManager getInstance() {
        if (em==null) {
            if (emf==null) {
                try {
                    emf = Persistence.createEntityManagerFactory("floorplannerDatasource");
                } catch (org.hibernate.service.spi.ServiceException e) {
                    return null;
                }

            }
            em = emf.createEntityManager();
        }
        return em;
    }
}