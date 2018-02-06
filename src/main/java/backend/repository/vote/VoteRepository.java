package backend.repository.vote;

import backend.model.vote.VoteModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<VoteModel, Long> {
    VoteModel findById(long id);

    VoteModel findByForumPostId(long id);
}