package me.gulsum.otopark.model;

import me.gulsum.otopark.UI.Model.User;

public class LoginResponse {
        private String message;
        private User user;

        // Getter ve Setter metodları
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

