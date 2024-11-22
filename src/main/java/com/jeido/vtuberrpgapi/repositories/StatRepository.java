package com.jeido.vtuberrpgapi.repositories;

import com.jeido.vtuberrpgapi.entites.Stat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatRepository extends CrudRepository<Stat, UUID> {
    List<Stat> findByVtuberId(UUID id);
    Optional<Stat> findByVtuberIdAndLabel(UUID id, String label);
    boolean existsByVtuberIdAndLabel(UUID vtuberId, String label);

}

