package controllers;

import java.io.Console;
import java.time.LocalDate;
import java.util.Scanner;
import utils.Validation;

import models.Member;
import services.MemberService;


public class MemberController {
    public static final int NUMBER_OF_TRIES = 3;
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
                System.out.println("Username or Password Incorrect!");
                if (count < NUMBER_OF_TRIES) {
                    System.out.println("Please try again...");
                }
            } else {
                System.out.println("Login Success!");
                System.out.println("==========================================================================>");
                System.out.println("\n ");
            }
        } while (!loginResult && count < NUMBER_OF_TRIES);

        if (!loginResult) {
            System.out.println("You have entered wrong three times. Please try again later!");
            System.out.println("==========================================================================>");
            return false;
        }
        return true;
    }


    public boolean signup() {
        boolean isValid= true;
        String firstName;
        String lastName;
        LocalDate birthday = null;
        String gender;
        String country;
        String city;
        String login;
        String password;

        Member member = new Member();

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.print(" Enter Firstname => ");
            firstName = scanner.nextLine();
            member.setFirstName(firstName);

            System.out.print(" Enter Lastname => ");
            lastName = scanner.nextLine();
            member.setLastName(lastName);

            Validation.setBirthday(member, scanner);

            System.out.print(" Enter Gender (M/F)=> ");
            gender= scanner.nextLine();
            member.setGender(gender);

            System.out.print(" Enter Country => ");
            country= scanner.nextLine();
            member.setCountry(country);

            System.out.print(" Enter City => ");
            city= scanner.nextLine();
            member.setCity(city);

            Validation.UsernameValidator(member,scanner);

            Validation.setPassword(member, scanner);

        }
        if(isValid) ms.insert(member);
        return isValid;
    }



    public void sendFriendRequest(){
        ms.sendFriendRequest();
    }

    public void acceptDenyRequest() {
        ms.acceptDenyFriendRequest();
    }
    public void friendsLt()
    {
		System.out.println("Your Friends List:");
		ms.membersFriends();


    }
    public int addPostToWall(){
        Scanner inputInt = new Scanner(System.in);
        Scanner inputString = new Scanner(System.in);

        System.out.println("please choose the id of the friend friend timeline you want to write on:");
        ms.membersFriends();
        int userWallId = inputInt.nextInt();

        System.out.println("please choose the content of your msg:");
        String content = inputString.nextLine();

        return ms.addToWall(userWallId,content);
    }

}
