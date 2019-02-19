package com.katalog.kozmetik.catalog.bases;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public abstract class BaseFragment extends Fragment{

    private ViewGroup vgContainer;

    public Context context;
    public BaseActivity activity;

    private boolean mIsDestroyed;

    public int getLayoutId() { return -1; }

    public void initViews() {}

    public void defineObjects() {}

    public void bindEvents() {}

    public void setProperties() {}

    public void onLayoutCreate() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (BaseActivity) getActivity();
        context = activity;

        if (savedInstanceState != null) {
            vgContainer = (ViewGroup) inflater.inflate((savedInstanceState.getInt("layout_id")), null);
        } else if (getLayoutId() != -1) {
            vgContainer = (ViewGroup) inflater.inflate(getLayoutId(), null);
        }
        initViews();
        defineObjects();
        bindEvents();
        setProperties();
        if (vgContainer != null) {
            ViewTreeObserver observer = vgContainer.getViewTreeObserver();
            if (observer.isAlive()) {
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        vgContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        onLayoutCreate();
                    }
                });
            }
        }
        return vgContainer;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsDestroyed = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("layout_id", getLayoutId());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((BaseActivity) getActivity()).setRunning(true);
    }

    public View findViewById(int id) {
        return vgContainer.findViewById(id);
    }

    public Fragment findFragmentById(int id) {
        return getChildFragmentManager().findFragmentById(id);
    }

    public int getColor(int id) {
        return ContextCompat.getColor(context, id);
    }

    public void finishActivity() {
        activity.finish();
    }

    public boolean isDestroyed() {
        return mIsDestroyed;
    }

}
