package com.splitwise.application.repositories.group;

import com.splitwise.application.models.entities.group.CostEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CostRepository extends JpaRepository<CostEntity, Long>, JpaSpecificationExecutor<CostEntity> {

    @Query(value = "select entity from CostEntity entity left join entity.group group left join group.users groupUsers where groupUsers.id = :userId and entity.id = :id and entity.groupId = :groupId and entity.deleted is null ")
    Optional<CostEntity> findByIdAndUserIdAndGroupId(@Param("id") Long id, @Param("userId") Long userId, @Param("groupId") Long groupId);
}
