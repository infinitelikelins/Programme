package com.bearya.sdk.socket;

import android.content.Context;
import android.util.Log;

import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.dispatcher.IRegister;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClient;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientIOCallback;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClientPool;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerActionListener;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerManager;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IServerShutdown;
import com.xuhao.didi.socket.server.action.ServerActionAdapter;
import com.xuhao.didi.socket.server.impl.OkServerOptions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SocketServerManager {

    private static SocketServerManager manager = null;
    private IRegister<IServerActionListener, IServerManager> server;
    private ServerActionAdapter socketActionListener;
    private IServerManager<OkServerOptions> serverManager;
    private final ConcurrentMap<String, IClient> iClients = new ConcurrentHashMap<>();
    private final WeakReference<Context> mContextRef;
    private SocketServerCallback mCallback;

    public static SocketServerManager getInstance(Context context) {
        if (manager == null)
            manager = new SocketServerManager(context);
        return manager;
    }

    private SocketServerManager(Context context) {
        mContextRef = new WeakReference<>(context.getApplicationContext());
        server = OkSocket.server(NetTools.SOCKET_PORT);
        socketActionListener = new ServerActionAdapter() {
            @Override
            public void onServerListening(int serverPort) {
                logger("服务启动成功");
            }

            @Override
            public void onClientConnected(IClient client, int serverPort, IClientPool clientPool) {
                super.onClientConnected(client, serverPort, clientPool);
                client.addIOCallback(new IClientIOCallback() {
                    @Override
                    public void onClientRead(OriginalData originalData, IClient client, IClientPool<IClient, String> clientPool) {
                        handleFileByBytes(client, originalData);
                    }

                    @Override
                    public void onClientWrite(ISendable sendable, IClient client, IClientPool<IClient, String> clientPool) {

                    }
                });
                iClients.putIfAbsent(client.getUniqueTag(), client);
                logger("客户端扫码加入");
            }

            @Override
            public void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool) {
                super.onClientDisconnected(client, serverPort, clientPool);
                client.removeAllIOCallback();
                iClients.remove(client.getUniqueTag());
                logger("客户端离开");
            }

            @Override
            public void onServerWillBeShutdown(int serverPort, IServerShutdown shutdown, IClientPool clientPool, Throwable throwable) {
                logger("服务由于异常信息而必须关闭时,在此方法中完成服务器保存和关闭工作.");
                for (IClient client : iClients.values()) {
                    client.removeAllIOCallback();
                    client.disconnect();
                }
                iClients.clear();
                shutdown.shutdown();
                close();
            }

            @Override
            public void onServerAlreadyShutdown(int serverPort) {
                logger("服务已经成功shutdown");
            }
        };
        serverManager = server.registerReceiver(socketActionListener);
        logger("服务注册成功");
    }

    public void listen(SocketServerCallback callback) {
        mCallback = callback;
        if (!serverManager.isLive()) {
            OkServerOptions.setIsDebug(true);
            serverManager.listen();
            logger("服务开始等待连接");
        }
    }

    public void close() {

        server.unRegisterReceiver(socketActionListener);
        serverManager.shutdown();

        socketActionListener = null;
        serverManager = null;
        server = null;
        manager = null;

        logger("服务反注册成功");
    }

    private void logger(String log) {
        Log.i("SocketServerManager", log);
        if (mCallback != null) {
            mCallback.onMessage(log);
        }
    }

    private void handleFileByBytes(final IClient client, final OriginalData originalData) {
        logger("收到图片，这就着手准备保存图片");
        new Thread(() -> {
            final Context context = mContextRef.get();
            if (context != null) {
                String pathname = context.getFilesDir() + "/local/" + System.currentTimeMillis() + ".jpg";
                File file = new File(pathname);
                File parentFile = file.getParentFile();
                if (parentFile == null) {
                    logger("库的目录错误");
                    client.send(new ReceiveResultData(false));
                    return;
                }
                if (!parentFile.exists() && !parentFile.mkdirs()) {
                    logger("库目录创建失败");
                    client.send(new ReceiveResultData(false));
                    return;
                }
                logger("图片正在保存中");
                try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                    bufferedOutputStream.write(originalData.getBodyBytes());
                    bufferedOutputStream.flush();
                    logger("图片保存完成");
                    client.send(new ReceiveResultData(true));
                } catch (Exception exception) {
                    exception.printStackTrace();
                    logger("图片保存失败");
                    client.send(new ReceiveResultData(false));
                }
            }
        }).start();
    }

    private static class ReceiveResultData implements ISendable {
        private final String result;

        ReceiveResultData(boolean isSuccess) {
            result = isSuccess ? "CMD=SUCCESS" : "CMD=FAIL";
        }

        @Override
        public byte[] parse() {
            byte[] bytes = result.getBytes(StandardCharsets.UTF_8);
            ByteBuffer allocate = ByteBuffer.allocate(4 + bytes.length);
            allocate.order(ByteOrder.BIG_ENDIAN);
            allocate.putInt(bytes.length);
            allocate.put(bytes);
            return allocate.array();
        }
    }

}