package team.orion.pathetic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText UserName,FullName,PhoneNo,Address;
    private Button SaveInformationButton;
    private CircleImageView profileImage;
    private TextView textView;
    private ProgressDialog loadingBar;



    private FirebaseAuth mAuth;
    private DatabaseReference UserRef,RootRef;
    private StorageReference UserImagesRef;

    String currentUserID;
    final static int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Customers").child(currentUserID);
        UserImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        UserName = (EditText) findViewById(R.id.setup_username);
        FullName = (EditText) findViewById(R.id.setup_fullname);
        PhoneNo = (EditText) findViewById(R.id.setup_phone_number);
        Address = (EditText) findViewById(R.id.setup_address);
        SaveInformationButton = (Button) findViewById(R.id.setup_button);
        profileImage = (CircleImageView) findViewById(R.id.setup_profile_image);
        textView = (TextView) findViewById(R.id.setup_textview);
        loadingBar = new ProgressDialog(this);

        SaveInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountSetupInformation();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("image")){
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(profileImage);

                    }
                    else{
                        Toast.makeText(SetupActivity.this, "Please select profile Image first",Toast.LENGTH_LONG).show();

                    }

                    //Picasso.get().load(image).into(profileImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    //a method for displaying cropped image from gallery and save it in db
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null){
            Uri ImageUri = data.getData();

            //code for cropping image
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK)
            {
                if(resultCode == RESULT_OK){

                    loadingBar.setTitle("set Profile Image");
                    loadingBar.setMessage("Please wait,while your profile is uploading..");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    Uri resultUri = result.getUri();//getting the result;

                    //storing the result in firebase storage
                    StorageReference filePath = UserImagesRef.child(currentUserID + ".jpg");
                    filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SetupActivity.this, "Profile Image Uploaded successfully",Toast.LENGTH_LONG).show();

                                //saving the link in db
                                final String downloadUrl = task.getResult().getMetadata().getReference().getDownloadUrl().toString();

                                UserRef.child("image").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Intent selfIntent = new Intent(SetupActivity.this,SetupActivity.class);
                                                    Toast.makeText(SetupActivity.this, "Image stored in Database,Successfully.. .",Toast.LENGTH_LONG).show();
                                                    loadingBar.dismiss();

                                                }
                                                else{
                                                    String messege = task.getException().toString();
                                                    Toast.makeText(SetupActivity.this, "Attention: "+messege,Toast.LENGTH_LONG).show();
                                                    loadingBar.dismiss();

                                                }
                                            }
                                        });
                            }
                            else{
                                String messege = task.getException().toString();
                                Toast.makeText(SetupActivity.this, "Attention: "+messege,Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();

                            }
                        }
                    });

                }
            }
        }
    }

    private void SaveAccountSetupInformation() {
        String username = UserName.getText().toString();
        String fullname = FullName.getText().toString();
        String phone = PhoneNo.getText().toString();
        String address = Address.getText().toString();


        if(TextUtils.isEmpty(username) ){
            UserName.requestFocus();
            UserName.setError("A Username is must required!");
            return;
        }

        else if(TextUtils.isEmpty(fullname) ){
            FullName.requestFocus();
            FullName.setError("A Full name is required to Continue!");
            return;
        }

        else if(TextUtils.isEmpty(phone) ){
            PhoneNo.requestFocus();
            PhoneNo.setError("How we will know it is you? type it now!");
            return;
        }

        else if(TextUtils.isEmpty(address) ){
            Address.requestFocus();
            Address.setError("From where to pickup you regular? Default Address will save your time!");
            return;
        }
        /*else if(!profileImage.isSelected()){
            //profileImage.requestFocus();
            Toast.makeText(SetupActivity.this,"Please set a profile image first!", Toast.LENGTH_LONG).show();
            //return;
        }*/

        else{

            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait,while we are creating your new Account completely..");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            //inserting info into Fdb
            HashMap userMap = new HashMap();
            userMap.put("username",username);
            userMap.put("fullname",fullname);
            userMap.put("phoneNo",phone);
            userMap.put("Defaddress",address);
            userMap.put("UID",currentUserID);
            userMap.put("adresstosend","please select");

            UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        SendUserToMainActivity();

                        Toast.makeText(SetupActivity.this,"your are ready to gooo!", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();

                    }
                    else{
                        String messege = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this,"Attention: "+messege, Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();

                    }
                }
            });
        }



    }


    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


}
