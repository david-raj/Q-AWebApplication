import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Question} from "./question";
import {Observable} from 'rxjs/Observable';
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {Answer} from "./answer";

@Injectable()
export class QuestionsService {

    getQuestionURL = 'http://localhost:8080/questions';
    getSearchURL;
    getTagSearchURL;
    getUpvoteURL;
    getDownvoteURL;
    postAnswerURL: string = null;


    //addQuestionURL = 'http://localhost:8080/user/1/questions';

    private sourceQuestion = new BehaviorSubject<Question>(null);
    currentQuestion = this.sourceQuestion.asObservable();

    constructor(private http: HttpClient) {}

    getQuestions(): Observable<Question[]> {
        return this.http.get<Question[]>(this.getQuestionURL);
    }

    getQuestionsWithURL(URL): Observable<Question[]> {
      return this.http.get<Question[]>(URL);
  }

    setCurrentQuestion(question: Question){
      this.sourceQuestion.next(question);
    }

    addAnswerToQuestion(answer: Answer, questionID: Number): Observable<Answer>{
      this.postAnswerURL = 'http://localhost:8080/user/' + sessionStorage.getItem('id') + '/questions/' + questionID + '/replies';
      return this.http.post<Answer>(this.postAnswerURL, answer);
    }

    searchDashboard(searchTerm) {
      this.getSearchURL = 'http://localhost:8080/questions/search/' + searchTerm;
      return this.getQuestionsWithURL(this.getSearchURL);
    }

    searchTag(tag) {
      this.getTagSearchURL = 'http://localhost:8080/questions/search/' + tag;
      return this.getQuestionsWithURL(this.getTagSearchURL);
    }

    upVoting(userID, questionID){
      this.getUpvoteURL = 'http://localhost:8080/user/' + userID + '/questions/' + questionID + '/upVote';
      return this.http.get(this.getUpvoteURL);
    }

    downVoting(userID, questionID){
      this.getDownvoteURL = 'http://localhost:8080/user/' + userID + '/questions/' + questionID + '/downVote';
      return this.http.get(this.getDownvoteURL);
    }
}
