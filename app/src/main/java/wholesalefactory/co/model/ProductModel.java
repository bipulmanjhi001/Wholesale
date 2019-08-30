package wholesalefactory.co.model;

public class ProductModel {

    private String id,name,url;
    private boolean checkeda=false;

    public ProductModel(String id, String name, String url, boolean checkeda) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.checkeda = checkeda;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isCheckeda() {
        return checkeda;
    }

    public void setCheckeda(boolean checkeda) {
        this.checkeda = checkeda;
    }
}
