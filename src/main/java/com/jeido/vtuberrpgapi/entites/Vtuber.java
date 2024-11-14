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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vtubers")
public class Vtuber {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "vtuber_id")
    private UUID id;

    private String name;

    @ManyToMany
    @JoinTable(name = "impersonations",
            joinColumns = @JoinColumn(name = "vtuber_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @OneToMany(mappedBy = "id.vtuber")
    private List<Stat> stats;

    @OneToMany(mappedBy = "id.vtuber")
    private List<Trigger> triggers;

}
