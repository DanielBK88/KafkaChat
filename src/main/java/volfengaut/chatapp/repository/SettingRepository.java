package volfengaut.chatapp.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import volfengaut.chatapp.api.repository.ISettingsRepository;
import volfengaut.chatapp.entity.settings.ApplicationSettings;
import volfengaut.chatapp.entity.settings.DeveloperSettings;
import java.util.List;

@Repository
public class SettingRepository implements ISettingsRepository {

    private EntityManager entityManager;

    @Override
    public void setFullName(String fullName) {
        DeveloperSettings settings = getDeveloperSettings();
        settings.setFullName(fullName);
        entityManager.persist(settings);
    }

    @Override
    public void setMailAddress(String mailAddress) {
        DeveloperSettings settings = getDeveloperSettings();
        settings.setMailAddress(mailAddress);
        entityManager.persist(settings);
    }

    @Override
    public void setMemory(int memory) {
        ApplicationSettings settings = getApplicationSettings();
        settings.setMemory(memory);
        entityManager.persist(settings);
    }

    @Override
    public void setOSName(String osName) {
        ApplicationSettings settings = getApplicationSettings();
        settings.setOsName(osName);
        entityManager.persist(settings);
    }

    @Override
    public String getFullName() {
        return getDeveloperSettings().getFullName();
    }

    @Override
    public String getMailAddress() {
        return getDeveloperSettings().getMailAddress();
    }

    @Override
    public int getMemory() {
        return getApplicationSettings().getMemory();
    }

    @Override
    public String getOSName() {
        return getApplicationSettings().getOsName();
    }
    
    private DeveloperSettings getDeveloperSettings() {
        String queryString =
                "FROM DeveloperSettings";
        Query query = entityManager.createQuery(queryString);
        List results = query.getResultList();
        return results == null || results.isEmpty() ? new DeveloperSettings() : (DeveloperSettings) results.get(0);
    }

    private ApplicationSettings getApplicationSettings() {
        String queryString =
                "FROM ApplicationSettings";
        Query query = entityManager.createQuery(queryString);
        List results = query.getResultList();
        return results == null || results.isEmpty() ? new ApplicationSettings() : (ApplicationSettings) results.get(0);
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
