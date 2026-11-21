package dreamcloud.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 40000, "请求参数错误"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 40100, "未登录或令牌已过期"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, 40101, "用户名或密码错误"),
    FORBIDDEN(HttpStatus.FORBIDDEN, 40300, "无权访问该资源"),
    NOT_FOUND(HttpStatus.NOT_FOUND, 40400, "目标数据不存在"),
    CONFLICT(HttpStatus.CONFLICT, 40900, "数据已存在"),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, 40901, "用户名已存在"),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, 42900, "操作过于频繁，请稍后再试"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "服务器内部错误");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

}
