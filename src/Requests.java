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
    public Requests(String customerId, String customerName, LoanOptions loanOpt, String loanAmount) throws IOException {
        // Use database to add loan request
        String generatedId = LoanRequestDAO.addLoanRequest(
            customerId,
            customerName,
            loanOpt.toString(),
            Double.parseDouble(loanAmount),
            "Pending"
        );

        if (generatedId != null) {
            this.reqID = generatedId;
        } else {
            throw new IOException("Failed to create loan request in database");
        }
    }
    public Requests() {}
    public static boolean alreadyAppliedForLoan(String customerID) throws IOException {
        try {
            java.sql.Connection conn = DBConnection.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM LoanRequests WHERE account_id = ? AND status = 'Pending'"
            );
            ps.setString(1, customerID);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                ps.close();
                conn.close();
                return count > 0;
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getId(){
        return reqID;
    }

    // Helper method to get account number from customer ID
    private String getAccountNumberByCustomerId(String customerId) {
        try {
            java.sql.Connection conn = DBConnection.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(
                "SELECT account_number FROM AccountHolders WHERE id = ?"
            );
            ps.setString(1, customerId);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String accountNumber = rs.getString("account_number");
                rs.close();
                ps.close();
                conn.close();
                return accountNumber;
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public boolean approveLoanRequest(String reqID, String customerID, String loanAmount) throws IOException {
        try {
            // Step 1: Update loan request status to "Accepted" in database
            boolean statusUpdated = LoanRequestDAO.updateStatus(reqID, "Accepted");
            if (!statusUpdated) {
                return false;
            }

            // Step 2: Deposit loan amount into customer's account using TransactionDAO
            // Get account number from customer ID
            String accountNumber = getAccountNumberByCustomerId(customerID);
            if (accountNumber == null) {
                return false;
            }

            // Deposit the loan amount and create "Loan Granted" transaction
            double amount = Double.parseDouble(loanAmount);
            boolean deposited = TransactionDAO.deposit(accountNumber, amount, true);

            if (deposited) {
                // Update the transaction description to be more specific
                // The deposit already created a transaction, we just need to update its description
                // Or we can use a separate method - for now, the deposit with isAdmin=true creates the record
                return true;
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean rejectLoanRequests(String reqID) throws IOException {
//        File loanRequestsFile = new File("src/Loan Requests.txt");
//        ArrayList<String> lines = new ArrayList<>();
//        boolean rejected = false;
//
//        try (BufferedReader br = new BufferedReader(new FileReader(loanRequestsFile))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts[0].equals(reqID)) {
//                    parts[5] = "Rejected";
//                    lines.add(String.join(",", parts));
//                    rejected = true;
//                } else {
//                    lines.add(line);
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        if (rejected) {
//            try (BufferedWriter bw = new BufferedWriter(new FileWriter(loanRequestsFile))) {
//                for (String l : lines) {
//                    bw.write(l);
//                    bw.newLine();
//                }
//            }
//        }
//        return rejected;

        return LoanRequestDAO.updateStatus(reqID, "Rejected");
    }

    public static ArrayList<String> getLoanRequests() throws IOException {
        // Get pending loan requests from database
        String[][] requests = LoanRequestDAO.getPendingRequests();
        ArrayList<String> lines = new ArrayList<>();

        for (String[] req : requests) {
            // Format: reqID,accountID,accountHolderName,loanType,loanAmount
            // Joining first 5 fields (excluding status field at index 5)
            String line = req[0] + "," + req[1] + "," + req[2] + "," + req[3] + "," + req[4];
            lines.add(line);
        }

        return lines;
    }


}
enum LoanOptions{
    SILVER,GOLD,PLATINIUM;
}
