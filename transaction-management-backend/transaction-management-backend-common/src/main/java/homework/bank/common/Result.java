package homework.bank.common;

public class Result<T> {

    private Integer code;

    private String info;

    private T data;

    private Result(){

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static Result<Void> _400(String info) {
        Result<Void> result = new Result<>();
        result.code = 400;
        result.info = info;
        return result;
    }

    public static Result<Void> _200(String info) {
        Result<Void> result = new Result<>();
        result.code = 200;
        result.info = info;
        return result;
    }

    public static class Builder {
        public <T>  Result <T>  success (T t) {
            Result<T> result = new Result<>();
            result.code = 200;
            result.info = "成功";
            result.data = t;
            return result;
        }
    }
}


