package com.jeido.vtuberrpgapi.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "influences")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatInfluence {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "influence_id")
    private UUID id;

    @ManyToOne
    private Trigger trigger;

    @ManyToOne
    private Stat stat;

    private String value;
    private String operator;
}