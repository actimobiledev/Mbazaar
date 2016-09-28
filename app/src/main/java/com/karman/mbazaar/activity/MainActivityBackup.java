package com.karman.mbazaar.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.karman.mbazaar.R;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.HashMap;

public class MainActivityBackup extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private AccountHeader headerResult;
    private Drawer result;
    private IProfile profile;
    GoogleApiClient client;

    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        new DrawerBuilder ().withActivity (this).build ();
        client = new GoogleApiClient.Builder (this).addApi (AppIndex.API).build ();

        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById (R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle (getString (R.string.drawer_item_collapsing_toolbar_drawer));

        collapsingToolbarLayout.setTitle (" ");//carefull there should a space between double quote otherwise it wont work

        AppBarLayout appBarLayout = (AppBarLayout) findViewById (R.id.appbar);
        appBarLayout.addOnOffsetChangedListener (new AppBarLayout.OnOffsetChangedListener () {
            boolean isShow = false;
            int scrollRange = - 1;

            @Override
            public void onOffsetChanged (AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == - 1) {
                    scrollRange = appBarLayout.getTotalScrollRange ();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle ("Title");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle (" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        profile = new ProfileDrawerItem ().withName ("Karman Singh").withIcon (getResources ().getDrawable (R.drawable.profile6));

        headerResult = new AccountHeaderBuilder ()
                .withActivity (this)
                .withCompactStyle (false)
                .addProfiles (profile)
                .withSelectionListEnabledForSingleProfile (false)

                .withHeaderBackground (R.drawable.header)
                .withSavedInstance (savedInstanceState)
                .build ();

        result = new DrawerBuilder ()
                .withActivity (this)
                .withAccountHeader (headerResult)
                .withToolbar (toolbar)
                .withFullscreen (true)
                .addDrawerItems (
                        new PrimaryDrawerItem ().withName (R.string.drawer_item_home).withIcon (FontAwesome.Icon.faw_home).withIdentifier (1),
                        new PrimaryDrawerItem ().withName (R.string.drawer_item_free_play).withIcon (FontAwesome.Icon.faw_gamepad),
                        new PrimaryDrawerItem ().withName (R.string.drawer_item_custom).withIcon (FontAwesome.Icon.faw_eye),
                        new SectionDrawerItem ().withName (R.string.drawer_item_section_header),
                        new SecondaryDrawerItem ().withName (R.string.drawer_item_settings).withIcon (FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem ().withName (R.string.drawer_item_help).withIcon (FontAwesome.Icon.faw_question).withEnabled (false),
                        new SecondaryDrawerItem ().withName (R.string.drawer_item_open_source).withIcon (FontAwesome.Icon.faw_github),
                        new SecondaryDrawerItem ().withName (R.string.drawer_item_contact).withIcon (FontAwesome.Icon.faw_bullhorn)
                )
                .withSavedInstance (savedInstanceState)
                .build ();

//        loadBackdrop ();


        result.getActionBarDrawerToggle ().setDrawerIndicatorEnabled (true);


        mDemoSlider = (SliderLayout) findViewById (R.id.slider);

        HashMap<String, String> url_maps = new HashMap<String, String> ();
        url_maps.put ("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put ("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put ("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put ("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

//        HashMap<String, Integer> file_maps = new HashMap<String, Integer> ();
//        file_maps.put ("Hannibal", R.drawable.hannibal);
//        file_maps.put ("Big Bang Theory", R.drawable.bigbang);
//        file_maps.put ("House of Cards", R.drawable.house);
//        file_maps.put ("Game of Thrones", R.drawable.game_of_thrones);

        for (String name : url_maps.keySet ()) {
            TextSliderView textSliderView = new TextSliderView (this);
            // initialize a SliderLayout
            textSliderView
                    .description (name)
                    .image (url_maps.get (name))
                    .setScaleType (BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener (this);

            //add your extra information
            textSliderView.bundle (new Bundle ());
            textSliderView.getBundle ().putString ("extra", name);
            mDemoSlider.addSlider (textSliderView);
        }
        mDemoSlider.setPresetTransformer (SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator (SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation (new DescriptionAnimation ());
        mDemoSlider.setDuration (6000);
        mDemoSlider.addOnPageChangeListener (this);
    }

    private void loadBackdrop () {
//        final ImageView imageView = (ImageView) findViewById (R.id.backdrop);
//        Picasso.with (this).load ("https://unsplash.it/600/300/?random").into (imageView);
//        Glide.with (this).load ("https://unsplash.it/600/300/?random").centerCrop ().into (imageView);
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
        Log.d ("Slider Demo", "Page Changed: " + position);
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


}
