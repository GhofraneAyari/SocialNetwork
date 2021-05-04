package controllers;

import models.Groups;
import services.GroupsService;
import services.MemberService;
import utils.Cache;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GroupsController {
	GroupsService gs = new GroupsService();
	MemberService memberService = new MemberService();
	public void joinGroup(){
		gs.joinGroup();

	}

	public void createGroup() {
		Scanner scanner = new Scanner(System.in);
		Groups groups = new Groups();
		String input;
		System.out.println("Please enter the group name:");
		input = scanner.nextLine();
		groups.setGroupName(input);
		System.out.println("Please enter the group genre:");
		input = scanner.nextLine();
		groups.setGenre(input);
		groups.setCreationDate(LocalDate.now());
		groups.setCreator(Cache.member);
		try {
			gs.createGroup(groups);
		} catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
		}
	}

	public void addAdmin(){
		Scanner scannerInt = new Scanner(System.in);
		List<Groups> groups = gs.getGroupsWhereLoggedInUserIsAdmin();
		System.out.println("please choose the group you want to add admin in :");
		groups.forEach(System.out::println);
		int groupId = scannerInt.nextInt();
		System.out.println("please choose the ID of the member you want to add to the group");
		memberService.friendsNotAdminInGroup(groupId);
		int userId = scannerInt.nextInt();
		if (gs.addAdmin(userId,groupId) != 0) {
			System.out.println("the user have been added as admin to the group");
		} else {
			System.out.println("an error has occurred please try another time");
		}

	}

}
