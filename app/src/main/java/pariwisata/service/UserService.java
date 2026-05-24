package pariwisata.service;

import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import pariwisata.dao.UserRepository;
import pariwisata.model.User;

public class UserService {
    public static boolean register(String username, String email, String password) {
        if (username.isEmpty()) {
            throw new IllegalArgumentException(
                "Username tidak boleh kosong"
            );
        }

        if (email.isEmpty()) {
            throw new IllegalArgumentException(
                "Email tidak boleh kosong"
            );
        }

        if (password.length() < 8 || password.isEmpty()) {
            throw new IllegalArgumentException(
                "Password minimal 8 karakter dan tidak boleh kosong"
            );
        }

        User user = new User(
            UUID.randomUUID().toString(),
            username,
            email,
            password
        );

        new UserRepository().create(user);
        return true;
    } 

    public static User login(String email, String password) {
        User user = new UserRepository().getByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException(
                "Email tidak ditemukan"
            );
        }
        
        boolean isValid = BCrypt.checkpw(password, user.getPassword());

        if (!isValid) {
            throw new IllegalArgumentException(
                "Password salah"
            );
        }

        return user;
    }

    public static User getById(String id) {
            return new UserRepository().getById(id);
        }

    public static List<User> getAll() {
        return new UserRepository().getAll();
    }

    public static boolean update(User user) {
        return new UserRepository().update(user);
    }

    public static boolean delete(String id) {
        return new UserRepository().delete(id);
    }

    public static boolean changePassword(String id, String oldPassword, String newPassword) {
        User user = new UserRepository().getById(id);
        if (user == null) {
            throw new IllegalArgumentException(
                "User tidak ditemukan"
            );
        }

        boolean isValid = BCrypt.checkpw(oldPassword, user.getPassword());

        if (!isValid) {
            throw new IllegalArgumentException(
                "Password salah"
            );
        }

        if (newPassword.length() < 8 || newPassword.isEmpty()) {
            throw new IllegalArgumentException(
                "Password baru minimal 8 karakter dan tidak boleh kosong"
            );
        }

        String hashPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(hashPassword);
        return new UserRepository().update(user);
    }
}
