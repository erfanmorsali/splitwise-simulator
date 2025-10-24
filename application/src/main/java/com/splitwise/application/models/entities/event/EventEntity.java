package com.splitwise.application.models.entities.event;

import com.splitwise.application.models.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events", schema = "splitwise")
public class EventEntity extends BaseEntity {
    @Id
    @Column(name = "id_pk", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "events_sequence")
    @SequenceGenerator(name = "events_sequence", sequenceName = "events_sequence")
    private Long id;
    @Column(name = "topic", length = 100)
    private String topic;
    @Column(name = "failed", columnDefinition = "boolean default false")
    private boolean failed;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Object payload;
}
