package backend.repository.qa;

import backend.model.qa.AnswerModel;
import backend.repository.qa.common.ForumPostRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created by Kevin Tan 2018-01-30
 */

@Transactional
public interface AnswerRepository extends ForumPostRepository<AnswerModel> {
    Collection<AnswerModel> findByQuestionId(long questionId);
}