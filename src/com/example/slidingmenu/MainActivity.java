package com.example.slidingmenu;

import com.example.slidingmenu.view.SlidingMenu;

import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

	private SlidingMenu leftMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        leftMenu = (SlidingMenu) findViewById(R.id.id_menu);
    }
    
    public void toggleMenu(View view){
    	
    	leftMenu.toggle();
    }
}
