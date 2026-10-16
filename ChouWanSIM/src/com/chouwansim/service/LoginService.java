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
            userAccountDao.findByEmail(
                email
            );

        if (user == null) {
            return null;
        }

        if (user.getStatus() != 1) {
            return null;
        }

        boolean ok =
            PasswordUtil.verifyPassword(
                password,
                user.getPasswordSalt(),
                user.getPasswordIterations(),
                user.getPasswordHash()
            );

        if (!ok) {
            return null;
        }

        return user;
    }
}
