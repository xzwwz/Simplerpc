package v2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class RpcClient {
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
            Object result = RpcClient.call("127.0.0.1",8080,"add",List.of(3,4));
            System.out.println("RESULT: "+result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
