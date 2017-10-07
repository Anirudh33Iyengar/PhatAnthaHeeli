package com.example.android.phatanthaheeli;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.phatanthaheeli.utils.TextViewAdapter;

import static android.view.Gravity.CENTER;

public class MainActivity extends AppCompatActivity {

    GridView answer, fills;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        answer= (GridView) findViewById(R.id.gridView1);
        fills= (GridView) findViewById(R.id.gridView2);
        TextView retry= (TextView) findViewById(R.id.retry);
        String[] answerArray={"a","","","","",""};
        String[] fillsArray={"ಕಿ","ವಾ","ತಿ","ಲ್ಮೀ","ಜ","ಯಂ","ರ" };
        answer.setAdapter(new TextViewAdapter(this,answerArray,true,null,null));
        fills.setAdapter(new TextViewAdapter(this,fillsArray,false,answerArray,answer));
        answer.setGravity(CENTER);
        String imgUrl = "http://192.168.43.176/th.png";

        ImageView imageView = (ImageView)findViewById(R.id.clue);

        Glide.with(this).load(imgUrl)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        retry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                reset();
            }
        });

    }
//new push
    public void reset()
    {
        answerArray={"a","","","","",""};
        fillsArray={"ಕಿ","ವಾ","ತಿ","ಲ್ಮೀ","ಜ","ಯಂ","ರ" };
        answer.setAdapter(new TextViewAdapter(this,answerArray,true,null,null));
        fills.setAdapter(new TextViewAdapter(this,fillsArray,false,answerArray,answer));
    }
}
