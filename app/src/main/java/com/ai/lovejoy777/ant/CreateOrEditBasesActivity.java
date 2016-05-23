package com.ai.lovejoy777.ant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class CreateOrEditBasesActivity extends AppCompatActivity implements View.OnClickListener {


    private ExampleDBHelper dbHelper ;

    ScrollView scrollView1;
    RelativeLayout MRL1;
    Toolbar toolBar;

    TextView textViewName;
    TextView textViewLocalip;
    TextView textViewPort;

    EditText nameEditText;
    EditText localipEditText;
    EditText portEditText;

    Button saveButton;
    LinearLayout buttonLayout;
    Button editButton, deleteButton;

    TextView titleTextView;

    int baseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseID = getIntent().getIntExtra(MainActivity.KEY_EXTRA_BASE_ID, 0);

        setContentView(R.layout.activity_edit_base);

        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        MRL1 = (RelativeLayout) findViewById(R.id.MRL1);
        toolBar = (Toolbar) findViewById(R.id.toolbar);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewLocalip = (TextView) findViewById(R.id.textViewLocalip);
        textViewPort = (TextView) findViewById(R.id.textViewPort);

        titleTextView.setText("Create Base");

        nameEditText = (EditText) findViewById(R.id.editTextName);
        localipEditText = (EditText) findViewById(R.id.editTextLocalip);
        portEditText = (EditText) findViewById(R.id.editTextPort);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(this);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        dbHelper = new ExampleDBHelper(this);



        if(baseID > 0) {
            saveButton.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.VISIBLE);

            titleTextView = (TextView) findViewById(R.id.titleTextView);
            titleTextView.setText("Edit Base");

            Cursor rs = dbHelper.getBase(baseID);
            rs.moveToFirst();
            String baseName = rs.getString(rs.getColumnIndex(ExampleDBHelper.BASE_COLUMN_NAME));
            String baseLocalip = rs.getString(rs.getColumnIndex(ExampleDBHelper.BASE_COLUMN_LOCALIP));
            String basePort = rs.getString(rs.getColumnIndex(ExampleDBHelper.BASE_COLUMN_PORT));
            if (!rs.isClosed()) {
                rs.close();
            }

            nameEditText.setText(baseName);
            nameEditText.setFocusable(false);
            nameEditText.setClickable(false);

            localipEditText.setText((CharSequence) baseLocalip);
            localipEditText.setFocusable(false);
            localipEditText.setClickable(false);

            portEditText.setText((CharSequence) (basePort + ""));
            portEditText.setFocusable(false);
            portEditText.setClickable(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                persistBase();
                return;
            case R.id.editButton:
                saveButton.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.GONE);
                nameEditText.setEnabled(true);
                nameEditText.setFocusableInTouchMode(true);
                nameEditText.setClickable(true);

                localipEditText.setEnabled(true);
                localipEditText.setFocusableInTouchMode(true);
                localipEditText.setClickable(true);

                portEditText.setEnabled(true);
                portEditText.setFocusableInTouchMode(true);
                portEditText.setClickable(true);
                return;
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteBase);
                builder.setInverseBackgroundForced(true)

                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deleteBase(baseID);
                                dbHelper.deleteAllBaseEntries(baseID);
                                Toast.makeText(getApplicationContext(), "Deleted Base Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Delete Base?");
                d.show();
                return;
        }
    }

    public void persistBase() {
        if(baseID > 0) {
            if(dbHelper.updateBase(baseID, nameEditText.getText().toString(),
                    localipEditText.getText().toString(),
                    portEditText.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Base Update Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Base Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(dbHelper.insertBase(nameEditText.getText().toString(),
                    localipEditText.getText().toString(),
                    portEditText.getText().toString())){
                Toast.makeText(getApplicationContext(), "Base Inserted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Could not Insert Base", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


}
