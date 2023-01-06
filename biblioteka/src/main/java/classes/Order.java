package classes;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access= AccessLevel.PUBLIC)
@Getter
public class Order {
    int OrderID;
    String status;
    String dateBorrow;
    String dateReturn;
    String bookTitle;
}
