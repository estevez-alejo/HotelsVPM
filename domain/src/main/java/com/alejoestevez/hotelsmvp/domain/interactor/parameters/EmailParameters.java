package com.alejoestevez.hotelsmvp.domain.interactor.parameters;

public class EmailParameters {

    public static final class Parameters {

        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        private Parameters(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public static Parameters Create(String email, String password) {
            return new Parameters(email, password);
        }
    }
}
