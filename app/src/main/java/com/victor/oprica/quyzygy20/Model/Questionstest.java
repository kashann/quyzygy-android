package com.victor.oprica.quyzygy20.Model;

public class Questionstest {

    private String AnswerA,AnswerB,AnswerC,AnswerD, CorrectAnswer,CategorytestId, IsImageQuestion,Questiontest;

    public Questionstest() {
    }

    public Questionstest(String answerA, String answerB, String answerC, String answerD, String correctAnswer, String categorytestId, String isImageQuestion, String question) {
        AnswerA = answerA;
        AnswerB = answerB;
        AnswerC = answerC;
        AnswerD = answerD;
        CorrectAnswer = correctAnswer;
        CategorytestId = categorytestId;
        IsImageQuestion = isImageQuestion;
        Questiontest = question;
    }

    public String getAnswerA() {
        return AnswerA;
    }

    public void setAnswerA(String answerA) {
        AnswerA = answerA;
    }

    public String getAnswerB() {
        return AnswerB;
    }

    public void setAnswerB(String answerB) {
        AnswerB = answerB;
    }

    public String getAnswerC() {
        return AnswerC;
    }

    public void setAnswerC(String answerC) {
        AnswerC = answerC;
    }

    public String getAnswerD() {
        return AnswerD;
    }

    public void setAnswerD(String answerD) {
        AnswerD = answerD;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public String getCategorytestId() {
        return CategorytestId;
    }

    public void setCategorytestId(String categorytestId) {
        CategorytestId = categorytestId;
    }

    public String getIsImageQuestion() {
        return IsImageQuestion;
    }

    public void setIsImageQuestion(String isImageQuestion) {
        IsImageQuestion = isImageQuestion;
    }

    public String getQuestiontest() {
        return Questiontest;
    }

    public void setQuestion(String question) {
        Questiontest = question;
    }
}
