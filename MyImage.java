public class MyImage {
    private String landmarkName;
    private String imageUrl;

    public MyImage(String landmarkName, String imageUrl) {
        this.landmarkName = landmarkName;
        this.imageUrl = imageUrl;
    }

    public String getLandmarkName() {
        return landmarkName;
    }

    public String getUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return "MyImage{" + "landmarkName='" + landmarkName + '\'' + ", imageUrl='" + imageUrl + '\'' + '}';
    }
}
