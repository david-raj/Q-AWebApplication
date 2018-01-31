package backend.model.qa;

import backend.model.qa.common.ForumPost;
import backend.model.user.UserModel;
import backend.model.vote.AnswerVoteModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by Kevin Tan 2018-01-28
 */

@Entity
public class AnswerModel extends ForumPost {

    @OneToOne(cascade = CascadeType.ALL)
    private AnswerVoteModel votes;
    @ManyToOne
    @JsonIgnore
    private QuestionModel question;
    @ManyToOne
    @JsonIgnore
    private UserModel userModel;

    public AnswerModel(QuestionModel question, String replyMessage, String postedTime) {
        super(replyMessage, postedTime);
        this.question = question;
    }

    public AnswerModel() {
        super("", "");
    }

    public void setVotes(AnswerVoteModel votes) {
        this.votes = votes;
    }

    public void setQuestion(QuestionModel question) {
        this.question = question;
    }

    public AnswerVoteModel getVotes() {
        return votes;
    }

    public QuestionModel getQuestion() {
        return question;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
