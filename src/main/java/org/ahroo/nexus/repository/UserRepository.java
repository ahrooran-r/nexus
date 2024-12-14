package org.ahroo.nexus.repository;

import org.ahroo.nexus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsUserByEmail(String email);

    @Query("select new User(u.id, u.email, u.password, u.isDeleted, u.isEnabled) from User u where u.email=:email")
    Optional<User> findByEmail(String email);

    @Query("update User u set u.isEnabled=true, u.lastUpdatedOn=CURRENT_TIMESTAMP where u.email=:email")
    @Modifying
    int activateUser(String email);
}
