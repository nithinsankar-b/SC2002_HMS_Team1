package controllers;

import services.UserService;
import models.User;
import views.UserView;

public class UserController {
    private UserService userService;
    private UserView userView;

    public UserController(UserService userService) {
        this.userService = userService;
        this.userView = new UserView(userService);
    }

    public void run() {
        userView.displayLogin();
        //userView.displayChangePassword();
    }
}
