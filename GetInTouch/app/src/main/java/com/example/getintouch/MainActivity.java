package com.example.getintouch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.getintouch.fragments.ChatsFragment;
import com.example.getintouch.fragments.UsersFragment;
import com.example.getintouch.model.ModelClass;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    // getting info from xml file
    private CircleImageView profileImageView;
    private TextView userProfileName;

    //Below code for the getting firebase instances
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting the toolbar
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //getting the id's of the CircleImageView and TextView

        profileImageView = findViewById(R.id.userProfileImage);
        userProfileName = findViewById(R.id.userProfileName);

        // below code for getting the info from the firebase

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = databaseReference.child(firebaseUser.getUid());
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ModelClass user = dataSnapshot.getValue(ModelClass.class);
                userProfileName.setText(user.getUsername());

//                if(user.getUserImageUrl().equals("")){
//                    profileImageView.setImageResource(R.mipmap.ic_launcher_round);
//                }else{
//                    Glide.with(MainActivity.this).load(user.getUserImageUrl()).into(profileImageView);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // below code is for the tabLayout of the app
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        viewPageAdapter.addFragment(new ChatsFragment(),"Chats");
        viewPageAdapter.addFragment(new UsersFragment(),"Users");
        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    // below code is to show the menu on the toolbar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,StartActivity.class));
                finish();
                return true;
        }
        return false;
    }


    // create Adapter for ViewerPage for tabLayout

    class ViewPageAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPageAdapter(@ NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        //we create this method to add the fragments and and their title
        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
