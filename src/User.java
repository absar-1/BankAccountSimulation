import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract class User implements Identifiable {
    protected String name;
    protected String age;
    protected String id;
    protected String gender;
    protected String username;
    protected String password;
    protected String email;
    protected String cnic;
    protected String contact;

    protected User(String name, String age, String gender, String username, String password,String cnic,String contact,String email) throws IOException {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.cnic = cnic;
        this.contact = contact;
        this.email=email;
    }

    protected User() {}

    public String getId() { return this.id; }
    protected String getName() { return this.name; }
    protected String getUsername() { return this.username; }
    protected String getPassword() { return this.password; }
    public ArrayList<String> getAllBanks() throws IOException {
        BufferedReader br=new BufferedReader(new FileReader("src/Banks.txt"));
        String line;
        ArrayList<String> banks = new ArrayList<>();
        while((line= br.readLine())!=null){
            banks.add(line);
        }
        br.close();
        return banks;
    }
}

class Admin extends User {
    private static int assignId;
    private final String superAdminID="HBL-0001";
    public Admin(String name, String cnic, String contact, String age, String gender, String password,String email) throws IOException {
        AdminDAO.addAdmin(name,cnic,contact,age,gender,password,email);
    }


    public Admin(String id, String name, String age, String gender, String username, String password,String cnic,String contact,String email) throws IOException {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.cnic = cnic;
        this.contact = contact;
        this.email=email;
    }
    public boolean removeAdmin(String ID) throws IOException {
        return AdminDAO.removeAdmin(ID);
    }
    public boolean removeAccount(String ID) throws IOException {
        return AdminDAO.removeAccount(ID);
    }
    public boolean addBank(String bankName){
        return AdminDAO.addBank(bankName);
    }
    public boolean removeBank(String bankName) throws IOException {
        return AdminDAO.removeBank(bankName);
    }

//    public static String[] loginAndgetAdminDetails(String username, String password) throws IOException {
//        try (BufferedReader br = new BufferedReader(new FileReader("src/admins.txt"))) {
//            String newline;
//            while ((newline = br.readLine()) != null) {
//                String[] details = newline.split(",");
//                if (username.equalsIgnoreCase(details[4]) && (password.equals(details[5]))){
//                    br.close();
//                    return details;
//                }
//            }
//        }
//        return null;
//    }

    public static String[] loginAndgetAdminDetails(String username, String password) {
        return AdminDAO.loginAndGetAdminDetails(username, password);
    }


    public boolean editCustomerDetails(String customerAccNum,String newDetails) throws IOException {
        File AccountHolderFile=new File("src/Account_Holders.txt");
        BufferedReader br=new BufferedReader(new FileReader(AccountHolderFile));
        String newline;
        ArrayList<String> lines=new ArrayList<>();
        boolean edited=false;
        while((newline= br.readLine())!=null){
            String[] part = newline.split(",");
            if(part[7].equals(customerAccNum)){
                lines.add(newDetails);
                edited=true;
            }
            else{
                lines.add(newline);
            }
        }
        br.close();
        if (edited) {
           BufferedWriter bw=new BufferedWriter(new FileWriter("src/Account_Holders.txt"));
            for (int i = 0; i < lines.size(); i++) {
                bw.write(lines.get(i));
                bw.newLine();
            }
            bw.close();
        }
        return edited;
    }
    public  String[][] getRequests() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/Requests.txt"));
        int lineCount = 0;
        String line;
        while ((line = br.readLine()) != null) {
            if(line.split(",")[9].equalsIgnoreCase("Unanswered")){
                lineCount++;
            }
        }
        br.close();
        if (lineCount == 0) { return new String[0][]; }
            String[][] requests = new String[lineCount][];
            br = new BufferedReader(new FileReader("src/Requests.txt"));
            int index = 0;
            while ((line = br.readLine()) != null) {
                String[] req=line.split(",");
                if(req[9].equalsIgnoreCase("Unanswered")){
                    requests[index++] = req;
                }
            }
            br.close();
            return requests;
        }
        public String[][] getCustomers() throws IOException {
        final File accountHoldersFile=new File("src/Account_Holders.txt");
        BufferedReader br=new BufferedReader(new FileReader(accountHoldersFile));
        int noOfCustomers=0;
        while (br.readLine() != null) {noOfCustomers++;}
        br.close();
        BufferedReader br2=new BufferedReader(new FileReader(accountHoldersFile));
        String[][] customers=new String[noOfCustomers][];
        int index=0;
        String line;
        while((line= br2.readLine())!=null){
            customers[index++]=line.split(",");
        }
        br2.close();
        return customers;
        }
        public ArrayList<String> getFeedback_Queries_Suggestions() throws IOException {
            BufferedReader br=new BufferedReader(new FileReader("src/Feedback_Queries_Suggestions.txt"));
            ArrayList<String> allResponses=new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                allResponses.add(line);
            }
            br.close();
            return allResponses;
        }
        public ArrayList<String> getAllAdmins() throws IOException {
            return AdminDAO.getAllAdmins();
        }
}

abstract class AccountHolder extends User {
    protected double balance;
    protected String accountNumber;
    protected String accountType;
    protected String address;

    final File AccountHolderFile = new File("src/Account_Holders.txt");

    protected AccountHolder(String name, String age, String gender,String accType, String address, String contact, String cnic, String email){ // used when admin opens account
        AdminDAO.addIndAcc(name,age,gender,accType,address,contact,cnic,email);
    }
    protected AccountHolder(String name1, String age1, String gender1, String address1, String contact1, String cnic1, String email1,String name2, String age2, String gender2, String address2, String contact2, String cnic2, String email2,String accType,String accTitle){ // used when admin opens account
        AdminDAO.addJointAcc(name1,age1,gender1,address1,contact1,cnic1,email1,name2,age2,gender2,address2,contact2,cnic2,email2,accType,accTitle);
    }

//    public AccountHolder(String name, String age, String gender, String accType, String address, String contact, String cnic, String email) throws IOException { // used when request accepted
//        this.name = name;
//        this.age = age;
//        this.gender = gender;
//        this.accountType = accType;
//        this.balance = 0;
//        this.contact = contact;
//        this.address = address;
//        this.email = email;
//        this.cnic = cnic;
//        Random random = new Random();
//        this.accountNumber = "011102220333" + (random.nextInt(999, 9999));
//        boolean foundUsername=false;
//        BufferedReader br = new BufferedReader(new FileReader(AccountHolderFile));
//        String newline;
//        int linesCount = 0;
//        while ((newline = br.readLine()) != null) {
//            String[] details = newline.split(",");
//            linesCount++;
//            if (details[1].equalsIgnoreCase(this.name)) {
//                foundUsername = true;
//            }
//        }
//        br.close();
//        assignId = linesCount;
//        this.id = "HBL(" + accType.substring(0, 1) + "A)-000" + (++assignId);
//        this.username="";
//        if(!foundUsername) {
//            String [] parts=name.split(" ");
//            for (int i = 0; i <parts.length ; i++) {
//                this.username+=parts[i]+"";
//            }
//            this.username = this.username.toLowerCase().trim();
//            System.out.println(this.username);
//        }
//        else{
//            String [] parts=name.split(" ");
//            for (int i = 0; i <parts.length ; i++) {
//                this.username+=parts[i]+"";
//            }
//            this.username = (this.username.toLowerCase() + assignId).trim();
//            System.out.println(username);
//        }
//        this.password = String.valueOf(random.nextInt(999, 9999));
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(AccountHolderFile, true))) {
//            bw.write(this.id + "," + this.name + "," + this.balance + "," + this.age + "," + this.gender + "," + this.username + "," + this.password + "," + this.accountNumber + "," + this.accountType + "," + this.address + "," + this.contact + "," + this.cnic + "," + this.email);
//            bw.newLine();
//        }
//    }

    protected AccountHolder(String id, String name, double balance, String age, String gender, String username, String password, String accountNumber, String accType, String address, String contact, String cnic, String email) {  // used for already stored user
        this.balance = balance;
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.accountType = accType;
        this.contact = contact;
        this.address = address;
        this.email = email;
        this.cnic = cnic;
        this.accountNumber = accountNumber;
    }

    public String getAccountType() throws IOException {
        return this.accountType;
    }
    public boolean addBeneficiary(String beneficiaryAccNum, String beneficiaryName, String beneficiaryBankName) throws IOException {
        return AccountHolderDAO.addBeneficiary(this.id,beneficiaryAccNum,beneficiaryName,beneficiaryBankName);
    }
    public boolean submitFeedback_Queries_Suggestions(String categoryType,String text) throws IOException {
        int idAssign=0;
        BufferedReader br = new BufferedReader(new FileReader("src/Feedback_Queries_Suggestions.txt"));
        String line;
        while((line=br.readLine())!=null){
            idAssign++;
        }
        br.close();
        String feedbackID= categoryType.substring(0,1)+"-000"+ ++idAssign;
        BufferedWriter bw=new BufferedWriter(new FileWriter("src/Feedback_Queries_Suggestions.txt",true));
        bw.write(feedbackID+","+this.id+","+categoryType+","+text);
        bw.newLine();
        bw.close();
        return true;
    }
    public ArrayList<String> viewLoanOptions() throws IOException {
        // Get loan options from database
        String[][] options = LoanOptionsDAO.getAllLoanOptions();
        ArrayList<String> lines = new ArrayList<>();

        for (String[] opt : options) {
            // Format: loan_amount, minimum_balance, duration_months, interest_percentage
            // Index: 0=id, 1=package_name, 2=loan_amount, 3=minimum_balance, 4=duration, 5=interest
            lines.add(opt[2] + "," + opt[3] + "," + opt[4] + "," + opt[5]);
        }

        return lines;
    }
    public ArrayList<String> getBeneficiaries() throws IOException {
       return AccountHolderDAO.getBeneficiaries(this.id);
    }
    public static String getAccountType(String id) throws IOException {
        return AccountHolderDAO.getAccountType(id);
    }
    public static String[] loginAndgetAccountDetails(String username, String password) throws IOException {

       String[] result=AccountHolderDAO.loginAndgetAccountDetails(username,password);
       return result;
    }
    public static String[] getAccountdetails(String accountNumber) throws IOException {
        return AccountHolderDAO.getAccountDetailsByAccountNumber(accountNumber);
    }

    public double getBalance(){
        return this.balance;
    }
    public String getAddress(){
        return this.address;
    }
    public String getAccountNumber(){
        return this.accountNumber;
    }
    public String getEmail(){
        return this.email;
    }

    public static boolean checkAccount(String accountNumber) throws IOException {
        return AccountHolderDAO.checkAccount(accountNumber);
    }
    public ArrayList<String> getCardDetails() throws IOException {
        BufferedReader br=new BufferedReader(new FileReader("src/Cards.txt"));
        String line;
        while ((line= br.readLine())!=null){
            String[] details = line.split(",");
            if(details[0].equalsIgnoreCase(this.accountNumber)){
                return new ArrayList<>(List.of(details[1],details[2],details[3],details[4],details[5]));
            }
        }
        return null;
    }

    public boolean change(String accountNumber, String newDetail,int indexToChange) throws IOException {
        if (indexToChange==6) {
            return AccountHolderDAO.updatePassword(this.accountNumber,newDetail);
        }
        return false;
    }
    private static final double cardCharges = 2230.0;
    private Random random = new Random();

    public boolean issueCard(cardType type) throws IOException {
        File input = new File("src/Account_Holders.txt");
        File cardFile=new File("src/Cards.txt");
        BufferedReader br = new BufferedReader(new FileReader(input));
        BufferedReader br2=new BufferedReader(new FileReader(cardFile));
        String line;
        while((line=br2.readLine())!=null){
            String[] details = line.split(",");
            if(details[0].equals(this.accountNumber)){
                return false;
            }
        }
        ArrayList<String> lines = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String[] details = line.split(",");

            if (details[7].equals(this.accountNumber)) {
                this.balance -= cardCharges;
                details[2] = String.valueOf(this.balance);
                lines.add(String.join(",", details));
            }
            else {
                lines.add(line);
            }
        }
        br.close();

        BufferedWriter bw = new BufferedWriter(new FileWriter(input));
        for (String l : lines) {
            bw.write(l);
            bw.newLine();
        }
        bw.close();
        String cardHolderName = this.name;
        String cardNumber = generateCardNumber();
        String cvv = generateCVV();
        String expiryDate = "31-12-25";

        ArrayList<String> cardDetails = new ArrayList<>();
        cardDetails.add(this.accountNumber);
        cardDetails.add(cardHolderName);
        cardDetails.add(cardNumber);
        cardDetails.add(cvv);
        cardDetails.add(expiryDate);
        cardDetails.add(type.toString());
        BufferedWriter bw1 = new BufferedWriter(new FileWriter(cardFile,true));
        bw1.write(String.join(",", cardDetails));
        bw1.newLine();
        bw1.close();
        Transaction t=new Transaction(this);
        t.storeNewTransaction("Card issued ("+ type.toString() + ")",cardCharges,"Card Bought");
        return true;
    }

    private String generateCardNumber() {
        String num = "";
        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10);
            num = num + digit;
        }
        return num;
    }

    private String generateCVV() {
        int cvv = random.nextInt(900) + 100;
        return String.valueOf(cvv);
    }
}
enum AccountTypes{
    SAVINGS,CURRENT;
}

class SavingsAccountHolder extends AccountHolder {
    private static final double defaultInterestRate = 0.02;

    public double getInterestRate() {
        return defaultInterestRate;
    }
    public double getInterestAmount() {
        int currentMonth = LocalDate.now().getMonthValue();
        double earnedInterestRate = (defaultInterestRate * currentMonth) / 12.0;
        double interestAmount=earnedInterestRate*balance;
        return interestAmount;
    }
    public boolean getBalanceWithInterest() throws IOException {
        double interestAmount = getInterestAmount();
        if(interestAmount<=0){
            return false;
        }
        else {
            BufferedReader br = new BufferedReader(new FileReader(AccountHolderFile));
            ArrayList<String> lines=new ArrayList<>();
            String line;
            boolean updatedBalance=false;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[7].equals(getAccountNumber())) {
                    this.balance += interestAmount;
                    details[2]=String.valueOf(balance);
                    lines.add(String.join(",",details));
                    updatedBalance=true;
                }
                else{
                    lines.add(line);
                }
            }
            br.close();
            if(updatedBalance) {
                BufferedWriter bw=new BufferedWriter(new FileWriter(AccountHolderFile));
                for (int i = 0; i < lines.size(); i++) {
                    bw.write(lines.get(i));
                    bw.newLine();
                }
                bw.close();
                Transaction t = new Transaction(SavingsAccountHolder.getAccountObject(this.id));
                t.storeNewTransaction("Interest Deposited into Account", interestAmount, "Interest Deposit");
            }
            return updatedBalance;
        }
    }

    public SavingsAccountHolder(String id, String name, double balance, String age, String gender, String username, String accountNumber, String password, String accType, String address, String contact, String cnic, String email) {
        super(id, name, balance, age, gender, username, accountNumber, password, accType, address, contact, cnic, email);
    }
    public SavingsAccountHolder(String name, String age, String gender, String accType, String address, String contact, String cnic, String email) throws IOException { // constructor when request accepted as auto username and password would be assigned
        super(name, age, gender, accType, address, contact, cnic, email);
    }

    protected SavingsAccountHolder(String name1, String age1, String gender1, String address1, String contact1, String cnic1, String email1,String name2, String age2, String gender2, String address2, String contact2, String cnic2, String email2,String accType,String accTitle){ // used when admin opens account
        super(name1,age1,gender1,address1,contact1,cnic1,email1,name2,age2,gender2,address2,contact2,cnic2,email2,accType,accTitle);
    }

    public static SavingsAccountHolder getAccountObject(String id) throws IOException {
        String[] details = AccountHolderDAO.getAccountDetailsByID(id);
        return new SavingsAccountHolder(details[0], details[1], Double.parseDouble(details[2]), details[3], details[4], details[5], details[6], details[7], details[8], details[9], details[10], details[11], details[12]);
    }
    }

class CurrentAccountHolder extends AccountHolder {

    public CurrentAccountHolder(String id, String name, double balance, String age, String gender, String username, String accountNumber, String password, String accType, String address, String contact, String cnic, String email) {
        super(id, name, balance, age, gender, username, accountNumber, password, accType, address, contact, cnic, email);
    }

    public static CurrentAccountHolder getAccountHolderObject(String id) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("src/Account_Holders.txt"))) {
            String newline;
            while ((newline = br.readLine()) != null) {
                String[] details = newline.split(",");
                if (id.equalsIgnoreCase(details[0])) {
                    return new CurrentAccountHolder(details[0], details[1], Double.parseDouble(details[2]), details[3], details[4], details[5], details[6], details[7], details[8], details[9], details[10], details[11], details[12]);
                }
            }
        }
        return null;
    }

//    public CurrentAccountHolder(String name, String age, String gender, String username, String password, String accType, String address, String contact, String cnic, String email) throws IOException {
//        super(name, age, gender, username, password, accType, address, contact, cnic, email);
//    }
    public CurrentAccountHolder(String name, String age, String gender, String accType, String address, String contact, String cnic, String email) throws IOException {
        super(name, age, gender, accType, address, contact, cnic, email);
    }
    protected CurrentAccountHolder(String name1, String age1, String gender1, String address1, String contact1, String cnic1, String email1,String name2, String age2, String gender2, String address2, String contact2, String cnic2, String email2,String accType,String accTitle){ // used when admin opens account
        super(name1,age1,gender1,address1,contact1,cnic1,email1,name2,age2,gender2,address2,contact2,cnic2,email2,accType,accTitle);
    }
}
enum Gender{
    MALE,FEMALE;
}
enum cardType {
    DEBIT, CREDIT
}


