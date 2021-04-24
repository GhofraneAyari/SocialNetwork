package services;
import java.util.Scanner;

import models.Groups;
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

	public int addAdmin(int idUser, int idGroup) throws SQLException {
		Connection cnx= SingletonDatabaseConnection.getInstance().cnx;
		try (Statement stmt1 = cnx.createStatement()) {
			return stmt1.executeUpdate("INSERT INTO groupmanager( groupId , userId ) " +
					"VALUES(" + idGroup + "," + idUser + ");");
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
	//add message on the wall
}
