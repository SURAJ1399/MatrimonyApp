package com.ombre.matrimonialapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Welcome extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    List<ProfileModel> homeModels;
    ProfileModel homeModel;
    Adapter_Welcome adapter_home;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        recyclerView = findViewById(R.id.recyclerview);
        homeModels = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(Welcome.this,LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);

        adapter_home = new Adapter_Welcome(homeModels);
        recyclerView.setAdapter(adapter_home);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.BLACK);

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);


        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

                switch (menuItem.getItemId()) {


                    case R.id.nav_profile:

                        getProfile();
                }

                drawer.closeDrawers();


                //   loadHomeFragment();

                return true;
            }
        });

    }




    public void  getProfile()
    {
        final ProgressDialog progressDialog=new ProgressDialog(Welcome.this);
        progressDialog.setMessage("Fetching...");
        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, "https://pseudoblog.in/Matrimony/getprofile.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
           //     Toast.makeText(Welcome.this, ""+response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject=new JSONObject(response);



                    String success =jsonObject.getString("success");
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    Log.e("state",success);

                    if(success.equals("1")&& jsonArray.length()>0) {
                        Intent intent = new Intent(Welcome.this, ProfileUpdate.class);
                    startActivity(intent);
//                        for(int i=0;i<jsonArray.length();i++)
//                        {
//                            JSONObject object=jsonArray.getJSONObject(i);
//                            name.setText(object.getString("name"));
//                            job.setText(object.getString("job"));
//                            contactno.setText(object.getString("cno"));
//                            whatsappno.setText(object.getString("wno"));
//                            caste.setText(object.getString("caste"));
//                            date.setText(object.getString("dob"));
//                            time.setText(object.getString("tob"));
//
//
//
//                            salary=object.getString("salary");
//                            religion=object.getString("religion");
//                            height=object.getString("height");
//                            weight=object.getString("weight");
//                            color=object.getString("color");
//                            int position =    spinner2.getItems().indexOf(salary);
//                            spinner2.setSelectedIndex(position);
//
//                            int position1 =    spinner3.getItems().indexOf(weight);
//                            spinner3.setSelectedIndex(position1);
//                            int position2 =    spinner4.getItems().indexOf(height);
//                            spinner4.setSelectedIndex(position2);
//                            int position3 =    spinner5.getItems().indexOf(color);
//                            spinner5.setSelectedIndex(position3);
//                            int position4 =    spinner6.getItems().indexOf(religion);
//                            spinner6.setSelectedIndex(position4);
//
//
//                            Glide.with(ProfileUpdate.this).load("https://pseudoblog.in/Matrimony/"+object.getString("image")).into(circleImageView);
//
//                            cc=object.getString("image");
//
//
//
//                        }

                    }
                    else
                    {
                        Intent intent = new Intent(Welcome.this, Profile.class);
                        startActivity(intent);
                    }


                }catch (JSONException e)
                {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Welcome.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }


        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String string  = dateFormat.format(new Date());

                Map<String,String>params=new HashMap<>();
                params.put("userid",firebaseAuth.getCurrentUser().getUid());

                // params.put("cnumber",cnumber.getText().toString());


                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Welcome.this);
        requestQueue.add(request);

    }
}
