package com.victor.oprica.quyzygy20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.victor.oprica.quyzygy20.entities.Identity;
import com.victor.oprica.quyzygy20.entities.WebSocketClientPacket;
import com.victor.oprica.quyzygy20.entities.WebSocketServerPacket;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class EnterRoomActivity extends AppCompatActivity {
    EditText et_invite;
    Button joinBtn;
    TextView output;
    private SharedPreferences keyPreferences;
    private OkHttpClient client;
    private Identity identity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_room);

        et_invite = findViewById(R.id.et_invitecode);
        joinBtn = findViewById(R.id.btn_invitecode);
        output = findViewById(R.id.outputtv);
        identity = new Identity();
        keyPreferences = getSharedPreferences("keyPrefs", MODE_PRIVATE);
        identity.SecretKey = keyPreferences.getString("sk", "");
    }

    public void enterRoom(View view) {
        if(et_invite.length() == 4){
            identity.AccessCode = Integer.parseInt(et_invite.getText().toString());
            et_invite.setEnabled(false);
            joinBtn.setEnabled(false);
            start();
        }
        else Toast.makeText(this, "Access Code not complete!", Toast.LENGTH_SHORT).show();
    }

    public void navigateToStudentGrades(View view) {
        Intent explicitIntent = new Intent(EnterRoomActivity.this, StudentGradesActivity.class);
        startActivity(explicitIntent);
    }

    public void navigateToMainActivity(View view){
        Intent explicitIntent = new Intent(EnterRoomActivity.this, MainActivity.class);
        startActivity(explicitIntent);
    }

    private final class EchoWebSocketListener extends WebSocketListener {

        private static final int NORMAL_CLOSURE_STATUS = 1000;
        public Identity identity;
        public EchoWebSocketListener(Identity i){
            identity = i;
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            if(this.identity.WSID == "null" || this.identity.WSID == null)
                this.identity.WSID = "";
            webSocket.send("{" + identity.toJson() + "}");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output(text);
            WebSocketServerPacket packet = new Gson().fromJson(text, WebSocketServerPacket.class);

            if (packet.Success){
                if (identity.WSID == null || identity.WSID == ""){
                    identity.WSID = packet.Data;
                    WebSocketClientPacket packe0t = new WebSocketClientPacket();
                    packe0t.Identity = identity;
                    packe0t.Action = "JoinQuiz";
                    webSocket.send(packe0t.toJson());
                }
                else{
                    if (packet.Action != null && packet.Action.equals("QuizStarted")){
                        webSocket.close(NORMAL_CLOSURE_STATUS, null);
                        enableControls();
                        navigateToQuiz(identity);
                    }
                }
                setJoinButton("Joining...");
            }
            else{
                if (packet.Data.equals("Invalid Access Code")){
                    invalidQuiz();
                    setJoinButton(getString(R.string.enter));
                }
                /*
                webSocket.close(NORMAL_CLOSURE_STATUS, null);
                enableControls();
                */
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
            enableControls();
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }

    private void setJoinButton(final String string){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                joinBtn.setText(string);
            }
        });
    }

    private void start() {
        output.setText("");
        client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://" + getString(R.string.ip) + ":8082").build();
        EchoWebSocketListener listener = new EchoWebSocketListener(identity);
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private void enableControls(){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                setJoinButton(getString(R.string.enter));
                et_invite.setEnabled(true);
                et_invite.removeTextChangedListener(watcher);
                joinBtn.setEnabled(true);
            }
        });
    }

    private void invalidQuiz(){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                setJoinButton(getString(R.string.enter));
                et_invite.setEnabled(true);
                et_invite.addTextChangedListener(watcher);
                et_invite.setText("");
                et_invite.setHint("Invalid A.C.");
                Toast.makeText(EnterRoomActivity.this, "Invalid Access Code!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToQuiz(final Identity i){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                identity = i;
                keyPreferences.edit().putString("wsid", i.WSID).commit();
                keyPreferences.edit().putString("acc", String.valueOf(i.AccessCode)).commit();

                Toast.makeText(getApplicationContext(), "Quiz started!", Toast.LENGTH_LONG).show();
                Intent explicitIntent = new Intent(EnterRoomActivity.this, QuizActivity.class);
                et_invite.setText("");
                startActivityForResult(explicitIntent, 1);
            }
        });
    }

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
    }

    public void navigateToMapsFromRoom(View view){
        Intent explicitIntent = new Intent(EnterRoomActivity.this, PermissionActivity.class);
        startActivity(explicitIntent);
    }

    public void navigateToLearn(View view){
        Intent explicitIntent = new Intent(EnterRoomActivity.this, HomeLearn.class);
        startActivity(explicitIntent);
    }

    public void navigateToTest(View view){
        Intent explicitIntent = new Intent(EnterRoomActivity.this, CategorytestActivity.class);
        startActivity(explicitIntent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    protected TextWatcher watcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            joinBtn.setEnabled(true);
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    };
}