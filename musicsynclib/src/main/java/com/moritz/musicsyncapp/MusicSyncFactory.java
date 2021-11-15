package com.moritz.musicsyncapp;

import com.moritz.musicsyncapp.controller.playlist.IPlaylistController;
import com.moritz.musicsyncapp.controller.playlist.PlaylistControllerImpl;
import com.moritz.musicsyncapp.model.playlist.IPlaylistProvider;

public class MusicSyncFactory {

    public IPlaylistController getPlaylistProvider ()
    {
        return new PlaylistControllerImpl();
    }

}