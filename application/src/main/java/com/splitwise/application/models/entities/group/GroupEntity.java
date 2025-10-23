package com.splitwise.application.models.entities.group;


import com.splitwise.application.models.entities.BaseEntity;
import com.splitwise.application.models.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction(value = "deleted is null")
@Entity
@Table(name = "groups", schema = "splitwise")
public class GroupEntity extends BaseEntity {
    @Id
    @Column(name = "id_pk", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groups_sequence")
    @SequenceGenerator(name = "groups_sequence", sequenceName = "groups_sequence")
    private Long id;
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    @Column(name = "description", length = 100, nullable = false)
    private String description;
    @Column(name = "creator_id_fk")
    private Long creatorId;

    @JoinColumn(name = "creator_id_fk", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity creator;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "group_id_fk"),
            inverseJoinColumns = @JoinColumn(name = "user_id_fk"))
    private Set<UserEntity> users;
}
