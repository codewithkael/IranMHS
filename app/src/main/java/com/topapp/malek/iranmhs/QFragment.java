package com.topapp.malek.iranmhs;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class QFragment extends Fragment {

    private static final String ARG_PARAM1 = "mdata";
    private static final String TAG = "QFragment";

    public  boolean qidm = false;
    private int qid;
    private int Userid = 1;
    private questionnaire qdata;
    private ArrayList<questions> mquestions;
    public RecyclerView recyclerView;
    ExpandableLayout expendableLayout;
    private LinearLayoutManager linearLayoutManager;
    private int  currentScrollPosition = 0;
    boolean isexp = true;
    boolean isup = true;
    boolean isExpended = false;
    public QFragment() {
        // Required empty public constructor
    }


    public static QFragment newInstance(questionnaire data,int userid) {
        QFragment fragment = new QFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, data.QID);
        //  args.putInt(ARG_PARAM1, data.QID);
        // Userid = userid;
        fragment.setArguments(args);
        fragment.Userid = userid;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBase db = new DataBase(getActivity().getApplicationContext());
        if (getArguments() != null) {
            qid = getArguments().getInt(ARG_PARAM1);
            qdata = db.getquestinare(String.valueOf( qid));
        }
        mquestions = db.getquestions(String.valueOf(qid));

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // if(recyclerView != null)

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mv = inflater.inflate(R.layout.fragment_q, container, false);

        recyclerView = mv.findViewById(R.id.myrecycle);
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext()){

            @Override
            public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate) {
                return false;
            }
            @Override
            public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
                return false;
            }


        };

        recyclerView.setLayoutManager(linearLayoutManager);
        questionadapter adapter = new questionadapter(getActivity().getApplicationContext(), mquestions,Userid,this);

        // recyclerView.setItemAnimator(null);
//        ((LinearLayoutManager)recyclerView.getLayoutManager()).setInitialPrefetchItemCount(3);
        ((DefaultItemAnimator)recyclerView.getItemAnimator()).setAddDuration(10);
        ((DefaultItemAnimator)recyclerView.getItemAnimator()).setMoveDuration(10);
        ((DefaultItemAnimator)recyclerView.getItemAnimator()).setRemoveDuration(10);
        ((DefaultItemAnimator)recyclerView.getItemAnimator()).setChangeDuration(10);
        ((DefaultItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        recyclerView.setAdapter(adapter);
        //   recyclerView.scrollToPosition(0);


        recyclerView.addOnScrollListener(

                new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if(expendableLayout == null){
                            expendableLayout = ((ExpandableLayout)((View)recyclerView.getParent().getParent().getParent()).findViewById(R.id.expandable_layout));
                        }

                        if (dy>0){
                            expendableLayout.collapse();

                        }else

                        if (!recyclerView.canScrollVertically(-1)){
                            Log.d(TAG, "onScrolleed: cant scroll top anymore");
                            expendableLayout.expand();
                        }

//                        currentScrollPosition += dy;
//                        Log.d(TAG, "onScrolled: "+currentScrollPosition);
//
//
//                        if(currentScrollPosition<linearLayoutManager.getChildAt(0).getHeight()/10){
//                            // Its at top
//                            // We're at the top
//
//                                if (!expendableLayout.isExpanded()){
//                                    expendableLayout.expand();
//                                    isExpended = true;
//                                }
//
//
//                        } else {
//
//                                if (expendableLayout.isExpanded()){
//                                    expendableLayout.collapse();
//                                    isExpended = false;
//                                }
//
//
//                        }
//                        if (dy < 0 && el != null) {
//                            if(el.isExpanded())
//                                el.collapse();
//                        }


                        //   Log.d("scoroll","       scroll => " +dy);
//                        if (dy > 0) {
//                            isup = true;
//                        } else if (dy < 0) {
//                            isup = false;
//                        }


                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

//                        if(newState == RecyclerView.SCROLL_STATE_IDLE )
//                            isup = true;
                        super.onScrollStateChanged(recyclerView, newState);


                    }
                });


        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                return true;
            }
        });

        // Inflate the layout for this fragment
        return mv;
    }







}
