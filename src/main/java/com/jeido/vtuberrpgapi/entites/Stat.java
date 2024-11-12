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
@IdClass(StatCompositeKey.class)
public class Stat {

    @ManyToOne
    @JoinColumn(name = "vtuber_id")
    @Id
    private Vtuber vtuber;

    @Id
    private String label;

    private String value;

}
