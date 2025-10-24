package com.splitwise.application.repositories.event;

import com.splitwise.application.models.entities.event.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
