//사용자의 입력과 관계없는 로직
package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.ArrayList;

public class WiseSayingService {
    private final WiseSayingRepository repository;
    private int lastId;

    public WiseSayingService() {
        this.repository = new WiseSayingRepository();
        this.lastId = repository.findLastId();
    }

    public boolean isValidString(String str) {
        return str.matches("^[a-zA-Z0-9가-힣\\s]*$");
    }

    public ArrayList<WiseSaying> search(String keywordType, String keyword) {
        return repository.search(keywordType, keyword);
    }

    public WiseSaying write(String content, String author) {
        lastId++;
        int id = lastId;

        WiseSaying wiseSaying = new WiseSaying(id, content, author);
        repository.save(wiseSaying);
        repository.saveLastId(lastId);

        return wiseSaying;
    }

    public ArrayList<WiseSaying> findAll() {
        return repository.findAll();
    }

    public WiseSaying findById(int id) {
        ArrayList<WiseSaying> list = repository.findAll();
        for (WiseSaying w : list) {
            if (w.id == id) {
                return w;
            }
        }
        return null;
    }

    public void remove(WiseSaying wiseSaying) {
        repository.remove(wiseSaying.id);
    }

    public void modify(WiseSaying wiseSaying, String content, String author) {
        wiseSaying.content = content;
        wiseSaying.author = author;
        repository.save(wiseSaying);
    }

    public void buildJson() {
        ArrayList<WiseSaying> list = repository.findAll();
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");

        for (int i = 0; i < list.size(); i++) {
            WiseSaying w = list.get(i);
            String indentedJson = w.toJson().replace("\n", "\n  ");
            jsonBuilder.append("  ").append(indentedJson);

            if (i < list.size() - 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\n");
        }

        jsonBuilder.append("]");
        repository.build(jsonBuilder.toString());
    }


}