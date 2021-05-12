package services;

import models.Groups;
import models.Member;
import utils.Cache;
import utils.ConsoleColors;
import utils.SingletonDatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GroupsService {
    public void joinGroup() {
        try {

            Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
            Statement stmt1 = cnx.createStatement();
            //	ResultSet rs=stmt1.executeQuery("Select * from groups"); //select only groups where not joined
            ResultSet rs = stmt1.executeQuery("select g.groupId, g.groupName, g.groupCreationDate, g.genre "
                    + "from groups g "
                    + "where NOT EXISTS ( SELECT 1 " +
                    "                      FROM groupmembers gm" +
                    "                     WHERE gm.userId = " + Cache.member.getMember_id() +
                    "                       AND gm.groupId = g.groupId" +
                    "                  ) " + ";");
            System.out.println("Here is the list of groups available to you:\n");
            System.out.println(" ID\t  " + "Group Name" + "\t \t" + "Creation Date" + "\t \t" + "Genre");
            while (rs.next()) {
                System.out.println(" " + rs.getInt(1) + "\t   " + rs.getString(2) + "\t \t" + rs.getDate(3) + "\t \t " + rs.getString(4));
            }
            System.out.println("---------------------------------------");
            System.out.println("Would you like to join a group? (Y/N)");
            Scanner input = new Scanner(System.in);
            String yesNo = input.nextLine();
            if (yesNo.equals("Y") || yesNo.equals("y")) {
                System.out.println("Enter the ID of the group that you would like to join...");
                int response = input.nextInt();
                Statement stmt2 = cnx.createStatement();
                int test = stmt2.executeUpdate("INSERT INTO groupmembers( groupId , userId ) VALUES(" + response + "," + MemberService.currentUser.getMember_id() + ");");
                if (test > 0) {
                    System.out.println(ConsoleColors.ACC_COLOR + "You have joined the group number " + response + " successfully!" + ConsoleColors.MENU_COLOR);
                }


            } else {

                System.out.println("Maybe next time!");
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());

        }


    }

    public void createGroup(Groups groups) throws SQLException {
        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
        int insertResult;
        try (Statement stmt1 = cnx.createStatement()) {
            insertResult = stmt1.executeUpdate("INSERT INTO groups ( groupName, groupCreationDate, groupCreator, genre) " +
                    "VALUES ('" + groups.getGroupName() + "', '" + groups.getCreationDate() + "', '" + groups.getCreator().getMember_id() + "', '" + groups.getGenre() + "');", Statement.RETURN_GENERATED_KEYS);

            if (insertResult == 0) {
                throw new SQLException(ConsoleColors.WARNING_COLOR + "Insert failed, no rows are affected.\n" + ConsoleColors.MENU_COLOR);
            }

            try (ResultSet generatedKeys = stmt1.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    addAdmin(Cache.member.getMember_id(), generatedKeys.getInt(1));
                } else {

                    throw new SQLException(ConsoleColors.WARNING_COLOR + "Creating admin failed, no ID obtained.\n" + ConsoleColors.MENU_COLOR);

                }
            }
        }
    }

    public int deleteGroup(int groupId) {
        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
        deleteAllAdminsFromGroup(groupId);
        deleteAllMembersFromGroup(groupId);
        try (Statement stmt1 = cnx.createStatement()) {
            return stmt1.executeUpdate("DELETE FROM groups WHERE groupId = " + groupId);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }

    public int deleteMemberFromGroup(int groupId, int memberId) {
        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
        try (Statement stmt1 = cnx.createStatement()) {
            return stmt1.executeUpdate("DELETE FROM groupmembers WHERE groupId = " + groupId + " AND userId = " + memberId);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }

    public int deleteAllMembersFromGroup(int groupId) {
        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
        try (Statement stmt1 = cnx.createStatement()) {
            return stmt1.executeUpdate("DELETE FROM groupmembers WHERE groupId = " + groupId);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }

    public int deleteAllAdminsFromGroup(int groupId) {
        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
        try (Statement stmt1 = cnx.createStatement()) {
            return stmt1.executeUpdate("DELETE FROM groupmanager WHERE groupId = " + groupId);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }

    public int addAdmin(int idUser, int idGroup) {
        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
        try (Statement stmt1 = cnx.createStatement()) {
            return stmt1.executeUpdate("INSERT INTO groupmanager( groupId , userId ) " +
                    "VALUES(" + idGroup + "," + idUser + ");");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }

    public int deleteAdmin(int idAdmin, int groupId) {
        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
        try (Statement stmt1 = cnx.createStatement()) {
            return stmt1.executeUpdate("DELETE FROM groupmanager WHERE groupId = " + groupId + " AND userId = " + idAdmin);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }

    public List<Groups> getGroupsWhereLoggedInUserIsAdmin() {
        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
        ArrayList<Groups> groups = new ArrayList<>();
        String req = "SELECT groups.groupId, groups.groupCreator, groups.groupName, groups.genre, groups.groupCreationDate from groupmanager, groups WHERE groups.groupId = groupmanager.groupId AND groupmanager.userId = " + Cache.member.getMember_id();
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
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return Collections.emptyList();
    }
    public List<Member> getGroupMembers(int idGroup) {
        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
        ArrayList<Member> members = new ArrayList<>();
        String req = "SELECT user.userId,user.firstName,user.lastName from groupmembers, user " +
                "WHERE groupmembers.userId = user.userId AND groupmembers.groupId = " + idGroup;
        try (Statement stmt1 = cnx.createStatement()) {
            ResultSet rs = stmt1.executeQuery(req);
            while (rs.next()) {

                Member member = new Member();
                member.setMember_id(rs.getInt(1));
                member.setFirstName(rs.getString(2));
                member.setLastName(rs.getString(3));

                members.add(member);
            }
            return members;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return Collections.emptyList();
    }
    public List<Member> getGroupAdmins(int idGroup) {
        Connection cnx = SingletonDatabaseConnection.getInstance().cnx;
        ArrayList<Member> members = new ArrayList<>();
        String req = "SELECT user.userId,user.firstName,user.lastName from groupmanager, user " +
                "WHERE groupmanager.userId = user.userId AND groupmanager.groupId = " + idGroup;
        try (Statement stmt1 = cnx.createStatement()) {
            ResultSet rs = stmt1.executeQuery(req);
            while (rs.next()) {

                Member member = new Member();
                member.setMember_id(rs.getInt(1));
                member.setFirstName(rs.getString(2));
                member.setLastName(rs.getString(3));

                members.add(member);
            }
            return members;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return Collections.emptyList();
    }
}
