package controllers;

import models.Groups;
import models.Page;
import services.PageService;
import services.MemberService;
import utils.Cache;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class PageController {
    PageService ps = new PageService();
    MemberService ms = new MemberService();
    public void likePage(){
        ps.likePage();

    }

    public void createPage() {
        Scanner scanner = new Scanner(System.in);
        Page page = new Page();
        String pagename;
        String pagegenre;
        System.out.println(" Please enter the name of the page ");
        pagename = scanner.nextLine();
        page.setPageName(pagename);
        System.out.println(" Please enter the genre of the page ");
        pagegenre = scanner.nextLine();
        page.setGenre(pagegenre);
        page.setCreationDate(LocalDate.now());
        page.setCreator(Cache.member);
        try {
            ps.createPage(page);
        } catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }

    public void addOtherAdmin(){
        ps.addOtherAdmin();
    }
    public void deletePage(){
        ps.deletePage();
    }
    public void deleteAdmin(){
        ps.deleteAdmin();

    }

}