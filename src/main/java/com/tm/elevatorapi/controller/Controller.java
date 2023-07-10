package com.tm.elevatorapi.controller;

import com.tm.elevatorapi.repository.ElevatorEventRepository;
import com.tm.elevatorapi.repository.ElevatorRepository;
import com.tm.elevatorapi.repository.UserEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.tm.elevatorapi.service.ElevatorService;

@RestController
public class Controller {
    private final UserEventRepository userEventRepository;
    private final ElevatorRepository elevatorRepository;
    private final ElevatorEventRepository elevatorEventRepository;

    @Autowired
    ElevatorService elevatorService = new ElevatorService();

    public Controller(UserEventRepository userEventRepository, ElevatorRepository elevatorRepository, ElevatorEventRepository elevatorEventRepository) {
        this.userEventRepository = userEventRepository;
        this.elevatorRepository = elevatorRepository;
        this.elevatorEventRepository = elevatorEventRepository;
    }
    @GetMapping("/elevator/call/{elevatorId}")
    private String callElevator(@PathVariable Integer elevatorId, String name, Integer floor) {
        return elevatorService.callElevator(elevatorId,name,floor);
    }
    @GetMapping("/elevator/status/{elevatorId}")
    private String elevatorStatus(@PathVariable Integer elevatorId) {
        return elevatorService.elevatorStatus(elevatorId);
    }
    @GetMapping("/elevator/status/all")
    private String allElevatorsStatus() {
       return elevatorService.allElevatorsStatus();
    }
}
