package dreamcloud.backend.common.exception;

import dreamcloud.backend.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //    业务异常拦截
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusiness(BusinessException e){
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(Result.fail(errorCode.getCode(),errorCode.getMessage()));
    }
    //    参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleValidation(MethodArgumentNotValidException e){
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err->err.getField()+"："+err.getDefaultMessage())
                .orElse(ErrorCode.BAD_REQUEST.getMessage());
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getHttpStatus())
                .body(Result.fail(ErrorCode.BAD_REQUEST.getCode(), msg));
    }

    //    数据库唯一索引冲突
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Result<Void>> handleDuplicateKey(DuplicateKeyException e){
        ErrorCode errorCode = ErrorCode.CONFLICT;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(Result.fail(errorCode.getCode(), errorCode.getMessage()));
    }

    //    接口不存在
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Result<Void>> handleNoResource(NoResourceFoundException e){
        ErrorCode errorCode = ErrorCode.NOT_FOUND;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(Result.fail(errorCode.getCode(), "接口不存在"));
    }

    //    兜底异常拦截
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleUnknow(Exception e){
        log.error("Unhandled exception",e);
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(Result.fail(errorCode.getCode(),errorCode.getMessage()));
    }
}
