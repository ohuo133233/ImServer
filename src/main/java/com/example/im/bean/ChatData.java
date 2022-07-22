package com.example.im.bean;


public class ChatData {

    private long id;
    private String name;
    private CharType charType;

    private long toID;

    private String message;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CharType getCharType() {
        return charType;
    }

    public void setCharType(CharType charType) {
        this.charType = charType;
    }

    public void setToID(long toID) {
        this.toID = toID;
    }

    public long getToID() {
        return toID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", charType=" + charType +
                ", toID=" + toID +
                ", message='" + message + '\'' +
                '}';
    }
}
