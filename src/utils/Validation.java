package utils;

import models.Member;

import java.io.Console;
import java.time.LocalDate;
import java.util.Scanner;

public class Validation {
    public static void UsernameValidator(Member member, Scanner scanner) {
        String regularExpression = "[A-Za-z0-9_]+";

        System.out.print(" Enter Username=> ");
        String login = scanner.nextLine();
        if (!(login.matches(regularExpression))){
            System.out.println("Username must contain letters-numbers");
            UsernameValidator(member, scanner);
        }else {
            member.setLogin(login);
        }

    }

    public static void setBirthday(Member member, Scanner scanner) {
        System.out.print(" Enter Date of birth (yyyy-MM-dd)=> ");
        LocalDate birthday = null;
        try {
            birthday = LocalDate.parse(scanner.nextLine());
        }
        catch (Exception e) {
            setBirthday( member,scanner);
        }
        member.setBirthday(birthday);
    }


    public static void setPassword(Member member, Scanner scanner) {
        System.out.print(" Enter Password => ");
        String password = scanner.nextLine();
        if (password.length() < 4) {
            System.out.println("Password must contain at least 4 characters");
            setPassword(member,scanner);
        } else {
            member.setPassword(password);
        }

    }


    //    private void setEmail(Member member, Scanner scanner) {
//        System.out.print(" Enter Email => ");
//        String emailId = scanner.nextLine();
//        if (isEmailValid(emailId) == false) {
//            System.out.println("Invalid E-mail");
//            setEmail(member,scanner);
//        } else {
//            member.setEmailId(emailId);
//        }
//
//    }
    //Email validation
    boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    //Password Encryption
    public static void Encrypt(String password) {
        Console console = System.console();
        console.printf("Password is: %s%n", new String(password));

    }

}
