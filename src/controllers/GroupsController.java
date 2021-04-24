package controllers;

import models.Groups;
import services.GroupsService;
import services.MemberService;
import utils.Cache;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class GroupsController {
	GroupsService gs = new GroupsService();
	public void joinGroup(){
		gs.joinGroup();
		
	}

	public void createGroup() {
		Scanner scanner = new Scanner(System.in);
		Groups groups = new Groups();
		String input;
		System.out.println("group name please");
		input = scanner.nextLine();
		groups.setGroupName(input);
		System.out.println("group genre please");
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

	}

}
