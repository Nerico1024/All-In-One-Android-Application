package com.example.allinoneapp;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ServiceDetails {

    String    Question;
    String    Description;
    String    ControlType ;
    List<String> Choices;
    String    Answer;
    List<String>  ChoicesAnswer;

    public ServiceDetails(String question, String description, String controlType,
                          List<String> choices, String answer, List<String> choicesAnswer ){
        this.Question = question;
        this.Description = description;
        this.ControlType = controlType;
        this.Choices = choices;
        this.Answer = answer;
        this.ChoicesAnswer = choicesAnswer;
    }

    public String getQuestion() { return  Question;}

    public String getDescription() { return  Description;}

    public String getControlType() { return  ControlType;}

    public List<String>  getChoices() { return  Choices;}

    public  String getAnswer() { return Answer; }

    public void setAnswer(String  text){
        Answer = text;
    }

    public List<String>  getChoicesAnswer() { return  ChoicesAnswer;}

    public void setChoicesAnswer(String text, int index)
    {
        ChoicesAnswer.set(index, text);
    }








}
