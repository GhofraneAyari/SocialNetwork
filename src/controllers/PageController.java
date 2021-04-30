package controllers;

import models.Page;
import services.PageService;
import utils.Cache;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class PageController {
    PageService ps = new PageService();
    public void likePage(){
        ps.likePage();

    }

    public void createPage() {
        Scanner scanner = new Scanner(System.in);
        Page page = new Page();
        String pagename;
        String pagegenre;
        System.out.println("Page name please");
        pagename = scanner.nextLine();
        page.setPageName(pagename);
        System.out.println("Page genre please");
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

    public void addAdmin(){
    }
    public void addOtherAdmin(){
    }
    public void deletePage(){

    }

}