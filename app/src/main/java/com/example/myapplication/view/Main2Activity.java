package com.example.myapplication.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.myapplication.Adapter.DataAdapter;
import com.example.myapplication.R;
import com.example.myapplication.model.SignIn;
import com.example.myapplication.clickinterface.onClickListener;
import com.example.myapplication.model.Video;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<Video> articleStructure = new ArrayList<>();
    public static Activity fa;
    private DataAdapter adapter;
    private RecyclerView recyclerView;
    private TextView mTxvNoResultsFound;
    private SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        fa=this;
        setSupportActionBar(toolbar);
        this.setTitle("Video Player");
        pb=(ProgressBar)findViewById(R.id.progressbar) ;
        mTxvNoResultsFound = findViewById(R.id.tv_no_results);
        mTxvNoResultsFound.setVisibility(View.VISIBLE);
        mTxvNoResultsFound.setText("Loading Videos...");
         createRecyclerView();
        onLoadingSwipeRefreshLayout();
    }

    private void createRecyclerView() {
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setVisibility(View.INVISIBLE);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        recyclerView.setLayoutManager(new LinearLayoutManager(Main2Activity.this));
    }


    @Override
    public void onRefresh() {
loadJSON();

    }

    private void loadJSON() {
        Call<ArrayList<Video>> call =new SignIn().getData();
        call.enqueue(new Callback<ArrayList<Video>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Video>> call, @NonNull Response<ArrayList<Video>> response) {

                if (!articleStructure.isEmpty()) {
                    articleStructure.clear();
                }
                recyclerView.setVisibility(View.VISIBLE);
                mTxvNoResultsFound.setVisibility(View.GONE);
                pb.setVisibility(View.INVISIBLE);
                articleStructure = (ArrayList<Video>) response.body();
                adapter = new DataAdapter(Main2Activity.this, articleStructure);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
                adapter.setOnItemClickListener(new onClickListener() {
                    @Override
                    public void onItemClick(Video selectedData, int position) {
                        Intent i=new Intent(getApplicationContext(), ExoPlayer.class);
                        new SignIn().IntenttoExo(i,selectedData);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Video>> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(true);
                mTxvNoResultsFound.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                mTxvNoResultsFound.setText("Please check your Internet Connection and Again load the Data" );
                pb.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
             AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
            builder.setTitle("Vieo Player ");
            builder.setIcon(R.mipmap.ic_launcher_round);
            builder.setMessage("Do you want to Exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                Intent i = new Intent(getApplicationContext(), Profile.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void onLoadingSwipeRefreshLayout() {
        swipeRefreshLayout.setRefreshing(true);
        loadJSON();
    }
}