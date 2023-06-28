package com.tm.elevatorapi.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Elevator {
    private int id;
    private String name;
    private int start;
    private int end;

    public Elevator() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ColumnDefault("0")
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @ColumnDefault("0")
    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
