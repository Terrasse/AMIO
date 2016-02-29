package net.telecomnancy.projetamio;

import java.io.IOException;
import java.util.List;

/**
 * Created by sam on 28/02/2016.
 */
public class IotlabUtils {
    public static int LIGHT_ON_OFF_STEP = 250;

    public static boolean hasLightOn(IotlabData iotlabData){
        return iotlabData.getValue()>LIGHT_ON_OFF_STEP;
    }
}
