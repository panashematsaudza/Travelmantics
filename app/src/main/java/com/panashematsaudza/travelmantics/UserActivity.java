package com.panashematsaudza.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class UserActivity extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_activity_menu ,menu);
MenuItem insertMenu = menu.findItem(R.id.insert_menu);
if(FirebaseUtil.isAdmin){
    insertMenu.setVisible(true);

}else {

    insertMenu.setVisible(false);

}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.insert_menu:
                Intent intent = new Intent(this ,MainActivity.class);
                startActivity(intent);
                return true;
            case  R.id.logout_menu:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Logout", "User Logged out ");
                                FirebaseUtil.attachListener();
                            }
                        });
                    FirebaseUtil.detachListener();
                    return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.openFbReference("traveldeals",this);


        RecyclerView  rv_deals  = findViewById(R.id.rv_deals);
        final DealAdapter adapter = new DealAdapter();
        rv_deals.setAdapter(adapter);
        LinearLayoutManager dealLayoutManager =
                new LinearLayoutManager(this,RecyclerView.VERTICAL ,false);
        rv_deals.setLayoutManager(dealLayoutManager);
        FirebaseUtil.attachListener();
    }

    @Override
    protected void onPause() {
        super.onPause();

        FirebaseUtil.detachListener();
    }

    public void showMenu(){
        invalidateOptionsMenu();
    }
}
