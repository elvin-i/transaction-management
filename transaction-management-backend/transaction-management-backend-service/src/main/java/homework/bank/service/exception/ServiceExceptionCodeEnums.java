package homework.bank.service.exception;

public enum ServiceExceptionCodeEnums {


    ILLEGAL_ARGUMENT_EXCEPTION(1000,"参数错误!"),
    UPDATE_ORDER_WRONG_ORDER_NOT_EXIST_OR_NOT_CHANGE(2000,"更新交易信息失败,交易信息不存在或没有数据变化!"),
    DELETE_ORDER_WRONG_ORDER_NOT_EXIST(3000,"删除交易信息失败,交易信息不存在!"),

    ;
    private Integer code;
    private String info;


    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    ServiceExceptionCodeEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }
}
