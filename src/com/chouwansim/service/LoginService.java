package com.chouwansim.service;

import java.sql.SQLException;

import com.chouwansim.dao.UserAccountDao;
import com.chouwansim.entity.UserAccount;
import com.chouwansim.util.PasswordUtil;

public class LoginService {


    private UserAccountDao userAccountDao =
        new UserAccountDao();


    public UserAccount login(
        String email,
        String password)
        throws SQLException {


        UserAccount user =
            userAccountDao.findByEmail(email);


        if (user == null) {

            return null;

        }


        /*
         * 0 = ÒÑ½ûÓÃ
         */

        if (user.getStatus() != 1) {

            return null;

        }


        boolean correct =
            PasswordUtil.verifyPassword(
                password,
                user.getPasswordHash(),
                user.getPasswordSalt(),
                user.getPasswordIterations()
            );


        if (!correct) {

            return null;

        }


        return user;

    }

}