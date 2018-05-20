package cdills.helloworld;

/**
 * Created by cdills on 10/25/2017.
 */

public class result {

    private String title;
    private String url;

    //Step 2: Creating Constructor of Student class
    public result(String title, String url) {
        this.title = title;
        this.url = url;
    }

    //Step 3: Getting and Setting the values
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//Step 4: Gives meaning to the object

    @Override

    public String toString() {

        return "Result [title=" + title + ", url=" + url + "]";

    }
}