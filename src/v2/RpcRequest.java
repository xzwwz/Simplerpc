package v1;

import java.io.Serializable;
import java.util.List;

public class RpcRequest implements Serializable {
    private String methodName;
    private List<Object> params;

    public RpcRequest(String methodName, List<Object> params) {
        this.methodName = methodName;
        this.params = params;
    }


    public String getMethodName() {
        return methodName;
    }

    public List<Object> getParams() {
        return params;
    }
}
