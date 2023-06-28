package com.tm.elevatorapi.model;

import jakarta.persistence.*;

@Entity
@NamedQuery(name = "ElevatorEvent.findCurrentEvent", query = "select e from ElevatorEvent e where e.start <= ?1 and e.end >= ?1 and e.elevator = ?2")
@NamedQuery(name = "ElevatorEvent.findLastEvent", query = "select e from ElevatorEvent e where e.elevator = ?1 order by id desc limit 1")
@Table(name = "elevator_events")
public class ElevatorEvent {
    private int id;
    private int elevator;
    private int origin;
    private int destination;
    private long start;
    private long duration;
    private long end;

    public ElevatorEvent() {
    }

    public ElevatorEvent(int elevator, int origin, int destination, long start) {
        this.elevator = elevator;
        this.origin = origin;
        this.destination = destination;
        this.start = start;
        duration = Math.abs(((((destination - origin) * 5L) + 2) * 1000));
        end = start + duration;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getElevator() {
        return elevator;
    }

    public void setElevator(int elevator) {
        this.elevator = elevator;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
