package com.avinash.notes;

public class firebasemodel {
    private String tittle;
    private String content;



    public firebasemodel(){

    }

    public  firebasemodel(String tittle,String content){
        this.tittle=tittle;
        this.content=content;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

