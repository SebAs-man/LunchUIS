package co.edu.uis.lunchuis.identityservice.infrastructure.persistence.adapter;

import co.edu.uis.lunchuis.common.enums.RoleType;
import co.edu.uis.lunchuis.identityservice.domain.model.Role;
import co.edu.uis.lunchuis.identityservice.domain.repository.RoleRepository;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.mapper.RoleEntityMapper;
import co.edu.uis.lunchuis.identityservice.infrastructure.persistence.repository.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {
    private final JpaRoleRepository jpaRoleRepository;
    private final RoleEntityMapper roleEntityMapper;

    @Override
    public Optional<Role> findByName(RoleType name) {
        return jpaRoleRepository.findByName(name)
                .map(roleEntityMapper::toDomain);
    }
}
