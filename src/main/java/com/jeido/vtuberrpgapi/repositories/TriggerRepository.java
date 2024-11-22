package com.jeido.vtuberrpgapi.repositories;

import com.jeido.vtuberrpgapi.entites.Trigger;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TriggerRepository extends CrudRepository<Trigger, UUID> {
    List<Trigger> findByVtuberId(UUID id);

    Optional<Trigger> findByVtuberIdAndLabel(UUID id, String label);

    boolean existsByVtuberIdAndLabel(UUID id, String label);
}
