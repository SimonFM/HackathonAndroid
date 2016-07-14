package com.sourcey.hackathon;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PaymentActivity extends AppCompatActivity {

    @Bind(R.id.nameField) EditText _name;
    @Bind(R.id.cardNumber) EditText _cardNumber;
    @Bind(R.id.cvcField) EditText _cvcField;
    @Bind(R.id.month) EditText _month;
    @Bind(R.id.amountField) EditText _amount;

    @Bind(R.id.payNowButton) Button _paymentButton;

    Dialog dialog;

    private String host = "http://10.0.2.2:4000";
    private final String paymentURL = host + "/payment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        dialog = new Dialog(this);
        ButterKnife.bind(this);

        dialog.setContentView(R.layout.activity_booking_dialog);
        dialog.setTitle("Are you sure?");



        _paymentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button dismissButton = (Button) dialog.findViewById(R.id.button);
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                Button confirmButton = (Button) dialog.findViewById(R.id.button2);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makePayment();
                    }
                });
            }
        });
    }

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.arg1){
                case 1:
                    Toast.makeText(getApplicationContext(), "Payment successful", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "Unable to pay this slot, please try again", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };



    private void makePayment(){

        final String name = _name.getText().toString();
        final String cardnumber = _cardNumber.getText().toString();
        final String cvc = _cvcField.getText().toString();
        final String month = _month.getText().toString();
        final String amounToPay = _amount.getText().toString();

        final boolean[] success = {true};

        new Thread(new Runnable(){
            @Override
            public void run() {
                // Your implementation goes here
                try{
                    OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "{\r\n  \"amount\": \"" + amounToPay + "\", \r\n  \"description\": \"Test Payment\", \r\n  \"currency\": \"USD\"\r\n}");
                    Request request = new Request.Builder()
                            .url(paymentURL)
                            .post(body)
                            .addHeader("content-type", "application/json")
                            .addHeader("cache-control", "no-cache")
                            .build();

                    Response response = client.newCall(request).execute();

                    if(response.isSuccessful()){
                        Message msg = handler.obtainMessage();
                        msg.arg1 = 1;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = handler.obtainMessage();
                        msg.arg1 = 2;
                        handler.sendMessage(msg);
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

}
