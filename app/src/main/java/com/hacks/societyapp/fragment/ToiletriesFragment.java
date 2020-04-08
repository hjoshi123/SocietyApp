package com.hacks.societyapp.fragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hacks.societyapp.R;
import com.hacks.societyapp.Utils.adapter.ItemsAdapter;
import com.hacks.societyapp.Utils.adapter.SliderAdapter;
import com.hacks.societyapp.model.Items;
import com.hacks.societyapp.model.SliderItem;
import com.hacks.societyapp.retrofitapi.SocietyAPI;
import com.hacks.societyapp.retrofitclient.SocietyClient;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToiletriesFragment extends Fragment {
    private SocietyAPI mSocietyAPI;
    private RecyclerView mToiletriesRecyclerView;
    private MaterialProgressBar mToiletriesProgress;

    public ToiletriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_toiletries, container, false);

        mSocietyAPI = SocietyClient.getClient(getContext())
                .create(SocietyAPI.class);
        mToiletriesRecyclerView = view.findViewById(R.id.toiletries_recycler);

        View progressView = view.findViewById(R.id.progress_container);
        mToiletriesProgress = progressView.findViewById(R.id.toiletries_progress);

        View sliderContainer = view.findViewById(R.id.toiletries_slider);
        SliderView sliderView = sliderContainer.findViewById(R.id.imageSlider);

        List<SliderItem> slider = new ArrayList<SliderItem>();
        slider.add(new SliderItem("Order Now",
                "https://images.assetsdelivery.com/compings_v2/mangpor2004/mangpor20041712/mangpor2004171200038.jpg"));
        slider.add(new SliderItem("Groceries",
                "https://image.freepik.com/free-photo/colorful-food-groceries-white-countertop_8087-2209.jpg"));

        SliderAdapter adapter = new SliderAdapter();
        sliderView.setSliderAdapter(adapter);
        adapter.renewItems(slider);

        sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(false);

        getDataAndUpdate();

        return view;
    }

    private void getDataAndUpdate() {
        mSocietyAPI.getItems()
            .enqueue(new Callback<ArrayList<Items>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Items>> call,
                                       @NonNull Response<ArrayList<Items>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<Items> items = response.body();
                        ArrayList<Items> toiletriesItems = new ArrayList<>();

                        for (Items item: items) {
                            if (item.getCategory().equals("Toileteries")) {
                                toiletriesItems.add(item);
                            }
                        }

                        ItemsAdapter adapter = new ItemsAdapter(toiletriesItems, getContext());
                        mToiletriesRecyclerView.setAdapter(adapter);
                        mToiletriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        mToiletriesProgress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Items>> call, Throwable t) {

                }
            });
    }
}
