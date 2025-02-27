package v2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RpcClient_B {
    public static CompletableFuture<Object> callAsync(String serverHost,int serverPort,String methodName,List<Object> params){
        return CompletableFuture.supplyAsync(()->{
            try{
                return call(serverHost,serverPort,methodName,params);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        });
    }

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

    public static void main(String[] args){
        try{
//            Object result = RpcClient_B.call("127.0.0.1",8080,"add",List.of(3,4));
//            System.out.println("RESULT: "+result);

            CompletableFuture<Object> resultFuture = RpcClient_B.callAsync("127.0.0.1",8080,"add",List.of(3,4));
            resultFuture.thenAccept(result->{
               System.out.println("Async RESULT: "+result);
            }).exceptionally(ex ->{
                System.out.println("Async error occurred: : "+ex);
                return null;
            });

            System.out.println("do something else while waiting .. . . .");

            resultFuture.join();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
