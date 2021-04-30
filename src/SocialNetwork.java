import controllers.GroupsController;
import controllers.MemberController;
import controllers.PageController;

import java.util.Scanner;

public class SocialNetwork {

	public static void main(String[] args) {
		MemberController mc = new MemberController();
		GroupsController gc = new GroupsController();
		PageController pc = new PageController();

		Scanner scanner = new Scanner(System.in);
		WelcomePage(mc, scanner);

		mc.sendFriendRequest();
		mc.acceptDenyRequest();


		//gc.joinGroup();



	}

	private static void WelcomePage(MemberController mc, Scanner scanner) {
		System.out.print("Welcome to SocialNetwork! \nIf you want to Signup enter 1 \nIf you want to Login enter 2");
		int input = scanner.nextInt();
		if (input == 1) mc.signup();
		else if (input == 2) mc.login();
		else WelcomePage(mc,scanner);
	}

}

