package co.edu.uis.lunchuis.identityservice.infrastructure.persistence.adapter;

import co.edu.uis.lunchuis.identityservice.domain.model.Role;
import co.edu.uis.lunchuis.identityservice.domain.model.User;
import co.edu.uis.lunchuis.identityservice.domain.repository.UserRepository;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.RoleEntity;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.entity.UserEntity;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.mapper.RoleEntityMapper;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.mapper.UserEntityMapper;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserEntityMapper userEntityMapper;
    private final RoleEntityMapper roleEntityMapper;

    @Override
    public void save(User user) {
        UserEntity userEntity = userEntityMapper.toEntity(user);
        userEntityMapper.toDomain(jpaUserRepository.save(userEntity));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id)
                .map(userEntityMapper::toDomain);
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
    public List<User> findAll() {
        return jpaUserRepository.findAll().stream()
                .map(userEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByRole(Role role) {
        RoleEntity roleEntity = roleEntityMapper.toEntity(role);
        return jpaUserRepository.findByRole(roleEntity).stream()
                .map(userEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByEnabled(Boolean enabled) {
        return jpaUserRepository.findByEnabled(enabled).stream()
                .map(userEntityMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByInstitutionalCode(Integer code) {
        return jpaUserRepository.existsByInstitutionalCode(code);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaUserRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaUserRepository.deleteById(id);
    }

    @Override
    public void delete(User user) {
        UserEntity userEntity = userEntityMapper.toEntity(user);
        jpaUserRepository.delete(userEntity);
    }
}
