package classes;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@AllArgsConstructor(access= AccessLevel.PUBLIC)
@Getter
public class Copy {
    int copyID;
    String libraryName;
    public Boolean equalsName(ArrayList<Copy> copies)     // TODO uzywać do rezerwacji
    {
        for (Copy copy: copies)
        {
            if (libraryName.equals(copy.libraryName))
            {
                return false;
            }
        }
        return true;
    }

}
