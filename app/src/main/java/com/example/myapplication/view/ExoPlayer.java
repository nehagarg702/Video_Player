package com.example.myapplication.view;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.Adapter.RelatedAdapter;
import com.example.myapplication.R;
import com.example.myapplication.model.SignIn;
import com.example.myapplication.clickinterface.onClickListener;
import com.example.myapplication.database.MobioticsDb;
import com.example.myapplication.model.Video;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;



import java.util.ArrayList;


public class ExoPlayer extends AppCompatActivity {

    PlayerView playerView;
    PlayerControlView cv;
    SimpleExoPlayer player;
    String url,id,title,description;
    ImageView more;
    Button scroll;
    String state="a";
    TextView headline,Description;
    private ArrayList<Video> articleStructure = new ArrayList<>();
    private RelatedAdapter adapter;
    private RecyclerView recyclerView;
    long playbackPosition = 0;
    int currentWindow = 0;
    boolean playWhenReady = false;
    TextView text;
    private SwipeRefreshLayout swipeRefreshLayout;

    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        scroll=(Button)findViewById(R.id.button3);
        scroll.setVisibility(View.INVISIBLE);
        text=(TextView)findViewById(R.id.textView7) ;
        text.setVisibility(View.INVISIBLE);
        layout=(LinearLayout)findViewById(R.id.linear);
        layout.setVisibility(View.INVISIBLE);
        playerView=(PlayerView)findViewById(R.id.player_view);
        url=getIntent().getStringExtra("url");
        id=getIntent().getStringExtra("id");
        title=getIntent().getStringExtra("title");
        this.setTitle(title);
        description=getIntent().getStringExtra("description");
        headline=(TextView) findViewById(R.id.textView3);
        headline.setText(title);
        Description=(TextView)findViewById(R.id.textView);
        more=(ImageView)findViewById(R.id.imageView);
       more.setImageResource(R.drawable.expand);
        more.setVisibility(View.VISIBLE);
        if(description.length()>100) {
            Description.setText(description.substring(0, 100) + "...");
         }
        else
        {
            Description.setText(description);
            more.setVisibility(View.INVISIBLE);
        }
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Description.getText().length()==103) {
                    Description.setText(description);
                    more.setImageResource(R.drawable.collapse);
                }
                else
                {
                    Description.setText(description.substring(0,100)+"...");
                    more.setImageResource(R.drawable.expand);
                     }
            }
            });
        createRecyclerView();
        loadJSON();
        scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingSwipeRefreshLayout();
            }
        });
    }

    private void onLoadingSwipeRefreshLayout() {
         loadJSON();
    }

    private void loadJSON() {
        Call<ArrayList<Video>> call = new SignIn().getData();
        call.enqueue(new Callback<ArrayList<Video>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Video>> call, @NonNull Response<ArrayList<Video>> response) {

                if (!articleStructure.isEmpty()) {
                    articleStructure.clear();
                }
                articleStructure = (ArrayList<Video>) response.body();
                adapter = new RelatedAdapter(ExoPlayer.this, articleStructure,id);
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
                text.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.INVISIBLE);
                scroll.setVisibility(View.INVISIBLE);
                adapter.setOnItemClickListener(new onClickListener() {
                    @Override
                    public void onItemClick(Video selectedData, int position) {
                        Intent i=new Intent(getApplicationContext(),ExoPlayer.class);
                       new SignIn().IntenttoExo(i,selectedData);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Video>> call, @NonNull Throwable t) {
               text.setVisibility(View.VISIBLE);
               layout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                scroll.setVisibility(View.VISIBLE);
                text.setText("Please check your Internet Connection and Again load the Data by clicking on Retry" );
            }
        });
    }

    private void createRecyclerView() {
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ExoPlayer.this));
    }


    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            if(state.equals("change"))
                playbackPosition=0;
            else
            {
                playbackPosition = player.getCurrentPosition();
            }
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
            new MobioticsDb(getApplicationContext()).updateRecord(id,String.valueOf(playbackPosition));
            state="a";
        }
    }

    private void initializePlayer() {
        playbackPosition=Long.parseLong(new MobioticsDb(getApplicationContext()).getRecord(id));
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector, loadControl);
        playerView.setPlayer(player);

        Uri uri = Uri.parse(url);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);

        try {
            playbackPosition = Long.parseLong(String.valueOf(playbackPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }

        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_ENDED:
                        state="change";
                        Video selectedData=new MobioticsDb(getApplicationContext()).getNextRecord(id);
                        Intent i = new Intent(getApplicationContext(), ExoPlayer.class);
                       new SignIn().IntenttoExo(i,selectedData);
                        startActivity(i);
                        break;
                    default:
                        break;
                }
            }
        });
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
    }


    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-mobiotics")).
                createMediaSource(uri);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
