package com.example.pikalti.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pikalti.Data;
import com.example.pikalti.MainActivity;
import com.example.pikalti.R;
import com.example.pikalti.databinding.ActivityMainBinding;
import com.example.pikalti.databinding.FragmentMusicBinding;

public class MusicFragment extends Fragment {
    private FragmentMusicBinding binding;
    private ListView musicList;
    private Data data;

    public MusicFragment(){

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        musicList = (ListView) view.findViewById(R.id.musicList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Data.musics );
        musicList.setAdapter(adapter);

        return view;
    }
}
