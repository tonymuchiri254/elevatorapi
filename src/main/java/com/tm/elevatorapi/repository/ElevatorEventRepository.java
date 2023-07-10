package com.tm.elevatorapi.repository;

import com.tm.elevatorapi.model.ElevatorEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElevatorEventRepository extends JpaRepository<ElevatorEvent, Integer> {
    ElevatorEvent findLastEvent(int elevatorId);
    ElevatorEvent findCurrentEvent(long now, int elevatorId);
}
