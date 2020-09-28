package com.swack.customer.adapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.swack.customer.R;
import com.swack.customer.activities.JobDetailActivity;
import com.swack.customer.activities.OrderHistoryActivity2;
import com.swack.customer.model.OLPending;
import com.swack.customer.model.Order;

import java.util.ArrayList;
import java.util.Locale;

import es.dmoral.toasty.Toasty;


public class FinalBillAdapter2 extends RecyclerView.Adapter<FinalBillAdapter2.MyViewHolder> {

    private OrderHistoryActivity2 context;

    private ArrayList<OLPending> arrayLists;
    private ArrayList<OLPending> tempArrayLists;

    public FinalBillAdapter2(OrderHistoryActivity2 context) {
        this.context = context;
    }

    public void setListArray(ArrayList<OLPending> arrayLists) {
        this.arrayLists = arrayLists;
        tempArrayLists = new ArrayList<>();
        tempArrayLists.addAll(arrayLists);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.finalbilladapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final OLPending mOrder = arrayLists.get(position);

        holder.txt_joborderid.setText(mOrder.getService_typ_name());
        holder.txt_jobid.setText("Order Id - "+mOrder.getOrdreq_id());
        holder.txtJobDate.setText("Date - "+mOrder.getIs_create());
        holder.txtJobUnique.setText(Html.fromHtml("<b>"+mOrder.getVehicled_regno()+"</b> "+mOrder.getVehicle_name()+" | "+mOrder.getVehicle_cat_name()));
        holder.txtJobCustomer.setText("Customer Details - "+mOrder.getCus_name()+"("+mOrder.getCus_mob()+")");
        holder.txtJobCustomer.setVisibility(View.GONE);
        holder.txtJobLocation.setText("Address - "+mOrder.getOrdreq_location());
        holder.txtJobLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  try {
                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", Float.parseFloat(mOrder.getGar_lat()), Float.parseFloat(mOrder.getGar_long()));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);
                }catch (ActivityNotFoundException e){
                    Toasty.error(context,"Google Map App Not Found").show();
                }*/
            }
        });
        holder.txtJobDistance.setText("Distance - "/*+mOrder.getDistance()*/);
        holder.txtJobStatus.setText(mOrder.getStatus_name());

        holder.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, JobDetailActivity.class)
                        .putExtra("order_id",mOrder.getOrdreq_id())
                        .putExtra("customer_id",mOrder.getCus_id())
                        .putExtra("button",true));
            }
        });
    }


    @Override
    public int getItemCount() {
        return (arrayLists == null) ? 0 : arrayLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_joborderid,txt_jobid,txtJobDate,txtJobUnique,txtJobCustomer,txtJobDistance,txtJobLocation,txtJobStatus;
        public CardView cvCategory;

        public MyViewHolder(View view) {
            super(view);
            txt_jobid = view.findViewById(R.id.txt_jobid);
            txt_joborderid = view.findViewById(R.id.txt_joborderid);
            txtJobDate = view.findViewById(R.id.txtJobDate);
            txtJobUnique = view.findViewById(R.id.txtJobUnique);
            txtJobCustomer = view.findViewById(R.id.txtJobCustomer);
            txtJobLocation = view.findViewById(R.id.txtJobLocation);
            txtJobDistance = view.findViewById(R.id.txtJobDistance);
            txtJobStatus = view.findViewById(R.id.txtJobStatus);
            cvCategory = view.findViewById(R.id.cvCategory);


        }
    }
}
