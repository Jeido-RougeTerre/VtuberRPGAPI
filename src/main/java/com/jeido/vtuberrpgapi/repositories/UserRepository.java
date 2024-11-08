package com.jeido.vtuberrpgapi.repositories;

import com.jeido.vtuberrpgapi.entites.User;
import com.jeido.vtuberrpgapi.entites.Vtuber;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    List<User> findAllByUsernameContainingIgnoreCase(String username);
    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAllByIsAdmin(Boolean isAdmin);

    List<User> findAllByVtubersContains(Vtuber vtuber);

    @Query("SELECT u FROM User u WHERE size(u.vtubers) = :size")
    List<User> findAllByVtubersSize(@Param("size") int size);
    @Query("SELECT u FROM User u WHERE size(u.vtubers) >= :size")
    List<User> findAllByVtubersSizeGreaterThan(@Param("size") int size);
    @Query("SELECT u FROM User u WHERE size(u.vtubers) <= :size")
    List<User> findAllByVtubersSizeLessThan(@Param("size") int size);

    @Query("SELECT u FROM User u ORDER BY size(u.vtubers)")
    List<Vtuber> findAllOrderByVtubersSize();
}
