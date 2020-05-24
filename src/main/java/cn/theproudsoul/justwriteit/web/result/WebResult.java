package cn.theproudsoul.justwriteit.web.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author TheProudSoul
 */
@ToString
@Getter
@Setter
public class WebResult {
    private String errCode;
    private String errMsg;
    private Object data;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static WebResult error(ERRORDetail errorDetail) {
        WebResult result = new WebResult();
        result.setErrCode(errorDetail.getResultCode());
        result.setErrMsg(errorDetail.getDetails());
        result.setData(null);
        return result;
    }

    public static WebResult error(String errMsg) {
        WebResult result = new WebResult();
        result.setErrCode(ERRORDetail.RC_6666666.getResultCode());
        result.setErrMsg(errMsg);
        result.setData(null);
        return result;
    }

    public static WebResult success() {
        return success(null);
    }

    public static WebResult success(Object data) {
        WebResult result = new WebResult();
        result.setErrCode(ERRORDetail.RC_0000000.getResultCode());
        result.setErrMsg(ERRORDetail.RC_0000000.getDetails());
        result.setData(data);
        return result;
    }
}
