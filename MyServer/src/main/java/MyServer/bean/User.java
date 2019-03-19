package MyServer.bean;

/**
 * 实体类
 */
public class User {
    private Long id;
    private Long account;
    private String username;
    private String password;
    private String userpic;
    private int exp;

    public User() {
        id= Long.valueOf(0);
        account= Long.valueOf(0);
        username="";
        password="";
        userpic="";

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }
}
