package controllers;

import models.Groups;
import models.Member;
import services.GroupsService;
import services.MemberService;
import utils.Cache;
import utils.ConsoleColors;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GroupsController {
    GroupsService gs = new GroupsService();
    MemberService memberService = new MemberService();

    public void joinGroup() {
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
            System.out.println(ConsoleColors.ACC_COLOR + "The group " + groupName + " was successfully created!" + ConsoleColors.MENU_COLOR);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void addAdmin() {
        Scanner scannerInt = new Scanner(System.in);
        List<Groups> groups = gs.getGroupsWhereLoggedInUserIsAdmin();
        System.out.println("Please choose the group you want to add an admin into:");
        groups.forEach(System.out::println);
        System.out.println("Choice:");
        int groupId = scannerInt.nextInt();
        System.out.println("Please choose the ID of the member you want to add:");
        memberService.friendsNotAdminInGroup(groupId);
        System.out.println("Choice:");
        int userId = scannerInt.nextInt();
        if (gs.addAdmin(userId, groupId) != 0) {
            System.out.println(ConsoleColors.ACC_COLOR + "The user has been added as an admin to the group successfully!" + ConsoleColors.MENU_COLOR);
        } else {
            System.out.println(ConsoleColors.WARNING_COLOR + "An error has occurred, please try again!" + ConsoleColors.MENU_COLOR);
        }
    }
    public void deleteGroup() {
        //add recursive delete
        Scanner scannerInt = new Scanner(System.in);
        List<Groups> groups = gs.getGroupsWhereLoggedInUserIsAdmin();
        System.out.println("Please choose the group you want to delete:");
        groups.forEach(System.out::println);
        int groupId = scannerInt.nextInt();
        if (gs.deleteGroup(groupId) != 0) {
            System.out.println(ConsoleColors.ACC_COLOR+"Group deleted successfully!"+ConsoleColors.MENU_COLOR);
        } else {
            System.out.println(ConsoleColors.WARNING_COLOR+"Something went wrong while deleting this group."+ConsoleColors.MENU_COLOR);
        }
    }
    public void deleteAdminFromGroup() {
        Scanner scannerInt = new Scanner(System.in);
        List<Groups> groups = gs.getGroupsWhereLoggedInUserIsAdmin();
        System.out.println("Please choose the group you want to delete an admin from:");
        groups.forEach(System.out::println);
        System.out.println("Choice:");
        int groupId = scannerInt.nextInt();
        List<Member> members = gs.getGroupAdmins(groupId);
        System.out.println("Please choose the admin you want to delete from the group:");
        members.forEach(System.out::println);
        System.out.println("Choice:");
        int memberId = scannerInt.nextInt();
        gs.deleteAdmin(groupId,memberId);
        if (gs.deleteAdmin(memberId,groupId) != 0) {
            System.out.println(ConsoleColors.ACC_COLOR+"Admin deleted successfully from the group!"+ConsoleColors.MENU_COLOR);
        } else {
            System.out.println(ConsoleColors.WARNING_COLOR+"Something went wrong while deleting this admin."+ConsoleColors.MENU_COLOR);
        }

    }

    public void deleteMemberFromGroup() {
        Scanner scannerInt = new Scanner(System.in);
        List<Groups> groups = gs.getGroupsWhereLoggedInUserIsAdmin();
        System.out.println("Please choose the group you want to delete a member from:");
        groups.forEach(System.out::println);
        System.out.println("Choice:");
        int groupId = scannerInt.nextInt();
        List<Member> members = gs.getGroupMembers(groupId);
        System.out.println("Please choose the member you want to delete from the group:");
        members.forEach(System.out::println);
        System.out.println("Choice:");
        int memberId = scannerInt.nextInt();
        gs.deleteMemberFromGroup(groupId,memberId);
        if (gs.deleteMemberFromGroup(groupId,memberId) != 0) {
            System.out.println(ConsoleColors.ACC_COLOR+"member deleted successfully from the group!"+ConsoleColors.MENU_COLOR);
        } else {
            System.out.println(ConsoleColors.WARNING_COLOR+"Something went wrong while deleting this member."+ConsoleColors.MENU_COLOR);
        }

    }

}
