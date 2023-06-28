package com.tm.elevatorapi;

import com.tm.elevatorapi.model.Elevator;
import com.tm.elevatorapi.model.ElevatorEvent;
import com.tm.elevatorapi.model.UserEvent;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class Controller {
    private final UserEventRepository userEventRepository;
    private final ElevatorRepository elevatorRepository;
    private final ElevatorEventRepository elevatorEventRepository;

    public Controller(UserEventRepository userEventRepository, ElevatorRepository elevatorRepository, ElevatorEventRepository elevatorEventRepository) {
        this.userEventRepository = userEventRepository;
        this.elevatorRepository = elevatorRepository;
        this.elevatorEventRepository = elevatorEventRepository;
    }

    @GetMapping("/elevator/call/{elevatorId}")
    private String callElevator(@PathVariable Integer elevatorId, String name, Integer floor) {
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

    @GetMapping("/elevator/status/{elevatorId}")
    private String elevatorStatus(@PathVariable Integer elevatorId) {
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

    @GetMapping("/elevator/status/all")
    private String allElevatorsStatus() {
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
