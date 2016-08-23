package com.example.ahmad.patientmanagement;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class StaffLogin extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    Context context;

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        //Setting the NFC Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Toast.makeText(this,
                    "NFC NOT supported on this devices!",
                    Toast.LENGTH_LONG).show();
            finish();
        }else if(!nfcAdapter.isEnabled()){
            Toast.makeText(this,
                    "NFC NOT Enabled!",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Creates a new Intent each time
        Intent intent = getIntent();
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            //Toast.makeText(this, "onResume() - ACTION_TECH_DISCOVERED" Toast.LENGTH_SHORT).show();

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(tag == null){
            }else{
                int temp = 0;
                String tagInfo = "";
                byte[] tagId = tag.getId();
                for(int i=0; i<tagId.length; i++){
                    tagInfo = Integer.toHexString(tagId[0] & 0xFF);
                    temp += Integer.valueOf(tagInfo,16);
                }
                String tempp = Integer.toString(temp);
                StaffBackgroundWorker staffBackgroundWorker = new StaffBackgroundWorker(StaffLogin.this);
                staffBackgroundWorker.execute(tempp);
                //new BackgroundWorker(this).execute();

            }
        }else{
            Toast.makeText(this,
                    "onResume() : " + action,
                    Toast.LENGTH_SHORT).show();
        }

    }
}
