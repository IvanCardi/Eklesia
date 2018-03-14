package io.eklesia.eklesia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChiesaActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    private Toolbar toolbar;
    private Button pulsanteLocalizza;
    private Button pulsanteChiama;
    private ImageView mProfileImage;
    private int mMaxScrollSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chiesa);
        pulsanteLocalizza=(Button)findViewById(R.id.miachiesa_pulsante_localizza);
        pulsanteChiama=(Button)findViewById(R.id.miachiesa_pulsante_chiama);
        TextView nomeChiesa = (TextView) findViewById(R.id.nome_chiesa_activity);
        TextView indirizzo = (TextView) findViewById(R.id.indirizzo_chiesa_activity);
        TextView email = (TextView) findViewById(R.id.miachiesa_email_textview);
        TextView sito = (TextView) findViewById(R.id.miachiesa_sito_textview);
        TextView congregazione = (TextView) findViewById(R.id.miachiesa_congregazione_textview);
        if(MiaChiesa.getCongregazione()!=null){
            congregazione.setText(MiaChiesa.getCongregazione().getNome());
            congregazione.setVisibility(View.VISIBLE);
        }
        pulsanteLocalizza.setText(MiaChiesa.getIndirizzo());
        pulsanteChiama.setText(MiaChiesa.getTelefono());
        nomeChiesa.setText(MiaChiesa.getNome());
        email.setText(MiaChiesa.getEmail());
        sito.setText(MiaChiesa.getSito());
        //CircleImageView imgProfilo = (CircleImageView) findViewById(R.id.materialup_profile_image);
        //imgProfilo.setImageResource(MiaChiesa.getFoto());
        indirizzo.setText(MiaChiesa.getIndirizzo()+", "+MiaChiesa.getComune().getNome()+", "+MiaChiesa.getComune().getSiglaProvincia());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setElevation(8);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        mProfileImage = (ImageView) findViewById(R.id.materialup_profile_image);
        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();
    }

    private void setupViewPager(ViewPager viewPager) {
        ChiesaViewPagerAdapter adapter = new ChiesaViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new IncontriFragment(), "Incontri");
        adapter.addFragment(new NewsFragment(), "News");
        adapter.addFragment(new PredicheFragment(), "Prediche");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
            pulsanteLocalizza.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
            pulsanteChiama.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;
            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
            pulsanteLocalizza.animate()
                    .scaleY(1).scaleX(1)
                    .start();
            pulsanteChiama.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }
}
