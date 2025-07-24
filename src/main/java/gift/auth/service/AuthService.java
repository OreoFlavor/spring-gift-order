package gift.auth.service;

import gift.auth.JwtProvider;
import gift.auth.PasswordUtil;
import gift.auth.dto.UserSignupResponseDto;
import gift.common.exception.EmailAlreadyExistsException;
import gift.common.exception.InvalidPasswordException;
import gift.common.exception.NoSuchEmailException;
import gift.user.domain.User;
import gift.auth.dto.UserLoginRequestDto;
import gift.auth.dto.UserSingupRequestDto;
import gift.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    public UserSignupResponseDto signUp(UserSingupRequestDto userSignupRequestDto) {
        if(userRepository.findByEmail(userSignupRequestDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        byte[] salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.encryptPassword(userSignupRequestDto.getPassword(), salt);

        User user = new User(userSignupRequestDto.getEmail(), hashedPassword, Base64.getEncoder().encodeToString(salt));

        return new UserSignupResponseDto(userRepository.save(user), jwtProvider.createToken(user));
    }

    public String login(UserLoginRequestDto userLoginRequestDto) {
        User user = userRepository.findByEmail(userLoginRequestDto.getEmail())
                .orElseThrow(()->new NoSuchEmailException("등록되지 않은 이메일입니다."));

        if(!user.isEqualToPassword(userLoginRequestDto.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
        return jwtProvider.createToken(user);
    }
}
