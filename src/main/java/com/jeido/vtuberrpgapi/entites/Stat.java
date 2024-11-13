package com.jeido.vtuberrpgapi.entites;

import com.jeido.vtuberrpgapi.entites.keys.StatCompositeKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stat {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "vtuber", column = @Column(name = "vtuber_id", insertable = false, updatable = false)),
            @AttributeOverride(name = "label", column = @Column(name = "label", insertable = false, updatable = false))
    })
    private StatCompositeKey id;

    private String value;

}
