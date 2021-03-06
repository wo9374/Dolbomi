package com.example.project;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.project.Model.Eatting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EattingWriteActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener{
    private ImageView first_eat_image;
    private ImageView second_eat_image;
    private ImageView third_eat_image;
    private EditText eattingWritefirstText;
    private EditText eattingWritesecondText;
    private EditText eattingWritethirdText;
    ProgressDialog progressDialog;

    private Button saveEating;
    private String uid;

    //????????? ??????
    String mCurrentPhotoPath; //?????? ?????? ?????? ??????
    Uri imageUri;
    Uri photoURI;
    Uri albumURI;
    Uri albumURI1;
    Uri albumURI2;
    Uri albumURI3;



    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;

    int select_photo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_eatting_activity);

        //????????????
        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(EattingWriteActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                try {
                    Locale locale=new Locale("ko","KR");
                    Date d = new SimpleDateFormat
                            ("yyyy-MM-dd", locale.getDefault()).parse(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    TextView nowDate=findViewById(R.id.nowDate);
                    nowDate.setText(String.valueOf(year)+"??? "+String.valueOf(monthOfYear+1)+"??? "+String.valueOf((dayOfMonth))+"???");
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        eattingWritefirstText = (EditText) findViewById(R.id.eattingFirst);
        eattingWritesecondText = (EditText) findViewById(R.id.eattingSecond);
        eattingWritethirdText = (EditText) findViewById(R.id.eattingThird);
        first_eat_image = findViewById(R.id.first_eat_image);
        second_eat_image = findViewById(R.id.second_eat_image);
        third_eat_image = findViewById(R.id.third_eat_image);
        saveEating = (Button)findViewById(R.id.saveEating);
        progressDialog = new ProgressDialog(this);

        saveEating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });

    }
    private void uploadFile() {
        final String key = FirebaseDatabase.getInstance().getReference().child("Eating").push().getKey();
        final Eatting eatting = new Eatting();
        //???????????? ????????? ????????? ??????
        if (albumURI != null) {
            //????????? ?????? Dialog ?????????
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("????????????...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique??? ???????????? ?????????.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";
            //storage ????????? ?????? ???????????? ????????? ??????.
            StorageReference storageRef1 = storage.getReferenceFromUrl("gs://dolbomi1.appspot.com/").child("eatingImages/").child("firstImages/" + filename);
            final StorageReference storageRef2 = storage.getReferenceFromUrl("gs://dolbomi1.appspot.com/").child("eatingImages/").child("secondImages/" + filename);
            final StorageReference storageRef3 = storage.getReferenceFromUrl("gs://dolbomi1.appspot.com/").child("eatingImages/").child("thirdImages/" + filename);
            //???????????????...
            storageRef1.putFile(albumURI1)
                    //?????????
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageRef2.putFile(albumURI2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    Task<Uri> imageUrl2 = task.getResult().getStorage().getDownloadUrl();
                                    while(!imageUrl2.isComplete());

                                    SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy.MM.dd ?????????");
                                    Date time = new Date();
                                    String time1 = format1.format(time);

                                    eatting.nowtime = time1;
                                    eatting.first = eattingWritefirstText.getText().toString();
                                    eatting.second = eattingWritesecondText.getText().toString();
                                    eatting.third = eattingWritethirdText.getText().toString();
                                    eatting.secondImageUrl = imageUrl2.getResult().toString();

                                    FirebaseDatabase.getInstance().getReference().child("Eating").child(key).setValue(eatting);
                                }
                            });
                            storageRef3.putFile(albumURI3).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    Task<Uri> imageUrl3 = task.getResult().getStorage().getDownloadUrl();
                                    while(!imageUrl3.isComplete());

                                    SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy.MM.dd ?????????");
                                    Date time = new Date();
                                    String time1 = format1.format(time);

                                    eatting.nowtime = time1;
                                    eatting.first = eattingWritefirstText.getText().toString();
                                    eatting.second = eattingWritesecondText.getText().toString();
                                    eatting.third = eattingWritethirdText.getText().toString();
                                    eatting.thirdImageUrl = imageUrl3.getResult().toString();

                                    FirebaseDatabase.getInstance().getReference().child("Eating").child(key).setValue(eatting);
                                }
                            });
                            Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                            while(!imageUrl.isComplete());

                            SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy.MM.dd ?????????");
                            Date time = new Date();
                            String time1 = format1.format(time);


                            eatting.nowtime = time1;
                            eatting.first = eattingWritefirstText.getText().toString();
                            eatting.second = eattingWritesecondText.getText().toString();
                            eatting.third = eattingWritethirdText.getText().toString();
                            eatting.firstImageUrl = imageUrl.getResult().toString();

                            FirebaseDatabase.getInstance().getReference().child("Eating").child(key).setValue(eatting).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "???????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                    EattingWriteActivity.this.finish();
                                }
                            });
                        }

                    })
                    //?????????
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //?????????
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog??? ???????????? ???????????? ????????? ??????
                            progressDialog.setMessage("????????? " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onTimeChanged(TimePicker timePicker, int i, int i1) {

    }

    public void onClick(View view) {
        switch (view.getId()){

            case R.id.first_eat_image:
                select_photo=1;
                albumDialog();
                break;

            case R.id.second_eat_image:
                select_photo=2;
                albumDialog();
                break;

            case R.id.third_eat_image:
                select_photo=3;
                albumDialog();
                break;

            case R.id.saveEating:

        }
    }

    private  void albumDialog(){
        checkPermission();

        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                captureCamera();
            }
        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getAlbum();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(this)   //????????? ????????? ??????
                .setTitle("???????????? ????????? ??????")
                .setPositiveButton("?????? ??????", cameraListener)
                .setNeutralButton("?????? ??????", albumListener)
                .setNegativeButton("??????", cancelListener)
                .show();
    }

    private void captureCamera() {
        String state = Environment.getExternalStorageState();
        //?????? ????????? ??????

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("captureCamera Error", ex.toString());
                }
                if (photoFile != null) {
                    //getUriForFile??? ????????? ????????? Manifest provider??? authorites??? ???????????????

                    Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    imageUri = providerURI;

                    // ???????????? ????????? ?????? FileProvider??? Return?????? content://??????, providerURI??? ?????? ????????? ???????????? ?????? ??????
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } else {
            Toast.makeText(this, "??????????????? ?????? ???????????? ???????????????", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    public File createImageFile() throws IOException {
        //Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "gyeom");

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdir();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    private void getAlbum() {
        Log.i("getAlbum", "Call");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }

    private void galleryAddPic() {
        Log.i("galleryAddPic", "Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        //?????? ????????? ?????? ????????? ????????? (?????? ????????? ???????????? ???)

        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "????????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show();

    }

    public void cropImage() {
        Log.i("cropImage", "Call");
        Log.i("cropImage", "photoURI : " + photoURI + "/albumURI : " + albumURI);

        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        //50x50?????? ????????? ???????????? ????????? ?????? ?????? + ?????????, ?????? ?????? ???????????? ??????
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        cropIntent.setDataAndType(photoURI, "image/*");

        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 200);
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);

        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output", albumURI);//????????? ???????????? ??????????????? ??????

        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //????????? ?????? ????????? ??????
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Log.i("REQUEST_TAKE_PHOTO","OK");
                        galleryAddPic();

                        if(resultCode == Activity.RESULT_OK){
                            galleryAddPic();
                            if(select_photo==1){
                                first_eat_image.setImageURI(imageUri);
                            }else if (select_photo==2){
                                second_eat_image.setImageURI(imageUri);
                            }else if(select_photo==3){
                                third_eat_image.setImageURI(imageUri);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("REQUEST_TAKE_PHOTO", e.toString());
                    }
                } else {
                    Toast.makeText(EattingWriteActivity.this, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_TAKE_ALBUM:
                if (resultCode == Activity.RESULT_OK){
                    if(data.getData() != null){
                        try {
                            File albumFile = null;
                            albumFile=createImageFile();
                            photoURI=data.getData();
                            albumURI=Uri.fromFile(albumFile);
                            cropImage();
                        }catch (Exception e){
                            Log.e("TAKE_ALBUM_SINGLE ERROR", e.toString());
                        }
                    }
                }
                break;

            case REQUEST_IMAGE_CROP:
                if(resultCode == Activity.RESULT_OK){
                    galleryAddPic();
                    if(select_photo==1){
                        first_eat_image.setImageURI(albumURI);
                        albumURI1=albumURI;
                    }else if (select_photo==2){
                        second_eat_image.setImageURI(albumURI);
                        albumURI2=albumURI;
                    }else if(select_photo==3){
                        third_eat_image.setImageURI(albumURI);
                        albumURI3=albumURI;
                    }
                }
                break;
        }
    }

    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //?????? ????????? if()?????? ????????? false??? ?????? ??? -> else{..}??? ???????????? ?????????

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))) {
                new AlertDialog.Builder(this)
                        .setTitle("??????")
                        .setMessage("????????? ????????? ?????????????????????. ????????? ???????????? ?????? ????????? ?????? ??????????????? ?????????.")
                        .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package: " + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
            }
        }
    }
}
