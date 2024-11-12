package com.jeido.vtuberrpgapi.repositories;

import com.jeido.vtuberrpgapi.entites.Stat;
import com.jeido.vtuberrpgapi.entites.keys.StatCompositeKey;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatRepository extends CrudRepository<Stat, StatCompositeKey> {
    List<Stat> findByVtuberId(UUID id);
    Optional<Stat> findByLabelAndVtuberId(String label, UUID id);
    Boolean existsByLabelAndVtuberId(String label, UUID id);

    @Modifying
    @Query("DELETE FROM Stat s WHERE s.label= :label AND s.vtuber.id= :id")
    Void deleteByLabelAndVtuberId(@Param("label") String label, @Param("id") UUID id);

    @Modifying
    @Query("DELETE FROM Stat s WHERE s.vtuber.id= :id")
    Void deleteByVtuberId(@Param("id") UUID id);
}
