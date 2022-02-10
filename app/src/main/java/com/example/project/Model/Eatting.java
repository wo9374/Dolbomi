package com.example.project.Model;

public class Eatting {
    public String nowtime;
    public String firstImageUrl;
    public String secondImageUrl;
    public String thirdImageUrl;
    public String first;
    public String second;
    public String third;

    public Eatting() {
    }

    public Eatting(String nowtime, String firstImageUrl, String secondImageUrl, String thirdImageUrl, String first, String second, String third) {
        this.nowtime = nowtime;
        this.firstImageUrl = firstImageUrl;
        this.secondImageUrl = secondImageUrl;
        this.thirdImageUrl = thirdImageUrl;
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public String getNowtime() {
        return nowtime;
    }

    public void setNowtime(String nowtime) {
        this.nowtime = nowtime;
    }

    public String getFirstImageUrl() {
        return firstImageUrl;
    }

    public void setFirstImageUrl(String firstImageUrl) {
        this.firstImageUrl = firstImageUrl;
    }

    public String getSecondImageUrl() {
        return secondImageUrl;
    }

    public void setSecondImageUrl(String secondImageUrl) {
        this.secondImageUrl = secondImageUrl;
    }

    public String getThirdImageUrl() {
        return thirdImageUrl;
    }

    public void setThirdImageUrl(String thirdImageUrl) {
        this.thirdImageUrl = thirdImageUrl;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }
}
