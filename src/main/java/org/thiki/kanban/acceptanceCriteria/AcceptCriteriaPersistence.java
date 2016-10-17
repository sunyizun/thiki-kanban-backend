package org.thiki.kanban.acceptanceCriteria;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xubt on 10/17/16.
 */
@Repository
public interface AcceptCriteriaPersistence {
    Integer addAcceptCriteria(@Param("userName") String userName, @Param("cardId") String cardId, @Param("acceptanceCriteria") AcceptCriteria acceptCriteria);

    AcceptCriteria findById(String id);

    List<AcceptCriteria> loadAcceptanceCriteriasByCardId(@Param("cardId") String cardId);
}