package com.evaluation.repository;


import com.evaluation.entity.Evaluation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationRepository extends CrudRepository<Evaluation, Integer> {

    @Query("from Evaluation where productId=:productId")
    public List<Evaluation> getProductEvaluation(@Param("productId")int productId);

    @Modifying
    @Query("delete Evaluation where userId=:userId")
    public void deleteEvaluationByUser(@Param("userId")int userId);

    @Modifying
    @Query("delete Evaluation where productId=:productId")
    public void deleteEvaluationByProduct(@Param("productId")int productId);
}
