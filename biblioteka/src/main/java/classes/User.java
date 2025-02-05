package classes;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access= AccessLevel.PUBLIC)
@Getter
public class User {
    String name;
    String surname;
    String login;
    String password;
}
