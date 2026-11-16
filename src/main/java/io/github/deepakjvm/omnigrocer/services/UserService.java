package io.github.deepakjvm.omnigrocer.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.deepakjvm.omnigrocer.models.User;
import io.github.deepakjvm.omnigrocer.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<User> getUsers() {
		return this.userRepository.findAll();
	}

	public User addUser(User user) {
		try {
			if (user.getPassword() != null && !isPasswordEncoded(user.getPassword())) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
			}
			return this.userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalStateException("Unable to create user due to data integrity constraints.", e);
		}
	}

	public boolean checkUserExists(String username) {
		return this.userRepository.existsByUsername(username);
	}

	public User getUserByUsername(String username) {
		User user = userRepository.findByUsername(username).orElse(null);
		if (user != null && user.getPassword() != null && !isPasswordEncoded(user.getPassword())) {
			// Migrate legacy plain-text passwords to BCrypt when the user is loaded.
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
		}
		return user;
	}

	public User getUserById(int id) {
		return this.userRepository.findById(id).orElse(null);
	}

	public User updateUserProfile(int userId, String username, String email, String password, String address) {
		User existingUser = this.userRepository.findById(userId).orElse(null);
		if (existingUser == null) {
			return null;
		}

		existingUser.setUsername(username);
		existingUser.setEmail(email);
		existingUser.setAddress(address);

		if (password != null && !password.trim().isEmpty()) {
			existingUser.setPassword(isPasswordEncoded(password) ? password : passwordEncoder.encode(password));
		}

		return this.userRepository.save(existingUser);
	}

	private boolean isPasswordEncoded(String password) {
		return password != null
				&& (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$"));
	}
}
