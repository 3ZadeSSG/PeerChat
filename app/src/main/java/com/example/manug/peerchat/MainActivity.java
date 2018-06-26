package com.example.manug.peerchat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    String message="";
    EditText messageTextView;
    EditText ip;
    EditText port;
    TextView responseTextView;
    static MessageAdapter mAdapter;
    ListView messageList;
    ArrayList<Message> messageArray;
    EditText portText;
    int myport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageList = findViewById(R.id.message_list);
        messageArray = new ArrayList<Message>();
        portText = findViewById(R.id.myPortEditText);
        mAdapter = new MessageAdapter(this, messageArray);
        messageList.setAdapter(mAdapter);
        messageTextView=findViewById(R.id.messageEditText);
        ip=findViewById(R.id.ipEditText);
        port=findViewById(R.id.portEditText);
    }

    public void startServer(View view) {
        myport = Integer.parseInt(portText.getText().toString());
        Server s = new Server(messageList, messageArray, myport);
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
                clientSocket.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return msg;
        }
        protected void onPostExecute(String result) {
            messageArray.add(new Message("Sent: " + result, 0));
            messageList.setAdapter(mAdapter);
        }
    }
}
