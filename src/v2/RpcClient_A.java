package v2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class RpcClient_A {
    public static Object call(String serverHost, int serverPort, String methodName , List<Object> params) throws Exception {
        // 连接到服务器
        Socket socket = new Socket(serverHost,serverPort);
        // 创建输入输出流
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        //
        RpcRequest request = new RpcRequest(methodName,params);

        out.writeObject(request);
        out.flush();

        Object response = in.readObject();

        in.close();
        out.close();
        socket.close();

        return response;
    }

    public static Future<Object> callAsync(String serverHost, int serverPort, String methodName , List<Object> params) throws Exception {

        CompletableFuture<Object> future = new CompletableFuture<>();

        new Thread(() -> {
            try{
                Object result = call(serverHost,serverPort,methodName,params);
                future.complete(result);
            }catch (Exception e){
                future.completeExceptionally(e);
            }
        }).start();

        return future;

    }

    public static void main(String[] args){
        try{
//            Object result = RpcClient_A.call("127.0.0.1",8080,"add",List.of(3,4));
            Future<Object> resultFuture = RpcClient_A.callAsync("127.0.0.1",8080,"sub",List.of(3,4));
            System.out.println("do someting else while waiting ....");
            Object result = resultFuture.get();
            System.out.println("RESULT: "+result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
