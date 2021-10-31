package com.samsung.android.sdk.accessory.example.consumer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class Analysis extends Activity {
    TextView TAG, RC1, RV1, RC2, RV2, Item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        TAG = (TextView) findViewById(R.id.TAG);
        RC1 = (TextView) findViewById(R.id.RC1);
        RC2 = (TextView) findViewById(R.id.RC2);
        RV1 = (TextView) findViewById(R.id.RV1);
        RV2 = (TextView) findViewById(R.id.RV2);
        Item = (TextView) findViewById(R.id.item);

        Bundle bundle = getIntent().getExtras();
        String tag_val = bundle.getString("tag_val");
        String rc1_val = bundle.getString("RC1_val");
        String rc2_val = bundle.getString("RC2_val");
        String rv1_val = bundle.getString("RV1_val");
        String rv2_val = bundle.getString("RV2_val");

        TAG.setText(tag_val);
        RC1.setText(rc1_val);
        RV1.setText(rv1_val);
        RC2.setText(rc2_val);
        RV2.setText(rv2_val);
        Item.setText(rc2_val);
    }
}
