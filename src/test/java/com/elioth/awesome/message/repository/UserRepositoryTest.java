package com.elioth.awesome.message.repository;

import com.elioth.awesome.message.repository.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserRepositoryTest {

    private static final Long TEST_CREATED_MILLIS = 1646200800000L;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testStoreUser() {
        final UserEntity newUser = new UserEntity();
        newUser.setUsername("testUser");
        newUser.setCreatedAt(Instant.ofEpochMilli(TEST_CREATED_MILLIS));
        final UserEntity storedUser = userRepository.save(newUser);
        assertThat(storedUser).isNotNull();
        assertThat(storedUser.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(storedUser.getId()).isEqualTo(newUser.getId());
        assertThat(storedUser.getCreatedAt()).isEqualTo(newUser.getCreatedAt());
    }

    @Test
    public void testStoreDuplicateUser() {
        final UserEntity newUser = new UserEntity();
        newUser.setUsername("testUser");
        newUser.setCreatedAt(Instant.ofEpochMilli(TEST_CREATED_MILLIS));

        final UserEntity newUser2 = new UserEntity();
        newUser.setUsername("testUser");
        newUser.setCreatedAt(Instant.ofEpochMilli(TEST_CREATED_MILLIS + 1));
        userRepository.save(newUser);
        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(newUser2));
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).contains("com.elioth.awesome.message.repository.entity.UserEntity.username");
    }

    @Test
    void findById() {
        final UserEntity newUser = new UserEntity();
        newUser.setUsername("testUser");
        newUser.setCreatedAt(Instant.ofEpochMilli(TEST_CREATED_MILLIS));
        final UserEntity storedUser = userRepository.save(newUser);
        final UserEntity foundUserEntity = userRepository.findById(storedUser.getId()).orElse(null);
        assertThat(foundUserEntity).isNotNull();
        assertThat(foundUserEntity.getUsername()).isEqualTo(storedUser.getUsername());
        assertThat(foundUserEntity.getId()).isEqualTo(storedUser.getId());
        assertThat(foundUserEntity.getCreatedAt()).isEqualTo(storedUser.getCreatedAt());
    }

    @Test
    void existsByUsername() {
        final UserEntity newUser = new UserEntity();
        newUser.setUsername("testUser");
        newUser.setCreatedAt(Instant.ofEpochMilli(TEST_CREATED_MILLIS));
        final UserEntity storedUser = userRepository.save(newUser);
        final boolean isUserFound = userRepository.findByUsername(newUser.getUsername()).isPresent();
        assertThat(isUserFound).isTrue();
    }
}