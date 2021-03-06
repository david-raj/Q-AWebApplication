import {Component, OnInit} from '@angular/core';
import {Question} from "./question";
import {QuestionsService} from "./questions.service";
import {Answer} from "./answer";
import {ActivatedRoute, Router} from "@angular/router";
import {votes} from "./votes";
import {UserProfileService} from "../user-profile/user-profile.service";
import {User} from "../user-profile/user";

@Component({
  selector: 'app-questions',
  templateUrl: './questions.component.html',
  styleUrls: ['./questions.component.css'],
})
export class QuestionsComponent implements OnInit {

  displayAnswerBox:boolean = (sessionStorage.getItem('status') == 'true');
  currentQuestion: Question;
  currentUser: string;
  editing: Number = 0;
  id: number;
  currentPoster: boolean = false;
  currentUserID: string = sessionStorage.getItem('id');
  answerArray = [];

  constructor(private questionsService: QuestionsService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.id = parseInt(this.route.snapshot.paramMap.get('id'));

    // this code checks if the current user is the same as the poster of the current question
    this.questionsService.getQuestionWithID(this.id).subscribe(currentQuestion => {
      if (currentQuestion.userId == parseInt(sessionStorage.getItem('id'))) {
        this.currentPoster = true;
      }
    });


    if(sessionStorage.getItem('id') != null){
      this.currentUser = sessionStorage.getItem('username');
    }

    this.questionsService.getQuestionWithID(this.id).subscribe(currentQuestion => this.currentQuestion = currentQuestion);

    this.initAnswersAndSort(); // this line stores the answerModel into an arra and sorts it so that the best answer is at index 0
  }

  addAnswer(message: string): void{
    if(!message){return;}

    const newAnswer: Answer = { message } as Answer;
    let userID = parseInt(sessionStorage.getItem('id'));
    this.questionsService.addAnswerToQuestion(newAnswer, this.currentQuestion.id, userID)
      .subscribe(answer => this.currentQuestion.answerModel.push(answer));
  }

  registerButtonClick(){
    this.router.navigate(['/register']);
  }

  loginButtonClick(){
    this.router.navigate(['/login']);
  }

  editQuestion(editedQuestion: string){
    let userID = parseInt(sessionStorage.getItem('id'));
    this.currentQuestion.message = editedQuestion;
    this.questionsService.editingQuestion(userID, this.currentQuestion)
      .subscribe();
    this.editing = 0;
  }


  deleteQuestion(){
    let userID = parseInt(sessionStorage.getItem('id'));
    this.questionsService.deletingQuestion(userID, this.currentQuestion)
      .subscribe(null, null, ()=> {
        this.router.navigate(['/dashboard'])
      });
  }

  editAnswer(answer: Answer, editedAnswer: string){
    let userID = parseInt(sessionStorage.getItem('id'));
    answer.message = editedAnswer;
    this.questionsService.editingAnswer(answer, userID, this.currentQuestion)
      .subscribe();
    this.editing = 0;
  }


  deleteAnswer(answer: Answer){
    let userID = parseInt(sessionStorage.getItem('id'));

    this.questionsService.deletingAnswer(answer, userID, this.currentQuestion)
      .subscribe(null, null, ()=> {
        location.reload();
      });
  }


  upVoteQuestionClick() {
    let userID = sessionStorage.getItem('id');
    let initialVotes: votes = this.currentQuestion.votes;

    this.questionsService.upVotingQuestion(this.currentQuestion, userID)
      .subscribe(value => {
        if (value.votes.upVotes == initialVotes.upVotes) {
          this.questionsService.unVotingQuestion(this.currentQuestion, userID)
            .subscribe(value => this.currentQuestion.votes = value.votes);
        } else {
          this.currentQuestion.votes = value.votes;
        }
      });
  }

  downVoteQuestionClick(){
    let userID = sessionStorage.getItem('id');
    let initialVotes: votes = this.currentQuestion.votes;

    this.questionsService.downVotingQuestion(this.currentQuestion, userID)
      .subscribe(value => {
        if (value.votes.downVotes == initialVotes.downVotes) {
          this.questionsService.unVotingQuestion(this.currentQuestion, userID)
            .subscribe(value => this.currentQuestion.votes = value.votes);
        } else {
          this.currentQuestion.votes = value.votes;
        }
      });
  }

  upVoteAnswerClick(answer: Answer){
    let userID = sessionStorage.getItem('id');
    let initialVotes = answer.votes;
    this.questionsService.upVotingAnswer(answer, this.currentQuestion.id, userID)
      .subscribe(value => {
        if(initialVotes.upVotes == value.votes.upVotes){
          this.questionsService.unVotingAnswer(answer, this.currentQuestion.id, userID)
            .subscribe(value => answer.votes = value.votes);
        }else{
          answer.votes = value.votes;
        }
      });
  }

  downVoteAnswerClick(answer: Answer){
    let userID = sessionStorage.getItem('id');
    let initialVotes = answer.votes;
    this.questionsService.downVotingAnswer(answer, this.currentQuestion.id, userID)
      .subscribe(value => {
        if(initialVotes.downVotes == value.votes.downVotes){
          this.questionsService.unVotingAnswer(answer, this.currentQuestion.id, userID)
            .subscribe(value => answer.votes = value.votes);
        }else{
          answer.votes = value.votes;
        }
      });
  }

  // allows the user to choose the best answer. calls the question service passing the answer to mark as best, the user and poster's ids, then reloads the page
  chooseBestAnswer(answer: Answer) {
    this.questionsService.bestAnswer(answer, this.id, this.currentUserID).subscribe(answer => answer = answer);
    location.reload();
  }

  // stores the answers in and array and moves the best answer to index 0
  initAnswersAndSort() {
    this.questionsService.getQuestionWithID(this.id).subscribe(currentQuestion => {
      this.answerArray = this.currentQuestion.answerModel;

        for(let i=0; i<this.answerArray.length; i++){
          if (this.answerArray[i].id == currentQuestion.bestAnswer) {
            let store = this.answerArray[0];
            this.answerArray[0] = this.answerArray[i];
            this.answerArray[i] = store;
          } 
      }
    });
  }

}
