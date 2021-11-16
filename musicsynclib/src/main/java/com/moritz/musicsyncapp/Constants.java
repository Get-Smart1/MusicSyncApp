package com.moritz.musicsyncapp;

import java.io.File;
import java.io.FileFilter;

public class Constants {

    public final static String[] SUPPORTED_MUSIC_FILES = {"WAV"};
    public static FileFilter SUPPORTED_MUSIC_FILES_FILTER () {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                String fileName = file.getName().toLowerCase();
                for (String supportedMusicFile : SUPPORTED_MUSIC_FILES) {
                    if (fileName.endsWith("." + supportedMusicFile))
                        return true;
                }
                 return false;
            }
        };
    }

}
