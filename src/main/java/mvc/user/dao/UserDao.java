package mvc.user.dao;

import mvc.user.model.po.User;

import java.util.List;

public interface UserDao {
    int addUser(User user);
    int updateUser(Integer id, User user);
    int deleteUser(Integer id);
    User getUserById(Integer id);
    List<User> findAllUsers();
}
