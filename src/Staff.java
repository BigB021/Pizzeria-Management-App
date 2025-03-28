/* 
 * Author: Youssef AITBOUDDROUB
 */

public class Staff {

    private static int nextId = 1;
    public enum StaffRole {
    MANAGER,
    WAITER,
    COOK,
    CASHIER
    }

    private int id_staff;
    private String name;
    private String telephone;
    private String email;
    private StaffRole role;
    private String password;

    // Constructors
    public Staff() {
        
        this.id_staff = nextId++;
    }

    public Staff(int id, String name, String telephone, String email, Staff.StaffRole role) {
        this.id_staff = id;
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.role = role;
    }

    // Getters and setters
    public int getId() {
        return id_staff;
    }

    public void setId(int id) {
        this.id_staff = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Staff.StaffRole getRole() {
        return role;
    }

    public void setRole(Staff.StaffRole role) {
        this.role = role;
    }

    public void setRole(String role) {
        switch (role.toUpperCase()) {
            case "MANAGER":
                this.role = StaffRole.MANAGER;
                break;
            case "WAITER":
                this.role = StaffRole.WAITER;
                break;
            case "COOK":
                this.role = StaffRole.COOK;
                break;
            case "CASHIER":
                this.role = StaffRole.CASHIER;
                break;
            default:
                // Handle invalid role
                System.out.println("Invalid role: " + role);
        }
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String psswd){
        this.password = psswd;
    }



}
