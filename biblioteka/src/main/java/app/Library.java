package app;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@AllArgsConstructor(access= AccessLevel.PUBLIC)
@Getter
public class Library {
    String name;
    String street;
    String city;
    int phone;
    ArrayList<String>  WorkTimes;
}
