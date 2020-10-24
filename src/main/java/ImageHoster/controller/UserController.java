package ImageHoster.controller;

import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.model.UserProfile;
import ImageHoster.service.ImageService;
import ImageHoster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    //This controller method is called when the request pattern is of type 'users/registration'
    //This method declares User type and UserProfile type object
    //Sets the user profile with UserProfile type object
    //Adds User type object to a model and returns 'users/registration.html' file
    @RequestMapping("users/registration")
    public String registration(Model model) {
        User user = new User();
        UserProfile profile = new UserProfile();
        user.setProfile(profile);
        model.addAttribute("User", user);
        return "users/registration";
    }

    //Adds User type object along with password error message to a model and returns 'users/registration.html' file
    @RequestMapping("users/registration/error")
    public String registrationWithError(Model model) {
        User user = new User();
        UserProfile profile = new UserProfile();
        user.setProfile(profile);

        String error = "Password must contain atleast 1 alphabet, 1 number & 1 special character";
        model.addAttribute("passwordTypeError", error);

        model.addAttribute("User", user);

        return "users/registration";
    }

    //This controller method is called when the request pattern is of type 'users/registration' and also the incoming request is of POST type
    //This method calls the business logic and after the user record is persisted in the database, directs to login page
    @RequestMapping(value = "users/registration", method = RequestMethod.POST)
    public String registerUser(User user, Model model) {
        String password = user.getPassword();
        if (checkPasswordStrength(password)) {
            userService.registerUser(user);
            return "redirect:/users/login";
        }
        else {

            return "redirect:/users/registration/error";
        }
    }

    //This controller method is called when the request pattern is of type 'users/login'
    @RequestMapping("users/login")
    public String login() {
        return "users/login";
    }

    //This controller method is called when the request pattern is of type 'users/login' and also the incoming request is of POST type
    //The return type of the business logic is changed to User type instead of boolean type. The login() method in the business logic checks whether the user with entered username and password exists in the database and returns the User type object if user with entered username and password exists in the database, else returns null
    //If user with entered username and password exists in the database, add the logged in user in the Http Session and direct to user homepage displaying all the images in the application
    //If user with entered username and password does not exist in the database, redirect to the same login page
    @RequestMapping(value = "users/login", method = RequestMethod.POST)
    public String loginUser(User user, HttpSession session) {
        User existingUser = userService.login(user);
        if (existingUser != null) {
            session.setAttribute("loggeduser", existingUser);
            return "redirect:/images";
        } else {
            return "users/login";
        }
    }

    //This controller method is called when the request pattern is of type 'users/logout' and also the incoming request is of POST type
    //The method receives the Http Session and the Model type object
    //session is invalidated
    //All the images are fetched from the database and added to the model with 'images' as the key
    //'index.html' file is returned showing the landing page of the application and displaying all the images in the application
    @RequestMapping(value = "users/logout", method = RequestMethod.POST)
    public String logout(Model model, HttpSession session) {
        session.invalidate();

        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "index";
    }

    //This method checks the password strength entered at the time of registering a new user.
    // It returns true if the password requirements are met, and returns false otherwise.
    private boolean checkPasswordStrength(String password) {
        int specialCharCount = 0;
        int alphabetCount = 0;
        int numberCount = 0;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            int asciiNumber = (int) ch;

            if ((asciiNumber >= 32 && asciiNumber <= 47) || (asciiNumber >= 58 && asciiNumber <= 64) || (asciiNumber >= 91 && asciiNumber <= 96) || (asciiNumber >=123 && asciiNumber <= 126)) {
                specialCharCount++;
            }
            if ((asciiNumber >=65 && asciiNumber <= 90) || (asciiNumber >= 97 && asciiNumber <= 122)) {
                alphabetCount++;
            }
            if (asciiNumber >= 48 && asciiNumber <=57) {
                numberCount++;
            }
        }
        if (specialCharCount>0 && alphabetCount>0 && numberCount>0) {
            return true;
        }
        else {
            return false;
        }
    }
}
