package com.jeido.vtuberrpgapi.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "triggers")
public class Trigger {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "trigger_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "vtuber_id")
    private Vtuber vtuber;

    private String label;

    @OneToMany(mappedBy = "trigger")
    private List<StatInfluence> influences;
}
