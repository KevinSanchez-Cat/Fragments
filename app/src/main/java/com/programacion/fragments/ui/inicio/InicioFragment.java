package com.programacion.fragments.ui.inicio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.programacion.fragments.HomeFragment;
import com.programacion.fragments.R;
import com.programacion.fragments.bd.Sql;
import com.programacion.fragments.bd.Sqlite;
import com.programacion.fragments.databinding.FragmentInicioBinding;

public class InicioFragment extends Fragment {

    public static VideoView vidWeb;
    private InicioModel inicioModel;
    private FragmentInicioBinding binding;

    Button btn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inicioModel =
                new ViewModelProvider(this).get(InicioModel.class);

        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
 /*       btn=binding.button;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Sqlite sqlite=new Sqlite(getContext());
                if (sqlite.abrirDB() != null) {

                    sqlite.cerrarDB();
                }
            }
        });*/
        
        vidWeb = binding.vidWeb;
        vidWeb.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.video_3));
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(vidWeb);
        vidWeb.setMediaController(mediaController);
        vidWeb.start();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}