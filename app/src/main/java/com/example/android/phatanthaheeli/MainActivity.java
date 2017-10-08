package com.example.android.phatanthaheeli;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.phatanthaheeli.utils.ExpandableHeightGridView;
import com.example.android.phatanthaheeli.utils.TextViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.Gravity.CENTER;

public class MainActivity extends AppCompatActivity {

    ExpandableHeightGridView answer, fills;
    ImageView imageView;
    String[] cluesArray;
    int answerLength;
    String questionId;
    RequestQueue queue;
    ProgressBar progress;
    RelativeLayout network_error;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        answer= (ExpandableHeightGridView) findViewById(R.id.gridView1);
        fills= (ExpandableHeightGridView) findViewById(R.id.gridView2);
        answer.setExpanded(true);
        fills.setExpanded(true);
        TextView retry= (TextView) findViewById(R.id.retry);
        imageView = (ImageView)findViewById(R.id.clue);
        progress= (ProgressBar) findViewById(R.id.progress);
        network_error= (RelativeLayout) findViewById(R.id.network_error);
        Button try_again= (Button) findViewById(R.id.try_again);
        progress.setVisibility(View.VISIBLE);
        queue = Volley.newRequestQueue(this);
        retry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                reset();
            }
        });
        getQuestion();
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQuestion();

            }
        });
    }
//new push
    public void reset()
    {
        String[] answerArray=new String[answerLength];
        for(int i=0;i<answerLength;i++)
        {
            answerArray[i]="";
        }
        answer.setAdapter(new TextViewAdapter(this,answerArray,true,null,null));
        fills.setAdapter(new TextViewAdapter(this, Arrays.copyOf(cluesArray, cluesArray.length),false,answerArray,answer));
    }

    public void getQuestion(){
        progress.setVisibility(View.VISIBLE);
        network_error.setVisibility(View.GONE);
        String url = "http://192.168.43.229/pah/getQuestion.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
// TODO Auto-generated method stub
                try {
                    String question=response.getString("question");
                    questionId=response.getString("questionId");
                    id=response.getInt("id");
                    answerLength=response.getInt("answerLength");
                    String[] clues=response.getString("clues").split(",");
                    int id=response.getInt("id");
                    Glide.with(MainActivity.this).load("http://192.168.43.229/pah/images/"+questionId+".jpg")
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                    cluesArray= Arrays.copyOf(clues, clues.length);
                    reset();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progress.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
                network_error.setVisibility(View.VISIBLE);
// TODO Auto-generated method stub
                Log.d("Resp",error.toString());
            }
        });
        queue.add(jsObjRequest);
    }

    public void checkAnswer(final String[] answerArray){
        progress.setVisibility(View.VISIBLE);
        Log.d("answer",TextUtils.join("", answerArray));
        Log.d("answer","here");
        Log.d("answerQID",questionId);
        Log.d("answerID",id+"");
        network_error.setVisibility(View.GONE);
        String url = "http://192.168.43.229/pah/checkAnswer.php";
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
// TODO Auto-generated method stub
                try {
                    JSONObject response=new JSONObject(res);
                    if(response.getBoolean("end"))
                    {
                        Toast.makeText(MainActivity.this, "Congrats!", Toast.LENGTH_SHORT).show();
                    }
                    else if(response.getBoolean("success"))
                   {
                       String question=response.getString("nextQuestion");
                       questionId=response.getString("nextQuestionId");
                       answerLength=response.getInt("answerLength");
                       String[] clues=response.getString("nextClues").split(",");
                       id=response.getInt("nextId");
                       Glide.with(MainActivity.this).load("http://192.168.43.229/pah/images/"+questionId+".jpg")
                               .crossFade()
                               .diskCacheStrategy(DiskCacheStrategy.ALL)
                               .into(imageView);
                       cluesArray= Arrays.copyOf(clues, clues.length);
                       reset();
                   }
                   else {
                       Toast.makeText(MainActivity.this, "Wrong answer", Toast.LENGTH_SHORT).show();
                       reset();
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progress.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
                network_error.setVisibility(View.VISIBLE);
// TODO Auto-generated method stub
                Log.d("Resp",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<>();
                params.put("questionId", questionId);
                params.put("id", String.valueOf(id));
                params.put("answer", TextUtils.join("", answerArray));
                return params;
            }
        };
        queue.add(jsObjRequest);
    }
}
