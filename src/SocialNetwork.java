import java.io.Console;
import java.util.Scanner;

import controllers.*;
import services.MemberService;
import utils.Cache;

public class SocialNetwork {

    public static void main(String[] args) {
		/*Console console = System.console();
		if (console == null) {
			System.out.println("Couldn't get Console instance");
			System.exit(0);
		}

		console.printf("Testing password%n");
		char[] passwordArray = console.readPassword("Enter your secret password: ");
		console.printf("Password entered was: %s%n", new String(passwordArray));*/
        GroupsController groupsController = new GroupsController();
        MemberController mc = new MemberController();
        System.out.println("\t---------- ** WELCOME ** ----------\t \n");
        System.out.println("\n");
        System.out.println("\t \t--Login Form--");
        System.out.println("---------------------------------------------");
        mc.login();
        groupsController.addAdmin();
        /*System.out.println("---------------------------------------------");
		System.out.println("        Welcome "+ Cache.member.getFirstName() + " " + Cache.member.getLastName() + "!");
        System.out.println("\t \t--Dashboard--");
        System.out.println("\n");
        System.out.println("    1)FRIENDS       2)PAGES       3)GROUPS");
        System.out.println("\n");
        System.out.println("What would you like to consult?");
        Scanner inputInt = new Scanner(System.in);
		Scanner inputString = new Scanner(System.in);

		int operator = inputInt.nextInt();
        //add while
        switch (operator) {
            case 1:
                System.out.println("\t \t--Friends--");
                mc.friendsLt();
                System.out.println("\n");
                System.out.println(" A)Consult Friends Requests \t \t B)Send Friend Request");
                System.out.println(" C)Write On A Friend's Timeline\t \t D)Search");
                System.out.println(" Choice: ");
                String choice = inputString.nextLine();
                if (choice.equals("A") || choice.equals("a")) {
                    mc.acceptDenyRequest();

                }
                break;
            case 2:
                System.out.println("\t \t--Groups--");
                break;
            case 3:
                System.out.println("\t \t--Pages--");
                break;


			default:
				System.out.println("please choose something mawjouda");
		}
		*//*mc.sendFriendRequest();
		mc.acceptDenyRequest();
		GroupsController gc = new GroupsController();
		gc.joinGroup();
		gc.createGroup();*//*

*/
    }

}
