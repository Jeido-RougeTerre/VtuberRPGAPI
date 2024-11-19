package com.jeido.vtuberrpgapi.entites;

import com.jeido.vtuberrpgapi.entites.keys.VtuberStringCompositeKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "triggers")
public class Trigger {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "vtuber", column = @Column(name = "vtuber_id", insertable = false, updatable = false)),
            @AttributeOverride(name= "label", column = @Column(name= "label", insertable = false, updatable = false))
    })
    private VtuberStringCompositeKey id;

    @OneToMany(mappedBy = "trigger")
    private List<StatInfluence> influences;
}
