package com.example.android.phatanthaheeli.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.android.phatanthaheeli.R;

import java.util.Arrays;

/**
 * Created by Anirudh on 07-10-2017.
 */

public class TextViewAdapter extends BaseAdapter {
    private Context context;
    private final String[] textViewValues;
    private final String[] answerValues;
    GridView answer;
    boolean answerBox;
    public TextViewAdapter(Context context, String[] textViewValues, boolean answerBox, String[] answerValues, GridView answer) {
        this.context = context;
        this.textViewValues = textViewValues;
        this.answerBox=answerBox;
        this.answerValues=answerValues;
        this.answer=answer;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.item, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            if(textViewValues[position]!="" && this.answerBox)
            {
                textView.setBackgroundColor(Color.parseColor("#3F51B5"));
                textView.setTextColor(Color.parseColor("#FFFFFF"));
            }
            textView.setText(textViewValues[position]);
            textView.setTag(position);
            if(!this.answerBox)
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos= (int) v.getTag();
                    setAnswer(answerValues,textViewValues[pos]);
                    textViewValues[pos]="";
                    ((TextView)v).setText("");
                    answer.setAdapter(new TextViewAdapter(context,answerValues,true,null,null));
                    Log.d("answerArray", Arrays.toString(answerValues));
                }
            });
        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    public void setAnswer(String[] arr, String value)
    {
        for(int i=0;i<arr.length;i++)
        {
            if(arr[i].equals(""))
            {
                arr[i]=value;
                Log.d("answerArray", i+" "+value);
                break;
            }

        }
    }

    @Override
    public int getCount() {
        return textViewValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}