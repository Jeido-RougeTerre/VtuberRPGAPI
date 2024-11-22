package com.jeido.vtuberrpgapi.repositories;

import com.jeido.vtuberrpgapi.entites.Stat;
import com.jeido.vtuberrpgapi.entites.StatInfluence;
import com.jeido.vtuberrpgapi.entites.Trigger;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StatInfluenceRepository extends CrudRepository<StatInfluence, UUID> {
    List<StatInfluence> findByTrigger(Trigger trigger);

    List<StatInfluence> findByStat(Stat stat);

    @Query("SELECT si FROM StatInfluence si WHERE si.trigger.vtuber.id= :id")
    List<StatInfluence> findByVtuberId(@Param("id") UUID id);

    boolean existsByTrigger(Trigger trigger);
    boolean existsByStat(Stat stat);

    @Query("SELECT (count(si) > 0) FROM StatInfluence si WHERE si.trigger.vtuber.id = :id")
    boolean existsByVtuberId(@Param("id") UUID id);
}
