package com.ombre.matrimonialapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    String salary,religion,weight,height,color;
    MaterialEditText name,job,caste,time,date,contactno,whatsappno;

    LinearLayout linearLayout;
    Button btn,choose,choose1;
    Uri postimageuri;
    String encoded_image,encoded_image1;
    Bitmap bitmap;
    String cc="c";
    CircleImageView circleImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        name=findViewById(R.id.name);
        contactno=findViewById(R.id.mobileno);
        whatsappno=findViewById(R.id.whatsappno);
        job=findViewById(R.id.job);
        caste=findViewById(R.id.caste);
        btn=findViewById(R.id.sumbit);
        linearLayout=findViewById(R.id.ll);
        circleImageView=findViewById(R.id.image);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(name.getText().toString())||
                        TextUtils.isEmpty(job.getText().toString())||
                        TextUtils.isEmpty(salary)||
                        TextUtils.isEmpty(religion)||
                        TextUtils.isEmpty(weight)||
                        TextUtils.isEmpty(color)||
                        TextUtils.isEmpty(height)||
                        TextUtils.isEmpty(contactno.getText().toString())||
                        TextUtils.isEmpty(whatsappno.getText().toString())||
                        TextUtils.isEmpty(date.getText().toString())||
                        TextUtils.isEmpty(time.getText().toString())||TextUtils.isEmpty(encoded_image))
                {
                    Toast.makeText(Profile.this, "Complete Your Profile", Toast.LENGTH_SHORT).show();
                }
                else

                    setprofile();
            }
        });


        choose=findViewById(R.id.choose);
        choose1=findViewById(R.id.choose1);


        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        MaterialSpinner spinner2 = (MaterialSpinner)findViewById(R.id.salary);
        MaterialSpinner spinner3 = (MaterialSpinner)findViewById(R.id.weight);
        MaterialSpinner spinner4 = (MaterialSpinner)findViewById(R.id.height);
        MaterialSpinner spinner5 = (MaterialSpinner)findViewById(R.id.color);
        MaterialSpinner spinner6= (MaterialSpinner)findViewById(R.id.religion);


        spinner2.setItems("10k+","20k+","30k+","50k+","70k+","90k+","1Lkh+");
        spinner2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
          salary=item;

            }
        });



        spinner3.setItems("30Kg+","50Kg+","60Kg+","70Kg+","80Kg+");
        spinner3.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
weight=item;

            }
        });
        spinner4.setItems("5.1ft","5.2ft","5.3ft","5.4ft","5.5ft","5.6ft","5.7ft","5.8ft","5.9ft","6.0ft");
        spinner4.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) { height=item;

            }
        });
        spinner5.setItems("Fair","Dark");
        spinner5.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                color=item;

            }
        });
        spinner6.setItems("Hindu","Muslim","Sikhism","Christian","Jain","Buddhism");
        spinner6.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                religion=item;

            }
        });
        choose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                TimePickerDialog timePickerDialog=new TimePickerDialog(Profile.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay+":"+minute);

                    }
                },0,0,true);timePickerDialog.show();
            }
        });



        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Profile.this);
                // alertDialog.setTitle("ADD ROOM");
                //   alertDialog.setMessage(" PLZ ADD Room details");

                LayoutInflater inflater = LayoutInflater.from(Profile.this);
                final View date_layout = inflater.inflate(R.layout.layout_date, null);

                alertDialog.setView(date_layout);
                final CollapsibleCalendar collapsibleCalendar = date_layout.findViewById(R.id.calendarView);
                collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
                    @Override
                    public void onDaySelect() {
                        Day day = collapsibleCalendar.getSelectedDay();
                        Log.i(getClass().getName(), "Selected Day: "
                                + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());
                        ///   Toast.makeText(addmatch.this, , Toast.LENGTH_SHORT).show();
                        date.setText(day.getDay()+"-"+(day.getMonth()+1)+"-"+day.getYear());
                    }

                    @Override
                    public void onItemClick(View view) {

                    }

                    @Override
                    public void onDataUpdate() {

                    }

                    @Override
                    public void onMonthChange() {

                    }

                    @Override
                    public void onWeekChange(int i) {

                    }
                });

                alertDialog.show();


            }
        });

    }
    public void  setprofile()
    {
        final Context context=Profile.this;
        final ProgressDialog progressDialog=new ProgressDialog(context);
        final SharedPreferences prefs =Profile.this.getSharedPreferences("xy", Context.MODE_PRIVATE);

        if(prefs.getString("encodeurl", null)!=null){
            encoded_image1=prefs.getString("encodeurl",null);

        }
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, "https://pseudoblog.in/Matrimony/addprofile.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
                 Intent intent=new Intent(Profile.this,Welcome.class);
                 startActivity(intent);
                 finish();
//                try {
//
//                    JSONArray jsonArray=new JSONArray(response);
//                    //   Log.v("data",response);
//
//
//                    for(int i=0;i<jsonArray.length();i++)
//                    {
//                        JSONObject object=jsonArray.getJSONObject(i);
//                        String name=object.getString("subcategory");
//                        Log.v("name",name);
//
//                        String imageurl=object.getString("imageurl"
//                        );
//
//
//
//                        //Toast.makeText(AllPostActivity.this, ""+filter, Toast.LENGTH_SHORT).show();
//                        subCategorymodel=new SubcategoryModel(name,imageurl);
//                        categoryModelList.add(subCategorymodel);
//                        adapter_subcategory.notifyDataSetChanged();
//
////
////
////
////
////
////
////
////
////
//                    }
//
//
//                }
//
//                catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }


                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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

                params.put("name",name.getText().toString());
                params.put("job",job.getText().toString());
                params.put("cno",contactno.getText().toString());
                params.put("wno",whatsappno.getText().toString());

                params.put("salary",salary);
                params.put("religion",religion);
                params.put("weight",weight);
                params.put("color",color);
                params.put("height",height);


                params.put("dob",date.getText().toString());

                params.put("tob",time.getText().toString());
                params.put("caste",caste.getText().toString());
                if(cc.equals("n"))
                {
                    params.put("image",encoded_image1);
                }
                else
                {
                    params.put("image","m");
                }






                // params.put("cnumber",cnumber.getText().toString());


                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(request);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            postimageuri = data.getData();
            cc="n";
            try {

                bitmap= MediaStore.Images.Media.getBitmap(Profile.this.getContentResolver(),postimageuri);
                circleImageView.setImageURI(postimageuri);
                imagestore(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //  Bitmap bitmap = MediaStore.Images.Media.getBitmap( postimageuri);
            //  addimage.setImageBitmap(bitmap);


        }
    }

    public String imagestore(Bitmap bitmap) {
        linearLayout.setVisibility(View.INVISIBLE );

//        Toast.makeText(getActivity(), "Enter", Toast.LENGTH_SHORT).show();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] imagebytes=stream.toByteArray();
        encoded_image= Base64.encodeToString(imagebytes, Base64.DEFAULT);
        SharedPreferences.Editor editor = Profile.this.getSharedPreferences("xy", Context.MODE_PRIVATE).edit();
        editor.putString("encodeurl",encoded_image);
        editor.apply();
        //  Toast.makeText(getContext(), "fdd"+encoded_image, Toast.LENGTH_SHORT).show();
        Log.e("info", encoded_image);
        return  encoded_image;


    }
}
