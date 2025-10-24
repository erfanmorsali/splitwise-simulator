package com.splitwise.application.models.entities.user;


import com.splitwise.application.models.entities.BaseEntity;
import com.splitwise.application.models.entities.group.GroupEntity;
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
@Table(name = "users", schema = "splitwise")
public class UserEntity extends BaseEntity {
    @Id
    @Column(name = "id_pk", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    @SequenceGenerator(name = "users_sequence", sequenceName = "users_sequence")
    private Long id;
    @Column(name = "mobile", length = 11, nullable = false)
    private String mobile;
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    @Column(name = "suspended", columnDefinition = "boolean default false")
    private boolean suspended;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "user_id_fk"),
            inverseJoinColumns = @JoinColumn(name = "group_id_fk"))
    private Set<GroupEntity> groups;
}
