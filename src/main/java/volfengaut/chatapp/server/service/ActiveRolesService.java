package volfengaut.chatapp.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import volfengaut.chatapp.server.api.service.IActiveRolesService;
import volfengaut.chatapp.server.entity.role.UserRole;

import static volfengaut.chatapp.constant.CommonConstants.ADMIN_ROLE_NAME;
import static volfengaut.chatapp.constant.CommonConstants.CHATTER_ROLE_NAME;

@Service
public class ActiveRolesService implements IActiveRolesService {

    private UserRole chatterRole;
    
    private UserRole adminRole;
    
    @Autowired
    private RoleService roleService;
    
    @Override
    public UserRole getChatterRole() {
        if (chatterRole == null) {
            chatterRole = roleService.getRoleByName(CHATTER_ROLE_NAME);
        }
        return chatterRole;
    }

    @Override
    public UserRole getAdminRole() {
        if (adminRole == null) {
            adminRole = roleService.getRoleByName(ADMIN_ROLE_NAME);
        }
        return adminRole;
    }

}
