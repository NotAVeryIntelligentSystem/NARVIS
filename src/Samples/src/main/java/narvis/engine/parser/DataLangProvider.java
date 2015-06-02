package narvis.engine.parser;


import java.util.ArrayList;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zack
 */
public class DataLangProvider {
    
    public ArrayList<String> getIgnoredWords()
    {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList("the","in","on","?","."));
        return list;
    }
}
