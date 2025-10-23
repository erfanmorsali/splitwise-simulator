package com.splitwise.application.repositories.group;


import com.splitwise.application.models.entities.group.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long>, JpaSpecificationExecutor<GroupEntity> {

    @Query(value = "select entity from GroupEntity entity left join fetch entity.users left join fetch entity.creator where entity.id = :id")
    Optional<GroupEntity> findGroupByIdAndFetchUsers(@Param("id") Long id);
}
