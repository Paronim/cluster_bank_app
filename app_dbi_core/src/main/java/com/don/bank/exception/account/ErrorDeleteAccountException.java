package com.don.bank.exception.account;

public class ErrorDeleteAccountException extends RuntimeException {

        public ErrorDeleteAccountException() {
            super("Can't delete account.");
        }

        public ErrorDeleteAccountException(String message) {
            super(message);
        }

        public ErrorDeleteAccountException(String message, Throwable cause) {
            super(message, cause);
        }

        public ErrorDeleteAccountException(Throwable cause) {
            super(cause);
        }
}
