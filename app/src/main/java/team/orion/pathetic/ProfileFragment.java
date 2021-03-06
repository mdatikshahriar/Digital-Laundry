package team.orion.pathetic;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private EditText UserName, FullName, Address, PhoneNo;
    private Button UpdateProfileButton;
    private CircleImageView ProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private FirebaseStorage firebaseStorage;

    String CurrentUserID;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        UserName = v.findViewById(R.id.profile_username);
        FullName = v.findViewById(R.id.profile_fullname);
        Address = v.findViewById(R.id.profile_address);
        PhoneNo = v.findViewById(R.id.profile_phone);
        UpdateProfileButton = v.findViewById(R.id.profile_update_button);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.child("Customers").child(CurrentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild("username")) {
                                String retrieveUserName = (String) dataSnapshot.child("username").getValue();
                                UserName.setText(retrieveUserName);

                            }
                            if (dataSnapshot.hasChild("fullname")) {
                                String retrieveFullName = (String) dataSnapshot.child("fullname").getValue();
                                FullName.setText(retrieveFullName);

                            }
                            if ((dataSnapshot.hasChild("Defaddress"))) {
                                String retrieveAddress = (String) dataSnapshot.child("Defaddress").getValue();
                                Address.setText(retrieveAddress);

                            }
                            if ((dataSnapshot.hasChild("phoneNo"))) {
                                String retrievePhoneNo = (String) dataSnapshot.child("phoneNo").getValue();
                                PhoneNo.setText(retrievePhoneNo);

                            }
                        }

                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("username")) && (dataSnapshot.hasChild("fullname")) && (dataSnapshot.hasChild("Defaddress")) && (dataSnapshot.hasChild("phoneNo")) && (dataSnapshot.hasChild("image"))) {
                            String retrieveUserName = (String) dataSnapshot.child("username").getValue();
                            String retrieveFullName = (String) dataSnapshot.child("fullname").getValue();
                            String retrieveAddress = (String) dataSnapshot.child("Defaddress").getValue();
                            String retrievePhoneNo = (String) dataSnapshot.child("phoneNo").getValue();
                            String retrieveImage = (String) dataSnapshot.child("image").getValue();

                            UserName.setText(retrieveUserName);
                            FullName.setText(retrieveFullName);
                            Address.setText(retrieveAddress);
                            PhoneNo.setText(retrievePhoneNo);
                        } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("username")) && (dataSnapshot.hasChild("fullname")) && (dataSnapshot.hasChild("Defaddress")) && (dataSnapshot.hasChild("phoneNo"))) {
                            String retrieveUserName = (String) dataSnapshot.child("username").getValue();
                            String retrieveFullName = (String) dataSnapshot.child("fullname").getValue();
                            String retrieveAddress = (String) dataSnapshot.child("Defaddress").getValue();
                            String retrievePhoneNo = (String) dataSnapshot.child("phoneNo").getValue();

                            UserName.setText(retrieveUserName);
                            FullName.setText(retrieveFullName);
                            Address.setText(retrieveAddress);
                            PhoneNo.setText(retrievePhoneNo);
                        } else {
                            //Toast.makeText(HomeFragment.this,"abc",Toast.LENGTH_LONG).show();
                            //Toast.makeText(HomeFragment.this, "Please set & update your profile information...", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return v;
    }

}
