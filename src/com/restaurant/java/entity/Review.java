package com.restaurant.java.entity;

public class Review {
    private int id;
    private String content;
    private User user;
    private int star;

    public Review(int id, String content, User user, int star) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.star = star;
    }

    public Review(String content, User user, int star) {
        this.content = content;
        this.user = user;
        this.star = star;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public static void tableHeader(){
        System.out.println("+----------------------------------------------------------+");
        System.out.printf("|%-5s|%-30s|%-15s|%-5s|\n","ID","Content","User","Star");
        System.out.println("+----------------------------------------------------------+");
    }

    public void displayData(){
        System.out.printf("|%-5d|%-30s|%-15s|%-5d|\n",this.id,this.content,this.user.getUsername(),this.star);
        System.out.println("+----------------------------------------------------------+");
    }
}
