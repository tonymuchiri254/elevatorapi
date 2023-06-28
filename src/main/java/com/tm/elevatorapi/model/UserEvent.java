package com.tm.elevatorapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_events")
public class UserEvent {
    private int id;
    private String name;
    private int elevatorId;
    private int floor;

    public UserEvent() {
    }

    public UserEvent(String name, int elevatorId, int floor) {
        this.name = name;
        this.elevatorId = elevatorId;
        this.floor = floor;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(int elevatorId) {
        this.elevatorId = elevatorId;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
