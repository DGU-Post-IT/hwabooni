package com.postit.hwabooni.presentation.plant;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.postit.hwabooni.databinding.FragmentPlantAddBinding;
import com.postit.hwabooni.model.PlantData;

import java.io.File;

public class PlantAddFragment extends DialogFragment {

    private static final String TAG = "PlantAddFragment";

    FragmentPlantAddBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private final int REQUEST_CODE = 200;
    File destFile;
    Uri uri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlantAddBinding.inflate(inflater,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StorageReference storageRef = storage.getReference();

        binding.btnAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("사진추가버튼클릭함", "사진추가버튼클릭함");
                Intent intent = new Intent();
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                // 위의 Activity를 실행한 이후 이벤트를 정의
                startActivityForResult(intent, REQUEST_CODE);

            }
        });
        binding.btnAddPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("식물추가버튼클릭함", "식물추가버튼클릭함");

                if(binding.tvPlantName.getText().equals("") || uri==null){
                    Toast.makeText(getContext(), "업로드 불가", Toast.LENGTH_SHORT).show();
                    return;
                }

                //사진 업로드
                StorageReference upfile = storageRef.child("Test.jpg");
                UploadTask uploadTask = upfile.putFile(uri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("사진업로드","사진업로드실패");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("사진업로드","사진업로드성공");
                    }
                });;

                //업로드한 사진 url가져오기
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "then: url가져오기 실패");
                            throw task.getException();
                        }

                        Log.d(TAG, "then: url은 가져오는거같기도함");
                        // Continue with the task to get the download URL
                        return upfile.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String urlString = downloadUri.toString();  //사진경로
                            
                            //문서 생성 및 업로드
                            String newPlantName = binding.tvPlantName.getText().toString(); //식물이름
                            Log.d("url가져오기 성공 url : ", urlString.toString());
                            DocumentReference ref = db.collection("dummyPlant").document();
                            
                            PlantData newPlantData = new PlantData(newPlantName, urlString);
                            ref.set(newPlantData);

                        } else {
                            Log.d(TAG, "onComplete: url가져오기 실패");
                            // Handle failures
                            // ...
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 사진업로드 이벤트
            case REQUEST_CODE:
                // 사진 선택
                if(resultCode == Activity.RESULT_OK)
                {
                    try{
                        // Image 상대경로를 가져온다
                        uri = data.getData();

                        // Image의 절대경로를 가져온다
                        String imagePath = getRealPathFromURI(uri);
                        // File변수에 File을 집어넣는다
                        destFile = new File(imagePath);

                        // 이미지 전송
                        //SendImage();
                    }
                    catch(Exception e) {
                        // 대기메시지 종료
                        //activity.progressDismiss();
                    }
                }
                // 사진 선택 취소
                else if(resultCode == getActivity().RESULT_CANCELED)
                {
                    Toast.makeText(getActivity(), "사진 선택 취소", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    // Image의 절대경로를 가져오는 메소드
    private String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }
        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = { MediaStore.Files.FileColumns.DATA };
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst())
            { return cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        } return null;
    }

}
