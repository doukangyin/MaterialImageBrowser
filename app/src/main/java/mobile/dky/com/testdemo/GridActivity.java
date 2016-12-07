package mobile.dky.com.testdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11 on 2016/12/6.
 */

public class GridActivity extends Activity {
    RecyclerView recyclerView;

    GridAdapter adapter;

    List<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        list = new ArrayList<>();
        list.add("file:///android_asset/1.jpg");
        list.add("file:///android_asset/2.jpg");
        list.add("file:///android_asset/3.jpg");
        list.add("file:///android_asset/4.jpg");
        list.add("file:///android_asset/5.jpg");
        list.add("file:///android_asset/6.jpg");
        list.add("file:///android_asset/7.jpg");
        list.add("file:///android_asset/8.jpg");
        list.add("file:///android_asset/9.jpg");
        adapter = new GridAdapter(R.layout.item_layout, list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
