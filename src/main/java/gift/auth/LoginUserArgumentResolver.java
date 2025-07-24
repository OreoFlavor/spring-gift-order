package gift.auth;

import gift.common.exception.NoAuthorizationHeaderException;

import gift.user.domain.User;
import gift.user.service.UserService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    private final String HEADER_PREFIX = "Bearer ";

    public LoginUserArgumentResolver(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class) && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authHeader = webRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith(HEADER_PREFIX)) {
            throw new NoAuthorizationHeaderException("Invalid or missing Authorization header");
        }
        String token = authHeader.substring(HEADER_PREFIX.length());

        String email = jwtProvider.getEmail(token);

        return userService.findByEmail(email);
    }
}