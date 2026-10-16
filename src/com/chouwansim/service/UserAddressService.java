package com.chouwansim.service;

import java.sql.SQLException;
import java.util.List;

import com.chouwansim.dao.UserAddressDao;
import com.chouwansim.entity.UserAddress;

public class UserAddressService {

    private UserAddressDao addressDao =
        new UserAddressDao();

    public List<UserAddress> findByUserId(
        long userId)
        throws SQLException {

        return addressDao.findByUserId(
            userId
        );
    }

    public UserAddress findByIdAndUser(
        long addressId,
        long userId)
        throws SQLException {

        return addressDao.findByIdAndUser(
            addressId,
            userId
        );
    }

    public void save(
        UserAddress address)
        throws SQLException {

        validate(address);

        if (address.getId() == 0L) {

            address.setSlot(
                addressDao.findNextSlot(
                    address.getUserId()
                )
            );

            addressDao.insert(
                address
            );

        } else {

            UserAddress existing =
                addressDao.findByIdAndUser(
                    address.getId(),
                    address.getUserId()
                );

            if (existing == null) {

                throw new IllegalArgumentException(
                    "收货地址不存在。"
                );
            }

            address.setSlot(
                existing.getSlot()
            );

            addressDao.update(
                address
            );
        }
    }

    public void delete(
        long addressId,
        long userId)
        throws SQLException {

        addressDao.delete(
            addressId,
            userId
        );
    }

    private void validate(
        UserAddress address) {

        if (isEmpty(
                address.getReceiverName())) {

            throw new IllegalArgumentException(
                "请输入收件人姓名。"
            );
        }

        if (isEmpty(
                address.getPhone())) {

            throw new IllegalArgumentException(
                "请输入手机号。"
            );
        }

        if (isEmpty(
                address.getAddress1())) {

            throw new IllegalArgumentException(
                "请输入地址1。"
            );
        }

        if (address.getReceiverName()
                   .length() > 100) {

            throw new IllegalArgumentException(
                "收件人姓名过长。"
            );
        }

        if (address.getPhone()
                   .length() > 50) {

            throw new IllegalArgumentException(
                "手机号过长。"
            );
        }
    }

    private boolean isEmpty(
        String value) {

        return value == null
            || value.trim().length() == 0;
    }
}