package com.splitwise.application.repositories.group;

import com.splitwise.application.models.entities.group.CostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CostRepository extends JpaRepository<CostEntity, Integer> {
}
