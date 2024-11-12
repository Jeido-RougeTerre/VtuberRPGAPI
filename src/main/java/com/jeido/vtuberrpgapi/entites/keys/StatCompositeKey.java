package com.jeido.vtuberrpgapi.entites.keys;

import com.jeido.vtuberrpgapi.entites.Vtuber;

import java.io.Serializable;

public class StatCompositeKey implements Serializable {
    private Vtuber vtuber;
    private String label;
}
