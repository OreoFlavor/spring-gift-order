package gift.user.service;

import gift.auth.PasswordUtil;
import gift.kakao.KakaoUserPatchRequestDto;
import gift.kakao.KakaoUserSaveRequestDto;
import gift.user.domain.User;
import gift.user.dto.UserPatchRequestDto;
import gift.user.dto.UserSaveRequestDto;
import gift.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public UserService(UserRepository userRepository, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public User createUser(UserSaveRequestDto userSaveRequestDto) {
        byte[] salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.encryptPassword(userSaveRequestDto.getPassword(), salt);

        User user = new User(userSaveRequestDto.getEmail(), hashedPassword, Base64.getEncoder().encodeToString(salt));
        return userRepository.save(user);
    }

    @Transactional
    public User createKakaoUser(KakaoUserSaveRequestDto kakaoUserSaveRequestDto) {
        byte[] salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.encryptPassword(kakaoUserSaveRequestDto.getPassword(), salt);

        User user = new User(kakaoUserSaveRequestDto.getEmail(), hashedPassword, Base64.getEncoder().encodeToString(salt), kakaoUserSaveRequestDto.getAccessToken(), kakaoUserSaveRequestDto.getRefreshToken(), kakaoUserSaveRequestDto.getAccessTokenExpiredAt(), kakaoUserSaveRequestDto.getRefreshTokenExpiredAt());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("해당 ID가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public User findByEmail(String Email) {
        return userRepository.findByEmail(Email)
                .orElseThrow(()->new EntityNotFoundException("등록되지 않은 이메일입니다."));
    }

    @Transactional
    public User updateUser(Long id, UserPatchRequestDto userPatchRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("해당 ID가 존재하지 않습니다."));

        byte[] salt = Base64.getDecoder().decode(user.getSalt());
        String hashedPassword = PasswordUtil.encryptPassword(userPatchRequestDto.getPassword(), salt);

        return userRepository.save(new User(user.getId(), userPatchRequestDto.getEmail(), hashedPassword, user.getSalt()));
    }

    @Transactional
    public User updateKakaoUser(Long id, KakaoUserPatchRequestDto kakaoUserPatchRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("해당 ID가 존재하지 않습니다."));

        byte[] salt = Base64.getDecoder().decode(user.getSalt());
        String hashedPassword = PasswordUtil.encryptPassword(kakaoUserPatchRequestDto.getPassword(), salt);

        return userRepository.save(new User(user.getId(), kakaoUserPatchRequestDto.getEmail(), hashedPassword, user.getSalt(), kakaoUserPatchRequestDto.getAccessToken(), kakaoUserPatchRequestDto.getRefreshToken(), kakaoUserPatchRequestDto.getAccessTokenExpiredAt(), kakaoUserPatchRequestDto.getRefreshTokenExpiredAt()));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID가 존재하지 않습니다."));

        userRepository.delete(user);
    }
}
