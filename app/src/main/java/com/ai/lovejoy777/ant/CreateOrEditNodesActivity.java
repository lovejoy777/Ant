package com.ai.lovejoy777.ant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lovejoy777 on 03/10/15.
 */
public class CreateOrEditNodesActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String KEY_EXTRA_BASE_ID = "KEY_EXTRA_BASE_ID";
    public final static String KEY_EXTRA_BASE_NAME = "KEY_EXTRA_BASE_NAME";

    ScrollView scrollView1;
    RelativeLayout MRL1;
    Toolbar toolBar;
    Spinner swType;
    Spinner swNum;
    TextView titleTextView, textViewName, textViewAddress, textViewRSAddress, textViewType, textViewSwnum, textViewSw1, textViewSw2, textViewSw3, textViewSw4;
    EditText nameEditText, addressEditText, RSaddressEditText, typeEditText, swnumEditText, sw1EditText, sw2EditText, sw3EditText, sw4EditText;
    TextView base_idText;
    LinearLayout buttonLayout;
    Button saveButton, editButton, deleteButton;

    int nodeID;
    int baseID;
    String baseName;
    private BaseNodeDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_node);

        // Toast.makeText(getApplicationContext(), "Trip used " + tripID, Toast.LENGTH_SHORT).show();
        nodeID = getIntent().getIntExtra(MainActivityNodes.KEY_EXTRA_NODE_ID, 0);
        baseID = getIntent().getIntExtra(MainActivityNodes.KEY_EXTRA_BASE_ID, 0);
        baseName = getIntent().getStringExtra(MainActivityNodes.KEY_EXTRA_BASE_NAME);

        scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
        MRL1 = (RelativeLayout) findViewById(R.id.MRL1);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        titleTextView = (TextView) findViewById(R.id.titleTextView);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewRSAddress = (TextView) findViewById(R.id.textViewRSAddress);
        textViewType = (TextView) findViewById(R.id.textViewType);
        textViewSwnum = (TextView) findViewById(R.id.textViewSwnum);
        textViewSw1 = (TextView) findViewById(R.id.textViewSw1);
        textViewSw2 = (TextView) findViewById(R.id.textViewSw2);
        textViewSw3 = (TextView) findViewById(R.id.textViewSw3);
        textViewSw4 = (TextView) findViewById(R.id.textViewSw4);

        nameEditText = (EditText) findViewById(R.id.editTextName);
        addressEditText = (EditText) findViewById(R.id.editTextAddress);
        RSaddressEditText = (EditText) findViewById(R.id.editTextRSAddress);
        typeEditText = (EditText) findViewById(R.id.editTextType);
        swType = (Spinner) findViewById(R.id.swType);
        swnumEditText = (EditText) findViewById(R.id.editTextSwnum);
        swNum = (Spinner) findViewById(R.id.swNum);
        sw1EditText = (EditText) findViewById(R.id.editTextSw1);
        sw2EditText = (EditText) findViewById(R.id.editTextSw2);
        sw3EditText = (EditText) findViewById(R.id.editTextSw3);
        sw4EditText = (EditText) findViewById(R.id.editTextSw4);

        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        saveButton = (Button) findViewById(R.id.saveButton);
        editButton = (Button) findViewById(R.id.editButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        base_idText = (TextView) findViewById(R.id.TextViewBase_ID);

        titleTextView.setText("Create Node");

        // Switch Type Item Selected Listener
        swType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String item = adapter.getItemAtPosition(position).toString();
                typeEditText.setText(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                typeEditText.setText("");
                // TODO Auto-generated method stub

            }
        });

        // Switch Number Item Selected Listener
        swNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String item = adapter.getItemAtPosition(position).toString();
                swnumEditText.setText(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //typeEditText.setText("");
                // TODO Auto-generated method stub

            }
        });

        // pre fill text fields
        base_idText.setText("" + baseID);
        addressEditText.setText("");
        RSaddressEditText.setText("");
        typeEditText.setText("");
        swnumEditText.setText("");
        sw1EditText.setText("");
        sw2EditText.setText("");
        sw3EditText.setText("");
        sw4EditText.setText("");

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(this);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        dbHelper = new BaseNodeDBHelper(this);

        if (nodeID > 0) {
            saveButton.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.VISIBLE);

            titleTextView = (TextView) findViewById(R.id.titleTextView);
            titleTextView.setText("Edit Node");

            Cursor rs = dbHelper.getNode(nodeID);
            rs.moveToFirst();
            String nodeName = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_NAME));
            String nodeAddress = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_ADDRESS));
            String nodeRSAddress = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_RSADDRESS));
            String nodeType1 = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_TYPE));
            String nodeSwnum1 = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_SWNUM));
            String nodeSw1 = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_SW1));
            String nodeSw2 = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_SW2));
            String nodeSw3 = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_SW3));
            String nodeSw4 = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_SW4));
            String nodeBase_ID = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_BASE_ID));
            if (!rs.isClosed()) {
                rs.close();
            }

            nameEditText.setText(nodeName);
            nameEditText.setFocusable(false);
            nameEditText.setClickable(false);

            addressEditText.setText((CharSequence) nodeAddress);
            addressEditText.setFocusable(false);
            addressEditText.setClickable(false);

            RSaddressEditText.setText((CharSequence) nodeRSAddress);
            RSaddressEditText.setFocusable(false);
            RSaddressEditText.setClickable(false);

            typeEditText.setText(nodeType1);
            typeEditText.setFocusable(false);
            typeEditText.setClickable(false);

            swnumEditText.setText((nodeSwnum1 + ""));
            swnumEditText.setFocusable(false);
            swnumEditText.setClickable(false);

            sw1EditText.setText((CharSequence) (nodeSw1 + ""));
            sw1EditText.setFocusable(false);
            sw1EditText.setClickable(false);

            sw2EditText.setText((CharSequence) (nodeSw2 + ""));
            sw2EditText.setFocusable(false);
            sw2EditText.setClickable(false);

            sw3EditText.setText((CharSequence) (nodeSw3 + ""));
            sw3EditText.setFocusable(false);
            sw3EditText.setClickable(false);

            sw4EditText.setText((CharSequence) (nodeSw4 + ""));
            sw4EditText.setFocusable(false);
            sw4EditText.setClickable(false);

            base_idText.setText((CharSequence) nodeBase_ID);
            base_idText.setFocusable(false);
            base_idText.setClickable(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                persistNode();
                return;
            case R.id.editButton:
                saveButton.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.GONE);

                nameEditText.setEnabled(true);
                nameEditText.setFocusableInTouchMode(true);
                nameEditText.setClickable(true);

                addressEditText.setEnabled(true);
                addressEditText.setFocusableInTouchMode(true);
                addressEditText.setClickable(true);

                RSaddressEditText.setEnabled(true);
                RSaddressEditText.setFocusableInTouchMode(true);
                RSaddressEditText.setClickable(true);

                typeEditText.setEnabled(true);
                swType.setEnabled(true);
                swType.setClickable(true);

                swnumEditText.setEnabled(true);
                swNum.setEnabled(true);
                swNum.setClickable(true);

                sw1EditText.setEnabled(true);
                sw1EditText.setFocusableInTouchMode(true);
                sw1EditText.setClickable(true);

                sw2EditText.setEnabled(true);
                sw2EditText.setFocusableInTouchMode(true);
                sw2EditText.setClickable(true);

                sw3EditText.setEnabled(true);
                sw3EditText.setFocusableInTouchMode(true);
                sw3EditText.setClickable(true);

                sw4EditText.setEnabled(true);
                sw4EditText.setFocusableInTouchMode(true);
                sw4EditText.setClickable(true);

                base_idText.setEnabled(true);
                base_idText.setFocusableInTouchMode(true);
                base_idText.setClickable(true);
                return;

            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
                builder.setMessage(R.string.sure)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deleteNode(nodeID);
                                Toast.makeText(getApplicationContext(), "Deleted Node Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra(KEY_EXTRA_BASE_ID, baseID);
                                intent.putExtra(KEY_EXTRA_BASE_NAME, baseName);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle(R.string.deleteNode);
                d.show();
                return;
        }
    }

    public void persistNode() {
        if (nodeID > 0) {
            if (dbHelper.updateNode(nodeID,
                    nameEditText.getText().toString(),
                    addressEditText.getText().toString(),
                    RSaddressEditText.getText().toString(),
                    typeEditText.getText().toString(),
                    swnumEditText.getText().toString(),
                    sw1EditText.getText().toString(),
                    sw2EditText.getText().toString(),
                    sw3EditText.getText().toString(),
                    sw4EditText.getText().toString(),
                    base_idText.getText().toString())) {

                Toast.makeText(getApplicationContext(), "Node Update Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(KEY_EXTRA_BASE_NAME, baseName);
                intent.putExtra(KEY_EXTRA_BASE_ID, baseID);
                startActivity(intent);

            } else {
                Toast.makeText(getApplicationContext(), "Node Update Failed", Toast.LENGTH_SHORT).show();
            }

        } else {
            if (dbHelper.insertNode(nameEditText.getText().toString(),
                    addressEditText.getText().toString(),
                    RSaddressEditText.getText().toString(),
                    typeEditText.getText().toString(),
                    swnumEditText.getText().toString(),
                    sw1EditText.getText().toString(),
                    sw2EditText.getText().toString(),
                    sw3EditText.getText().toString(),
                    sw4EditText.getText().toString(),
                    base_idText.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Node Inserted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "Could not Insert Node", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), MainActivityNodes.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(KEY_EXTRA_BASE_NAME, baseName);
            intent.putExtra(KEY_EXTRA_BASE_ID, baseID);
            startActivity(intent);
        }
    }
}