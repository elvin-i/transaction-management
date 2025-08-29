package homework.bank.service.exception;

import lombok.Data;

@Data
public class ServiceException extends RuntimeException {

    private Integer code;
    /** 信息*/
    private String info;

    private ServiceException () {}

    public ServiceException(ServiceExceptionCodeEnums serviceExceptionCodeEnums) {
        super(serviceExceptionCodeEnums.getInfo());
        this.code = serviceExceptionCodeEnums.getCode();
        this.info = serviceExceptionCodeEnums.getInfo();
    }

    public ServiceException(String info) {
        this.info = info;
    }
}
