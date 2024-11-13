package com.jeido.vtuberrpgapi.entites.keys;

import com.jeido.vtuberrpgapi.entites.Vtuber;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class StatCompositeKey implements Serializable {
    @ManyToOne
    @JoinColumn(name = "vtuber_id")
    private Vtuber vtuber;
    private String label;
}
