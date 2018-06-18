package com.example.manug.peerchat;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
public class Server extends Thread {
    TextView t;
    public Server(TextView t){
        this.t=t;
    }
    ServerSocket welcomeSocket=null;
    int port=60500;
    @Override
    public void run(){
        try{
            String sentence;
            welcomeSocket=new ServerSocket(port);
            while (true){
                Socket connectionSocket=welcomeSocket.accept();
                HandleClient c= new HandleClient();
                c.execute(connectionSocket);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public class HandleClient extends AsyncTask<Socket,Void,String>{
        String sentence;
        @Override
        protected String doInBackground(Socket... sockets) {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(sockets[0].getInputStream()));
                sentence = input.readLine();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return sentence ;
        }
        protected void onPostExecute(String result) {
            String s=t.getText().toString();
            s=s+"\nReceived : "+result;
            t.setText(s);
        }
    }
}
