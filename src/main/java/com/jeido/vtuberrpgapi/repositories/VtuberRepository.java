package com.jeido.vtuberrpgapi.repositories;

import com.jeido.vtuberrpgapi.entites.User;
import com.jeido.vtuberrpgapi.entites.Vtuber;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VtuberRepository extends CrudRepository<Vtuber, UUID> {
    List<Vtuber> findAllByNameContainingIgnoreCase(String name);
    List<Vtuber> findAllByUsersContaining(User user);

    @Query("SELECT v FROM Vtuber v WHERE size(v.users) = :size")
    List<Vtuber> findAllByUsersSize(@Param("size") int size);

    @Query("SELECT v FROM Vtuber v WHERE size(v.users) >= :size")
    List<Vtuber> findAllByUsersSizeGreaterThan(@Param("size") int size);

    @Query("SELECT v FROM Vtuber v WHERE size(v.users) <= :size")
    List<Vtuber> findAllByUsersSizeLessThan(@Param("size") int size);

    @Query("SELECT v FROM Vtuber v ORDER BY size(v.users)")
    List<Vtuber> findAllOrderByUsersSize();
}
