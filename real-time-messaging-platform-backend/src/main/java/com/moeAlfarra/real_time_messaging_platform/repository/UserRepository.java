package com.moeAlfarra.real_time_messaging_platform.repository;

import com.moeAlfarra.real_time_messaging_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByEmailNot(String email);

    boolean existsByEmail(String email);


}
