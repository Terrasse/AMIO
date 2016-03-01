package net.telecomnancy.projetamio;

import android.util.Log;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sam on 28/02/2016.
 */
public class History {
    private Map<String, List<IotlabData>> history;

    public History() {
        history = new HashMap<>();
    }

    public Map<String, List<IotlabData>> getHistory() {
        return history;
    }
    public void addAll(List<IotlabData> list){
        for(IotlabData d : list){
            add(d);
        }
    }
    public void add(IotlabData mote){
        if(mote==null){
            return;
        }
        if(!history.containsKey(mote.getId())){
            history.put(mote.getId(), new ArrayList<IotlabData>());
        }
        history.get(mote.getId()).add(mote);
        sortByDate(history.get(mote.getId()));
    }
    public List<IotlabData> getMoteHistory(String id, IotlabType type){
        if(!history.containsKey(id)){
            return null;
        }
        List<IotlabData> list = new ArrayList<>();
        for(IotlabData d :history.get(id)){
            if(d.getType().equals(type)){
                list.add(d);
            }
        }
        return list;
    }

    public List<IotlabData> getMoteHistory(String id){
        if(!history.containsKey(id)){
            return null;
        }
        return history.get(id);
    }

    public boolean haslightOn(String id) throws MoteDataException{
        List<IotlabData> list = getMoteHistory(id, IotlabType.HUMIDITY);
        if(list==null){
            throw new MoteDataException("No data for this mote : "+ id);
        }
        if(list.isEmpty()){
            throw new MoteDataException("No light data on this mote : "+ id);
        }
        return IotlabUtils.hasLightOn(list.get(0));
    }

    //Le premier de la list est le dernier évements du Mote
    public static void sortByDate(List<IotlabData> list){
        Collections.sort(list, new Comparator<IotlabData>() {
            @Override
            public int compare(IotlabData lhs, IotlabData rhs) {
                return -lhs.getDate().compareTo(rhs.getDate());
            }
        });
    }

    public IotlabData getLast(String id, IotlabType type){
        List<IotlabData> l = getMoteHistory(id,type);
        if(l==null || l.isEmpty()){
            return null;
        }
        return l.get(0);
    }
}
