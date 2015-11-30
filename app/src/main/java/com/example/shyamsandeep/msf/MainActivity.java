package com.example.shyamsandeep.msf;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String DEFAULT_DESTINATION = "9959333664";

    private String SENT_BROADCAST = "SMS_SENT";
    private String DELIVERED_BROADCAST = "SMS_DELIVERED";

    private SentBroadcastReceiver mSentReceiver;
    private DeliveredBroadcastReceiver mDeliveredReceiver;

    private IntentFilter mSentFilter;
    private IntentFilter mDeliveredFilter;

    Button sendsms;
    EditText CustomerName, AgentName, PhoneNo,HPNo,Amount, RceiptNum;

    private void sendSMS(final String phoneNumber, String message) {

        SmsManager sms = SmsManager.getDefault();

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT_BROADCAST), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED_BROADCAST), 0);
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private class SentBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    clearUI();

                    Toast.makeText(getBaseContext(), "SMS sent",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(getBaseContext(), "Generic failure",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(getBaseContext(), "No service",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(getBaseContext(), "Null PDU",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(getBaseContext(), "Radio off",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private class DeliveredBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {

                case Activity.RESULT_OK:
                    Toast.makeText(getBaseContext(), "SMS delivered",
                            Toast.LENGTH_SHORT).show();

                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getBaseContext(), "SMS not delivered",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mSentReceiver);
        unregisterReceiver(mDeliveredReceiver);

        super.onPause();
    }

    @Override
    protected void onResume () {
        super.onResume();

        registerReceiver(mSentReceiver, mSentFilter);
        registerReceiver(mDeliveredReceiver, mDeliveredFilter);

    }

    private void clearUI() {
        CustomerName.setText(null);
        AgentName = (EditText)findViewById(R.id.colAgentName);
        PhoneNo.setText(null);
        HPNo.setText(null);
        Amount.setText(null);
        RceiptNum.setText(null);
    }

    private boolean validateFields() {
        if(TextUtils.isEmpty(PhoneNo.getText()) ||
                TextUtils.getTrimmedLength(PhoneNo.getText()) < 10 ||
                TextUtils.isEmpty(CustomerName.getText()) ||
                TextUtils.getTrimmedLength(CustomerName.getText()) < 3 ||
                TextUtils.isEmpty(AgentName.getText()) ||
                TextUtils.getTrimmedLength(AgentName.getText()) < 3 ||
                TextUtils.getTrimmedLength(HPNo.getText()) < 3 ||
                TextUtils.isEmpty(HPNo.getText()) ||
                TextUtils.isEmpty(Amount.getText())||
                TextUtils.getTrimmedLength(Amount.getText()) < 3||
                TextUtils.isEmpty(RceiptNum.getText())||
                TextUtils.getTrimmedLength(RceiptNum.getText()) < 3

                ) {
            Toast.makeText(this, "Please enter valid data!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        mSentReceiver = new SentBroadcastReceiver();
        mDeliveredReceiver = new DeliveredBroadcastReceiver();

        mSentFilter = new IntentFilter(SENT_BROADCAST);
        mDeliveredFilter = new IntentFilter(DELIVERED_BROADCAST);

        sendsms = (Button)findViewById(R.id.button);
        sendsms.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(!validateFields()) {
                    return;
                }
                
                String no=PhoneNo.getText().toString();
                String msg = "Dear " + CustomerName.getText().toString() + " Your Payment of Rs. ";
                msg= msg + Amount.getText().toString()+ " for HP# " + HPNo.getText().toString() +
                        " is recieved by our Agent " + AgentName.getText().toString() + " for receipt " + RceiptNum.getText().toString();

                sendSMS(no, msg);
                sendSMS(DEFAULT_DESTINATION, msg);

            }
        });

        CustomerName = (EditText)findViewById(R.id.custName);
        AgentName = (EditText)findViewById(R.id.colAgentName);
        PhoneNo = (EditText)findViewById(R.id.phoneNum);
        HPNo = (EditText)findViewById(R.id.hpNum);
        Amount = (EditText)findViewById(R.id.amount);
        RceiptNum = (EditText)findViewById(R.id.RceiptNum);

    }



}