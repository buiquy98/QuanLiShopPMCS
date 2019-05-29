package com.example.cnpmcs.quanlishoppmcs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class PersonAllAdapter extends BaseAdapter {
    private List<Person> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public PersonAllAdapter(Context aContext, List<Person> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    public List<Person> getListData(){
        return listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Person getItem(int position) {return listData.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_per_list_all, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.pitextView1);
            holder.phoneView = (TextView) convertView.findViewById(R.id.pitextView2);
            holder.typeView = (TextView) convertView.findViewById(R.id.pitextView3);
            holder.addView = (TextView) convertView.findViewById(R.id.pitextView4);
            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        Person person = this.listData.get(position);
        holder.nameView.setText(person.getName());
        holder.phoneView.setText(person.getPhone());
        holder.typeView.setText(person.getType());
        holder.addView.setText(person.getAdd());
        return convertView;
    }

    static class ViewHolder{
        TextView nameView;
        TextView phoneView;
        TextView typeView;
        TextView addView;
    }
}
