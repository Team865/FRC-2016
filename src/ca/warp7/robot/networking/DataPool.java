package ca.warp7.robot.networking;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

import java.util.ArrayList;

public class DataPool {
	private static NetworkTable table = NetworkTable.getTable("data");
	private static ArrayList<DataPool> allPools = new ArrayList<>();
	private ArrayList<Object> data;
	private ArrayList<String> keys;
	private String name;

	public DataPool(String name) {
		allPools.add(this);
		data = new ArrayList<>();
		keys = new ArrayList<>();
		this.name = name;
	}

	public void logData(String key, Object o) {
        boolean alreadyExists = false;
        int index = 0;
        for(String item : keys) {
            if (item.equals(key)) {
            	alreadyExists = true;
            	break;
            }
            index++;
        }
        if(alreadyExists) {
            data.set(index, o);
        } else {
            data.add(o);
            keys.add(key);
        }
	}

    public void logInt(String key, int i) { logData(key, new Integer(i)); }
    public void logBoolean(String key, boolean b) { logData(key, new Boolean(b)); }
    public void logDouble(String key, double d) { logData(key, new Double(d)); }

	public static void collectAllData() {
        if(allPools.isEmpty()) return;
		for(DataPool pool : allPools) {
            if(!pool.data.isEmpty()) {
                ITable poolTable = table.getSubTable(pool.name);
                for (int i = 0; i < pool.data.size(); i++) {
                    poolTable.putValue(pool.keys.get(i), pool.data.get(i));
                }
            }
		}
	}
}