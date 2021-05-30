package com.son.soccertemple;

class User {
    String name;
    String score, result, number;

    public User() {}

    public User(String name, String score, String result, String number) {
        this.name = name;
        this.score = score;
        this.result = result;
        this.number = number;
    }

    public int getScore() {

        return Integer.parseInt(this.score);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String toString() {
        return String.format("%-" + 25 + "s", name) + String.format("%-" + 23 + "s", score) + result + "/" + number;
    }
}
