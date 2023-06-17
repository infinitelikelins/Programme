package com.bearya.robot.programme.station;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.adapter.MessageAdapter;
import com.bearya.sdk.socket.NetTools;
import com.bearya.sdk.socket.SocketServerCallback;
import com.bearya.sdk.socket.SocketServerManager;
import com.tamsiree.rxfeature.tool.RxQRCode;

public class ImageSocketActivity extends BaseActivity implements SocketServerCallback {

    public static void start(Context context) {
        context.startActivity(new Intent(context, ImageSocketActivity.class));
    }

    private AppCompatImageView qrImageView;
    private AppCompatTextView warning;
    private RecyclerView messages;
    private String ipAddress;

    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_socket);
        findViewById(R.id.ivBack).setOnClickListener(view -> finish());
        qrImageView = findViewById(R.id.qr_image_view);
        warning = findViewById(R.id.warning);
        messages = findViewById(R.id.messages);
        messageAdapter = new MessageAdapter();
        messages.setAdapter(messageAdapter);

        ipAddress = NetTools.getNetIpAddress(this);
        SocketServerManager.getInstance(this).listen(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        generateQRCode();
    }

    private void generateQRCode() {
        if (ipAddress != null) {
            warning.setText(R.string.qr_code_warning);
            messages.setVisibility(View.VISIBLE);
            qrImageView.setVisibility(View.VISIBLE);
            RxQRCode.INSTANCE.builder(ipAddress)
                    .backColor(-0x1)
                    .codeColor(-0x1000000)
                    .codeSide(600)
                    .codeBorder(1)
                    .into(qrImageView);
        } else {
            messages.setVisibility(View.INVISIBLE);
            qrImageView.setVisibility(View.INVISIBLE);
            warning.setText(R.string.qr_code_no_net);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketServerManager.getInstance(this).close();
    }

    @Override
    public void onMessage(final String message) {
        runOnUiThread(() -> {
            messageAdapter.addData(message);
            messages.smoothScrollToPosition(messageAdapter.getData().size());
        });
    }

}