package com.example.liars_dice.model.game;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Dice {
    private String name;
    private Integer max;
    private Integer value;

    public Dice(String name, Integer max) {
        this.name = name;
        this.max = max;
        this.value = null;
    }

    public Dice(
            @JsonProperty("name") String name,
            @JsonProperty("max") Integer max,
            @JsonProperty("value") Integer value
    ) {
        this.name = name;
        this.max = max;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getMax() {
        return max;
    }

    public Integer getValue() {
        return value;
    }
}
