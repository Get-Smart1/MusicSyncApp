package com.moritz.musicsyncapp.ui.mediacontroller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moritz.musicsyncapp.AndroidMusicSyncFactory;
import com.moritz.musicsyncapp.R;
import com.moritz.musicsyncapp.controller.sound.events.ISoundControllerOnTrackPlayEvent;
import com.moritz.musicsyncapp.model.track.ITrack;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MediaControllerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaControllerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MediaControllerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MediaControllerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaControllerFragment newInstance(String param1, String param2) {
        MediaControllerFragment fragment = new MediaControllerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_controller, container, false);
        AndroidMusicSyncFactory.get().getLocalSoundController().addOnTrackPlayEvent(new ISoundControllerOnTrackPlayEvent() {
            @Override
            public void onTrackPlayEvent(ITrack iTrack) {
                TextView textView = view.findViewById(R.id.textView);
                textView.setText(iTrack.getName());
            }
        });
        return view;
    }
}