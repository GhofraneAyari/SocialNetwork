package controllers;

import java.util.Scanner;

import services.MemberService;

public class MemberController {
    MemberService ms = new MemberService();

    public boolean login() {
        Scanner input = new Scanner(System.in);
        String login;
        String password;
        boolean loginResult;
        int count = 0;
        do {
            System.out.print("Login: \n");
            login = input.nextLine();
            System.out.print("Password: \n");
            password = input.nextLine();
            loginResult = ms.login(login, password);
            count++;
            if (!loginResult) {
                System.out.println("Login or Password Incorrect!");
                if (count < 3) {
                    System.out.println("Please try again...");
                }
            } else {
                System.out.println("Login Success!");
                System.out.println("==========================================================================>");
                System.out.println("\n ");
            }
        } while (!loginResult && count < 3);

        if (!loginResult) {
            System.out.println("You have entered wrong three times. Please try again in a few hours");
            System.out.println("==========================================================================>");
            return false;
        }
        return true;
    }

    public void sendFriendRequest() {
        ms.sendFriendRequest();
    }

    public void acceptDenyRequest() {
        ms.acceptDenyFriendRequest();
    }
}
