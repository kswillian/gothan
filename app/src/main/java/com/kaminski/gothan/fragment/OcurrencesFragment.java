package com.kaminski.gothan.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kaminski.gothan.R;
import com.kaminski.gothan.adapter.Adapter;
import com.kaminski.gothan.firebase.Firebase;
import com.kaminski.gothan.model.Ocurrence;
import com.kaminski.gothan.util.Base64Custom;
import com.kaminski.gothan.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class OcurrencesFragment extends Fragment {

    private TextView textViewOcurrenceEmpty;
    private RecyclerView recyclerViewOcurrences;
    private List<Ocurrence> ocurrenceList = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ocurrences, container, false);

        firebaseAuth = Firebase.getFirebaseAuth();
        databaseReference = Firebase.getFirebase().child("ocurrences");

        initCompenent(view);
        configRecycler(view);
        initEvent();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ocurrenceList.clear();
        findOcurrences();
        isEmpty();
    }

    public void initCompenent(View view){
        textViewOcurrenceEmpty = view.findViewById(R.id.textViewOcurrenceEmpty);
        recyclerViewOcurrences = view.findViewById(R.id.recyclerViewOcurrences);
    }

    public void configRecycler(View view){
        adapter = new Adapter(ocurrenceList, view.getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewOcurrences.setLayoutManager(layoutManager);
        recyclerViewOcurrences.setHasFixedSize(true);
        recyclerViewOcurrences.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.HORIZONTAL));
        recyclerViewOcurrences.setAdapter(adapter);
    }

    public void initEvent(){

        recyclerViewOcurrences.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerViewOcurrences,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        final Ocurrence ocurrence = ocurrenceList.get(position);

                        AlertDialog.Builder msg = new AlertDialog.Builder(getContext());
                        msg.setTitle(getResources().getString(R.string.alert_del_ocu_title));
                        msg.setMessage(getResources().getString(R.string.alert_del_ocu_desc));
                        msg.setPositiveButton(getResources().getString(R.string.alert_del_ocu_button_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteOcurrence(ocurrence.getId());
                            }
                        });
                        msg.setNegativeButton(getResources().getString(R.string.alert_del_ocu_button_cancel), null);
                        msg.show();

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));
    }

    public void findOcurrences(){

        Query query = databaseReference.
                orderByChild("userId").
                equalTo(Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail()));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ocurrenceList.add(ds.getValue(Ocurrence.class));
                    isEmpty();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        isEmpty();
    }

    public void deleteOcurrence(String ocurrenceId){
        DatabaseReference databaseRef = Firebase.getFirebase();
        DatabaseReference databaseReferenceGlobal = databaseRef.child("location_global");
        DatabaseReference databaseReferenceOcurrence = databaseRef.child("ocurrences");
        databaseReferenceGlobal.child(ocurrenceId).removeValue();
        databaseReferenceOcurrence.child(ocurrenceId).removeValue();
        ocurrenceList.clear();
        isEmpty();
        findOcurrences();
    }

    public void isEmpty(){
        if(!ocurrenceList.isEmpty())
           textViewOcurrenceEmpty.setVisibility(View.INVISIBLE);
    }
}
