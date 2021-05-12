package controllers;

import java.io.Console;
import java.util.Scanner;

import utils.ConsoleColors;
import utils.Validation;

import models.Member;
import services.MemberService;


public class MemberController {
    public static final int NUMBER_OF_TRIES = 3;
    MemberService ms = new MemberService();

    public void login(Scanner input) {
        System.out.println(ConsoleColors.TITLE_COLOR+"\t \t--Login Form--"+ConsoleColors.MENU_COLOR);

        Console console = System.console();

        String login;
        String password;
        boolean loginResult;
        int count = 0;
        do {
            System.out.print("Login: \n");
            login = input.nextLine();
            if (console == null) {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"Could not get console instance!!");
                System.out.println("Warning: Your password is going to be clear.");
                System.out.println(ConsoleColors.MENU_COLOR);
                System.out.print("Password: \n");
                password = input.nextLine();
            } else {
                System.out.println("to build your project in line command while using jdbc :" +
                        "java -cp .:/home/khlayil/Documents/workspace/social/SocialNetwork-Master/src/libs/mysql-connector-java-5.1.17-bin.jar SocialNetwork");

                char[] passwordArray = console.readPassword("Enter your secret password: ");
                password = String.valueOf(passwordArray);
                System.out.println(password);
            }

            loginResult = ms.login(login, password);
            count++;
            if (!loginResult) {
                System.out.println(ConsoleColors.WARNING_COLOR+"Username or Password Incorrect!"+ConsoleColors.MENU_COLOR);
                if (count < NUMBER_OF_TRIES) {
                    System.out.println(ConsoleColors.WARNING_COLOR+"Please try again..."+ConsoleColors.MENU_COLOR);
                }
            } else {
                System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT+"Login Success!"+ConsoleColors.MENU_COLOR);
                System.out.println("==========================================================================>");
            }
        } while (!loginResult && count < NUMBER_OF_TRIES);

        if (!loginResult) {
            System.out.println(ConsoleColors.WARNING_COLOR+"You have entered wrong three times. Please try again later!"+ConsoleColors.MENU_COLOR);
            System.out.println("==========================================================================>");
        }
    }


    public void signup(Scanner scanner) {
        System.out.println(ConsoleColors.TITLE_COLOR+"\t \t--Sign up Form--"+ConsoleColors.MENU_COLOR);
        boolean isValid= true;
        String firstName;
        String lastName;
        String gender;
        String country;
        String city;


        Member member = new Member();


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


        //u're not using is valid to b removed
        if(isValid) ms.insert(member);
    }



    public void sendFriendRequest(){
        ms.sendFriendRequest();
    }

    public void acceptDenyRequest() {
        ms.acceptDenyFriendRequest();
    }
    public void friendsLt()
    {
		System.out.println("Your Friends List is :");
		ms.membersFriends();


    }
    public int addPostToWall(){
        Scanner inputInt = new Scanner(System.in);
        Scanner inputString = new Scanner(System.in);

        System.out.println("Please enter the ID of the memeber you would like to send a message to:");
        ms.membersFriends();
        int userWallId = inputInt.nextInt();

        System.out.println("Please enter your message:");
        String content = inputString.nextLine();

        return ms.addToWall(userWallId,content);
    }
    public void consultFriendTimeline() {
    	Scanner input = new Scanner(System.in);
    	System.out.println("Would you like to consult the posts on your timeline? (Y/N)");
    	Scanner answer1 = new Scanner(System.in);
    	Scanner answer2 = new Scanner(System.in);
    	
		String yesNo = answer1.nextLine();
		if(yesNo.equals("Y") || yesNo.equals("y"))
		{
			ms.consultFriendTimelineById(MemberService.currentUser.getMember_id());
		}
		else {
			
			if(yesNo.equals("N") || yesNo.equals("n")){
				
				System.out.println("Would you like to consult posts on your friends timelines? (Y/N)");
				String anw = answer2.nextLine();
				if(anw.equals("Y") || anw.equals("y"))
				{
			        System.out.println("Please enter the ID of the member you would like to consult its timeline:");
			        int memberId= input.nextInt();
			        ms.consultFriendTimelineById(memberId);
				}
				else {
					System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT+"Okay!"+ConsoleColors.MENU_COLOR);
				}
				
			}
			
		
			
		}
    	

        
    }
}
