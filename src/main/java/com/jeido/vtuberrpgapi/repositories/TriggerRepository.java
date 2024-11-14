package com.jeido.vtuberrpgapi.repositories;

import com.jeido.vtuberrpgapi.entites.Trigger;
import com.jeido.vtuberrpgapi.entites.keys.VtuberStringCompositeKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TriggerRepository extends CrudRepository<Trigger, VtuberStringCompositeKey> {
    @Query("SELECT t FROM Trigger t WHERE t.id.vtuber.id= :id")
    List<Trigger> findByVtuberId(@Param("id") UUID id);
}
