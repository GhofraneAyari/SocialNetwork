package services;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import models.Groups;
import models.Member;
import sun.security.util.ArrayUtil;
import utils.Cache;
import utils.SingletonDatabaseConnection;

import java.sql.*;

public class GroupsService {
	public void joinGroup() {
		try{  
			
			Connection cnx= SingletonDatabaseConnection.getInstance().cnx;
			Statement stmt1=cnx.createStatement();  
		//	ResultSet rs=stmt1.executeQuery("Select * from groups"); //select only groups where not joined
			ResultSet rs=stmt1.executeQuery("select g.groupId, g.groupName, g.groupCreationDate, g.genre "
					+ "from groups g "
					+ "where NOT EXISTS ( SELECT 1 " +
					"                      FROM groupmembers gm" +
					"                     WHERE gm.userId = " +Cache.member.getMember_id() +
					"                       AND gm.groupId = g.groupId" +
					"                  ) " + ";" );
			System.out.println("Here is the list of groups available to you:\n");
			System.out.println("ID " +"Group Name"+"  "+"Creation Date"+"    "+"Genre");
			while(rs.next()) {
					System.out.println(" "+rs.getInt(1)+"   "+rs.getString(2)+"      "+rs.getDate(3)+"     "+rs.getString(4));}
			System.out.println("---------------------------------------");
			System.out.println("Would you like to join a group? (Y/N)");
			Scanner input = new Scanner(System.in);
			String yesNo = input.nextLine();
			if(yesNo.equals("Y") || yesNo.equals("y")) 
			{
				System.out.println("Enter the ID of the group that you would like to join...");
				int response = input.nextInt();
				Statement stmt2 = cnx.createStatement();
				int test = stmt2.executeUpdate("INSERT INTO groupmembers( groupId , userId ) VALUES("+response+","+ MemberService.currentUser.getMember_id()+ ");");
				if (test>0){
					System.out.println("You have joined the group number "+response+" successfully!");
				 }
					
		
			}
			else {
			 
				System.out.println("Maybe next time!");}
			 
			
			
			
	}catch(Exception e) {
		System.out.println(e.getMessage());
		
	}
		
		
	}

	public int createGroup(Groups groups) throws SQLException{
		Connection cnx= SingletonDatabaseConnection.getInstance().cnx;
		try (Statement stmt1 = cnx.createStatement()) {
			return stmt1.executeUpdate("INSERT INTO groups ( groupName, groupCreationDate, groupCreator, genre) " +
					"VALUES ('"+groups.getGroupName()+"', '"+groups.getCreationDate()+"', '"+groups.getCreator().getMember_id()+"', '"+groups.getGenre()+"');");
		}
		//addAdmin(Cache.member.getMember_id(), 2);
	}
	public int deleteGroup(int groupId) {
		Connection cnx= SingletonDatabaseConnection.getInstance().cnx;
		try (Statement stmt1 = cnx.createStatement()) {
			return stmt1.executeUpdate("DELETE FROM groups WHERE groupId = "+groupId);
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return 0;
	}
	public int deleteMemberFromGroup(int groupId, int memberId) {
		Connection cnx= SingletonDatabaseConnection.getInstance().cnx;
		try (Statement stmt1 = cnx.createStatement()) {
			return stmt1.executeUpdate("DELETE FROM groupmembers WHERE groupId = "+groupId+" AND userId = "+memberId);
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return 0;
	}
	public int addAdmin(int idUser, int idGroup)  {
		Connection cnx= SingletonDatabaseConnection.getInstance().cnx;
		try (Statement stmt1 = cnx.createStatement()) {
			return stmt1.executeUpdate("INSERT INTO groupmanager( groupId , userId ) " +
					"VALUES(" + idGroup + "," + idUser + ");");
		}catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return 0;
	}
	public int deleteAdmin(int idAdmin, int groupId) {
		Connection cnx= SingletonDatabaseConnection.getInstance().cnx;
		try (Statement stmt1 = cnx.createStatement()) {
			return stmt1.executeUpdate("DELETE FROM groupmanager WHERE groupId = "+groupId+" AND userId = "+idAdmin);
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return 0;
	}
	public List<Groups> getGroupsWhereLoggedInUserIsAdmin() {
		Connection cnx= SingletonDatabaseConnection.getInstance().cnx;
		ArrayList<Groups> groups = new ArrayList<>();
		String req = "SELECT groups.groupId, groups.groupCreator, groups.groupName, groups.genre, groups.groupCreationDate from groupmanager, groups WHERE groups.groupId = groupmanager.groupId AND groupmanager.userId = "+Cache.member.getMember_id();
		try (Statement stmt1 = cnx.createStatement()) {
			ResultSet rs = stmt1.executeQuery(req);
			while (rs.next()) {
				Groups group = new Groups();
				group.setGroupId(rs.getInt(1));
				Member member = new Member();
				member.setMember_id(rs.getInt(2));
				group.setCreator(member);
				group.setGroupName(rs.getString(3));
				group.setGenre(rs.getString(4));
				group.setCreationDate(rs.getDate(5).toLocalDate());
				groups.add(group);
			}
			return groups;
		}catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	return Collections.emptyList();
	}
}
