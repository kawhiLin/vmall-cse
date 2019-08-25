package com.evaluation.service;

import com.evaluation.entity.Evaluation;
import com.evaluation.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EvaluationService {
//    public Evaluation getEvaluation(int userId, int productId, String time);
//
    @Autowired
    EvaluationRepository evaluationRepository;

    @Transactional
    public void addEvaluation(Evaluation evaluation){
        evaluationRepository.save(evaluation);
   }


//
//    public boolean deleteEvaluation(int userId,int productId,String time);
//
//    public boolean updateEvaluation(Evaluation evaluation);
//

    @Transactional
    public boolean deleteEvaluationByUser(int userId){
        evaluationRepository.deleteEvaluationByUser(userId);
        return true;
    }

    @Transactional
    public boolean deleteEvaluationByProduct(int productId){
        evaluationRepository.deleteEvaluationByProduct(productId);
        return true;
    }

    public List<Evaluation> getProductEvaluation(int productId){
        return evaluationRepository.getProductEvaluation(productId);
    }
}
