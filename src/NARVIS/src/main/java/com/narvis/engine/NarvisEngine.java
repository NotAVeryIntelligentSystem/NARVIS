/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.engine;

import com.narvis.frontend.IOManager;
import com.narvis.frontend.MessageInOut;

/**
 *
 * @author Nakou
 */
public class NarvisEngine {

    private static NarvisEngine narvis;
    private static String IOname = "console"; // TODO : Change by load from conf files
    private IOManager inputs;
    private NarvisEngine(){}
    
    public static NarvisEngine getInstance(){
        if(narvis != null){
            return narvis;
        } else {
            narvis = new NarvisEngine();
            return narvis;
        }
    }
    
    public void start(){
        inputs = new IOManager(NarvisEngine.IOname);
    }
    
    public static void getMessage(MessageInOut lastMessage) {
        // GO TO PARSER AND STUFF
        // Xxoo - Nakou
    }
    
}
