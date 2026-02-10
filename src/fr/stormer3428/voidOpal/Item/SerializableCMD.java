package fr.stormer3428.voidOpal.Item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import io.papermc.paper.datacomponent.item.CustomModelData;

public class SerializableCMD implements ConfigurationSerializable {

    private final CustomModelData data;

	public SerializableCMD(CustomModelData data) { this.data = data; }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("colors", data.colors());
        map.put("flags", data.flags());
        map.put("floats", data.floats());
        map.put("strings", data.strings());
        return map;
    }

    public static SerializableCMD deserialize(Map<String, Object> map) {
    	@SuppressWarnings("unchecked")
		CustomModelData data = CustomModelData.customModelData()
			.addColors((List<Color>) map.get("colors"))
    		.addFlags((List<Boolean>) map.get("flags"))
    		.addFloats((List<Float>) map.get("floats"))
    		.addStrings((List<String>) map.get("strings"))
    		.build();
        return new SerializableCMD(data);
    }
    
    public CustomModelData getData() { return data; }
}