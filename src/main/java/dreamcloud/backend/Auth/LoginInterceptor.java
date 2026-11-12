package dreamcloud.backend.Auth;

import dreamcloud.backend.Auth.model.LoginToken;
import dreamcloud.backend.common.context.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    private final LoginTokenMapper loginTokenMapper;

    private boolean unauthorized(HttpServletResponse response) throws IOException{
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(("{\"code\":401,\"message\":\"未登录或登录已过期\",\"data\":null}"));
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if(authorization == null || !authorization.startsWith("Bearer "))   return unauthorized(response);
        LoginToken loginToken = loginTokenMapper.findByToken(authorization.substring(7));
        if(loginToken == null || loginToken.getExpiresTime().isBefore(LocalDateTime.now()))     return unauthorized(response);
        UserContext.setUserId(loginToken.getUserId());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserContext.clear();
    }
}
