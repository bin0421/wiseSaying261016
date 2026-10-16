//명언 객체 정보
package com.back.domain.wiseSaying.entity;

public class WiseSaying {
    public int id;
    public String content;
    public String author;

    public WiseSaying(int id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public String toJson() {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"content\": \"" + content + "\",\n" +
                "  \"author\": \"" + author + "\"\n" +
                "}";
    }
}