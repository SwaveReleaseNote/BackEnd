package com.swave.releasenotesharesystem.User.repository;

import com.swave.releasenotesharesystem.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    User findByEmailAndProvider(String email, String provider);


}
