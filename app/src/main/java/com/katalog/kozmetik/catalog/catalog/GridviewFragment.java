package com.katalog.kozmetik.catalog.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.katalog.kozmetik.catalog.R;
import com.katalog.kozmetik.catalog.bases.BaseFragment;


public class GridviewFragment extends BaseFragment {

    GridView gvCatalog;
    GridviewAdapter adapter;
    int brandPosition;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gridview_catalog, container, false);

        gvCatalog = view.findViewById(R.id.gvCatalog);
        brandPosition = getArguments().getInt("brand_position");


        adapter = new GridviewAdapter(getActivity(), brandPosition);
        gvCatalog.setAdapter(adapter);

        return view;
    }






}
