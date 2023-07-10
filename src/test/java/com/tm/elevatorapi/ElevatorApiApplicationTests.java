package com.tm.elevatorapi;

import com.tm.elevatorapi.model.Elevator;
import com.tm.elevatorapi.model.ElevatorEvent;
import com.tm.elevatorapi.repository.ElevatorEventRepository;
import com.tm.elevatorapi.repository.ElevatorRepository;
import com.tm.elevatorapi.service.ElevatorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class ElevatorApiApplicationTests {
	@Autowired
	ElevatorService elevatorService;
	@Autowired
	ElevatorRepository elevatorRepository;
	@Autowired
	ElevatorEventRepository elevatorEventRepository;
	String expectedResponse;
	Integer elevatorId=1;
	String name="user";
	Integer floor=5;

	@Test
	void contextLoads() {
	}

	@Test
	void getallelevators(){
		List<Elevator> es = elevatorRepository.findAll();
		String elevatorresponse=elevatorService.allElevatorsStatus();
		if (es.isEmpty()) {
			expectedResponse= "There are no elevators yet";
		} else {
			StringBuilder status = new StringBuilder();
			for (Elevator e : es) {
				status.append(elevatorService.elevatorStatus(e.getId())).append("\n");
			}
			expectedResponse=status.toString();
		}
		Assertions.assertEquals(expectedResponse,elevatorresponse);
	}

	@Test
	void elevatorStatus(){
		String elevatorStatus= elevatorService.elevatorStatus(elevatorId);
		Optional<Elevator> eq = elevatorRepository.findById(elevatorId);
		if (eq.isPresent()) {
			Elevator e = eq.get();
			long now = System.currentTimeMillis();
			ElevatorEvent currentEe = elevatorEventRepository.findCurrentEvent(now, elevatorId);
			ElevatorEvent lastEe = elevatorEventRepository.findLastEvent(elevatorId);

			if (currentEe == null) {
				if (lastEe == null) {
					expectedResponse= "Elevator " + elevatorId + ", '" + e.getName() + "' is at floor " + e.getStart() + " and is idle. It has never been used.";
				} else {
					expectedResponse= "Elevator " + elevatorId + ", '" + e.getName() + "' is currently idle at floor " + lastEe.getDestination() + ".";
				}
			} else {
				expectedResponse= "Elevator " + elevatorId + ",'" + e.getName() + "' is currently going from floor " + currentEe.getOrigin() + " to " + currentEe.getDestination() + ". It is moving " + (currentEe.getDestination() > currentEe.getOrigin() ? "upwards." : "downwards.");
			}
		} else {
			expectedResponse= "There is no elevator numbered " + elevatorId;
		}
		Assertions.assertEquals(expectedResponse,elevatorStatus);
	}
	@Test
	void callelevator(){
		String callelevatorresponse = elevatorService.callElevator(elevatorId,name,floor);
		Optional<Elevator> eq = elevatorRepository.findById(elevatorId);
		if (eq.isPresent()) {
			Elevator e = eq.get();
			long now = System.currentTimeMillis();
			ElevatorEvent currentEe = elevatorEventRepository.findCurrentEvent(now, elevatorId);
			ElevatorEvent lastEe = elevatorEventRepository.findLastEvent(elevatorId);

			if (currentEe == null) {
				if (lastEe == null) {
					expectedResponse= "Elevator " + elevatorId + ", '" + e.getName() + "' is at floor " + e.getStart() + " and is idle. It has never been used.";
				} else {
					expectedResponse= "Elevator " + elevatorId + ", '" + e.getName() + "' is currently idle at floor " + lastEe.getDestination() + ".";
				}
			} else {
				expectedResponse= "Elevator " + elevatorId + ",'" + e.getName() + "' is currently going from floor " + currentEe.getOrigin() + " to " + currentEe.getDestination() + ". It is moving " + (currentEe.getDestination() > currentEe.getOrigin() ? "upwards." : "downwards.");
			}
		} else {
			expectedResponse= "There is no elevator numbered " + elevatorId;
		}
		Assertions.assertEquals(expectedResponse,callelevatorresponse);
	}

}
