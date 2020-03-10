package team.orion.pathetic;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContractsFragment extends Fragment implements View.OnClickListener{


    Button btnCall,btnCall2,btnLocate,btnLocate2,btnProfile,btnProfile2,btnFAQs;
    View rootLayout, cardFace, cardBack;
    View rootLayout2, cardFace2, cardBack2;

    public ContractsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contracts, container, false);

        btnCall = v.findViewById(R.id.Contract_button_call);
        btnCall2 = v.findViewById(R.id.Contract_button_call2);
        btnLocate = v.findViewById(R.id.Contract_button_location);
        btnLocate2 = v.findViewById(R.id.Contract_button_location2);
        btnProfile = v.findViewById(R.id.Contract_button_profile);
        btnProfile2 = v.findViewById(R.id.Contract_button_profile2);
        btnFAQs = v.findViewById(R.id.btn_faqs);




        rootLayout = v.findViewById(R.id.root_layout);
        cardFace = v.findViewById(R.id.card_front);
        cardFace.setOnClickListener(this);
        cardBack = v.findViewById(R.id.card_back);
        cardBack.setOnClickListener(this);

        rootLayout2 = v.findViewById(R.id.root_layout2);
        cardFace2 = v.findViewById(R.id.card_front2);
        cardFace2.setOnClickListener(this);
        cardBack2 = v.findViewById(R.id.card_back2);
        cardBack2.setOnClickListener(this);

        btnCall.setOnClickListener(this);
        btnLocate.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        btnCall2.setOnClickListener(this);
        btnLocate2.setOnClickListener(this);
        btnProfile2.setOnClickListener(this);
        cardFace.setOnClickListener(this);
        cardBack.setOnClickListener(this);
        cardFace2.setOnClickListener(this);
        cardBack2.setOnClickListener(this);
        btnFAQs.setOnClickListener(this);

        FlipCode.cardFace = cardFace;
        FlipCode.cardBack = cardBack;
        FlipCode.rootLayout = rootLayout;
        FlipCode.cardFace2 = cardFace2;
        FlipCode.cardBack2 = cardBack2;
        FlipCode.rootLayout2 = rootLayout2;

        FlipCode.card1_flipped = FlipCode.card2_flipped = false;



        return v;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){



            case R.id.Contract_button_call:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:01719307020"));
                startActivity(callIntent);
                break;

            case R.id.Contract_button_location:

                //String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                String map = "<iframe src=\"https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d1279.3763678919966!2d91.83360599818896!3d24.910844121005102!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x366cc2c39fffc247!2sPuspo+Fashion+House!5e0!3m2!1sen!2sbd!4v1541704454708\" width=\"600\" height=\"450\" frameborder=\"0\" style=\"border:0\" allowfullscreen></iframe>";
                Intent locateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(locateIntent);
                break;

            case R.id.Contract_button_call2:
                Intent callIntent2 = new Intent(Intent.ACTION_CALL);
                callIntent2.setData(Uri.parse("tel:+8801784939769"));
                startActivity(callIntent2);
                break;
            case R.id.Contract_button_location2:
                //String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                String map2 = "https://www.google.com/maps/place/Sadiya+Bedding+%26+Foam+Porda+Gallery/@24.9103524,91.8501919,20z/data=!4m5!3m4!1s0x0:0xa247a9d984b23157!8m2!3d24.9104157!4d91.8504754";
                Intent locateIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(map2));
                startActivity(locateIntent2);
                break;
            case R.id.card_front:
                FlipCode.card1_flipped = true;
                onCardClick(rootLayout);
                break;
            case R.id.card_back:
                FlipCode.card1_flipped = false;
                onCardClick(rootLayout);
                break;
            case R.id.card_front2:
                FlipCode.card2_flipped = true;
                onCardClick2(rootLayout2);
                break;
            case R.id.card_back2:
                FlipCode.card2_flipped = false;
                onCardClick2(rootLayout2);
                break;

        }

    }

    public void onCardClick(View view) {
        flipCard();
    }

    @Override
    public void onDetach() {
        FlipCode.card1_flipped = false;
        FlipCode.card2_flipped = false;
        super.onDetach();
    }

    private void flipCard() {
        FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);

        if (cardFace.getVisibility() == View.GONE) {
            flipAnimation.reverse();
        }
        rootLayout.startAnimation(flipAnimation);
    }

    public void onCardClick2(View view) {
        flipCard2();
    }

    private void flipCard2() {
        FlipAnimation flipAnimation = new FlipAnimation(cardFace2, cardBack2);

        if (cardFace2.getVisibility() == View.GONE) {
            flipAnimation.reverse();
        }
        rootLayout2.startAnimation(flipAnimation);
    }
}
