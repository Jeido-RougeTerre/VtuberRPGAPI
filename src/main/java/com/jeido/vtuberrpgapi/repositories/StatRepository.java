package com.jeido.vtuberrpgapi.repositories;

import com.jeido.vtuberrpgapi.entites.Stat;
import com.jeido.vtuberrpgapi.entites.keys.StatCompositeKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface StatRepository extends CrudRepository<Stat, StatCompositeKey> {
    @Query("SELECT s FROM Stat s WHERE s.id.vtuber.id= :id")
    List<Stat> findByVtuberId(@Param("id") UUID id);
}
