package net.telecomnancy.projetamio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sam on 28/02/2016.
 */
public class History {
    Map<String, List<IotlabData>> history;

    public History() {
        history = new HashMap<>();
    }

    public Map<String, List<IotlabData>> getHistory() {
        return history;
    }
    public void add(IotlabData mote){
        if(mote==null){
            return;
        }
        if(!history.containsKey(mote.getId())){
            history.put(mote.getId(), new ArrayList<IotlabData>());
        }
        history.get(mote.getId()).add(mote);
    }
    public List<IotlabData> getMoteHistory(String id){
        //if()
        return null;
    }
}
