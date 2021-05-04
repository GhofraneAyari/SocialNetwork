import java.io.Console;
import java.util.Scanner;

import controllers.*;
import services.MemberService;
import utils.Cache;




public class SocialNetwork {

    public static void main(String[] args) {
		
        GroupsController gc = new GroupsController();
        MemberController mc = new MemberController();
        
        System.out.println("\t---------- ** WELCOME ** ----------\t \n");
        System.out.println("\n");
        System.out.println("\t \t--Login Form--");
        System.out.println("---------------------------------------------");
        mc.login();
        System.out.println("---------------------------------------------");
		System.out.println("Welcome "+ Cache.member.getFirstName() + " " + Cache.member.getLastName() + "!");
	    System.out.println("\n");
        System.out.println("\t \t--Dashboard--");
        System.out.println("\n");
        System.out.println("    1)FRIENDS       2)Groups       2)Pages");
        System.out.println("\n");
        System.out.println("What would you like to consult?");
        Scanner inputInt = new Scanner(System.in);
		Scanner inputChoice = new Scanner(System.in);
		
		int operator = inputInt.nextInt();
        //add while
        switch (operator) {
            case 1:
                System.out.println("\t \t--Friends--");
                mc.friendsLt();
                System.out.println("\n");
                System.out.println(" a) Consult Friends Requests \t \t b) Send Friend Request");
                System.out.println(" c) Write On A Friend's Timeline\t d)Search");
                System.out.println("\nChoice: ");
                String choice = inputChoice.nextLine();
                if (choice.equals("a")) {
                    mc.acceptDenyRequest();
                }
                if (choice.equals("b")) {
                    mc.sendFriendRequest();
                }
                if (choice.equals("c")) {
                    mc.addPostToWall();
                    mc.consultFriendTimeline();
                }
                if (choice.equals("d")) {
                    //Search Method
                }  
                break;
            case 2:
                System.out.println("\t \t--Groups--");
                System.out.println("\n");
                System.out.println(" a) Join A Group \t \t b) Create A Group");
                System.out.println(" c) Group Management");
                System.out.println("\nChoice: ");
                String choice2 = inputChoice.nextLine();
                if (choice2.equals("a")) {
                    gc.joinGroup();
                }
                if (choice2.equals("b")) {
                    gc.createGroup();
                }
                if (choice2.equals("c")) {
                   //group management
                }
                break;
            case 3:
                System.out.println("\t \t--Pages--");
                break;


			default:
				System.out.println("please choose something mawjouda");
		}
		
		
		

    }

}



