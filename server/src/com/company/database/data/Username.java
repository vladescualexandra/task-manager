package data;

public enum Username {

    ADMIN ("ADMIN"),
    ADRIANA ("ADRIANA"),
    BIANCA ("BIANCA"),
    CODRUTA ("CODRUTA"),
    IONELA ("IONELA");

    private String username;

    Username(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return username;
    }

}
