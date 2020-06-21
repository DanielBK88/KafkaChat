package volfengaut.chatapp.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.api.repository.ISettingsRepository;
import volfengaut.chatapp.api.service.ISettingService;

@Service
public class SettingService extends AbstractDataService implements ISettingService {

    @Setter(onMethod=@__({@Autowired}))
    private ISettingsRepository repository;

    @Override
    public void setFullName(String fullName) {
        checkText(fullName);
        doInTransaction(v -> {
            repository.setFullName(v);
        }, fullName, repository);
    }

    @Override
    public void setMailAddress(String mailAddress) {
        checkText(mailAddress);
        doInTransaction(v -> {
            repository.setMailAddress(v);
        }, mailAddress, repository);
    }

    @Override
    public void setMemory(int memory) {
        doInTransaction(v -> {
            repository.setMemory(v);
        }, memory, repository);
    }

    @Override
    public void setOSName(String osName) {
        checkText(osName);
        doInTransaction(v -> {
            repository.setFullName(v);
        }, osName, repository);
    }

    @Override
    public String getFullName() {
        return doInTransaction(() -> repository.getFullName(), repository);
    }

    @Override
    public String getMailAddress() {
        return doInTransaction(() -> repository.getMailAddress(), repository);
    }

    @Override
    public int getMemory() {
        return doInTransaction(() -> repository.getMemory(), repository);
    }

    @Override
    public String getOSName() {
        return doInTransaction(() -> repository.getOSName(), repository);
    }

}
