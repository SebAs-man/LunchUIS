package co.edu.uis.lunchuis.identityservice.infrastructure.persistence.adapter;

import co.edu.uis.lunchuis.identityservice.domain.model.User;
import co.edu.uis.lunchuis.identityservice.domain.repository.UserRepository;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.UserEntity;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.mapper.UserEntityMapper;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public void save(User user) {
        UserEntity userEntity = userEntityMapper.toEntity(user);
        userEntityMapper.toDomain(jpaUserRepository.save(userEntity));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByInstitutionalCode(Integer code) {
        return jpaUserRepository.findByInstitutionalCode(code)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByInstitutionalCode(Integer code) {
        return jpaUserRepository.existsByInstitutionalCode(code);
    }
}
