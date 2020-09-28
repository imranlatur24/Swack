package com.swack.customer.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swack.customer.R;
import com.swack.customer.activities.BreakDownActivity;
import com.swack.customer.activities.OrderHistoryActivity2;
import com.swack.customer.adapter.FinalBillAdapter2;
import com.swack.customer.data.APIService;
import com.swack.customer.data.APIUrl;
import com.swack.customer.model.ResponseResult;

import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.swack.customer.activities.BaseActivity.prefManager;

public class BreakDownFragment2 extends Fragment {

    private OrderHistoryActivity2 historyActivity;
    private RecyclerView frag_recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout error_layout;
    private Button btnRetry;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private TextView txtError;
    private FinalBillAdapter2 homeAdapter;
    private String cus_id,cus_name;
    private APIService apiService;

    public BreakDownFragment2() {
    }

    @SuppressLint("ValidFragment")
    public BreakDownFragment2(OrderHistoryActivity2 historyActivity) {
        this.historyActivity = historyActivity;
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);

        apiService = APIUrl.getClient().create(APIService.class);

        prefManager.connectDB();
        cus_id = prefManager.getString("cus_id");
        cus_name = prefManager.getString("cus_name");

        prefManager.closeDB();

        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);
        frag_recyclerView = view.findViewById(R.id.frag_recyclerView);
        mLayoutManager = new LinearLayoutManager(historyActivity, RecyclerView.VERTICAL,false);
        frag_recyclerView.setLayoutManager(mLayoutManager);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(historyActivity, BreakDownActivity.class));
                historyActivity.finish();
            }
        });
        finalbill();

        return view;
    }

    private void finalbill() {
//        swipeContainer.setRefreshing(false);
        progressBar.setVisibility(View.VISIBLE);
        frag_recyclerView.setVisibility(View.GONE);

        callCategory().enqueue(new Callback<ResponseResult>() {
            @Override
            public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                progressBar.setVisibility(View.GONE);
                try {
                    System.out.println("Transport Response : "+response.body().getResponse());
                    if (Integer.parseInt(response.body().getResponse()) == 101) {
                        if(!response.body().getOrderListPend().isEmpty()) {
                            errorLayout.setVisibility(View.GONE);
                            btnRetry.setVisibility(View.GONE);
                            frag_recyclerView.setVisibility(View.VISIBLE);
                            homeAdapter = new FinalBillAdapter2(historyActivity);
                            frag_recyclerView.setAdapter(homeAdapter);
                            homeAdapter.setListArray(response.body().getOrderListPend());
                        }else {
                            btnRetry.setVisibility(View.VISIBLE);
                            showErrorView(getResources().getString(R.string.hi)+ cus_name+"\n"+getResources().getString(R.string.orders_trasnport_error)+"\n"+getResources().getString(R.string.create_new));
                        }
                    } else if (Integer.parseInt(response.body().getResponse()) == 102) {
                        btnRetry.setVisibility(View.VISIBLE);
                        showErrorView(getResources().getString(R.string.hi)+ cus_name+"\n"+getResources().getString(R.string.orders_trasnport_error)+"\n"+getResources().getString(R.string.create_new));
                    }else {
                        btnRetry.setVisibility(View.VISIBLE);
                        showErrorView(getResources().getString(R.string.hi)+ cus_name+"\n"+getResources().getString(R.string.orders_trasnport_error)+"\n"+getResources().getString(R.string.create_new));
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    btnRetry.setVisibility(View.VISIBLE);
                    showErrorView(getResources().getString(R.string.hi)+ cus_name+"\n"+getResources().getString(R.string.orders_trasnport_error)+"\n"+getResources().getString(R.string.create_new));
                }
            }

            @Override
            public void onFailure(Call<ResponseResult> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showErrorView(t);
            }
        });
    }

    //categories
    private Call<ResponseResult> callCategory() {

        System.out.println("Pending Item API KEY " + APIUrl.KEY+" cus id : "+cus_id);
        return apiService.pending(
                APIUrl.KEY,
                cus_id
        );
    }

    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private void showErrorView(String error) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            txtError.setText(error);
        }
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.hi)+ cus_name+"\n"+getResources().getString(R.string.orders_error);

        if (!historyActivity.isNetworkAvailable()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.hi)+ cus_name+"\n"+getResources().getString(R.string.orders_error);
        }

        return errorMsg;
    }
}
