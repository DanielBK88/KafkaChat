package volfengaut.chatapp.api.service;

import volfengaut.chatapp.entity.role.Permisson;
import volfengaut.chatapp.entity.role.UserRole;

/**
 * Сервис по взаимодействию с ролями пользователей
 **/
public interface IRoleService {

    /**
     * Получение роли пользователя по названию
     **/
    UserRole getRoleByName(String name);
    
    /**
     * Удаление роли пользователя
     **/
    boolean deleteUserRole(UserRole role);
    
    /**
     * Добавление новой роли пользователя с названием и разрешениями
     **/
    UserRole addUserRole(String name, Permisson... permissons);
}
