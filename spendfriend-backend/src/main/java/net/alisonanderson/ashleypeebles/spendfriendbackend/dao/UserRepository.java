package net.alisonanderson.ashleypeebles.spendfriendbackend.dao;

import net.alisonanderson.ashleypeebles.spendfriendbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    long findIdByUsername(String username);

    User findById(long id);

    List<User> findAll();
}
