package com.example.batteries;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Battery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private int postcode, wattCapacity;

    Battery(String name, int postcode, int wattCapacity) {
        this.name = name;
        this.postcode = postcode;
        this.wattCapacity = wattCapacity;
    }

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getPostcode() {
        return postcode;
    }
    public int getWattCapacity() {
        return wattCapacity;
    }
}
