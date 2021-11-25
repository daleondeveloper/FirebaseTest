package com.daleondeveloper.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private FirebaseRemoteConfig firebaseRemoteConfig;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private RecyclerView counterRecycleView;
    private ImageView squareImage;
    private ImageView storageImage;
    private TextView tokenTextView;

    private Queue<String> imageStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageStack = new LinkedList<>();
        initViews();
        initFirebase();
        fireBaseRemoteFetchAndActivate();
        addImageNameToList();
        nextStorageImageClick(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showToken();
    }

    private void initViews(){
        counterRecycleView = (RecyclerView)findViewById(R.id.country_list);
        squareImage = (ImageView)findViewById(R.id.quarte_image);
        storageImage = (ImageView)findViewById(R.id.storage_image);
        tokenTextView = (TextView)findViewById(R.id.tokenTextView);
    }
    private void initFirebase(){
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("/images/");


    }
    private void showToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Error receive token", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String token = task.getResult();
                        tokenTextView.setText(token);
                    }
                });
    }
    private void fireBaseRemoteFetchAndActivate(){
        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                setSquareImageBackgroundColorByColorName(firebaseRemoteConfig.getString("color").toLowerCase());
                                setCountriesList();
                            } else {
                                Toast.makeText(MainActivity.this, "Fetch Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }
    public void nextStorageImageClick(View view){
        if(imageStack.size() > 0) {
            String currentImageName = imageStack.remove();
            imageStack.add(currentImageName);
            storageReference.child(currentImageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Picasso.with(getApplicationContext()).load(uri)
                            .into(storageImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ошибка!", Toast.LENGTH_SHORT);
                    toast.show();
                }


            });
        }
    }
    private void addImageNameToList(){
        imageStack.addAll(getImageNameFromFirebase());
    }
    private List<String> getImageNameFromFirebase(){
        return fromJsonToStringList(firebaseRemoteConfig.getString("images"));
    }
    private void setSquareImageBackgroundColorByColorName(String color){
        switch (color){
            case "red" :
                squareImage.setBackgroundResource(R.color.colorRed);
                break;
            case "green" :
                squareImage.setBackgroundResource(R.color.colorGreen);
                break;
            default:squareImage.setVisibility(View.GONE);
        }
    }
    private void setCountriesList(){
        String countriesJSON = firebaseRemoteConfig.getString("countries");
        if(!countriesJSON.isEmpty()) {
            setRecycleViewAdapter(fromJsonToStringList(countriesJSON));
        }
    }
    private List<String> fromJsonToStringList(String json){
        JSONArray jsonArray;
        List<String> parsedJsonList = new ArrayList<>();
        if(!json.isEmpty()) {
            try {
                jsonArray = new JSONArray(json);
                for(int i = 0; i < jsonArray.length(); i++){
                    if(!jsonArray.getString(i).isEmpty()){
                        parsedJsonList.add(jsonArray.getString(i));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return parsedJsonList;
    }
    private void setRecycleViewAdapter(List<String> list){
        ListAdapter adapter = new ListAdapter(this,list);
        counterRecycleView.setAdapter(adapter);
        counterRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }
}