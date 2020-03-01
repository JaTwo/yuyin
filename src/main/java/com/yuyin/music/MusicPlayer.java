package com.yuyin.music;

import java.io.*;
import java.io.File;
import java.util.*;

import com.yuyin.conf.ConfigEntry;
import javazoom.jl.player.Player;


public class MusicPlayer {

    private  boolean isNext = true; //默认播放结构化
    private static Player player = null;
    private static Set<Integer> hasPlayPre = new HashSet<>();
    private static Set<Integer> hasPlayNext = new HashSet<>();

    private static MusicPlayer musicPlayer = null;

    public static MusicPlayer getInstance() {
        if(musicPlayer == null) {
            musicPlayer = new MusicPlayer();
        }
        return musicPlayer;
    }

    private MusicPlayer() {

    }

    public String start(boolean isNext) {
        FileInputStream stream = null;
        int randomKey = 0;
        Random random = new Random();
        String fileName = null;
        if (isNext) {
            if (hasPlayNext.size() == ConfigEntry.JIEGOUHUA_COUNT) {
                hasPlayNext.clear();
            }
            randomKey = random.nextInt(ConfigEntry.JIEGOUHUA_COUNT) + 1;
            while (hasPlayNext.contains(randomKey)) {
                randomKey = random.nextInt(ConfigEntry.JIEGOUHUA_COUNT) + 1;
            }
            hasPlayNext.add(randomKey);
            fileName = ConfigEntry.FILE_OUT_PATH + "next//" + "next" + randomKey + ConfigEntry.OUT_TYPE;
        } else {
            if (hasPlayPre.size() == ConfigEntry.ENGLISH_COUNT) {
                hasPlayPre.clear();
            }
            randomKey = random.nextInt(ConfigEntry.ENGLISH_COUNT) + 1;
            while (hasPlayPre.contains(randomKey)) {
                randomKey = random.nextInt(ConfigEntry.ENGLISH_COUNT) + 1;
            }
            hasPlayPre.add(randomKey);
            fileName = ConfigEntry.FILE_OUT_PATH + "pre//" + "pre" + randomKey + ConfigEntry.OUT_TYPE;
        }
        try {
            stream = new FileInputStream(new File(fileName));
            player = new Player(stream);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }

    public void stop() {
        if(player != null) {
            player.close();
        }
    }
}
