package com.bookstore.repository;

import com.bookstore.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u from User u where u.username=?1")
    User findByUsername(String username);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_role(user_id,role_id) VALUE(?1,?2)",nativeQuery = true)
    void addRoleToUser(Long user_id, Long role_id);
    @Query("SELECT u.id FROM User u WHERE u.username=?1")
    Long GetUserIdByUsername(String username);
    @Query(value = "SELECT r.name FROM role r INNER JOIN user_role ur ON r.id = ur.role_id WHERE ur.user_id =?1",nativeQuery = true)
    String[] getRoleOfUser(Long userId);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_role WHERE user_id = ?1", nativeQuery = true)
    void removeRolesFromUser(Long userId);

    User findByEmail(String email);
    User findByResetPassToken(String resetToken);
}
