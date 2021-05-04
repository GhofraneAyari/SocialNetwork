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
		String groupName;
		String groupGenre;
		System.out.println("Please enter the name of the group:");
		groupName = scanner.nextLine();
		groups.setGroupName(groupName);
		System.out.println("Please enter the genre of the group:");
		groupGenre = scanner.nextLine();
		groups.setGenre(groupGenre);
		groups.setCreationDate(LocalDate.now());
		groups.setCreator(Cache.member);
		try {
			gs.createGroup(groups);
			System.out.println("The group "+groupName+" was successfully created!");
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
