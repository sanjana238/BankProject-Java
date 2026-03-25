import java.util.*;

class MaxDepositeException extends Exception{
    public String getMessage() {
        return "Deposit Amount cannot be more than 10000";
    }
}

class MiniDepositException extends Exception{
    public String getMessage() {
        return "Deposit Amount cannot be less than 1";
    }
}

class MinWithdrawalException extends Exception{
    public String getMessage() {
        return "withdrawal Amount cannot be less than 100";
    }
}

class InvaildAmountException extends Exception{
    public String getMessage() {
        return "withdrawal Amount is more than bal, insufficient balance";
    }
}

class InvalidAccountException extends Exception{
    public String getMessage() {
        return "Wrong acc number";
    }
}

class InvaildOldPinException extends Exception{
    public String getMessage() {
        return "Wrong Old pin";
    }
}

class InvaildPinException extends Exception{
    public String getMessage() {
        return "Wrong pin re-try";
    }
}

class Bankc{
    private long accnum;
    private int pin=1234;
    private String accname;
    private int bal=0;

    Bankc(long acc,String name){
        accnum=acc;
        accname=name;
    }

    void setPin(int p) { pin=p; }
    int getPin() { return pin; }
    void setBal(int b) { bal=b; }
    int getBal() { return bal; }
    long getAccNum() { return accnum; }
    String getName() { return accname; }
}

class BankFactory{
    public BankInterface createInstance() {
        return new bank_Impl();
    }
}

interface BankInterface{
    void deposit(Bankc ref);
    void withdrawal(Bankc ref);
    void check_Bal(Bankc ref);
    boolean updatePin(Bankc ref);
    boolean validate(Bankc ref);
    void choice();
}

class bank_Impl implements BankInterface{
    Scanner scan=new Scanner(System.in);

    public void choice() {
        System.out.println("Select the choice");
        System.out.println("1:Deposit");
        System.out.println("2:Withdrawal");
        System.out.println("3:Check Balance");
        System.out.println("4:Update Pin");
        System.out.println("5:Exit");
    }

    public void deposit(Bankc ref) {
        System.out.println("Enter the amount");
        int amt=scan.nextInt();
        try {
            if (amt>10000) throw new MaxDepositeException();
            else if(amt<1) throw new MiniDepositException();
            else {
                amt=amt+ref.getBal();
                ref.setBal(amt);
                System.out.println("Deposit Successful! New Balance: "+ref.getBal());
            }
        }
        catch(MaxDepositeException m) { System.out.println(m.getMessage()); }
        catch(MiniDepositException m) { System.out.println(m.getMessage()); }
    }

    public void withdrawal(Bankc ref) {
        System.out.println("Enter the amount");
        int amt=scan.nextInt();
        try {
            if (amt<100) throw new MinWithdrawalException();
            else if(amt>ref.getBal()) throw new InvaildAmountException();
            else {
                int a=ref.getBal()-amt;
                ref.setBal(a);
                System.out.println("Withdrawal Successful! New Balance: "+ref.getBal());
            }
        }
        catch(MinWithdrawalException m) { System.out.println(m.getMessage()); }
        catch(InvaildAmountException m) { System.out.println(m.getMessage()); }
    }

    public void check_Bal(Bankc ref) {
        System.out.println("Name: "+ref.getName());
        System.out.println("Account Number: "+ref.getAccNum());
        System.out.println("Balance: "+ref.getBal());
    }

    public boolean updatePin(Bankc ref) {
        System.out.println("Enter the account number");
        long n=scan.nextLong();
        try {
            if(n==ref.getAccNum()) {
                System.out.println("Enter the old pin");
                int p=scan.nextInt();
                if(p==ref.getPin()) {
                    System.out.println("Enter the new pin");
                    int np=scan.nextInt();
                    ref.setPin(np);
                    System.out.println("PIN Updated Successfully!");
                    return validate(ref);
                }
                else throw new InvaildOldPinException();
            }
            else throw new InvalidAccountException();
        }
        catch(InvalidAccountException e) { System.out.println(e.getMessage()); }
        catch(InvaildOldPinException e) { System.out.println(e.getMessage()); }
        return true;
    }

    public boolean validate(Bankc ref) {
        boolean a=false;
        int count=3;
        while(count>0) {
            System.out.println("Enter the PIN");
            int pin=scan.nextInt();
            if(pin==ref.getPin()) {
                a=true;
                System.out.println("Login Successful! Welcome "+ref.getName());
                break;
            }
            else {
                try {
                    count--;
                    throw new InvaildPinException();
                }
                catch(InvaildPinException e) {
                    System.out.println(e.getMessage());
                    if(count>0) System.out.println("Attempts remaining: "+count);
                }
            }
        }
        return a;
    }
}

public class Bank {
    public static void main(String[] args) {
        Scanner scan=new Scanner(System.in);
        System.out.println("Welcome to The Bank");
        System.out.println("====================");
        System.out.println("Enter the new account number to be created");
        long num=scan.nextLong();
        System.out.println("Enter the name");
        String name=scan.next();
        Bankc b1=new Bankc(num,name);
        BankFactory bf=new BankFactory();
        BankInterface bi=bf.createInstance();
        boolean z=bi.validate(b1);
        int ch=0;
        while(z) {
            while(ch!=5) {
                bi.choice();
                ch=scan.nextInt();
                switch(ch) {
                    case 1: bi.deposit(b1); break;
                    case 2: bi.withdrawal(b1); break;
                    case 3: bi.check_Bal(b1); break;
                    case 4: z=bi.updatePin(b1); break;
                    case 5: System.out.println("Exiting..."); break;
                    default: System.out.println("Invalid choice, try again");
                }
            }
            break;
        }
        if(!z) System.out.println("Too many wrong attempts. Please contact the bank.");
        System.out.println("========Thank You! Visit Again=====");
        scan.close();
    }
}
