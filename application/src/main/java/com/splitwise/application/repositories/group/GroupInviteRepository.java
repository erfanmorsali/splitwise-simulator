package com.splitwise.application.repositories.group;


import com.splitwise.application.models.entities.group.GroupInviteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupInviteRepository extends JpaRepository<GroupInviteEntity, Long> {
    Optional<GroupInviteEntity> findFirstByInvitedIdAndGroupId(Long inviteId, Long groupId);
}
