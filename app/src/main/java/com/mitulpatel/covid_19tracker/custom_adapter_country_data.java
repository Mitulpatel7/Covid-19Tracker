package com.mitulpatel.covid_19tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mitulpatel.covid_19tracker.api.CountryData;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

public class custom_adapter_country_data extends RecyclerView.Adapter<custom_adapter_country_data.CountryViewHolder> {


    private Context context;
    private List<CountryData> dataList;
    onCountryClickListener onCountryClickListener;

    public custom_adapter_country_data(Context context, List<CountryData> dataList, custom_adapter_country_data.onCountryClickListener onCountryClickListener) {
        this.context = context;
        this.dataList = dataList;
        this.onCountryClickListener = onCountryClickListener;
    }



    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_country_data_layoout,parent,false);


        return new CountryViewHolder(view,onCountryClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {

            CountryData data = dataList.get(position);

            int cases = Integer.parseInt(data.getCases());

            holder.country_case.setText(NumberFormat.getInstance().format(Integer.parseInt(data.getCases())));
            holder.country_name.setText(data.getCountry());
            holder.sno.setText(String.valueOf(position+1));

            Map<String,String> img = data.getCountryInfo();
        Glide.with(context).load(img.get("flag")).into(holder.country_flag);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void filterlist(List<CountryData> filterlistitem) {
        dataList = filterlistitem;
        notifyDataSetChanged();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView sno , country_name , country_case;
        ImageView country_flag;
        onCountryClickListener onCountryClickListener;


        public CountryViewHolder(@NonNull View itemView , onCountryClickListener onCountryClickListener) {
            super(itemView);
            sno = itemView.findViewById(R.id.sno);
            country_name = itemView.findViewById(R.id.country_name);
            country_case = itemView.findViewById(R.id.country_cases);
            country_flag = itemView.findViewById(R.id.flag);
            this.onCountryClickListener = onCountryClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCountryClickListener.onCountryClick(getAdapterPosition());
        }



    }

    public interface onCountryClickListener{
        void onCountryClick(int position);
    }
}
