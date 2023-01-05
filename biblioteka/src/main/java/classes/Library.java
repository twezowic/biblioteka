package classes;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access= AccessLevel.PUBLIC)
@Getter
public class Library {
    String name;
    String street;
    String city;
    int phone;
    WorkTime WorkTimes;
}
