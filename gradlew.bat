package com.daleondeveloper.cosmetogybeaty.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class second {
    List<String> filterNonSpaces(task[] data){
        ArrayList<String> dataNoSpaces = new ArrayList<>();
        try {
            for (task s : data) {
                if (!s.value.contains(" ")) {
                    dataNoSpaces.add(s.value);
                }
            }
        }catch (IOException)
        return dataNoSpaces;
    }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          