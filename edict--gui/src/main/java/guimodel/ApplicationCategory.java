package guimodel;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class ApplicationCategory {

	private String id;
	private String name;

	private String code;
	public ApplicationCategory() {
		super();
	}
	public ApplicationCategory(String id) {
		super();
		this.id = "urn:ngsi-ld:edict:ApplicationCategory:"+id;
	}

	public ApplicationCategory(String id, String name, String code) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("type", "ApplicationCategory");
		map.put("name", tempMap("Property",name));
		map.put("code", tempMap("Property",code));
		map.put("@context", "https://raw.githubusercontent.com/SAMSGBLab/edict--datamodels/main/context.jsonld");
		return map;
	}
		private Map<String, Object> tempMap(String type,Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		if(Objects.equals(type, "Relationship"))
			map.put("object", value);
		else
			map.put("value", value);
		return map;
	}

	@Override
	public String toString() {
		return id + "," + name + "," + code;
	}
}
