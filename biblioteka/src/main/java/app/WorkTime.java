package app;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class WorkTime {
    ArrayList<String> open;
    ArrayList<String> close;
}
