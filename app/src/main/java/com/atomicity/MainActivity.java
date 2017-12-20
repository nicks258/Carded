package com.atomicity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;


public class MainActivity extends AppCompatActivity implements ProfilePage.OnFragmentInteractionListener {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         sharedPreferences = MainActivity.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("id","");
        Log.i("UserId->",userId);
        if (userId.equals(""))
        {
//            Toast.makeText(this, "Welcome Back " + sharedPreferences.getString("name", null), Toast.LENGTH_SHORT).show();
//            Fragment selectedFragment = new ProfilePage();
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container,selectedFragment);
//            fragmentTransaction.commit();
            Logger.addLogAdapter(new AndroidLogAdapter());
            Logger.i("Login first");
            Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            MainActivity.this.finish();
        }
        else {
            Toast.makeText(this, "Welcome Back " + sharedPreferences.getString("first_name", null), Toast.LENGTH_SHORT).show();
            TextView bloodGroup                 = findViewById(R.id.blood_group);
            TextView userProfileName            = findViewById(R.id.user_profile_name);
            TextView userEmail                  = findViewById(R.id.user_profile_short_bio);
            TextView mobileNumber               = findViewById(R.id.mobileNumber);
            TextView weigth                     = findViewById(R.id.weigth);
            TextView heigth                     = findViewById(R.id.height);
            TextView dob                        = findViewById(R.id.dob);
            userProfileName.setText(sharedPreferences.getString("first_name","user") + " " + sharedPreferences.getString("last_name","user"));
            userEmail.setText("Email : "+sharedPreferences.getString("email_id","user"));
            mobileNumber.setText("Address : "+sharedPreferences.getString("address","user"));
            weigth.setText("Description : " + sharedPreferences.getString("description","user"));
            heigth.setText("Hash Key : "+sharedPreferences.getString("hash_key","user"));
            dob.setText(sharedPreferences.getString("dob","user"));
            bloodGroup.setText(sharedPreferences.getString("bloodGroup","user"));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Log.i("Clear","Clear");
            editor.clear().apply();
            Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            MainActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
