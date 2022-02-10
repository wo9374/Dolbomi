package com.example.project;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.project.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MembershipPopup extends Activity implements View.OnClickListener {
    String mCurrentPhotoPath; //실제 사진 파일 경로
    ImageView profilebtn;

    Uri imageUri;
    Uri photoURI;
    Uri albumURI;

    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;

    FirebaseAuth firebaseAuth;
    EditText register_id;
    EditText register_pw;
    EditText register_name;
    RadioGroup rgroup_job;
    RadioButton reg_Parent;
    RadioButton reg_Teacher;
    Button register_checkBtn;
    ProgressDialog progressDialog;


    ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> arrayIndex = new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_membership_popup);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        // 윈도우 매니저 객체 얻어오고 디스플레이 객체 얻어오기
        int width = (int) (display.getWidth() * 0.9);
        // 얻어온 화면의 폭의 90프로만큼 width에 지정
        int height = (int) (display.getHeight() * 0.7);
        // 얻어온 화면의 높이의 70프로 만큼 height에 지정
        getWindow().getAttributes().width = width;
        //멤버십팝업 레이아웃의 폭을 width로 지정
        getWindow().getAttributes().height = height;
        //멤버십팝업 레이아웃의 폭을 height 지정
        getWindow().setGravity(Gravity.CENTER);
        //멤버십팝업 레이아웃 센터지정

        firebaseAuth = FirebaseAuth.getInstance();
        register_id = (EditText) findViewById(R.id.register_id);
        register_pw = (EditText) findViewById(R.id.register_pw);
        register_name = (EditText) findViewById(R.id.register_name);
        reg_Parent = (RadioButton) findViewById(R.id.reg_Parent);
        reg_Teacher = (RadioButton) findViewById(R.id.reg_Teacher);
        rgroup_job = (RadioGroup)findViewById(R.id.rgroup_job);
        register_checkBtn = (Button) findViewById(R.id.register_checkBtn);
        progressDialog = new ProgressDialog(this);
        register_checkBtn.setOnClickListener(this);
        profilebtn = (ImageView) findViewById(R.id.profileImgBtn);

        register_checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (register_id.getText().toString() == null || register_pw.getText().toString() == null || register_name.getText().toString() == null || albumURI == null) {
                    Toast.makeText(getApplicationContext(), "가입실패", Toast.LENGTH_LONG).show();

                    return;
                }
                progressDialog.setMessage("가입중입니다. 잠시 기다려 주세요...");
                progressDialog.show();
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(register_id.getText().toString(), register_pw.getText().toString())
                        .addOnCompleteListener(MembershipPopup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final String uid = task.getResult().getUser().getUid();
                                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(register_name.getText().toString()).build();
                                    task.getResult().getUser().updateProfile(userProfileChangeRequest);

                                    FirebaseStorage.getInstance().getReference().child("userImages/").child(uid).putFile(albumURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                                            while (!imageUrl.isComplete()) ;
                                            int id = rgroup_job.getCheckedRadioButtonId();
                                            RadioButton rb = (RadioButton) findViewById(id);
                                            User user = new User();
                                            user.email = register_id.getText().toString();
                                            user.username = register_name.getText().toString();
                                            user.profileImageUrl = imageUrl.getResult().toString();
                                            user.job = rb.getText().toString();
                                            user.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "가입성공", Toast.LENGTH_LONG).show();
                                                    MembershipPopup.this.finish();
                                                }
                                            });
                                        }
                                    });
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "가입실패", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
            }
        });
    }

    public void profilebtn(View view) { //프로필 선택사진 눌렸을때  앨범선택 사진촬영 알림창 표시
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

        new AlertDialog.Builder(this)   //프로필 알림창 표시
                .setTitle("업로드할 이미지 선택")
                //.setPositiveButton("사진 촬영", cameraListener)
                .setNeutralButton("앨범 선택", albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();
    }

    private void captureCamera() {
        String state = Environment.getExternalStorageState();
        //외장 메모리 검사

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
                    //getUriForFile의 두번째 인자는 Manifest provider의 authorites와 일치해야함

                    Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    imageUri = providerURI;

                    // 인텐트에 전달할 때는 FileProvider의 Return값인 content://로만, providerURI의 값에 카메라 데이터를 넣어 보냄
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } else {
            Toast.makeText(this, "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
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
        //해당 경로에 있는 파일을 객체화 (새로 파일을 만든다는 것)

        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public void cropImage() {
        Log.i("cropImage", "Call");
        Log.i("cropImage", "photoURI : " + photoURI + "/albumURI : " + albumURI);

        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        //50x50픽셀 미만은 편집할수 없다는 문구 처리 + 갤러리, 포토 둘다 호환하는 방법
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        cropIntent.setDataAndType(photoURI, "image/*");

        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 200);
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);

        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output", albumURI);//크랍된 이미지를 해당경로에 저장

        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //선택한 사진 데이터 처리
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Log.i("REQUEST_TAKE_PHOTO","OK");
                        galleryAddPic();

                        profilebtn.setImageURI(imageUri);
                    } catch (Exception e) {
                        Log.e("REQUEST_TAKE_PHOTO", e.toString());
                    }
                } else {
                    Toast.makeText(MembershipPopup.this, "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
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
                    profilebtn.setImageURI(albumURI);
                }
                break;
        }
    }
    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //처음 호출엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA))){
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent =new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package: "+getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},MY_PERMISSION_CAMERA);
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //권한 요청 결과
        switch (requestCode){
            case MY_PERMISSION_CAMERA:
                for (int i=0;i<grantResults.length;i++){
                    if (grantResults[i]<0){
                        Toast.makeText(MembershipPopup.this,"해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    @Override
    public void onClick(View view) {

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK)
        {
            profileImgBtn.setImageURI(data.getData());
            imageUri = data.getData();
        }
   */}



