package com.katalog.kozmetik.catalog.registiration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.katalog.kozmetik.catalog.R;
import com.katalog.kozmetik.catalog.bases.BaseActivity;

public class FreeRegisterActivity extends BaseActivity {

    public static String ORIFLAME_URL = "https://tr.oriflame.com/business-opportunity/become-consultant?potentialSponsor=2855555";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_free_register;
    }

    public void openRegisterUrl(View view) {
        String url = ORIFLAME_URL;

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }

}
