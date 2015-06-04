/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.engine;

import com.narvis.dataaccess.impl.MetaDataProvider;
import com.narvis.dataaccess.interfaces.IDataProvider;
import com.narvis.dataaccess.interfaces.IDataProviderDetails;
import com.narvis.frontend.IOManager;
import com.narvis.frontend.MessageInOut;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nakou
 */
public class NarvisEngine {

    private static String IOname = "console"; // TODO : Change by load from conf files
    private IOManager inputs;
    private static NarvisEngine narvis;
    
    private Parser parser;
    private FondamentalAnalyser fondamental;
    private DetailsAnalyser detailAnalyser;
    private MetaDataProvider metaDataProvider;
    
    private NarvisEngine() throws Exception{
        parser = new Parser();
        fondamental = new FondamentalAnalyser();
        detailAnalyser = new DetailsAnalyser();
        metaDataProvider = new MetaDataProvider();
    }
    
    public static NarvisEngine getInstance() throws Exception{
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
    
    public void getMessage(MessageInOut lastMessage){
        this.brainProcess(lastMessage.getContent());
    }
    
    private void brainProcess(String message){
        List<String> parsedSentence = parser.Parse(message);
        Action action = fondamental.findAction(parsedSentence);
        Map<String,String> detailsTypes = detailAnalyser.getDetailsTypes(action.getDetails());
        IDataProvider provider = this.metaDataProvider.getDataProvider(action.getProviderName());
        if(provider instanceof IDataProviderDetails){
            String protoAnswer = ((IDataProviderDetails)provider).getDataDetails(detailsTypes, (String[]) action.getPrecisions().toArray());
        } else {
            String protoAnswer = ((IDataProvider)provider).getData((String[]) action.getPrecisions().toArray());
        }
        // AnswerBuilder;
        
    }
    
}
