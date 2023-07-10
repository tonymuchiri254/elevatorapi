package com.tm.elevatorapi.repository;

import com.tm.elevatorapi.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Integer> {
}
