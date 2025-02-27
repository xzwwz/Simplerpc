package v2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcServer {
    // 使用线程池
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Waiting for a client on port " + serverSocket.getLocalPort());

            while(true){
                Socket socket = serverSocket.accept();

                executorService.submit(()->handleClient(socket));


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket){
        try{
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            RpcRequest request = (RpcRequest) in.readObject();

            String methodName = request.getMethodName();
            List<Object> params = request.getParams();

            System.out.println("Method " + methodName + " with params " + params);

            Object result = invokeMethod(methodName,params);

            System.out.println("Result " + result);
            out.writeObject(result);
            out.flush();

            in.close();
            out.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Object invokeMethod(String methodName,List<Object> params){
        try{
            Map<String, Method> methodMap = new HashMap<>();
            methodMap.put("add",RpcServer.class.getMethod("add",int.class,int.class));
            methodMap.put("sub",RpcServer.class.getMethod("sub",int.class,int.class));
//            methodMap.put("addAll",RpcServer.class.getMethod("addAll",List.class));
            Method method = methodMap.get(methodName);
            if(method != null){
                return method.invoke(null,params.toArray());
            }else{
                return "Method not found";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Error occurred";
        }
    }

    public static int add(int a,int b){
        return a+b;
    }

    public static int sub(int a,int b){
        return a-b;
    }

}
