package com.honda.debrincar.Activities.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.honda.debrincar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaginaAnuncioSolFragment extends Fragment {


    public PaginaAnuncioSolFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pagina_anuncio_sol, container, false);

        return view;
    }

}
