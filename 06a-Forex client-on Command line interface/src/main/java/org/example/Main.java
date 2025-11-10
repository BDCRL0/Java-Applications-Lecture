package org.example;
import com.oanda.v20.Context;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.account.AccountSummary;
public class Main {
    public static void main(String[] args) {
        Context ctx = new Context("https://api-fxpractice.oanda.com","<TOKEN>");
        try {
            AccountSummary summary = ctx.account.summary(new AccountID("<AccountID>")).getAccount();
            System.out.println(summary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
