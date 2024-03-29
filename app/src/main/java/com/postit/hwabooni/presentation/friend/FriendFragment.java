package com.postit.hwabooni.presentation.friend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postit.hwabooni.R;
import com.postit.hwabooni.data.repository.FriendRepository;
import com.postit.hwabooni.databinding.FragmentFriendBinding;
import com.postit.hwabooni.model.FriendData;
import com.postit.hwabooni.model.User;
import com.postit.hwabooni.presentation.emotionrecord.EmotionRecordFragment;
import com.postit.hwabooni.presentation.friendplant.FriendPlantFragment;
import com.postit.hwabooni.presentation.login.LoginActivity;

import java.util.ArrayList;

public class FriendFragment extends Fragment implements DialogInterface.OnDismissListener {

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        refreshFragment();
    }

    private static final String TAG = "FriendFragment";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FragmentFriendBinding binding;
    FriendListAdapter adapter;

    private int refresh = 0;

    MutableLiveData<ArrayList<FriendData>> friendsList = new MutableLiveData<>(new ArrayList<>());

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            refreshFragment();
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFriendBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(auth.getCurrentUser()==null){
            launcher.launch(new Intent(getContext(), LoginActivity.class));
        }
        else {

            db.collection("User").document(auth.getCurrentUser().getEmail()).get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot == null){

                }else{
                    User user = documentSnapshot.toObject(User.class);
                    //Log.d("user이름", user.getName());
                    initRecyclerView();
                    adapter.setMyName(user==null?"null":user.getName());
                    if(user!=null){
                        adapter.setMyEmotion(user.getEmotion());
                    } else return;
                    if(user.getFollower()!=null){
                        new FriendRepository().getFriendsData(user.getFollower(),(data)->{
                            Log.d(TAG, "onViewCreated: callback");
                            Log.d(TAG, "onViewCreated: callback" + data);
                            adapter.setFriend(data);
                            adapter.notifyDataSetChanged();
                        });
                    }
                    friendsList.observe(getActivity(), data -> {
                        adapter.setFriend(data);
                        adapter.notifyDataSetChanged();
                    });

                }
            });

            

        }


    }

    private void initRecyclerView() {
        adapter=new FriendListAdapter(getContext());
        binding.friendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        adapter.setListener((v)->{
            new EmotionRecordFragment().show(requireActivity().getSupportFragmentManager(), "EMOTION_RECORD");
        });
        adapter.setMypageListener((v)->{
            launcher.launch(new Intent(getContext(), LoginActivity.class));
        });
        adapter.setFriendClickListener((name,email)->{
            Bundle bundle = new Bundle();
            bundle.putString(FriendPlantFragment.FRIEND_NAME_CODE,name);
            bundle.putString(FriendPlantFragment.FRIEND_EMAIL_CODE,email);
            FriendPlantFragment fragment = new FriendPlantFragment();
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
        });
        binding.friendRecyclerView.setAdapter(adapter);

        adapter.setFriendAddListener(view -> {
            //launcher.launch(new Intent(getContext(), FriendAddFragment.class));
            new FriendAddFragment().show(requireActivity().getSupportFragmentManager(), "Friend_Add");
            refresh=0;
        });


    }

    void refreshFragment(){
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.detach(this);
        ft.replace(R.id.fragment_container,new FriendFragment()).commit();
    }

}
