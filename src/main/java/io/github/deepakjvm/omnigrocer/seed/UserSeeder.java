package io.github.deepakjvm.omnigrocer.seed;

import io.github.deepakjvm.omnigrocer.models.User;
import io.github.deepakjvm.omnigrocer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void seed() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("adminpassword"));
        admin.setRole("ROLE_ADMIN");
        admin.setAddress("123, Albany Street");

        User user1 = new User();
        user1.setUsername("myuser");
        user1.setEmail("myuser@gmail.com");
        user1.setPassword(passwordEncoder.encode("myuserpassword"));
        user1.setRole("ROLE_NORMAL");
        user1.setAddress("765, 5th Avenue");

        User user2 = new User();
        user2.setUsername("deepak");
        user2.setEmail("deepak@gmail.com");
        user2.setPassword(passwordEncoder.encode("deepakpassword"));
        user2.setRole("ROLE_NORMAL");
        user2.setAddress("Sector 63, Noida");

        User user3 = new User();
        user3.setUsername("john");
        user3.setEmail("john@gmail.com");
        user3.setPassword(passwordEncoder.encode("johnpassword"));
        user3.setRole("ROLE_NORMAL");
        user3.setAddress("Times Square, New York");

        userRepository.saveAll(List.of(admin, user1, user2, user3));
    }
}
