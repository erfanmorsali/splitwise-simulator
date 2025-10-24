package com.splitwise.application.models.entities.group;


import com.splitwise.application.models.entities.BaseEntity;
import com.splitwise.application.models.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction(value = "deleted is null")
@Entity
@Table(name = "cost_documents", schema = "splitwise")
public class CostDocumentEntity extends BaseEntity {
    @Id
    @Column(name = "id_pk", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cost_documents_sequence")
    @SequenceGenerator(name = "cost_documents_sequence", sequenceName = "cost_documents_sequence")
    private Long id;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "group_id_fk")
    private Long groupId;
    @Column(name = "cost_id_fk", updatable = false, insertable = false)
    private Long costId;
    @Column(name = "debtor_id_fk")
    private Long debtorId;
    @Column(name = "creditor_id_fk")
    private Long creditorId;


    @JoinColumn(name = "debtor_id_fk", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity debtor;
    @JoinColumn(name = "creditor_id_fk", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity creditor;
    @JoinColumn(name = "group_id_fk", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupEntity group;
    @JoinColumn(name = "cost_id_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    private CostEntity cost;
}
