import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Validations {
    public static boolean checkEmail(String email, boolean isAdmin) throws IOException { // validation to check email while creating user or admin
        if (email == null || email.isEmpty()) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1 || atIndex != email.lastIndexOf('@')) {
            return false;
        }

        if (atIndex == 0 || atIndex == email.length() - 1) {
            return false;
        }

        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex + 1);

        if (localPart.isEmpty() || domainPart.isEmpty()) {
            return false;
        }

        if (!domainPart.contains(".") || domainPart.startsWith(".") || domainPart.endsWith(".")) {
            return false;
        }

        String allowedChars = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(allowedChars)) {
            return false;
        } else if(!isAdmin) {
            BufferedReader br = new BufferedReader(new FileReader("src/Account_Holders.txt"));
            String newline;
            while ((newline = br.readLine()) != null) {
                String[] details = newline.split(",");
                if (details[12].equals(email)) {
                    return false;
                }
            }
            br.close();
            return true;
        }
        else{
            BufferedReader br = new BufferedReader(new FileReader("src/admins.txt"));
            String newline;
            while ((newline = br.readLine()) != null) {
                String[] details = newline.split(",");
                if (details[8].equals(email)){
                    return false;
                }
            }
            br.close();
            return true;
        }
    }

    public static boolean checkEmail(String email,String accNum) throws IOException { // for editing user's email
        if (email == null || email.isEmpty()) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1 || atIndex != email.lastIndexOf('@')) {
            return false;
        }

        if (atIndex == 0 || atIndex == email.length() - 1) {
            return false;
        }

        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex + 1);

        if (localPart.isEmpty() || domainPart.isEmpty()) {
            return false;
        }

        if (!domainPart.contains(".") || domainPart.startsWith(".") || domainPart.endsWith(".")) {
            return false;
        }

        String allowedChars = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(allowedChars)) {
            return false;
        } else {
            BufferedReader br = new BufferedReader(new FileReader("src/Account_Holders.txt"));
            String newline;
            while ((newline = br.readLine()) != null) {
                String[] details = newline.split(",");
                if (details[12].equals(email) && !details[7].equals(accNum)) {
                    return false;
                }
            }
            br.close();
            return true;
        }
    }
    public static boolean checkPassword(String password,boolean isAdmin) { // checking password at the time of creating
        if(password == null || password.isEmpty()) {
            return false;
        }
        if(isAdmin) {
            if (password.length() >= 4) {
                boolean hasLetter = false;
                boolean hasDigit = false;
                boolean hasSpecialChar = false;
                for (int i = 0; i < password.length(); i++) {
                    if (Character.isLetter(password.charAt(i))) {
                        hasLetter = true;
                    } else if (Character.isDigit(password.charAt(i))) {
                        hasDigit = true;
                    } else if (Character.isSpaceChar(password.charAt(i))) {
                        return false;
                    } else if (!Character.isLetterOrDigit(password.charAt(i))) {
                        hasSpecialChar = true;
                    }
                }
                if (hasDigit && hasLetter && hasSpecialChar) {
                    return true;
                }
            }
            return false;
        }
        else{
            if (password.length() >= 4) {
                boolean hasLetter = false;
                boolean hasDigit = false;
                boolean hasSpecialChar = false;
                for (int i = 0; i < password.length(); i++) {
                    if (Character.isLetter(password.charAt(i))) {
                        hasLetter = true;
                    } else if (Character.isDigit(password.charAt(i))) {
                        hasDigit = true;
                    } else if (Character.isSpaceChar(password.charAt(i))) {
                        return false;
                    } else if (!Character.isLetterOrDigit(password.charAt(i))) {
                        hasSpecialChar = true;
                    }
                }
                if (hasDigit && !hasLetter && !hasSpecialChar) {
                    return true;
                }
            }
            return false;
        }
    }
    public static boolean checkUsername(String username,boolean isAdmin) throws IOException { // check username at the time of creation
        if(username == null || username.isEmpty()) {
            return false;
        }
        else{
            boolean hasDigit = false;
            boolean hasSpecialChar = false;
            boolean hasLetter = false;
            for (int i = 0; i < username.length(); i++) {
                for (int j = 0; j < username.length(); j++) {
                    if (Character.isLetter(username.charAt(i))) {
                        hasLetter = true;
                    } else if (Character.isDigit(username.charAt(i))) {
                        hasDigit = true;
                    } else if (Character.isSpaceChar(username.charAt(i))) {
                        return false;
                    } else if (!Character.isLetterOrDigit(username.charAt(i))) {
                        hasSpecialChar = true;
                    }
                }
                if(!hasLetter){
                    return false;
                }
            }
        }
        if(isAdmin) {
            BufferedReader br=new BufferedReader(new FileReader("src/admins.txt"));
            String newline;
            boolean isUnique=true;
            while((newline=br.readLine())!=null){
                String[] details=newline.split(",");
                if(details[4].equalsIgnoreCase(username)){
                    isUnique=false;
                    break;
                }
            }
            br.close();
            return isUnique;
        }
        else{
            BufferedReader br=new BufferedReader(new FileReader("src/Account_Holders.txt"));
            String newline;
            boolean isUnique=true;
            while((newline=br.readLine())!=null){
                String[] details=newline.split(",");
                if(details[5].equalsIgnoreCase(username)){
                    isUnique=false;
                    break;
                }
            }
            br.close();
            return isUnique;
        }
    }
    public static boolean checkUsername(String username,String accNum) throws IOException { // used when accHolder edits username
        if(username == null || username.isEmpty()) {
            return false;
        }
        else{
            boolean hasDigit = false;
            boolean hasSpecialChar = false;
            boolean hasLetter = false;
            for (int i = 0; i < username.length(); i++) {
                for (int j = 0; j < username.length(); j++) {
                    if (Character.isLetter(username.charAt(i))) {
                        hasLetter = true;
                    } else if (Character.isDigit(username.charAt(i))) {
                        hasDigit = true;
                    } else if (Character.isSpaceChar(username.charAt(i))) {
                        return false;
                    } else if (!Character.isLetterOrDigit(username.charAt(i))) {
                        hasSpecialChar = true;
                    }
                }
                if(!hasLetter){
                    return false;
                }
            }
        }
        BufferedReader br=new BufferedReader(new FileReader("src/Account_Holders.txt"));
        String newline;
        boolean isUnique=true;
        while((newline=br.readLine())!=null){
            String[] details=newline.split(",");
            if(details[5].equalsIgnoreCase(username) && !details[7].equals(accNum)){
                isUnique=false;
                break;
            }
        }
        br.close();
        return isUnique;
    }
    public static boolean checkCnic(String cnic,boolean isAdmin) throws IOException { // used when creating
        if(cnic == null || cnic.isEmpty()) {
            return false;
        }
        else if(cnic.length()!=13){
            return false;
        }
        else{
            boolean hasDigit = false;
            boolean hasSpecialChar = false;
            boolean hasLetter = false;
            for (int i = 0; i < cnic.length(); i++) {
                for (int j = 0; j < cnic.length(); j++) {
                    if (Character.isLetter(cnic.charAt(i))) {
                        return false;
                    } else if (Character.isDigit(cnic.charAt(i))) {
                        hasDigit = true;
                    } else if (Character.isSpaceChar(cnic.charAt(i))) {
                        return false;
                    } else if (!Character.isLetterOrDigit(cnic.charAt(i))) {
                        return false;
                    }
                }
                if(!hasDigit){
                    return false;
                }
            }
        }
        if(isAdmin) {
            BufferedReader br=new BufferedReader(new FileReader("src/admins.txt"));
            String newline;
            boolean isUnique=true;
            while((newline=br.readLine())!=null){
                String[] details=newline.split(",");
                if(details[6].equalsIgnoreCase(cnic)){
                    isUnique=false;
                    break;
                }
            }
            br.close();
            return isUnique;
        }
        else{
            BufferedReader br=new BufferedReader(new FileReader("src/Account_Holders.txt"));
            String newline;
            boolean isUnique=true;
            while((newline=br.readLine())!=null){
                String[] details=newline.split(",");
                if(details[11].equalsIgnoreCase(cnic)){
                    isUnique=false;
                    break;
                }
            }
            br.close();
            return isUnique;
        }
    }
    public static boolean checkCnic(String cnic,String accNum) throws IOException { // used when user is editing
        if(cnic == null || cnic.isEmpty()) {
            return false;
        }
        else if(cnic.length()!=13){
            return false;
        }
        else{
            boolean hasDigit = false;
            boolean hasSpecialChar = false;
            boolean hasLetter = false;
            for (int i = 0; i < cnic.length(); i++) {
                for (int j = 0; j < cnic.length(); j++) {
                    if (Character.isLetter(cnic.charAt(i))) {
                        return false;
                    } else if (Character.isDigit(cnic.charAt(i))) {
                        hasDigit = true;
                    } else if (Character.isSpaceChar(cnic.charAt(i))) {
                        return false;
                    } else if (!Character.isLetterOrDigit(cnic.charAt(i))) {
                        return false;
                    }
                }
                if(!hasDigit){
                    return false;
                }
            }
        }
        BufferedReader br=new BufferedReader(new FileReader("src/Account_Holders.txt"));
        String newline;
        boolean isUnique=true;
        while((newline=br.readLine())!=null){
            String[] details=newline.split(",");
            if(details[11].equalsIgnoreCase(cnic) && !details[7].equalsIgnoreCase(accNum)){
                isUnique=false;
                break;
            }
        }
        br.close();
        return isUnique;
    }
    public static boolean checkContact(String contact,boolean isAdmin) throws IOException { // used when creating
        if(contact == null || contact.isEmpty()) {
            return false;
        }
        else if(contact.length()!=11){
            return false;
        }
        else{
            boolean hasDigit = false;
            boolean hasSpecialChar = false;
            boolean hasLetter = false;
            for (int i = 0; i < contact.length(); i++) {
                if (Character.isLetter(contact.charAt(i))) {
                    return false;
                }
                else if(Character.isDigit(contact.charAt(i))) {
                    hasDigit = true;
                }
                else if(Character.isSpaceChar(contact.charAt(i))) {
                    return false;
                }
                else if(Character.isLetterOrDigit(contact.charAt(i))) {
                    return false;
                }
            }
            if(!hasDigit){
                return false;
            }
        }
        if(isAdmin) {
            BufferedReader br=new BufferedReader(new FileReader("src/admins.txt"));
            String newline;
            boolean isUnique=true;
            while((newline=br.readLine())!=null){
                String[] details=newline.split(",");
                if(details[7].equalsIgnoreCase(contact)) {
                    return false;
                }
            }
            br.close();
            return true;
        }
        else{
            BufferedReader br=new BufferedReader(new FileReader("src/Account_Holders.txt"));
            String newline;
            boolean isUnique=true;
            while((newline=br.readLine())!=null){
                String[] details=newline.split(",");
                if(details[10].equalsIgnoreCase(contact)) {
                    return false;
                }
            }
            br.close();
            return true;
        }
    }
    public static boolean checkContact(String contact,String accNum) throws IOException { // used when acc holder is editing
        if(contact == null || contact.isEmpty()) {
            return false;
        }
        else if(contact.length()!=11){
            return false;
        }
        else{
            boolean hasDigit = false;
            boolean hasSpecialChar = false;
            boolean hasLetter = false;
            for (int i = 0; i < contact.length(); i++) {
                if (Character.isLetter(contact.charAt(i))) {
                    return false;
                }
                else if(Character.isDigit(contact.charAt(i))) {
                    hasDigit = true;
                }
                else if(Character.isSpaceChar(contact.charAt(i))) {
                    return false;
                }
                else if(Character.isLetterOrDigit(contact.charAt(i))) {
                    return false;
                }
            }
            if(!hasDigit){
                return false;
            }
        }
        BufferedReader br=new BufferedReader(new FileReader("src/Account_Holders.txt"));
        String newline;
        boolean isUnique=true;
        while((newline=br.readLine())!=null){
            String[] details=newline.split(",");
            if(details[10].equalsIgnoreCase(contact) && !details[7].equalsIgnoreCase(accNum)) {
                return false;
            }
        }
        br.close();
        return true;
    }
    public static boolean checkAddress(String address){
        if(address == null || address.isEmpty()) {
            return false;
        }
        else if(address.length()<4){
            return false;
        }
        else{
            boolean hasLetter = false;
            for (int i = 0; i < address.length() ; i++) {
                if(Character.isLetter(address.charAt(i))) {
                    return true;
                }
            }
            return false;
        }
    }
    public static boolean checkAge(String age){
        if(age.isEmpty() || age.isBlank()){
            return false;
        }
        for (int i = 0; i < age.length(); i++) {
            if(!Character.isDigit(age.charAt(i))){
                return false;
            }
        }
        if(Integer.parseInt(age)<18){
            return false;
        }
        return true;
    }
    public static boolean checkName(String name){
        if(name == null || name.isEmpty()) {
            return false;
        }
        else{
            String[] names=name.split(" ");
            if(names.length<2){
                return false;
            }
            else{
                for (int i = 0; i < names.length ; i++) {
                    for (int j = 0; j <names[i].length(); j++) {
                        if(!Character.isLetter(names[i].charAt(j))){
                            return false;
                    }
                }
            }
        }
            return true;
        }
    }

    public static boolean checkTitle(String title){
        if(title == null || title.isEmpty()) {
            return false;
        }
        else{
            String[] titles=title.split(" ");
            if(titles.length<2){
                return false;
            }
            else{
                for (int i = 0; i < titles.length ; i++) {
                    for (int j = 0; j <titles[i].length(); j++) {
                        if(!Character.isLetter(titles[i].charAt(j))){
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }
}
