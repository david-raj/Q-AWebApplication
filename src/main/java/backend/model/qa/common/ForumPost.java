package backend.model.qa.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;

/**
 * Created by Kevin Tan 2018-01-30
 */

@Entity
@Inheritance
public abstract class ForumPost {

    @Id
    private long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String postedDate;
    private String message;

    public ForumPost(String message, String postedDate){
        this.message = message;
        this.postedDate = postedDate;
        this.updatedTime = postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
