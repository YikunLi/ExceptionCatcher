package com.le.mobile.exceptioncatcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ExceptionCatcher.ExceptionHandler {

    @Override
    public boolean handleException(Exception exception) {
        if (exception instanceof ClassCastException) {
            Toast.makeText(MainActivity.this, "Exception : " + exception + " has handled!",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExceptionCatcher.install();
        ExceptionCatcher.registerExceptionHandler(this);

        this.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 1 / 0;
            }
        });

        this.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v
                        .getLayoutParams();
            }
        });
    }
}
