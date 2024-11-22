package com.jeido.vtuberrpgapi.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "stat_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "vtuber_id")
    private Vtuber vtuber;

    private String label;

    private String value;

    @OneToMany(mappedBy = "stat")
    private List<StatInfluence> influences;

}
