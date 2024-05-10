package book.service.controller.request;

import book.service.entity.Role;

import java.util.Set;

public record UserRegisterRequest(String username, String password, String roles) {
}