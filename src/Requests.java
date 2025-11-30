import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;

public class Requests implements Identifiable {
    private String reqID;
    final File AccountHolderRequestsFile = new File("src/Requests.txt");
    final File LoanRequestsFile = new File("src/Loan Requests.txt");
    public Requests(String name,String age,String gender,String cnic,String contact,String address,String email,String accTyp) throws IOException { //to request for acc creation
        BufferedReader br = new BufferedReader(new FileReader(AccountHolderRequestsFile));
        int idAssign=0;
        String newline;
        while((newline=br.readLine())!=null){
            idAssign++;
        }
        br.close();
        reqID="REQ-00"+ ++idAssign;
        BufferedWriter bw = new BufferedWriter(new FileWriter(AccountHolderRequestsFile,true));
        bw.write(this.reqID+","+name+","+age+","+gender+","+cnic+","+contact+","+address+","+email+","+accTyp+","+"Unanswered");
        bw.newLine();
        bw.close();
    }
    public Requests(String customerId,String customerName,LoanOptions loanOpt,String loanAmount) throws IOException {//to request for acc creation
        BufferedReader br = new BufferedReader(new FileReader(LoanRequestsFile));
        int idAssign=0;
        String newline;
        while((newline=br.readLine())!=null){
            idAssign++;
        }
        br.close();
        reqID="REQ-00"+ ++idAssign;
        BufferedWriter bw = new BufferedWriter(new FileWriter(LoanRequestsFile,true));
        bw.write(this.reqID+","+customerId+","+customerName+","+loanOpt.toString()+","+loanAmount+","+"Unanswered");
        bw.newLine();
        bw.close();
    }
    public Requests() {}
    public static boolean alreadyAppliedForLoan(String customerID) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader("src/Loan Requests.txt"));
        String line;
        while((line=br.readLine())!=null){
            String[] details=line.split(",");
            if(details[1].equals(customerID) && details[5].equals("Unanswered")){
                return true;
            }
        }
        br.close();
        return false;
    }
    public String getId(){
        return reqID;
    }

public boolean approveAccountCreationRequest(String reqNum) throws IOException {
    boolean approved = false;
    String[] userArray=null;
    ArrayList<String> lines=new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(AccountHolderRequestsFile));) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[0].equalsIgnoreCase(reqNum)) {
                parts[9]= "Accepted";
                line = String.join(",", parts);
                lines.add(line);
                userArray=parts;
                approved = true;
            }
            else {
                lines.add(line);
            }
        }
    }catch (IOException e) {
        return false;
    }
    if (approved) {
        BufferedWriter bw=new BufferedWriter(new FileWriter(AccountHolderRequestsFile));
        for (int i = 0; i < lines.size(); i++) {
            bw.write(lines.get(i));
            bw.newLine();
        }
        bw.close();
        if(userArray[8].equalsIgnoreCase("Savings")){
            SavingsAccountHolder sah=new SavingsAccountHolder(userArray[1],userArray[2],userArray[3],userArray[8],userArray[6],userArray[5],userArray[4],userArray[7]);
        }
        else{
            CurrentAccountHolder cah=new CurrentAccountHolder(userArray[1],userArray[2],userArray[3],userArray[8],userArray[6],userArray[5],userArray[4],userArray[7]);
        }
    }
    return approved;
}
    public boolean rejectAccountCreationRequest(String reqNum) throws IOException {
        boolean found = false;
        ArrayList<String> lines=new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(AccountHolderRequestsFile));) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(reqNum)) {
                    parts[9]="Rejected";
                    lines.add(String.join(",",parts));
                    found = true;
                }
                else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            return false;
        }
        if(found){
            BufferedWriter bw=new BufferedWriter(new FileWriter(AccountHolderRequestsFile));
            for (int i = 0; i < lines.size(); i++) {
                bw.write(lines.get(i));
                bw.newLine();
            }
            bw.close();
        }
        return found;
    }
    public boolean approveLoanRequest(String reqID,String customerID,String loanAmount) throws IOException {
        File loanRequestsFile = new File("src/Loan Requests.txt");
        File accHolderFile = new File("src/Account_Holders.txt");
        ArrayList<String> lines = new ArrayList<>();
        boolean approved = false;

        try (BufferedReader br = new BufferedReader(new FileReader(loanRequestsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(reqID)) {
                    parts[5] = "Accepted";
                    lines.add(String.join(",", parts));
                    approved = true;
                } else {
                    lines.add(line);
                }
            }
        }
        if (approved) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(loanRequestsFile))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
            }
            BufferedReader br=new BufferedReader(new FileReader(accHolderFile));
            ArrayList<String> accHolderLines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts[0].equals(customerID)) {
                    parts[2] = String.valueOf((Double.valueOf(parts[2])+Double.valueOf(loanAmount)));
                    accHolderLines.add(String.join(",", parts));
                }
                else{
                    accHolderLines.add(line);
                }
            }
            br.close();
            BufferedWriter bw=new BufferedWriter(new FileWriter(accHolderFile));
            for (String l : accHolderLines) {
                bw.write(l);
                bw.newLine();
            }
            bw.close();
            String accType=AccountHolder.getAccountType(customerID);
            if(accType.equalsIgnoreCase("Savings")){
                 SavingsAccountHolder sah=SavingsAccountHolder.getAccountHolderObject(customerID);
                 Transaction t=new Transaction(sah);
                 t.storeNewTransaction("Loan Granted",Double.valueOf(loanAmount),"Loan");
            }
            else{
                CurrentAccountHolder cah=CurrentAccountHolder.getAccountHolderObject(customerID);
                Transaction t=new Transaction(cah);
                t.storeNewTransaction("Loan Granted",Double.valueOf(loanAmount),"Loan");
            }
        }
        return approved;
    }
    public boolean rejectLoanRequests(String reqID) throws IOException {
        File loanRequestsFile = new File("src/Loan Requests.txt");
        ArrayList<String> lines = new ArrayList<>();
        boolean rejected = false;

        try (BufferedReader br = new BufferedReader(new FileReader(loanRequestsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(reqID)) {
                    parts[5] = "Rejected";
                    lines.add(String.join(",", parts));
                    rejected = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (rejected) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(loanRequestsFile))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
            }
        }
        return rejected;
    }

    public static ArrayList<String> getLoanRequests() throws IOException {
        BufferedReader br=new BufferedReader(new FileReader("src/Loan Requests.txt"));
        String line;
        ArrayList<String> lines = new ArrayList<>();
        while((line= br.readLine())!=null){
            String[] parts = line.split(",");
            if(parts[5].equalsIgnoreCase("Unanswered")) {
                String l1 =parts[0]+","+parts[1]+","+parts[2]+","+parts[3]+","+parts[4];
                lines.add(l1);
            }
        }
        return lines;
    }


}
enum LoanOptions{
    SILVER,GOLD,PLATINIUM;
}
