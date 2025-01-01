package com.jeido.vtuberrpgapi.entites;

import com.jeido.vtuberrpgapi.utils.enums.LicenseType;
import com.jeido.vtuberrpgapi.utils.enums.Performer;
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

    private String title;

    private String thumbnailPath = "no-icon.png";

    private String version = "unknown";

    private String author = "no-author";

    private String contact = "";

    private String reference = "";

    private Performer performer = Performer.ONLY_AUTHOR;

    private boolean allowViolent = false;
    private boolean allowSexual = false;
    private boolean allowCommercial = false;

    private String otherLicenseUrl = "";

    private LicenseType redistribution = LicenseType.REDISTRIBUTION_PROHIBITED;
    private String otherRedistributionLicenseUrl = "";


    @ManyToMany
    @JoinTable(name = "impersonation",
            joinColumns = @JoinColumn(name = "vtuber_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @OneToMany(mappedBy = "vtuber")
    private List<Stat> stats;

    @OneToMany(mappedBy = "vtuber")
    private List<Trigger> triggers;

}
