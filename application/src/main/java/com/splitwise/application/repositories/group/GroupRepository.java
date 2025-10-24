package com.splitwise.application.repositories.group;


import com.splitwise.application.models.dtos.group.BalanceResponse;
import com.splitwise.application.models.entities.group.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long>, JpaSpecificationExecutor<GroupEntity> {

    @Query(value = "select entity from GroupEntity entity left join fetch entity.users left join fetch entity.creator where entity.id = :id")
    Optional<GroupEntity> findGroupByIdAndFetchUsers(@Param("id") Long id);

    @Query(value = """
            with  unsettled AS (SELECT creditor_id_fk AS user_id,
                                       cd.amount      AS credit_amount,
                                       0              AS debit_amount
                                FROM costs c
                                         left join cost_documents cd on c.id_pk = cd.cost_id_fk
                                WHERE c.settled = false
                                  and c.deleted is null
                                  and c.group_id_fk = :groupId
            
                                UNION ALL
            
                                SELECT debtor_id_fk AS user_id,
                                       0            AS credit_amount,
                                       cd.amount    AS debit_amount
                                FROM costs c
                                         left join cost_documents cd on c.id_pk = cd.cost_id_fk
                                WHERE c.settled = false
                                  and c.deleted is null
                                  and c.group_id_fk = :groupId),
                  all_users AS (select ug.user_id_fk as user_id
                                from costs  c
                                left join groups g on c.group_id_fk = g.id_pk
                                left join user_group ug on ug.group_id_fk = g.id_pk
                                where c.group_id_fk = :groupId
                                and c.deleted is null
                                            )
             SELECT u.user_id as "userId",
                    COALESCE(SUM(t.credit_amount), 0)                   as "totalCredit",
                    COALESCE(SUM(t.debit_amount), 0)                   as totalDebit,
                    COALESCE(SUM(t.credit_amount - t.debit_amount), 0) as netBalance
             FROM all_users u
                      LEFT JOIN unsettled t ON u.user_id = t.user_id
             GROUP BY u.user_id
             ORDER BY u.user_id
            """, nativeQuery = true)
    List<BalanceResponse> getBalanceOfGroup(@Param("groupId") Long groupId);
}
