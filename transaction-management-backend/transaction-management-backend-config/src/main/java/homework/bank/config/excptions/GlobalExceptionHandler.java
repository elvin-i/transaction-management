package homework.bank.config.excptions;

import homework.bank.common.Result;
import homework.bank.service.exception.ServiceException;
import homework.bank.service.exception.ServiceExceptionCodeEnums;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleValidationException(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult()
                            .getAllErrors()
                            .get(0)
                            .getDefaultMessage();
        return Result._400(errorMsg);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Result> handleServiceException(ServiceException serviceException) {
        if (ServiceExceptionCodeEnums.ILLEGAL_ARGUMENT_EXCEPTION.getCode().equals(serviceException.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result._400(serviceException.getInfo()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(Result._200(serviceException.getInfo()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleServiceException(Exception exception) {
        return ResponseEntity.status(HttpStatus.OK).body(Result._200("服务繁忙请稍后再试!"));
    }

}
