package composer;

import java.util.ArrayList;

import jmt.gui.common.CommonConstants;
import jmt.gui.common.definitions.CommonModel;

public class IoTdeviceHandler {

	
	public void addSources(CommonModel jmtModel, ArrayList<String> devicesNames) {
    	for (String deviceName : devicesNames)
    		jmtModel.addStation(deviceName, CommonConstants.STATION_TYPE_SOURCE);
    }
}
