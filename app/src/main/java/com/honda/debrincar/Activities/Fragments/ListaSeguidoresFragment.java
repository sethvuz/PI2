package com.honda.debrincar.Activities.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.honda.debrincar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaSeguidoresFragment extends Fragment {
    private static final String TAG = "ListaSeguidoresFragment";

    public ListaSeguidoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_seguidores, container, false);

        Toolbar mToolbar = view.findViewById(R.id.toolbar_com_backbtn);
        ImageView backBtn = mToolbar.findViewById(R.id.back_btn_appbar);
        TextView toolbarTitulo = mToolbar.findViewById(R.id.titulo_appbar_backbtn);
        toolbarTitulo.setText("Seguidores");


        return view;
    }

}
