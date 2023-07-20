package com.swave.urnr.user.repository;

import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.responsedto.UserListResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    User findByEmailAndProvider(String email, String provider);
    @Query(value = "SELECT user_id,username,department FROM User where is_deleted = false;", nativeQuery = true)
    List<UserListResponseDTO> findAllUser();

}
