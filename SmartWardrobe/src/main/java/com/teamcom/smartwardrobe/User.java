package com.teamcom.smartwardrobe;

/**
 * Created by d on 2018-01-19.
 */

public class User {
    //싱글톤: 하나의 객체를 이용해 돌려씀
    public String id;
    public String pw;
    public String name;
    private static User instance;

    private User() {

    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }


}

