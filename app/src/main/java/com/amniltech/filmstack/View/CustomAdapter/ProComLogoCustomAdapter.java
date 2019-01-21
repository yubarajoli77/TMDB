package com.amniltech.filmstack.View.CustomAdapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amniltech.filmstack.R;
import com.amniltech.filmstack.RetrofitService.ApiServiceClient;
import com.amniltech.filmstack.RoomDatabase.Entity.ProductionCompanyEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProComLogoCustomAdapter extends RecyclerView.Adapter<ProComLogoCustomAdapter.ProComLogoViewHolder> {
    List<ProductionCompanyEntity> companyEntityList = new ArrayList<>();
    @NonNull
    @Override
    public ProComLogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_produc_comp_item_row,parent,false);

        return new ProComLogoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProComLogoViewHolder holder, int position) {
        ProductionCompanyEntity companyEntity = companyEntityList.get(position);

        if(companyEntity.getLogoPath()!=null)
            Picasso.get().load(ApiServiceClient.POSTER_IMAGE_BASE_URL+companyEntity.getLogoPath()).into(holder.ivCompanyLogo);

    }

    @Override
    public int getItemCount() {
        return companyEntityList.size();
    }

    class ProComLogoViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivCompanyLogo;
        public ProComLogoViewHolder(View itemView) {
            super(itemView);
            ivCompanyLogo = itemView.findViewById(R.id.iv_produc_comp_logo);
        }
    }

    public void setData(List<ProductionCompanyEntity> newProdCompLogos){
        companyEntityList.clear();
        companyEntityList.addAll(newProdCompLogos);
        notifyDataSetChanged();
    }
}
