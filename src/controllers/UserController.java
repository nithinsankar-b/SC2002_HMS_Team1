package controllers;

import services.UserService;
import views.UserView;

public class UserController {
    private final UserView userView;

    public UserController(UserService userService) {
        this.userView = new UserView(userService);
    }

    public void run() {
        userView.displayLogin();
        // userView.displayChangePassword();
    }
}
