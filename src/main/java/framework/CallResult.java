package framework;

public class CallResult<T> {
	
	private boolean flag;			//	调用结果标识
	private int code;				//  编码
	private T data;					//	返回数据
	private String msg;				//	消息

	public CallResult() {
	}

	public CallResult(boolean flag) {
		this.flag = flag;
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
