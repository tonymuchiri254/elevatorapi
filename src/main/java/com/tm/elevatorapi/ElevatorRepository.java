package com.tm.elevatorapi;

import com.tm.elevatorapi.model.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElevatorRepository extends JpaRepository<Elevator, Integer> {
}
