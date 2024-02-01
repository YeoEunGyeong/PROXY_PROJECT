package com.mycompany.myapp;

public class MemberDto {
    private int seq;
    private String id;
    private String pw;
    private String name;
    private String rank;

    // 생성자
    public MemberDto() {
        // 기본 생성자
    }

    // Getter methods
    public int getSeq() {
        return seq;
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public String getName() {
        return name;
    }

    public String getRank() {
        return rank;
    }

    // Setter methods
    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
