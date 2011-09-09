/**
 * Base Domain model class
 * 
 * @author sheenobu
 *
 */
public class BaseModel {

	private String id;
	
	public BaseModel(String id) {
		this.id = id;
	}
	
	
	public String getId() {
		return id;
	}
}
