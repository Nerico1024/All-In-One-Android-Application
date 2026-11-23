package com.example.allinoneapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allinoneapp.R;
import com.example.allinoneapp.ui.notifications.NotificationsFragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private TextView serviceLabel;

    private ArrayList<ServiceCategory> serviceCategory;
    private ArrayList<ServiceModel> serviceModel;

    private MainAdapter adapter;
    private ServiceAdapter serviceAdapter;

    Integer[] langLogo = { R.drawable.category_home,R.drawable.category_events,R.drawable.category_fitness};
    String[] langName = { "Home & Office","Events","Health & Fitness"};
    String[] services = { "Cleaning Services","Pest Control","Plumbing Services","Electrical Services",
                          "Aircon Services","Appliances Services & Repair","Interior Design",
                           "Laundry & Dry Cleaning","Movers & Trucking Services","Home Repair",
                           "Office Maintenance","Gadget Services & Repair",
                           "Hardware, Software and IT Support" };
    String[] events = { "Food & Beverages","Catering Services", "Entertainment","Weddings"};
    String[] fitness = { "Rehabilitation and Wellness", "Beauty", "Massage"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView2 = root.findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);

        serviceLabel = root.findViewById(R.id.services_label);


        serviceCategory = new ArrayList<>();
        for (int i =0;i<langLogo.length; i++){
            ServiceCategory newModel = new ServiceCategory(langLogo[i],langName[i]);
            serviceCategory.add(newModel);
        }

        serviceModel = new ArrayList<>();
        for (int i =0;i<services.length; i++){
            ServiceModel newModel = new ServiceModel(services[i]);
            serviceModel.add(newModel);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new MainAdapter(getActivity(), this.serviceCategory);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MainAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                onServiceCategoryClicked(position);
            }
        });

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        serviceAdapter = new ServiceAdapter(getActivity(), this.serviceModel);
        recyclerView2.setAdapter(serviceAdapter);
        serviceAdapter.setOnItemClickListener(new ServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                    //  serviceModel.get(position).ChangeServiceText( serviceModel.get(position).getService() +" >>>>>>>>");
                    //  serviceAdapter.notifyItemChanged(position);

                    Bundle bundle = new Bundle();
                    bundle.putString("selectedService",serviceModel.get(position).getService());

                    NotificationsFragment nextFrag= new NotificationsFragment();
                    nextFrag.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(((ViewGroup)getView().getParent()).getId(), nextFrag, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
            }
        });

        return root;
    }

    private void onServiceCategoryClicked(Integer position){

           serviceLabel.setText("Services under " +  serviceCategory.get(position).getLangName() + " category");

            if (serviceCategory.get(position).getLangName() == "Home & Office") {
                serviceModel.clear();
                for (int i = 0; i < services.length; i++) {
                    ServiceModel newModel = new ServiceModel(services[i]);
                    serviceModel.add(newModel);
                }
                serviceAdapter.notifyDataSetChanged();
            }
            if (serviceCategory.get(position).getLangName() == "Events") {
                serviceModel.clear();
                for (int i = 0; i < events.length; i++) {
                    ServiceModel newModel = new ServiceModel(events[i]);
                    serviceModel.add(newModel);
                }
                serviceAdapter.notifyDataSetChanged();
            }
            if (serviceCategory.get(position).getLangName() == "Health & Fitness") {
                serviceModel.clear();
                for (int i = 0; i < fitness.length; i++) {
                    ServiceModel newModel = new ServiceModel(fitness[i]);
                    serviceModel.add(newModel);
                }
                serviceAdapter.notifyDataSetChanged();
            }

    }

}