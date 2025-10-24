package com.splitwise.application.repositories.user;

import com.splitwise.application.models.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByMobile(String mobile);

    @Query(value = "select entity from UserEntity entity left join entity.groups groups where groups.id = :groupId and entity.suspended is null ")
    List<UserEntity> findByGroupId(Long groupId);

}
