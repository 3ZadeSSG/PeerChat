package com.example.manug.peerchat;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    String message="";
    EditText messageTextView;
    EditText ip;
    EditText port;
    TextView responseTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageTextView=findViewById(R.id.messageEditText);
        responseTextView=findViewById(R.id.resultTextView);
        ip=findViewById(R.id.ipEditText);
        port=findViewById(R.id.portEditText);
        Server s=new Server(responseTextView);
        s.start();
    }
    public void sendResponse(View view){
       Client c =new Client();
       c.execute();
    }
    public void setView(String s){
        String str=responseTextView.getText().toString();
        str=str+"\nReceived: "+s;
        responseTextView.setText(str);

    }
    public class Client extends AsyncTask<Void,Void,String>{
        String msg;
        @Override
        protected String doInBackground(Void... voids) {
            try {
                msg = messageTextView.getText().toString();
                String ipadd = ip.getText().toString();
                int portr = Integer.parseInt(port.getText().toString());
                Socket clientSocket = new Socket(ipadd, portr);
                OutputStream outToServer =clientSocket.getOutputStream();
                PrintWriter output = new PrintWriter(outToServer);
                output.println(msg);
                output.flush();
               // BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
               // response = input.readLine();
                clientSocket.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return msg;
        }
        protected void onPostExecute(String result) {
            responseTextView.setText(responseTextView.getText().toString()+"\nSent : "+result);
        }
    }
}
