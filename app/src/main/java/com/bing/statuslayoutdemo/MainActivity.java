package com.bing.statuslayoutdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bing.statuslayout.StatusLayout;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StatusLayout layout = findViewById(R.id.statusLayout);
		layout.showViewByStatus(StatusLayout.STATUS_EMPTY);
		layout.setOnEmptyClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
