package com.moritz.musicsyncapp.ui.sessionplaylist;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moritz.musicsyncapp.AndroidMusicSyncFactory;
import com.moritz.musicsyncapp.R;
import com.moritz.musicsyncapp.controller.p2pnetwork.WifiDirectControllerAndroid;
import com.moritz.musicsyncapp.model.session.ISession;
import com.moritz.musicsyncapp.model.track.ITrack;
import com.moritz.musicsyncapp.model.track.LocalAndroidTrack;
import com.moritz.musicsyncapp.ui.selectsongs.SelectSongsActivity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SessionPlaylistFragment extends Fragment {



    private PropertyChangeListener sessionChanged;
    private PropertyChangeListener p2pNetworkChanged;

    private FloatingActionButton addSongsBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_session_playlist, container, false);

        addSongsBtn = view.findViewById(R.id.btn_add_song_to_session_playlist);
        ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == SelectSongsActivity.SUCCESS_CODE) {
                            String[] selectedTracks = o.getData().getStringArrayExtra(SelectSongsActivity.SELECT_SONGS_RESULT);
                            for (int i = 0; i < selectedTracks.length; i++) {
                                ITrack localTrack = LocalAndroidTrack.getByUri(selectedTracks[i], getContext());
                                AndroidMusicSyncFactory.get().getSessionController().getSession().getSessionPlaylist().addTrack(localTrack);
                            }
                        }
                    }
                });
        addSongsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SessionPlaylistFragment.this.getActivity(), SelectSongsActivity.class);
                activityLauncher.launch(intent);
            }
        });

        RecyclerView sessionSongsRV = view.findViewById(R.id.recycler_view_session_playlist_songs);
        sessionSongsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        SessionPlaylistAdapter sessionPlaylistAdapter = new SessionPlaylistAdapter();
        sessionSongsRV.setAdapter(sessionPlaylistAdapter);

        sessionChanged = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ISession session = (ISession) evt.getNewValue();
                        List<ITrack> allTracks = new ArrayList<>();
                        for (int i = 0; i < session.getSessionPlaylist().getTracks().length; i++) {
                            LocalAndroidTrack localAndroidTrack = LocalAndroidTrack.getByUri(session.getSessionPlaylist().getTracks()[i].getUri(), getContext());
                            if(localAndroidTrack != null) {
                                allTracks.add(localAndroidTrack);
                            } else {
                                allTracks.add(session.getSessionPlaylist().getTracks()[i]);
                            }
                        }
                        sessionPlaylistAdapter.setTrackList(allTracks);
                    }
                });

            }
        };
        AndroidMusicSyncFactory.get().getSessionController().addSessionChangeListener(sessionChanged);

        p2pNetworkChanged = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals(WifiDirectControllerAndroid.OWNER_CHANGED_EVENT)) {
                    isOwner((Boolean) evt.getNewValue());
                }
            }
        };
        isOwner(AndroidMusicSyncFactory.get().getNetworkController(null).isOwner());
        AndroidMusicSyncFactory.get().getNetworkController(null).addPropertyChangeListener(p2pNetworkChanged);

        sessionPlaylistAdapter.setTrackList(Arrays.asList(AndroidMusicSyncFactory.get().getSessionController().getSession().getSessionPlaylist().getTracks()));

        return view;
    }

    private void isOwner (boolean isOwner)
    {
        addSongsBtn.setEnabled(isOwner);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AndroidMusicSyncFactory.get().getSessionController().addSessionChangeListener(sessionChanged);
        AndroidMusicSyncFactory.get().getNetworkController(null).removePropertyChangeListener(p2pNetworkChanged);
    }
}