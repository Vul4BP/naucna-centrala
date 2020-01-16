package root.demo.config;

public class MyConfig {
    public static String roleObican = "ROLE_OBICAN";
    public static String roleRecenzent = "ROLE_RECENZENT";
    public static String roleAdmin = "ROLE_ADMIN";
    public static String roleUrednik = "ROLE_UREDNIK";

    public static boolean checkIfInRole(String role){
        if(role.equals(roleObican)){
            return true;
        }else if(role.equals(roleRecenzent)){
            return true;
        }else if(role.equals(roleAdmin)){
            return true;
        }else if(role.equals(roleUrednik)){
            return true;
        }
        return false;
    }
}
