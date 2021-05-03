package services;
import models.Member;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Scanner;

import com.mysql.jdbc.PreparedStatement;

import utils.Cache;
import utils.SingletonDatabaseConnection;


public class MemberService {
	
	public static Member currentUser = new Member();
	
	public Boolean login(String login, String password)
	{
		
		try {
			
			Connection cnx= SingletonDatabaseConnection.getInstance().cnx ; 
			Statement stmt=cnx.createStatement(); 
			ResultSet rs=stmt.executeQuery("select * from user where login='"+login+"'and password='"+password+"';");  
			
			if(rs.next()) {
				currentUser.setMember_id(rs.getInt(1));
				currentUser.setFirstName(rs.getString(2));
				currentUser.setLastName(rs.getString(3));
				Cache.member = currentUser;
				return true;
				
			}
			  
		}catch(Exception e){
			System.out.println(e.getMessage());
			
			
		}
		
		return false;
		
		
		
		 
	}

	public void sendFriendRequest()
	{
		try {
			Connection cnx= SingletonDatabaseConnection.getInstance().cnx ; 
			Statement stmt=cnx.createStatement();

			ResultSet rs=stmt.executeQuery("select u.userId, u.firstName, u.lastName, u.birthday, u.city "
					+ "from user u "
					+ "where NOT EXISTS ( SELECT 1 " +
							"                      FROM relationship r" +
									"                     WHERE r.sender = u.userId" +
									"                       AND r.receiver = " + currentUser.getMember_id() +
									"                  )"+
					"   AND NOT EXISTS ( SELECT 1\n" +
							"                      FROM relationship g" +
							"                     WHERE g.receiver = u.userId" +
							"                       AND g.sender = " + currentUser.getMember_id() +
							"                  )" +
							"   AND u.userId <> "+ currentUser.getMember_id() +";" );
			System.out.println("Here is the list of members available on the network:\n");
			System.out.println("ID"+"\t"+"First Name"+"\t"+"Last Name"+"\t"+"Birthday"+"\t "+"City");
			while(rs.next())
			{
					System.out.println(rs.getInt(1)+"\t "+rs.getString(2)+"\t \t "+rs.getString(3)+"\t \t "+rs.getDate(4)+"\t "+rs.getString(5));
			}
			System.out.println("Would you like to send a friend request? (Y/N)");
			Scanner input = new Scanner(System.in);
			String yesNo = input.nextLine();
			if(yesNo.equals("Y") || yesNo.equals("y")) {
				System.out.println("Enter the ID of the member you would like to send a friend request to...");
				input = new Scanner(System.in);
				int response = input.nextInt();
				Statement stmt2 = cnx.createStatement();
				int test = stmt2.executeUpdate("INSERT INTO relationship(sender, receiver, status) VALUES("+currentUser.getMember_id()+","+response+","+ 0 +");");
				if (test>0)
					{
						System.out.println("Your friend request was sent successfully!");
					}
			}
			System.out.println("\n");
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void acceptDenyFriendRequest() {
		
		try {
			Connection cnx= SingletonDatabaseConnection.getInstance().cnx ; 
			Statement stmt=cnx.createStatement();
			ResultSet rs=stmt.executeQuery("SELECT relationshipId, user.firstName, user.lastName, status from relationship, user WHERE relationship.sender = user.userId and status=0 and receiver ="+ currentUser.getMember_id());
			Scanner input = new Scanner(System.in);
			if(rs.next() == false) {
					System.out.println("You do not have any pending friends requests!");
			
			}else
			{
				System.out.println("ID"+"     "+"Sender Name"+ "       "+"Status");
				 do{
					System.out.println(rs.getInt(1)+"      "+rs.getString(2)+" "+rs.getString(3)+"  \t "+"Pending..");
				}while(rs.next());
				
			System.out.println("Please enter the ID of the friend request you would like to approve or deny: ");
			int reqID = input.nextInt();
			System.out.println("Would you like to approve or deny this friend request? (1: Approve  2: Deny)");
			int response = input.nextInt();
			Statement stmt2=cnx.createStatement(); 
			int test = stmt2.executeUpdate("Update relationship set status = " +response+" where relationshipId="+reqID);
			if (test> 0 && response == 1) {
				System.out.println("ok");
				
			}else
			{
				if(test> 0 && response == 2)
				{
					System.out.println("You have declined .. invitation!");
				}
					
			}
				
			}
		
		
			
			
			
			
			
			} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
	}




	public void membersFriends()
	{
		try {
			Connection cnx= SingletonDatabaseConnection.getInstance().cnx ;
			ResultSet rs;
			Statement stmt=cnx.createStatement();
			String req ="SELECT user.userId, user.firstName, user.lastName FROM user, relationship WHERE (relationship.status =1 AND ((relationship.receiver = user.userId AND relationship.sender= "+Cache.member.getMember_id()+") OR (relationship.receiver= "+Cache.member.getMember_id()+" AND relationship.sender = user.userId ))) ;";
			rs = stmt.executeQuery(req);
			while(rs.next())
			{
				System.out.println(rs.getInt(1)+"\t \t "+rs.getString(2)+"\t \t "+rs.getString(3));
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void friendsNotAdminInGroup(int idGroup)
	{
		try {
			Connection cnx= SingletonDatabaseConnection.getInstance().cnx ;
			ResultSet rs;
			Statement stmt=cnx.createStatement();
			String req ="SELECT user.userId, user.firstName, user.lastName FROM user, relationship WHERE (relationship.status =1 AND ((relationship.receiver = user.userId AND relationship.sender= "+Cache.member.getMember_id()+") OR (relationship.receiver= "+Cache.member.getMember_id()+" AND relationship.sender = user.userId" +
					" AND NOT EXISTS (SELECT 1 FROM groupmanager WHERE groupId = "+ idGroup +" AND 	userId = "+ Cache.member.getMember_id()+" ) ))) ;";

			rs = stmt.executeQuery(req);
			while(rs.next())
			{
				System.out.println(rs.getInt(1)+"\t \t "+rs.getString(2)+"\t \t "+rs.getString(3));
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//add message on the wall
	public int addToWall(int userWallId, String content) {
		Connection cnx= SingletonDatabaseConnection.getInstance().cnx;
		try (Statement stmt1 = cnx.createStatement()) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date utilDate = new java.util.Date();
			String dateTime = format.format(utilDate.getTime());
			return stmt1.executeUpdate("INSERT INTO post(creator, wall_user_id, creationTime, content) " +
					"VALUES(" + Cache.member.getMember_id() + ", " + userWallId + " ,  '" + dateTime + "', '"+ content +"');");
		}
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
		return 0;
	}

	public void consultFriendTimelineById(int userwallId) {
		try {
			Connection cnx= SingletonDatabaseConnection.getInstance().cnx ;
			ResultSet rs;
			Statement stmt=cnx.createStatement();
			String req ="SELECT post.creationTime, user.firstName, user.lastName, post.content FROM post, user WHERE user.userId = post.creator AND post.wall_user_id = "+userwallId;
			rs = stmt.executeQuery(req);
			int count = 0;
			while(rs.next())
			{
				count++;
				System.out.println(rs.getDate(1)+"\t \t "+rs.getString(2)+"\t \t "+rs.getString(3)+"\t \t "+rs.getString(4));
			}
			if (count == 0) {
				System.out.println("sorry this user don't have any post on his timeline");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}