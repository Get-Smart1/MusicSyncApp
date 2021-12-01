package com.moritz.musicsyncapp;

import android.content.Context;

import androidx.annotation.Nullable;

import com.moritz.musicsyncapp.controller.p2pnetwork.IP2PNetworkController;
import com.moritz.musicsyncapp.controller.p2pnetwork.WifiDirectControllerAndroid;
import com.moritz.musicsyncapp.controller.playlist.IPlaylistController;
import com.moritz.musicsyncapp.controller.playlist.PlaylistControllerAndroidImpl;
import com.moritz.musicsyncapp.controller.sound.ISoundController;

public class AndroidMusicSyncFactory implements IAndroidSyncFactory{

    private static AndroidMusicSyncFactory _instance;



    public static AndroidMusicSyncFactory get ()
    {
        if(_instance == null)
            throw new RuntimeException("not initalized");
        return _instance;
    }

    public static void init (Context context)
    {
        if(_instance != null)
            throw new RuntimeException("Factory already initzilized");
        _instance = new AndroidMusicSyncFactory(context);
    }

    private Context context;
    private ISoundController localSoundController;
    private IP2PNetworkController networkController;

    private AndroidMusicSyncFactory (Context context)
    {
        this.context = context;
    }

    @Override
    public IPlaylistController getPlaylistController() {
        return new PlaylistControllerAndroidImpl(context);
    }

    public static void registerSoundController (ISoundController soundController)
    {
        get().localSoundController = soundController;
    }

    @Nullable
    @Override
    public ISoundController getLocalSoundController() {

        return localSoundController;
    }

    @Override
    public IP2PNetworkController getNetworkController(String s) {
        if(networkController == null)
            networkController = new WifiDirectControllerAndroid(context);
        return networkController;
    }
}
