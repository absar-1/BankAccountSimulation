import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;


class Transaction implements Identifiable {
     private String transactionID;
     private static int idAssign = 0;
     private AccountHolder holder;
     private final double chequeBookAmount=500;
     private final File transactionFile = new File("src/Transactions.txt");

     public Transaction(AccountHolder accountHolder) throws IOException {
         idAssign = 0;

         if (transactionFile.exists()) {
             BufferedReader br = new BufferedReader(new FileReader(transactionFile));
             while (br.readLine() != null) {
                 idAssign++;
             }
             br.close();
         }

         idAssign++;
         this.transactionID = String.format("TID-%04d", idAssign);
         this.holder = accountHolder;
     }
     public String getId() {
         return transactionID;
     }

        public String[][] getTransactionDetails() throws IOException {
            BufferedReader br = new BufferedReader(new FileReader(transactionFile));
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                if (line.split(",")[0].equals(this.holder.id)) {
                    count++;
                }
            }
            br.close();

            String[][] details = new String[count][];
            BufferedReader br2 = new BufferedReader(new FileReader(transactionFile));
            int i = 0;
            while ((line = br2.readLine()) != null) {
                if (line.split(",")[0].equals(this.holder.id)) {
                    details[i++] = line.split(",");
                }
            }
            br2.close();
            return details;
        }

        public static boolean checkAmount(String amount) {
            System.out.println(amount);
            for (int i = 0; i <amount.length() ; i++) {
                if(!(Character.isDigit(amount.charAt(i)) || (amount.charAt(i) == '.'))) {
                    return false;
                }
            }
            double money = Double.parseDouble(amount);
            if(money>0){
                return true;
            }
            else{
                return false;
            }
     }

        public void storeNewTransaction(String description, double amount, String transactionType) throws IOException {
            LocalDateTime now = LocalDateTime.now();
            BufferedWriter bw = new BufferedWriter(new FileWriter(transactionFile, true));
            bw.write(this.holder.id + "," + this.transactionID + "," + description + "," + amount + "," + transactionType + "," +
                    now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear() + "," + now.getHour() + ":" + now.getMinute());
            bw.newLine();
            bw.close();
        }

        public void storeNewReceive(String receiverID, double amount) throws IOException {
            LocalDateTime now = LocalDateTime.now();
            BufferedReader br = new BufferedReader(new FileReader(transactionFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[1].startsWith("TID-")) {
                    int num = Integer.parseInt(details[1].substring(details[1].indexOf('-') + 1));
                    if (num != idAssign) {
                        idAssign = num;
                    }
                }
            }
            br.close();
            String tempID = "TID-" + String.format("%05d", ++idAssign);
            BufferedWriter bw = new BufferedWriter(new FileWriter(transactionFile, true));
            bw.write(receiverID + "," + tempID + "," + "Money Received from " + this.holder.getName() + "," + amount + "," + "Transfer Received" + "," +
                    now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear() + "," + now.getHour() + ":" + now.getMinute());
            bw.newLine();
            bw.close();
        }

        public void deposit(double amount,boolean isAdmin) throws IOException {
            File input = new File("src/Account_Holders.txt");
            BufferedReader br = new BufferedReader(new FileReader(input));
            String line;
            ArrayList<String> lines=new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[7].equals(this.holder.accountNumber)) {
                    holder.balance += amount;
                    details[2] = String.valueOf(this.holder.balance);
                    lines.add(String.join(",",details));
                } else {
                    lines.add(line);
                }
            }
            br.close();
            BufferedWriter bw=new BufferedWriter(new FileWriter("src/Account_Holders.txt"));
            for (int i = 0; i < lines.size(); i++) {
                bw.write(lines.get(i));
                bw.newLine();
            }
            bw.close();
            if(isAdmin) {
                storeNewTransaction("Money Deposited into Account by Admin", amount, "Deposit");
            }
            else{
                storeNewTransaction("Money Deposited into Account", amount, "Deposit");
            }
        }
    public boolean withdraw(double amount) throws IOException {
        File input = new File("src/Account_Holders.txt");
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line;
        ArrayList<String> lines=new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String[] details = line.split(",");
            if (details[7].equals(this.holder.accountNumber)) {
                holder.balance -= amount;
                details[2] = String.valueOf(this.holder.balance);
                lines.add(String.join(",",details));
            } else {
                lines.add(line);
            }
        }
        br.close();
        BufferedWriter bw=new BufferedWriter(new FileWriter("src/Account_Holders.txt"));
        for (int i = 0; i < lines.size(); i++) {
            bw.write(lines.get(i));
            bw.newLine();
        }
        bw.close();
        storeNewTransaction("Money Withdrawn from Account", amount, "Withdrawal");
        return true;
    }
    public boolean transfer(double amount, String receiverAccNum, String bankName,String receiverName) throws IOException {
         File input = new File("src/Account_Holders.txt");
         if (bankName.equalsIgnoreCase("Scam Bank Limited")) {
             if (AccountHolder.checkAccount(receiverAccNum)) {
                 String[] receiverDetails = AccountHolder.getAccountdetails(receiverAccNum);
                 BufferedReader br = new BufferedReader(new FileReader(input));
                 ArrayList<String> lines=new ArrayList<>();
                 String line;
                 while ((line = br.readLine()) != null) {
                     String[] details = line.split(",");
                     if (details[7].equals(receiverAccNum)) {
                         details[2] = String.valueOf(Double.parseDouble(details[2]) + amount);
                         lines.add(String.join(",", details));
                     } else if (details[7].equals(this.holder.accountNumber)) {
                         holder.balance -= amount;
                         details[2] = String.valueOf(this.holder.balance);
                         lines.add(String.join(",", details));
                     } else {
                         lines.add(line);
                     }
                 }
                 br.close();
                 BufferedWriter bw=new BufferedWriter(new FileWriter("src/Account_Holders.txt"));
                 for (int i = 0; i < lines.size(); i++) {
                     bw.write(lines.get(i));
                     bw.newLine();
                 }
                 bw.close();
                 storeNewReceive(receiverDetails[0], amount);
                 storeNewTransaction("Money Transferred to " + receiverDetails[1], amount, "Transfer");
                 return true;
             } else {
                 return false;
             }
         } else {
             BufferedReader br = new BufferedReader(new FileReader(input));
             BufferedReader br2 = new BufferedReader(new FileReader("src/Other_Banks_Account_Holders.txt"));
             String line;
             boolean found = false;
             while ((line = br2.readLine()) != null) {
                 String[] details = line.split(",");
                 if (details[1].equalsIgnoreCase(bankName) && details[2].equals(receiverAccNum)) {
                     found = true;
                     break;
                 }
             }
             br2.close();
             ArrayList<String> lines=new ArrayList<>();
             if (found) {
                 while ((line = br.readLine()) != null) {
                     String[] details = line.split(",");
                     if (details[7].equals(this.holder.accountNumber)) {
                         holder.balance -= amount;
                         details[2] = String.valueOf(this.holder.balance);
                         lines.add(String.join(",", details));
                     } else {
                         lines.add(line);
                     }
                 }
                 br.close();
                 BufferedWriter bw=new BufferedWriter(new FileWriter("src/Account_Holders.txt"));
                 for (int i = 0; i < lines.size(); i++) {
                     bw.write(lines.get(i));
                     bw.newLine();
                 }
                 bw.close();
                 storeNewTransaction("Money Transferred to "+receiverName, amount, "Transfer");
                 return true;
             }
             else {
                 return false;
             }
         }
     }

        public boolean billPayment(double amount, String billType) throws IOException {
            File input = new File("src/Account_Holders.txt");
            BufferedReader br = new BufferedReader(new FileReader(input));
            ArrayList<String> lines=new ArrayList<String>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[7].equals(this.holder.accountNumber)) {
                    holder.balance -= amount;
                    details[2] = String.valueOf(this.holder.balance);
                    lines.add(String.join(",", details));
                } else {
                    lines.add(line);
                }
            }
            br.close();
            BufferedWriter bw=new BufferedWriter(new FileWriter("src/Account_Holders.txt"));
            for (int i = 0; i < lines.size(); i++) {
                bw.write(lines.get(i));
                bw.newLine();
            }
            storeNewTransaction(billType + " paid", amount, "Bill Payment");
            return true;
        }

        public boolean issueChequebook() throws IOException {
            File input = new File("src/Account_Holders.txt");
            BufferedReader br = new BufferedReader(new FileReader(input));
            ArrayList<String> lines=new ArrayList<>();
            String line;
            boolean found = false;
            try {
                while ((line = br.readLine()) != null) {
                    String[] details = line.split(",");
                    if (details[7].equals(this.holder.accountNumber)) {
                        this.holder.balance -= chequeBookAmount;
                        details[2] = String.valueOf(this.holder.balance);
                        lines.add(String.join(",", details));
                        found = true;
                    } else {
                        lines.add(line);
                    }
                }
            }
            finally{
                br.close();
                BufferedWriter bw=new BufferedWriter(new FileWriter("src/Account_Holders.txt"));
                for (int i = 0; i < lines.size(); i++) {
                    bw.write(lines.get(i));
                    bw.newLine();
                }
                bw.close();
            }
            if (found) {
                storeNewTransaction("Cheque Book Issued", 500, "Cheque Book Charges");
            }
            return found;
        }

}
enum BillType {
    SUI_GAS,
    K_ELECTRIC,
    WATER_BILL,
    WAPDA,
    CHALLAN_PAYMENT,

    MOBILE_TOP_UP,
    FEE

}

