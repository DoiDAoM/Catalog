package com.katalog.kozmetik.catalog.catalog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.katalog.kozmetik.catalog.Api;
import com.katalog.kozmetik.catalog.App;
import com.katalog.kozmetik.catalog.R;
import com.katalog.kozmetik.catalog.bases.BaseActivity;
import com.katalog.kozmetik.catalog.bases.BaseFragment;
import com.katalog.kozmetik.catalog.brands.Brand;
import com.katalog.kozmetik.catalog.brands.Page;
import com.katalog.kozmetik.catalog.registiration.User;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class ViewPagerFragment extends BaseFragment {

    ViewPagerFixed viewPager;
    ViewPagerAdapter viewPagerAdapter;
    ImageView ivRightArrow, ivLeftArrow, ivShareButton;
    View viewLottieClick;
    int brandPosition = -1, pagePosition;
    TextView tvPageNumberPager;
    List<Brand> brandList;
    List<Page> pageList;
    User currentUser;
    LottieAnimationView animationView;



    public static ViewPagerFragment newInstance(int brandPosition, int pagePosition) {
        Bundle bundle = new Bundle();
        bundle.putInt("brand_position", brandPosition);
        bundle.putInt("page_position", pagePosition);

        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catalog_pager, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        ivRightArrow = view.findViewById(R.id.ivRightArrow);
        ivLeftArrow = view.findViewById(R.id.ivLeftArrow);
        tvPageNumberPager = view.findViewById(R.id.tvPageNumberPager);
        viewLottieClick = view.findViewById(R.id.viewLottieClick);
        ivShareButton = view.findViewById(R.id.ivShareButton);
        brandPosition = getArguments().getInt("brand_position");
        pagePosition = getArguments().getInt("page_position");
        brandList = ((BaseActivity)getContext()).getApp().brandList;;
        currentUser = ((BaseActivity)getContext()).getApp().currentUser;

        animationView = view.findViewById(R.id.btnLottieLike);
        animationView.setAnimation("heart_beating.json");

        if (brandPosition != -1) {
            pageList = ((BaseActivity)getContext()).getApp().brandList.get(brandPosition).getPageList();
        } else {
            pageList = ((BaseActivity)getContext()).getApp().currentUser.getFavouriteList();
        }

        animationView.setScale(0.1f);

        if (pageList.size() > 0) {
            if (pageList.get(0).isFavourite()) {
                animationView.setProgress(1);
                animationView.pauseAnimation();
            } else {
                animationView.setProgress(0);
                animationView.pauseAnimation();
            }
        }

        setupListeners();

        viewPagerAdapter = new ViewPagerAdapter(getContext(), brandPosition);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setPageTransformer(true, new DefaultTransformer());
        viewPager.setCurrentItem(pagePosition);
        viewPagerAdapter.notifyDataSetChanged();
        tvPageNumberPager.setText(viewPager.getCurrentItem() + 1 + "/" + pageList.size());

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("wwwww");
        viewPager.setCurrentItem(((BaseActivity)getContext()).getApp().selectedPage);
    }

    private void setupListeners() {
        ivLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewPager.getCurrentItem();

                if (position > 0) {
                    position--;
                    viewPager.setCurrentItem(position);
                    viewPagerAdapter.notifyDataSetChanged();
                    tvPageNumberPager.setText(viewPager.getCurrentItem()+1 + "/" + pageList.size());
                    ((BaseActivity)getContext()).getApp().analyticsHelper.sendEvent(pageList.get(position).getUrl(), "page_swipped_button_left");

                }


            }
        });

        ivRightArrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int position = viewPager.getCurrentItem();
                System.out.println(viewPager.getOffscreenPageLimit() + "mfmf");

                if (position < pageList.size()-1) {
                    position++;
                    viewPager.setCurrentItem(position);
                    viewPagerAdapter.notifyDataSetChanged();
                    tvPageNumberPager.setText(viewPager.getCurrentItem()+1 + "/" + pageList.size());
                    ((BaseActivity)getContext()).getApp().analyticsHelper.sendEvent("brand:" + brandPosition , "page_swipped_button_right");
                }


            }
        });

        ivShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                    return;
                } else {
                    ImageView image = (ImageView) viewPager.findViewWithTag("view" + viewPager.getCurrentItem()).findViewById(R.id.ivViewPager);
                    Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    Intent i = new Intent(Intent.ACTION_SEND);

                    i.setType("image/*");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
        /*compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();*/


                    i.putExtra(Intent.EXTRA_STREAM, getImageUri(bitmap));
                    try {
                        startActivity(Intent.createChooser(i, "My Profile ..."));
                    } catch (android.content.ActivityNotFoundException ex) {

                        ex.printStackTrace();
                    }
                }
            }
        });

        viewLottieClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = viewPager.getCurrentItem();


                if (pageList.get(itemPosition).isFavourite()) {
                    if (pageList.size() == 0) {
                        ((CatalogActivity)getContext()).toolbar.setVisibility(View.GONE);
                        ((CatalogActivity)getContext()).frLayout.setVisibility(View.GONE);
                        ((CatalogActivity)getContext()).cvFavoriesEmpty.setVisibility(View.VISIBLE);
                        ((CatalogActivity)getContext()).tvFavoriesEmpty.setText("Favori listeniz boş.");
                        animationView.setVisibility(View.GONE);
                        animationView.pauseAnimation();
                    } else {
                        animationView.setFrame(0);
                        animationView.pauseAnimation();
                        pageList.get(itemPosition).setFavourite(false);
                        deleteFavouriteRequest(App.BASE_URL_BRANDS + Api.DELETE_FAVORITE + "user_id=" + currentUser.getFirebaseId() + "&page_id="
                                + pageList.get(itemPosition).getId());

                        ((BaseActivity)getContext()).getApp().analyticsHelper.sendEvent(pageList.get(itemPosition).getUrl(), "favorite_deleted");

                    }

                } else {
                    animationView.playAnimation();
                    pageList.get(itemPosition).setFavourite(true);
                    setFavouriteRequest(App.BASE_URL_BRANDS + Api.SET_FAVORITE + "user_id=" + currentUser.getFirebaseId() + "&page_id="
                            + pageList.get(itemPosition).getId());

                    ((BaseActivity)getContext()).getApp().analyticsHelper.sendEvent( "brand:" + brandPosition ,
                            pageList.get(itemPosition).getUrl(), "favorite_added");

                }



            }

        });


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!pageList.get(position).isFavourite()) {
                    animationView.setFrame(0);
                    animationView.pauseAnimation();

                } else {
                    animationView.setFrame(60);
                    animationView.pauseAnimation();
                }
                ((BaseActivity)getContext()).getApp().adHelper.countAd();
                ((BaseActivity)getContext()).getApp().analyticsHelper.sendEvent("brand: " + pagePosition, "page_swipped");
                tvPageNumberPager.setText(viewPager.getCurrentItem() + 1 + "/" + pageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setFavouriteRequest(String url) {
        System.out.println("wwwww " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("wwww2" + response);
                if (getContext() != null) {
                    Toast toast = Toast.makeText(getContext(), "Favorilere eklendi :)", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 128);
                    View view = toast.getView();
                    view.setBackgroundResource(R.drawable.shape_toast_bg);
                    toast.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        ((BaseActivity)getContext()).getApp().queue.add(jsonObjectRequest);

    }

    private void deleteFavouriteRequest(String url) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getContext() != null) {
                    Toast toast = Toast.makeText(getContext(), "Favorilerden çıkarıldı.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 128);
                    View view = toast.getView();
                    view.setBackgroundResource(R.drawable.shape_toast_warning_bg);
                    toast.show();
                }
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        ((BaseActivity)getContext()).getApp().queue.add(jsonObjectRequest);

    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    //Viewpager animations:
       /* TRANSFORM_CLASSES = new ArrayList<>();
        TRANSFORM_CLASSES.add(new TransformerItem(DefaultTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(AccordionTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(BackgroundToForegroundTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(DepthPageTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(FlipHorizontalTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(FlipVerticalTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ForegroundToBackgroundTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(RotateDownTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(RotateUpTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ScaleInOutTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(StackTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(TabletTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ZoomInTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ZoomOutSlideTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(ZoomOutTransformer.class));
        TRANSFORM_CLASSES.add(new TransformerItem(DrawerTransformer.class));*/

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImageView image = viewPagerAdapter.viewHolder.photoView;
                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Intent i = new Intent(Intent.ACTION_SEND);

                i.setType("image/*");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
        /*compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();*/


                i.putExtra(Intent.EXTRA_STREAM, getImageUri(bitmap));
                try {
                    startActivity(Intent.createChooser(i, "My Profile ..."));
                } catch (android.content.ActivityNotFoundException ex) {

                    ex.printStackTrace();
                }
            } else {
                // User refused to grant permission.
            }
        }
    }
}
