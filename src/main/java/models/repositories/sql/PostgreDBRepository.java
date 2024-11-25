package models.repositories.sql;

public abstract class PostgreDBRepository implements IDataBaseConnection {
    protected String url;
    protected String username;
    protected String password;

    public PostgreDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
}
