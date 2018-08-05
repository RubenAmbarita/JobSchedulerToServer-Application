package metrodata.mii.ofline.Model;


import com.google.gson.annotations.SerializedName;

public class DataItem{

    public DataItem(String name, int status) {
        this.name = name;
        this.status = status;
    }

    @SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}



	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}