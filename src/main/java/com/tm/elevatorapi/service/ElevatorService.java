package com.tm.elevatorapi.service;

import com.tm.elevatorapi.repository.ElevatorEventRepository;
import com.tm.elevatorapi.repository.ElevatorRepository;
import com.tm.elevatorapi.repository.UserEventRepository;
import com.tm.elevatorapi.model.Elevator;
import com.tm.elevatorapi.model.ElevatorEvent;
import com.tm.elevatorapi.model.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import java.util.Optional;
@Service
public class ElevatorService {

    @Autowired
    ElevatorRepository elevatorRepository;
    @Autowired
    ElevatorEventRepository elevatorEventRepository;
    @Autowired
    UserEventRepository userEventRepository;
    public String callElevator(Integer elevatorId, String name, Integer floor) {
        if (name != null && floor != null) {
            Optional<Elevator> eq = elevatorRepository.findById(elevatorId);
            if (eq.isPresent()) {
                Elevator e = eq.get();
                if (floor >= e.getStart() && floor <= e.getEnd()) {
                    ElevatorEvent ee = elevatorEventRepository.findLastEvent(elevatorId);
                    ElevatorEvent newEe;

                    if (ee == null) {
                        newEe = new ElevatorEvent(elevatorId, e.getStart(), floor, System.currentTimeMillis());
                    } else {
                        if (ee.getDestination() == floor) {
                            return "The elevator is already on it's way to you, it will arrive in " + (ee.getEnd() - System.currentTimeMillis()) / 1000 + " seconds";
                        } else {
                            if (ee.getEnd() < System.currentTimeMillis()) {
                                newEe = new ElevatorEvent(elevatorId, ee.getDestination(), floor, System.currentTimeMillis());
                            } else {
                                newEe = new ElevatorEvent(elevatorId, ee.getDestination(), floor, ee.getEnd());
                            }
                        }
                    }

                    UserEvent ue = new UserEvent(name, elevatorId, floor);
                    elevatorEventRepository.save(newEe);
                    userEventRepository.save(ue);

                    long millis = newEe.getEnd() - System.currentTimeMillis();
                    return "Elevator " + elevatorId + " '" + e.getName() + "' is on it's way to you at floor " + floor + ", " + name + ". It will arrive in " + millis / 1000 + " seconds";
                } else {
                    return "This elevator does not contain the floor " + floor;
                }
            } else {
                return "There is no elevator numbered " + elevatorId;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    public String elevatorStatus(Integer elevatorId) {
        Optional<Elevator> eq = elevatorRepository.findById(elevatorId);
        if (eq.isPresent()) {
            Elevator e = eq.get();
            long now = System.currentTimeMillis();
            ElevatorEvent currentEe = elevatorEventRepository.findCurrentEvent(now, elevatorId);
            ElevatorEvent lastEe = elevatorEventRepository.findLastEvent(elevatorId);

            if (currentEe == null) {
                if (lastEe == null) {
                    return "Elevator " + elevatorId + ", '" + e.getName() + "' is at floor " + e.getStart() + " and is idle. It has never been used.";
                } else {
                    return "Elevator " + elevatorId + ", '" + e.getName() + "' is currently idle at floor " + lastEe.getDestination() + ".";
                }
            } else {
                return "Elevator " + elevatorId + ",'" + e.getName() + "' is currently going from floor " + currentEe.getOrigin() + " to " + currentEe.getDestination() + ". It is moving " + (currentEe.getDestination() > currentEe.getOrigin() ? "upwards." : "downwards.");
            }
        } else {
            return "There is no elevator numbered " + elevatorId;
        }
    }

    public String allElevatorsStatus() {
        List<Elevator> es = elevatorRepository.findAll();
        if (es.isEmpty()) {
            return "There are no elevators yet";
        } else {
            StringBuilder status = new StringBuilder();
            for (Elevator e : es) {
                status.append(elevatorStatus(e.getId())).append("\n");
            }
            return status.toString();
        }
    }

}
