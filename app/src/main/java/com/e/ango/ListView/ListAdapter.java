package com.e.ango.ListView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.ango.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<ListData> listData = null;
    private int list_cnt = 0;

    public ListAdapter(ArrayList<ListData> listData) {
        this.listData = listData;
        list_cnt = listData.size();
    }

    @Override
    public int getCount() {
        Log.i("TAG", "getCount");
        return list_cnt;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            final Context context = parent.getContext();
            if(inflater == null){
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // xml파일을 view객체
            }
            //attachToRoot가 true일경우 생성되는 View가 추가될 부모 뷰, attachToRoot가 false일 경우에는 LayoutParams값을 설정해주기 위한 상위 뷰, null로 설정할경우 android:layout_xxxxx값들이 무시됨.
            convertView = inflater.inflate(R.layout.listview_item, parent,false); //
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView_play);
        TextView textTitle = (TextView) convertView.findViewById(R.id.text_user);
        TextView textSummary = (TextView) convertView.findViewById(R.id.textSummary);
        TextView textAddress = (TextView) convertView.findViewById(R.id.textAddress);


        imageView.setImageBitmap(listData.get(position).getBitmap());
        textTitle.setText(listData.get(position).getTitle());
        textSummary.setText(listData.get(position).getSummary());
        textAddress.setText(listData.get(position).getAddress());

        return convertView;
    }
}