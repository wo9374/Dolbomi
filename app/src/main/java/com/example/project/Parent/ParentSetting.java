package com.example.project.Parent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project.LoginActivity;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

public class ParentSetting extends Activity {

    private static final int REQUEST_TAKE_ALBUM = 3333;
    final Context context = this;
    LinearLayout childManger;
    String uid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_setting);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            getActionBar().setBackgroundDrawable(getDrawable(R.color.colorPrimary));
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.psignout:
                DialogInterface.OnClickListener signcancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                DialogInterface.OnClickListener signoutListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getApplicationContext(), "???????????? ???????????????.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ParentSetting.this, LoginActivity.class));
                        finish();

                    }
                };
                new AlertDialog.Builder(this)   //????????? ????????? ??????
                        .setTitle("????????????")
                        .setMessage("???????????? ???????????????????")
                        .setPositiveButton("??????", signoutListener)
                        .setNegativeButton("??????", signcancelListener)
                        .show();
                break;

            case R.id.pdeleteuser:

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseStorage.getInstance().getReference().child("/userImages").child(uid).delete();

                        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ParentSetting.this, LoginActivity.class));
                                finish();
                            }
                        });
                    }
                };

                new AlertDialog.Builder(this)   //????????? ????????? ??????
                        .setTitle("?????? ??????")
                        .setMessage("????????? ????????? ????????? ??? ????????????.\n ?????????????????????????")
                        .setPositiveButton("??????", positiveListener)
                        .setNegativeButton("??????", cancelListener)
                        .show();
                break;
        }

    }
}

