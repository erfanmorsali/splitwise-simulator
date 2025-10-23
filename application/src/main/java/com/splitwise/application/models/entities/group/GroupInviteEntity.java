package com.splitwise.application.models.entities.group;

import com.splitwise.application.models.entities.BaseEntity;
import com.splitwise.application.models.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction(value = "deleted is null")
@Entity
@Table(name = "group_invites", schema = "splitwise")
public class GroupInviteEntity extends BaseEntity {
    @Id
    @Column(name = "id_pk", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_invites_sequence")
    @SequenceGenerator(name = "group_invites_sequence", sequenceName = "group_invites_sequence")
    private Long id;
    @Column(name = "inviter_id_fk")
    private Long inviterId;
    @Column(name = "invited_id_fk")
    private Long invitedId;
    @Column(name = "group_id_fk")
    private Long groupId;
    @Column(name = "accepted")
    private Boolean accepted;

    @JoinColumn(name = "inviter_id_fk", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity inviter;
    @JoinColumn(name = "invited_id_fk", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity invited;
    @JoinColumn(name = "group_id_fk", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupEntity group;

}
