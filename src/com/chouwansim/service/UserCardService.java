package com.chouwansim.service;

import java.sql.Date;
import java.sql.SQLException;

import java.util.Calendar;
import java.util.List;

import com.chouwansim.dao.CardKeepaliveDao;
import com.chouwansim.dao.UserCardDao;

import com.chouwansim.entity.CardKeepalive;
import com.chouwansim.entity.UserCard;

public class UserCardService {

    private UserCardDao userCardDao =
        new UserCardDao();

    private CardKeepaliveDao keepaliveDao =
        new CardKeepaliveDao();

    public List<UserCard> findByUserId(
        long userId)
        throws SQLException {

        List<UserCard> cards =
            userCardDao.findByUserId(
                userId
            );

        for (int i = 0;
             i < cards.size();
             i++) {

            UserCard card =
                cards.get(i);

            CardKeepalive keepalive =
                keepaliveDao
                .findByUserCardAndUser(
                    card.getId(),
                    userId
                );

            if (keepalive != null) {

                fillRemainingDays(
                    keepalive
                );

                card.setKeepalive(
                    keepalive
                );
            }
        }

        return cards;
    }

    public UserCard findByIdAndUser(
        long cardId,
        long userId)
        throws SQLException {

        UserCard card =
            userCardDao.findByIdAndUser(
                cardId,
                userId
            );

        if (card == null) {
            return null;
        }

        CardKeepalive keepalive =
            keepaliveDao
            .findByUserCardAndUser(
                cardId,
                userId
            );

        if (keepalive != null) {

            fillRemainingDays(
                keepalive
            );

            card.setKeepalive(
                keepalive
            );
        }

        return card;
    }

    public void saveCard(
        UserCard card)
        throws SQLException {

        normalizeCard(
            card
        );

        validateCard(
            card
        );

        if (card.getId() == 0L) {

            userCardDao.insert(
                card
            );

        } else {

            UserCard existing =
                userCardDao
                .findByIdAndUser(
                    card.getId(),
                    card.getUserId()
                );

            if (existing == null) {

                throw new IllegalArgumentException(
                    "卡片不存在，或者您无权修改该卡片。"
                );
            }

            card.setProductId(
                existing.getProductId()
            );

            userCardDao.update(
                card
            );
        }
    }

    public void saveKeepalive(
        long userId,
        CardKeepalive keepalive)
        throws SQLException {

        if (keepalive == null) {

            throw new IllegalArgumentException(
                "保号设置不能为空。"
            );
        }

        if (keepalive.getUserCardId()
            <= 0L) {

            throw new IllegalArgumentException(
                "卡片编号不正确。"
            );
        }

        UserCard card =
            userCardDao.findByIdAndUser(
                keepalive.getUserCardId(),
                userId
            );

        if (card == null) {

            throw new IllegalArgumentException(
                "卡片不存在，或者您无权修改该卡片。"
            );
        }

        validateKeepalive(
            keepalive
        );

        Date nextDate =
            calculateNextDate(
                keepalive.getLastDate(),
                keepalive.getCycleMonths()
            );

        keepalive.setNextDate(
            nextDate
        );

        fillRemainingDays(
            keepalive
        );

        keepaliveDao.save(
            keepalive,
            userId
        );
    }

    public void recordKeepalive(
        long userId,
        long cardId)
        throws SQLException {

        if (cardId <= 0L) {

            throw new IllegalArgumentException(
                "卡片编号不正确。"
            );
        }

        UserCard card =
            userCardDao.findByIdAndUser(
                cardId,
                userId
            );

        if (card == null) {

            throw new IllegalArgumentException(
                "卡片不存在，或者您无权操作该卡片。"
            );
        }

        CardKeepalive keepalive =
            keepaliveDao
            .findByUserCardAndUser(
                cardId,
                userId
            );

        if (keepalive == null) {

            throw new IllegalArgumentException(
                "该卡片尚未设置保号计划。"
            );
        }

        Date today =
            getToday();

        keepalive.setLastDate(
            today
        );

        Date nextDate =
            calculateNextDate(
                today,
                keepalive.getCycleMonths()
            );

        keepalive.setNextDate(
            nextDate
        );

        validateKeepalive(
            keepalive
        );

        fillRemainingDays(
            keepalive
        );

        keepaliveDao.save(
            keepalive,
            userId
        );
    }

    /**
     * 关闭卡片。
     *
     * 数据仍然保留，
     * 状态变成 CLOSED。
     */
    public void closeCard(
        long userId,
        long cardId)
        throws SQLException {

        UserCard card =
            userCardDao.findByIdAndUser(
                cardId,
                userId
            );

        if (card == null) {

            throw new IllegalArgumentException(
                "卡片不存在，或者您无权操作该卡片。"
            );
        }

        if (UserCard.STATUS_CLOSED.equals(
                card.getStatus())) {

            throw new IllegalArgumentException(
                "该卡片已经处于关闭状态。"
            );
        }

        userCardDao.close(
            cardId,
            userId
        );
    }

    /**
     * 彻底删除卡片。
     *
     * card_keepalive 会同时删除。
     * 历史订单不会受到影响。
     */
    public void deleteCard(
        long userId,
        long cardId)
        throws SQLException {

        UserCard card =
            userCardDao.findByIdAndUser(
                cardId,
                userId
            );

        if (card == null) {

            throw new IllegalArgumentException(
                "卡片不存在，或者您无权删除该卡片。"
            );
        }

        userCardDao.delete(
            cardId,
            userId
        );
    }

    private Date calculateNextDate(
        Date lastDate,
        int cycleMonths) {

        Calendar calendar =
            Calendar.getInstance();

        calendar.setTime(
            lastDate
        );

        calendar.add(
            Calendar.MONTH,
            cycleMonths
        );

        return new Date(
            calendar.getTimeInMillis()
        );
    }

    private Date getToday() {

        Calendar calendar =
            Calendar.getInstance();

        calendar.set(
            Calendar.HOUR_OF_DAY,
            0
        );

        calendar.set(
            Calendar.MINUTE,
            0
        );

        calendar.set(
            Calendar.SECOND,
            0
        );

        calendar.set(
            Calendar.MILLISECOND,
            0
        );

        return new Date(
            calendar.getTimeInMillis()
        );
    }

    private void fillRemainingDays(
        CardKeepalive keepalive) {

        if (keepalive == null
            || keepalive.getNextDate()
               == null) {

            return;
        }

        Calendar today =
            Calendar.getInstance();

        today.set(
            Calendar.HOUR_OF_DAY,
            0
        );

        today.set(
            Calendar.MINUTE,
            0
        );

        today.set(
            Calendar.SECOND,
            0
        );

        today.set(
            Calendar.MILLISECOND,
            0
        );

        Calendar target =
            Calendar.getInstance();

        target.setTime(
            keepalive.getNextDate()
        );

        target.set(
            Calendar.HOUR_OF_DAY,
            0
        );

        target.set(
            Calendar.MINUTE,
            0
        );

        target.set(
            Calendar.SECOND,
            0
        );

        target.set(
            Calendar.MILLISECOND,
            0
        );

        long difference =
            target.getTimeInMillis()
            - today.getTimeInMillis();

        long days =
            difference
            / (24L
               * 60L
               * 60L
               * 1000L);

        keepalive.setRemainingDays(
            days
        );
    }

    private void validateCard(
        UserCard card) {

        if (card == null) {

            throw new IllegalArgumentException(
                "卡片信息不能为空。"
            );
        }

        if (card.getUserId() <= 0L) {

            throw new IllegalArgumentException(
                "用户信息无效。"
            );
        }

        if (isEmpty(
                card.getNickname())) {

            throw new IllegalArgumentException(
                "请输入卡片昵称。"
            );
        }

        String status =
            card.getStatus();

        if (!UserCard.STATUS_ACTIVE
                .equals(status)
            && !UserCard.STATUS_INACTIVE
                .equals(status)
            && !UserCard.STATUS_CLOSED
                .equals(status)) {

            throw new IllegalArgumentException(
                "卡片状态不正确。"
            );
        }
    }

    private void validateKeepalive(
        CardKeepalive keepalive) {

        String method =
            keepalive.getMethod();

        if (!CardKeepalive
                .METHOD_RECHARGE
                .equals(method)
            && !CardKeepalive
                .METHOD_CONSUMPTION
                .equals(method)) {

            throw new IllegalArgumentException(
                "请选择正确的保号方式。"
            );
        }

        if (keepalive.getCycleMonths()
            < 1
            || keepalive.getCycleMonths()
               > 12) {

            throw new IllegalArgumentException(
                "保号周期必须在 1 至 12 个月之间。"
            );
        }

        if (keepalive.getLastDate()
            == null) {

            throw new IllegalArgumentException(
                "请选择上次保号日期。"
            );
        }
    }

    private void normalizeCard(
        UserCard card) {

        if (card == null) {
            return;
        }

        card.setNickname(
            trim(
                card.getNickname()
            )
        );

        card.setPhoneNumber(
            trim(
                card.getPhoneNumber()
            )
        );

        card.setNote(
            trim(
                card.getNote()
            )
        );

        if (isEmpty(
                card.getStatus())) {

            card.setStatus(
                UserCard.STATUS_ACTIVE
            );
        }
    }

    private String trim(
        String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }

    private boolean isEmpty(
        String value) {

        return value == null
            || value.trim().length() == 0;
    }
}