package com.karman.mbazaar.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.karman.mbazaar.R;
import com.karman.mbazaar.adapter.ProductsAdapter;
import com.karman.mbazaar.model.Banner;
import com.karman.mbazaar.model.Profile;
import com.karman.mbazaar.util.AppConfigTags;
import com.karman.mbazaar.util.AppConfigURL;
import com.karman.mbazaar.util.Constants;
import com.karman.mbazaar.util.NetworkConnection;
import com.karman.mbazaar.util.Utils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private AccountHeader headerResult;
    private Drawer result;
    Profile profile;
    GoogleApiClient client;
    Toolbar toolbar;


    private SliderLayout mDemoSlider;

    Bundle savedInstanceState;


    List<Banner> bannerList = new ArrayList<> ();

    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private List<Product> productList = new ArrayList<> ();


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView (R.layout.activity_main);

        initView ();
        initData ();
        getBanners ();
        getProfileDetails();

    }

    private void initCards(){

    }

    private void loadBackdrop () {
//        final ImageView imageView = (ImageView) findViewById (R.id.backdrop);
//        Picasso.with (this).load ("https://unsplash.it/600/300/?random").into (imageView);
//        Glide.with (this).load ("https://unsplash.it/600/300/?random").centerCrop ().into (imageView);
    }


    private void initView () {
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        mDemoSlider = (SliderLayout) findViewById (R.id.slider);
        recyclerView = (RecyclerView) findViewById (R.id.recycler_view);
    }

    private void initData () {
        setSupportActionBar (toolbar);
        new DrawerBuilder ().withActivity (this).build ();
        client = new GoogleApiClient.Builder (this).addApi (AppIndex.API).build ();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager (this, 2);
        recyclerView.setLayoutManager (mLayoutManager);
        recyclerView.addItemDecoration (new GridSpacingItemDecoration (2, dpToPx (5), true));
        recyclerView.setItemAnimator (new DefaultItemAnimator ());
        recyclerView.setAdapter (adapter);
    }

    private void getProfileDetails(){
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_GETPROFILEBASIC, true);
            StringRequest strRequest2 = new StringRequest (Request.Method.GET, AppConfigURL.URL_GETPROFILEBASIC,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    profile = new Profile (
                                            jsonObj.getInt (AppConfigTags.USER_ID),
                                            jsonObj.getString (AppConfigTags.USER_FIRST_NAME),
                                            jsonObj.getString (AppConfigTags.USER_LAST_NAME),
                                            jsonObj.getString (AppConfigTags.USER_PROFILE_IMAGE),
                                            jsonObj.getString (AppConfigTags.USER_MOBILE),
                                            jsonObj.getString (AppConfigTags.USER_EMAIL)
                                            );

                                    initDrawer ();
                                } catch (JSONException e) {
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }

                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
/*
                                try {
                                    JSONObject jsonObj = new JSONObject (new String (response.data));
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    Utils.showLog (Log.ERROR, AppConfigTags.ERROR, "" + is_error, true);
                                    Utils.showLog (Log.ERROR, AppConfigTags.MESSAGE, message, true);
                                } catch (JSONException e) {
                                    e.printStackTrace ();
                                }
*/
                            }
                        }
                    }) {

                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put ("api_key", Constants.api_key);
                    params.put ("user_login_key", Constants.user_login_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest2, 30);
        } else {
            Utils.showOkDialog (MainActivity.this, "Seems like there is no internet connection, the app will continue in Offline mode", false);
        }
    }

    private void initDrawer () {


        //initialize and create the image loader logic
        DrawerImageLoader.init (new AbstractDrawerImageLoader () {
            @Override
            public void set (ImageView imageView, Uri uri, Drawable placeholder) {
                Glide.with (imageView.getContext ()).load (uri).placeholder (placeholder).into (imageView);
            }

            @Override
            public void cancel (ImageView imageView) {
                Glide.clear (imageView);
            }

            @Override
            public Drawable placeholder (Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name ().equals (tag)) {
                    return DrawerUIUtils.getPlaceHolder (ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name ().equals (tag)) {
                    return new IconicsDrawable (ctx).iconText (" ").backgroundColorRes (com.mikepenz.materialdrawer.R.color.primary).sizeDp (56);
                } else if ("customUrlItem".equals (tag)) {
                    return new IconicsDrawable (ctx).iconText (" ").backgroundColorRes (R.color.md_white_1000).sizeDp (56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder (ctx, tag);
            }
        });
        headerResult = new AccountHeaderBuilder ()
                .withActivity (this)
                .withCompactStyle (true)
                .addProfiles (new ProfileDrawerItem ()
                        .withName (profile.getFirst_name ())
                        .withIcon (profile.getProfile_image ()))
                .withProfileImagesClickable (false)
                .withPaddingBelowHeader (false)
                .withSelectionListEnabledForSingleProfile (false)
                .withHeaderBackground (R.drawable.header)
                .withSavedInstance (savedInstanceState)
                .build ();

        result = new DrawerBuilder ()
                .withActivity (this)
                .withAccountHeader (headerResult)
                .withToolbar (toolbar)
//                .withItemAnimator (new AlphaCrossFadeAnimator ())
                .withFullscreen (true)
                .addDrawerItems (
                        new PrimaryDrawerItem ().withName (R.string.drawer_item_home).withIcon (FontAwesome.Icon.faw_home).withIdentifier (1),
                        new PrimaryDrawerItem ().withName (R.string.drawer_item_free_play).withIcon (FontAwesome.Icon.faw_gamepad).withIdentifier (2),
                        new PrimaryDrawerItem ().withName (R.string.drawer_item_custom).withIcon (FontAwesome.Icon.faw_eye).withIdentifier (3),
                        new SectionDrawerItem ().withName (R.string.drawer_item_section_header).withIdentifier (4),
                        new SecondaryDrawerItem ().withName (R.string.drawer_item_settings).withIcon (FontAwesome.Icon.faw_cog).withIdentifier (5),
                        new SecondaryDrawerItem ().withName (R.string.drawer_item_help).withIcon (FontAwesome.Icon.faw_question).withEnabled (false),
                        new SecondaryDrawerItem ().withName (R.string.drawer_item_open_source).withIcon (FontAwesome.Icon.faw_github).withIdentifier (6),
                        new SecondaryDrawerItem ().withName (R.string.drawer_item_contact).withIcon (FontAwesome.Icon.faw_bullhorn).withIdentifier (7)
                )
                .withSavedInstance (savedInstanceState)
                .build ();

        result.getActionBarDrawerToggle ().setDrawerIndicatorEnabled (true);
    }

    private void getBanners () {
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_GETBANNERS, true);
            StringRequest strRequest = new StringRequest (Request.Method.GET, AppConfigURL.URL_GETBANNERS,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    JSONArray jsonArrayBanner = jsonObj.getJSONArray (AppConfigTags.BANNERS);
                                    for (int i = 0; i < jsonArrayBanner.length (); i++) {
                                        JSONObject jsonObjectBanner = jsonArrayBanner.getJSONObject (i);
                                        Banner banner = new Banner (
                                                jsonObjectBanner.getInt (AppConfigTags.BANNER_ID),
                                                jsonObjectBanner.getInt (AppConfigTags.PRODUCT_ID),
                                                jsonObjectBanner.getInt (AppConfigTags.CATEGORY_ID),
                                                jsonObjectBanner.getString (AppConfigTags.BANNER_TEXT),
                                                jsonObjectBanner.getString (AppConfigTags.BANNER_IMAGE),
                                                jsonObjectBanner.getString (AppConfigTags.BANNER_TYPE)
                                                );
                                        bannerList.add (i, banner);
                                    }

                                    initSlider ();


                                } catch (JSONException e) {
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                Utils.showLog (Log.ERROR, AppConfigTags.ERROR, new String (response.data), true);
/*
                                try {
                                    JSONObject jsonObj = new JSONObject (new String (response.data));
                                    boolean is_error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    Utils.showLog (Log.ERROR, AppConfigTags.ERROR, "" + is_error, true);
                                    Utils.showLog (Log.ERROR, AppConfigTags.MESSAGE, message, true);
                                } catch (JSONException e) {
                                    e.printStackTrace ();
                                }
*/
                            }
                        }
                    }) {

                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put ("api_key", Constants.api_key);
                    params.put ("user_login_key", Constants.user_login_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }


            };
            Utils.sendRequest (strRequest, 30);
        } else {
            Utils.showOkDialog (MainActivity.this, "Seems like there is no internet connection, the app will continue in Offline mode", false);
        }
    }

    private void initSlider () {
        for(int i=0;i<bannerList.size ();i++){
            Banner banner = bannerList.get (i);
            TextSliderView textSliderView = new TextSliderView (this);
            // initialize a SliderLayout
            textSliderView
                    .description (banner.getText ())
                    .image (banner.getImage_url ())
                    .setScaleType (BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener (this);
            //add your extra information
            textSliderView.bundle (new Bundle ());
            textSliderView.getBundle ().putString ("extra", banner.getText ());
            mDemoSlider.addSlider (textSliderView);
        }

        mDemoSlider.setPresetTransformer (SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator (SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation (new DescriptionAnimation ());
        mDemoSlider.setDuration (6000);
        mDemoSlider.addOnPageChangeListener (this);
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState (outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState (outState);
        super.onSaveInstanceState (outState);
    }

    @Override
    public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected (int position) {
//        Log.d ("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged (int state) {
    }

    @Override
    public void onSliderClick (BaseSliderView slider) {
        Toast.makeText (this, slider.getBundle ().get ("extra") + "", Toast.LENGTH_SHORT).show ();
    }

    @Override
    public void onStart () {
        super.onStart ();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect ();
        Action viewAction = Action.newAction (
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse ("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse ("android-app://com.karman.mbazaar/http/host/path")
        );
        AppIndex.AppIndexApi.start (client, viewAction);
    }

    @Override
    public void onStop () {
        mDemoSlider.stopAutoCycle ();
        super.onStop ();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction (
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse ("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse ("android-app://com.karman.mbazaar/http/host/path")
        );
        AppIndex.AppIndexApi.end (client, viewAction);
        client.disconnect ();
    }


    @Override
    public void onBackPressed () {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen ()) {
            result.closeDrawer ();
        } else {
            super.onBackPressed ();
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration (int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets (Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition (view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx (int dp) {
        Resources r = getResources ();
        return Math.round (TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics ()));
    }

}
