//데이터(파일)의 CRUD
package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WiseSayingRepository {
    private final String dbDir = "db/wiseSaying";

    public WiseSayingRepository() {
        File dir = new File(dbDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void clear() {
        File dir = new File("db/wiseSaying");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }

    public ArrayList<WiseSaying> search(String keywordType, String keyword) {
        ArrayList<WiseSaying> allWiseSayings = findAll();
        ArrayList<WiseSaying> filteredList = new ArrayList<>();

        for (WiseSaying w : allWiseSayings) {
            if (keywordType.equals("content")) {
                if (w.content.contains(keyword)) {
                    filteredList.add(w);
                }
            } else if (keywordType.equals("author")) {
                if (w.author.contains(keyword)) {
                    filteredList.add(w);
                }
            }
        }
        return filteredList;
    }

    public int findLastId() {
        File lastIdFile = new File(dbDir + "/lastId.txt");
        if (lastIdFile.exists()) {
            try {
                String lastIdStr = Files.readString(Path.of(lastIdFile.getPath())).trim();
                if (!lastIdStr.isEmpty()) {
                    return Integer.parseInt(lastIdStr);
                }
            }
            catch (IOException e) {
            }
        }
        return 0;
    }

    public void saveLastId(int lastId) {
        try {
            Files.writeString(Path.of(dbDir + "/lastId.txt"), String.valueOf(lastId));
        }
        catch (IOException e) {
        }
    }

    public void save(WiseSaying wiseSaying) {
        try {
            String fileName = dbDir + "/" + wiseSaying.id + ".json";
            Files.writeString(Path.of(fileName), wiseSaying.toJson());
        }
        catch (IOException e) {
        }
    }

    public ArrayList<WiseSaying> findAll() {
        ArrayList<WiseSaying> wiseSayings = new ArrayList<>();
        File dir = new File(dbDir);
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json") && !file.getName().equals("data.json")) {
                    try {
                        String jsonContent = Files.readString(file.toPath());
                        int id = extractInt(jsonContent, "\"id\":");
                        String content = extractString(jsonContent, "\"content\":");
                        String author = extractString(jsonContent, "\"author\":");

                        wiseSayings.add(new WiseSaying(id, content, author));
                    }
                    catch (IOException e) {
                    }
                }
            }
        }
        wiseSayings.sort(Comparator.comparingInt(w -> w.id));
        return wiseSayings;
    }

    public void remove(int id) {
        File targetFile = new File(dbDir + "/" + id + ".json");
        if (targetFile.exists()) {
            targetFile.delete();
        }
    }

    public void build(String jsonStr) {
        try {
            Files.writeString(Path.of(dbDir + "/data.json"), jsonStr);
        }
        catch (IOException e) {
        }
    }

    private int extractInt(String json, String key) {
        Pattern pattern = Pattern.compile(key + "\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private String extractString(String json, String key) {
        Pattern pattern = Pattern.compile(key + "\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}