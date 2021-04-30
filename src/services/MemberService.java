package services;
import models.Member;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

import utils.Cache;
import utils.SingletonDatabaseConnection;


public class MemberService {

	public static Member currentUser = new Member();

	public Boolean login(String login, String password)
	{

		try {

			Connection cnx= SingletonDatabaseConnection.getInstance().cnx ;
			Statement stmt=cnx.createStatement();
			ResultSet rs=stmt.executeQuery("select * from user where login ='"+login+"'and password='"+password+"';");

			if(rs.next()) {
				currentUser.setMember_id(rs.getInt(1));
				Cache.member = currentUser;
				return true;

			}

		}catch(Exception e){
			System.out.println(e.getMessage());


		}

		return false;

	}
	public Boolean insert(Member member) {



		try {


			Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
			//use prepared statement to prevent sql injection
			Statement stmt = cnx.createStatement();
			stmt.executeUpdate("INSERT INTO user (firstName, lastName, birthday, gender, country, city, login, password)" + " VALUES (" + "\"" + member.getFirstName() + "\"" + "," + "\"" + member.getLastName() + "\"" + "," + "\"" + member.getBirthday() + "\"" + "," + "\"" + member.getGender() + "\"" + "," + "\"" + member.getCountry() + "\"" + ", " +   "\"" + member.getCity() +   "\"" + ", " +   "\"" + member.getLogin() +  "\"" + ", " +  "\"" + member.getPassword() +  "\"" + ");");
			System.out.println("Account with username " + member.getLogin() + " has been created.");


		} catch (Exception e) {
			System.out.println(e.getMessage());


		}

		return false;

	}



	public void sendFriendRequest()
	{
		System.out.println("----------"+Cache.member.getMember_id());
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
			System.out.println("Here is the list of members on the social network:\n");
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
			System.out.println("Would you like to consult your friends requests?(Y/N)");
			Scanner input = new Scanner(System.in);
			String yesNo = input.nextLine();
			if(yesNo.equals("Y") || yesNo.equals("y")) {
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


			}
			else {
				System.out.println("Okay, maybe later!");
			}



		} catch (Exception e) {
			// TODO: handle exception
		}



	}
} 
