package cdills.helloworld;

/**
 * Created by cdills on 10/25/2017.
 */

public class search {

    private String id;
    private String sub;
    private String query;

    //Step 2: Creating Constructor of Student class
    public search(String id, String sub, String query) {
        this.id = id;
        this.sub = sub;
        this.query = query;
    }

    //Step 3: Getting and Setting the values
    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }


//Step 4: Gives meaning to the object

    @Override

    public String toString() {

        return id + "" + sub + "" + query;

    }
}

