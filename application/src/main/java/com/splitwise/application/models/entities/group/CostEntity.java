package com.splitwise.application.models.entities.group;


import com.splitwise.application.models.entities.BaseEntity;
import com.splitwise.application.models.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction(value = "deleted is null")
@Entity
@Table(name = "costs", schema = "splitwise")
public class CostEntity extends BaseEntity {
    @Id
    @Column(name = "id_pk", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "costs_sequence")
    @SequenceGenerator(name = "costs_sequence", sequenceName = "costs_sequence")
    private Long id;
    @Column(name = "creator_id_fk")
    private Long creatorId;
    @Column(name = "group_id_fk")
    private Long groupId;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "title", length = 100)
    private String title;
    @Column(name = "description", length = 200)
    private String description;
    @Column(name = "settled" , columnDefinition = "boolean default false")
    private boolean settled;

    @JoinColumn(name = "creator_id_fk", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity creator;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "user_costs",
            joinColumns = @JoinColumn(name = "cost_id_fk"),
            inverseJoinColumns = @JoinColumn(name = "user_id_fk"))
    private Set<UserEntity> involvedUsers;
    @JoinColumn(name = "group_id_fk", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupEntity group;
}
