package com.katalog.kozmetik.catalog.bases;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

import com.katalog.kozmetik.catalog.App;
import com.katalog.kozmetik.catalog.R;
import com.katalog.kozmetik.catalog.about_us.AboutUsActivity;
import com.katalog.kozmetik.catalog.brands.BrandsActivity;
import com.katalog.kozmetik.catalog.catalog.CatalogActivity;
import com.katalog.kozmetik.catalog.registiration.FreeRegisterActivity;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.katalog.kozmetik.catalog.registiration.LoginActivity;
import com.katalog.kozmetik.catalog.registiration.SplashActivity;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Mevcutta aktif olan activity'yi tutar. En son hangi Activity'nin onStart'ina girilmisse bu degiskene o activity set'lenir.
     */
  //  private static BaseActivity sActiveActivity;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    /**
     * Herhangi bir activity'nin kullaniciya gorunur olup olmadigi bilgisini tutar. Ornegin NotificationMessageService sinifinda kullanilir. Push notification geldiginde
     * kullanici uygulamayi kullaniyorsa notification olusturulmaz dialog ile notification gosterilir. Uygulama arkaplandaysa notification olusturulur.
     */
    private static boolean sIsInForeground;

    public Context context;
    public BaseActivity activity;
    private ViewGroup mContentView;
    private ViewGroup mSnackbarAnchor;
    private boolean mIsAlive;
    private boolean mIsRunning;
    private boolean mIsRecreated;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
       // sActiveActivity = this;
        mIsRunning = true;
        mIsAlive = true;
        mContentView = (ViewGroup) findViewById(android.R.id.content);
        mIsRecreated = savedInstanceState != null;

        if (mIsRecreated) {
            restartApp();
        } else {
            if (savedInstanceState != null) {
                setContentView(savedInstanceState.getInt("layout_id"));
            } else if (getLayoutId() != -1) {
                setContentView(getLayoutId());
            }

            initViews();
            initToolbar();
            initMenuFragment();
            defineObjects(savedInstanceState);
            bindEvents();
            setProperties();
            fragmentManager = getSupportFragmentManager();

            mContentView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mContentView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mIsAlive) {
                                onLayoutCreate();
                            }
                        }
                    });
                }
            });
        }
    }
/*    public static BaseActivity getActiveActivity() {
        return sActiveActivity;
    }*/

    public static boolean isInForeground() {
        return sIsInForeground;
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height_bigger));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(new OnMenuItemClickListener() {
           @Override
           public void onMenuItemClick(View clickedView, int position) {
               if (position == 0) {
                   Intent intent = new Intent(context, CatalogActivity.class);
                   intent.putExtra("is_favorite", true);
                   intent.putExtra("brand_position", -1);
                   startActivity(intent);
               } else if (position == 1) {
                   Intent intent = new Intent(context, FreeRegisterActivity.class);
                   startActivity(intent);
               }
               else if (position == 2) {
                   Intent intent = new Intent(context, AboutUsActivity.class);
                   startActivity(intent);
               } else if (position == 3) {
                   Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.menu_invitation_message_title))
                           .setMessage(getString(R.string.menu_invitation_message_content))
                           .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                           .build();
                   startActivityForResult(intent, 5);
               } else if (position == 4) {
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
               } /*else if (position == 5) {
                   Intent intent = new Intent(context, LoginActivity.class);
                   startActivity(intent);
               }*/
           }
       });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("layout_id", getLayoutId());
        super.onSaveInstanceState(outState);
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();



        MenuObject favouritiesMenuObject = new MenuObject("Favoriler");
        favouritiesMenuObject.setResource(R.drawable.ic_heart_menu_100);

        MenuObject registerMenuObject = new MenuObject("Ücretsiz Kayıt");
        registerMenuObject.setResource(R.drawable.ic_register_100_dp);

        MenuObject aboutUsMenuObject = new MenuObject("Hakkımızda");
        aboutUsMenuObject.setResource(R.drawable.ic_about_128_dp);

        MenuObject shareMenuObject = new MenuObject("Uygulamayı Paylaş");
        shareMenuObject.setResource(R.drawable.ic_share_100_dp);

        MenuObject rateUsMenuObject = new MenuObject("Bizi Değerlendirin");
        rateUsMenuObject.setResource(R.drawable.ic_star_menu_100_dp);

   /*     MenuObject loginMenuObject = new MenuObject("Giriş Yap");
        loginMenuObject.setResource(R.drawable.ic_login_menu);*/

        menuObjects.add(favouritiesMenuObject);
        menuObjects.add(registerMenuObject);
        menuObjects.add(aboutUsMenuObject);
        menuObjects.add(shareMenuObject);
        menuObjects.add(rateUsMenuObject);
     //   menuObjects.add(loginMenuObject);

        return menuObjects;
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolBar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
       // sActiveActivity = this;
        sIsInForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        sIsInForeground = false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restartApp();
    }

    public int getLayoutId() {return -1;}

    /**
     * Activity baslatilirken olusturulmak istenen bir view varsa bu metod'da olusturulur. Ya da xml layout'ta olusturulmus bir view
     * activity tarafindaki bir degiskene baglanmak isteniyorsa (findViewById ile vs..) bu metod'da baglanir.
     */
    public void initViews() {
        findSnackbarAnchor();
    }

    /**
     * Activity baslatilirken yapilacak tum degisken tanimlamalari bu metod'da yapilir.
     *
     * @param state
     */
    public void defineObjects(Bundle state) {}

    /**
     * View'lara listener set'lemeleri bu metod'da yapilir.
     */
    public void bindEvents() {}

    /**
     * Activity baslatilirken view'lar uzerinde yapilacak degisiklikler bu metod'da yapilir.
     */
    public void setProperties() {}

    /**
     * Bu metod layout tamamen hazir oldugunda, yani ekrana ilk cizdirildigi anda tetiklenir. Genelde baslangicta gonderilecek API cagrilari icin
     * veya animasyonlari baslatmak icin kullanilir.
     */
    public void onLayoutCreate() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsRunning = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mIsRunning = true;
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void setRunning(boolean val) {
        mIsRunning = val;
    }





    public void replaceFragment(View view, Fragment fragment, boolean addToBackStack) {
        if (!fragment.isAdded()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(view.getId(), fragment);
            if (addToBackStack) {
                ft.addToBackStack(fragment.getClass().getSimpleName());
            }
            ft.commitAllowingStateLoss();
        }
    }



    private void restartApp() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void findSnackbarAnchor() {
        mSnackbarAnchor = (ViewGroup) findViewById(R.id.root);
        if (mSnackbarAnchor == null) {
            mSnackbarAnchor = mContentView;
        }
    }



    public Snackbar showSnackbar(int textResId, int actionResId, View.OnClickListener actionClickListener) {
        Snackbar snackbar = Snackbar.make(mSnackbarAnchor, getResources().getString(textResId), Snackbar.LENGTH_INDEFINITE);
        if (getResources().getString(actionResId) != null) {
            snackbar.setAction(getResources().getString(actionResId), actionClickListener);
        }
        View v = snackbar.getView();
        v.setBackgroundResource(R.color.colorPrimary);
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(ContextCompat.getColor(v.getContext(), android.R.color.white));
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(ContextCompat.getColor(v.getContext(), R.color.black));
        snackbar.show();
        return snackbar;
    }




    public App getApp() {
        return (App) getApplication();
    }




}
