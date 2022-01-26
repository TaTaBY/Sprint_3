public class RegisterCourierPOJO {

    private String login;
    private String password;
    private String firstName;

    public RegisterCourierPOJO(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public RegisterCourierPOJO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public RegisterCourierPOJO(String login) {
        this.login = login;
    }

    public RegisterCourierPOJO() {
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
