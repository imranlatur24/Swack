package com.swack.customer.adapter;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.swack.customer.R;
import com.swack.customer.activities.OrderHistoryActivity3;
import com.swack.customer.model.TrasnportPending;

import java.util.ArrayList;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class TransportAdapter3 extends RecyclerView.Adapter<TransportAdapter3.MyViewHolder> {

    private OrderHistoryActivity3 context;

    private ArrayList<TrasnportPending> arrayLists;
    private ArrayList<TrasnportPending> tempArrayLists;

    public TransportAdapter3(OrderHistoryActivity3 context) {
        this.context = context;
    }

    public void setListArray(ArrayList<TrasnportPending> arrayLists) {
        this.arrayLists = arrayLists;
        tempArrayLists = new ArrayList<>();
        tempArrayLists.addAll(arrayLists);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transportadapter3, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final TrasnportPending mOrder = arrayLists.get(position);

        holder.txt_joborderid.setText(mOrder.getTraProductName());
        holder.txt_jobid.setText(""/*"Order Id :"+mOrder.getTransport_id()*/);
        holder.txtJobDate.setText("Date  - "+mOrder.getIs_create());
        holder.txtJobUnique.setText("To District - "+mOrder.getToDistrict_Name());
        holder.txtJobCustomer.setVisibility(View.GONE);
        holder.txtJobLocation.setText("From District - "+mOrder.getFromDistrict_Name());
        holder.txtFromtaluka.setText("From Taluka - "+mOrder.getFromTalukaName());
        holder.txtToTaluka.setText("To Taluka - "+mOrder.getToTalukaName());
        holder.txtJobDistance.setText("Address - "+mOrder.getTrnreqaddress());
        holder.txtLocation.setText("TRACKING");
        holder.txt_distance.setText(mOrder.getDistance());
        holder.txttransname.setText("Name - "+mOrder.getTransport_name());
        holder.txttransname.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txttransmobile.setText("Mobile - "+mOrder.getTransport_mob());
        holder.txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    context. prefManager.connectDB();
                    String lati = context.prefManager.getString("latitude");
                    String log = context.prefManager.getString("longitude");
                    context. prefManager.closeDB();

                    System.out.println("&lati : "+lati);
                    System.out.println("&logi : "+log);

                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", Float.parseFloat(mOrder.getTransport_lat()), Float.parseFloat(mOrder.getTransport_long()));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);
                }catch (ActivityNotFoundException e){
                    Toasty.error(context,"Google Map App Not Found").show();
                }
            }
        });
       // holder.txtJobDistance.setEllipsize(TextUtils.TruncateAt.MARQUEE);
       // holder.txtJobDistance.setSelected(true);
        //holder.txtJobDistance.setSingleLine(true);
        holder.txtJobStatus.setText(mOrder.getStatus_name());

       /* holder.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              *//*  final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.info_dialog);
                dialog.setCancelable(true);
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width, height);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                TextView transport_order_id = dialog.findViewById(R.id.transport_order_id);
                transport_order_id.setText(""*//**//*mOrder.getTransport_id()*//**//*);
                TextView transport_from = dialog.findViewById(R.id.transport_from);
                transport_from.setText(mOrder.getFromDistrict_Name());
                TextView transport_to = dialog.findViewById(R.id.transport_to);
                transport_to.setText(mOrder.getToDistrict_Name());
                TextView transport_distance = dialog.findViewById(R.id.transport_distance);
                transport_distance.setText(""*//**//*mOrder.getDistance()*//**//*);
                TextView transport_status = dialog.findViewById(R.id.transport_status);
                transport_status.setText(mOrder.getStatus_name());
                TextView transport_address2 = dialog.findViewById(R.id.transport_address2);
                transport_address2.setText(mOrder.getTrnreqaddress());
                TextView date = dialog.findViewById(R.id.date);
                date.setText(mOrder.getIs_create());

                TextView transport_from_taluka = dialog.findViewById(R.id.transport_from_taluka);
                transport_from_taluka.setText(mOrder.getFromTalukaName());
                TextView transport_to_taluka = dialog.findViewById(R.id.transport_to_taluka);
                transport_to_taluka.setText(mOrder.getToTalukaName());

                dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();*//*

            }
        });*/
    }


    @Override
    public int getItemCount() {
        return (arrayLists == null) ? 0 : arrayLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_distance,txttransname,txtLocation,txttransmobile,txt_joborderid,txt_jobid,txtJobDate,txtJobUnique,txtJobCustomer,txtJobDistance,txtToTaluka,txtFromtaluka,txtJobLocation,txtJobStatus,
                transport_address2,transport_to_taluka,transport_from_taluka,date;
        public CardView cvCategory;

        public MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            txtLocation = view.findViewById(R.id.txtLocation);
            txt_distance = view.findViewById(R.id.txt_distance);
            txttransname = view.findViewById(R.id.txttransname);
            txttransmobile = view.findViewById(R.id.txttransmobile);
            transport_from_taluka = view.findViewById(R.id.transport_from_taluka);
            transport_to_taluka = view.findViewById(R.id.transport_to_taluka);
            transport_address2 = view.findViewById(R.id.transport_address2);
            txt_jobid = view.findViewById(R.id.txt_jobid);
            txt_joborderid = view.findViewById(R.id.txt_joborderid);
            txtJobDate = view.findViewById(R.id.txtJobDate);
            txtJobUnique = view.findViewById(R.id.txtJobUnique);
            txtJobCustomer = view.findViewById(R.id.txtJobCustomer);
            txtJobLocation = view.findViewById(R.id.txtJobLocation);
            txtFromtaluka = view.findViewById(R.id.txtFromtaluka);
            txtToTaluka = view.findViewById(R.id.txtToTaluka);
            txtJobDistance = view.findViewById(R.id.txtJobDistance);
            txtJobStatus = view.findViewById(R.id.txtJobStatus);
            cvCategory = view.findViewById(R.id.cvCategory);
        }
    }
}
