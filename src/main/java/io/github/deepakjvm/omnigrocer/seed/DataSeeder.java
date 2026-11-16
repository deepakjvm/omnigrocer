package io.github.deepakjvm.omnigrocer.seed;

import io.github.deepakjvm.omnigrocer.repository.UserRepository;
import io.github.deepakjvm.omnigrocer.seed.CategorySeeder;
import io.github.deepakjvm.omnigrocer.seed.UserSeeder;
import io.github.deepakjvm.omnigrocer.seed.ProductSeeder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
  private final UserSeeder userSeeder;
  private final CategorySeeder categorySeeder;
  private final ProductSeeder productSeeder;

  private final UserRepository userRepository;

  @Transactional
  @Override
  public void run(String... args) {
    if (userRepository.count() == 0) {
      userSeeder.seed();
      categorySeeder.seed();
      productSeeder.seed();
    }
  }
}
