package com.chouwansim.servlet;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chouwansim.entity.CardKeepalive;
import com.chouwansim.entity.UserCard;
import com.chouwansim.service.UserCardService;

public class UserCardServlet
    extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserCardService userCardService =
        new UserCardService();

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session =
            request.getSession(false);

        Long userId =
            getLoginUserId(
                session
            );

        if (userId == null) {

            response.sendRedirect(
                request.getContextPath()
                + "/login.jsp"
            );

            return;
        }

        try {

            moveFlashToRequest(
                request,
                session
            );

            List<UserCard> cards =
                userCardService.findByUserId(
                    userId.longValue()
                );

            request.setAttribute(
                "cards",
                cards
            );

            String mode =
                request.getParameter(
                    "mode"
                );

            if ("add".equals(mode)) {

                request.setAttribute(
                    "showAddForm",
                    Boolean.TRUE
                );
            }

            loadEditCard(
                request,
                userId.longValue()
            );

            loadKeepaliveCard(
                request,
                userId.longValue()
            );

            request.getRequestDispatcher(
                "/my-cards.jsp"
            ).forward(
                request,
                response
            );

        } catch (SQLException e) {

            throw new ServletException(
                e
            );
        }
    }

    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        request.setCharacterEncoding(
            "UTF-8"
        );

        HttpSession session =
            request.getSession(false);

        Long userId =
            getLoginUserId(
                session
            );

        if (userId == null) {

            response.sendRedirect(
                request.getContextPath()
                + "/login.jsp"
            );

            return;
        }

        String action =
            trim(
                request.getParameter(
                    "action"
                )
            );

        String errorRedirect =
            "";

        try {

            if ("saveCard".equals(
                    action)) {

                String cardId =
                    trim(
                        request.getParameter(
                            "cardId"
                        )
                    );

                if (cardId.length() > 0) {

                    errorRedirect =
                        "?editId="
                        + cardId;

                } else {

                    errorRedirect =
                        "?mode=add";
                }

                saveCard(
                    request,
                    userId.longValue()
                );

                setFlash(
                    session,
                    "卡片信息已保存。",
                    "success"
                );

                errorRedirect = "";

            } else if (
                "saveKeepalive".equals(
                    action)) {

                String cardId =
                    trim(
                        request.getParameter(
                            "cardId"
                        )
                    );

                if (cardId.length() > 0) {

                    errorRedirect =
                        "?keepaliveId="
                        + cardId;
                }

                saveKeepalive(
                    request,
                    userId.longValue()
                );

                setFlash(
                    session,
                    "保号设置已保存，下一次保号日期已自动计算。",
                    "success"
                );

                errorRedirect = "";

            } else if (
                "recordKeepalive".equals(
                    action)) {

                long cardId =
                    parseLong(
                        request.getParameter(
                            "cardId"
                        ),
                        "卡片编号"
                    );

                userCardService
                    .recordKeepalive(
                        userId.longValue(),
                        cardId
                    );

                setFlash(
                    session,
                    "本次保号已记录，并已开始新的保号周期。",
                    "success"
                );

                errorRedirect = "";

            } else if (
                "closeCard".equals(
                    action)) {

                long cardId =
                    parseLong(
                        request.getParameter(
                            "cardId"
                        ),
                        "卡片编号"
                    );

                userCardService.closeCard(
                    userId.longValue(),
                    cardId
                );

                setFlash(
                    session,
                    "卡片已关闭，持卡记录仍然保留。",
                    "success"
                );

                errorRedirect = "";

            } else if (
                "deleteCard".equals(
                    action)) {

                long cardId =
                    parseLong(
                        request.getParameter(
                            "cardId"
                        ),
                        "卡片编号"
                    );

                userCardService.deleteCard(
                    userId.longValue(),
                    cardId
                );

                setFlash(
                    session,
                    "卡片已删除。",
                    "success"
                );

                errorRedirect = "";

            } else {

                throw new IllegalArgumentException(
                    "无法识别当前操作。"
                );
            }

        } catch (IllegalArgumentException e) {

            setFlash(
                session,
                e.getMessage(),
                "error"
            );

        } catch (SQLException e) {

            throw new ServletException(
                e
            );
        }

        response.sendRedirect(
            request.getContextPath()
            + "/my-cards"
            + errorRedirect
        );
    }

    private void saveCard(
        HttpServletRequest request,
        long userId)
        throws SQLException {

        UserCard card =
            new UserCard();

        String cardIdValue =
            trim(
                request.getParameter(
                    "cardId"
                )
            );

        if (cardIdValue.length() > 0) {

            card.setId(
                parseLong(
                    cardIdValue,
                    "卡片编号"
                )
            );
        }

        card.setUserId(
            userId
        );

        card.setProductId(
            null
        );

        card.setNickname(
            trim(
                request.getParameter(
                    "nickname"
                )
            )
        );

        card.setPhoneNumber(
            trim(
                request.getParameter(
                    "phoneNumber"
                )
            )
        );

        card.setActivationDate(
            parseOptionalDate(
                request.getParameter(
                    "activationDate"
                ),
                "激活日期"
            )
        );

        card.setStatus(
            trim(
                request.getParameter(
                    "status"
                )
            )
        );

        card.setNote(
            trim(
                request.getParameter(
                    "note"
                )
            )
        );

        userCardService.saveCard(
            card
        );
    }

    private void saveKeepalive(
        HttpServletRequest request,
        long userId)
        throws SQLException {

        long cardId =
            parseLong(
                request.getParameter(
                    "cardId"
                ),
                "卡片编号"
            );

        CardKeepalive keepalive =
            new CardKeepalive();

        keepalive.setUserCardId(
            cardId
        );

        keepalive.setMethod(
            trim(
                request.getParameter(
                    "method"
                )
            )
        );

        keepalive.setCycleMonths(
            parseInt(
                request.getParameter(
                    "cycleMonths"
                ),
                "保号周期"
            )
        );

        keepalive.setLastDate(
            parseRequiredDate(
                request.getParameter(
                    "lastDate"
                ),
                "上次保号日期"
            )
        );

        keepalive.setNote(
            trim(
                request.getParameter(
                    "keepaliveNote"
                )
            )
        );

        userCardService.saveKeepalive(
            userId,
            keepalive
        );
    }

    private void loadEditCard(
        HttpServletRequest request,
        long userId)
        throws SQLException {

        String value =
            trim(
                request.getParameter(
                    "editId"
                )
            );

        if (value.length() == 0) {
            return;
        }

        try {

            long cardId =
                Long.parseLong(
                    value
                );

            UserCard card =
                userCardService
                .findByIdAndUser(
                    cardId,
                    userId
                );

            if (card == null) {

                request.setAttribute(
                    "pageError",
                    "没有找到该卡片，或者您无权访问。"
                );

                return;
            }

            request.setAttribute(
                "editCard",
                card
            );

        } catch (NumberFormatException e) {

            request.setAttribute(
                "pageError",
                "卡片编号格式不正确。"
            );
        }
    }

    private void loadKeepaliveCard(
        HttpServletRequest request,
        long userId)
        throws SQLException {

        String value =
            trim(
                request.getParameter(
                    "keepaliveId"
                )
            );

        if (value.length() == 0) {
            return;
        }

        try {

            long cardId =
                Long.parseLong(
                    value
                );

            UserCard card =
                userCardService
                .findByIdAndUser(
                    cardId,
                    userId
                );

            if (card == null) {

                request.setAttribute(
                    "pageError",
                    "没有找到该卡片，或者您无权访问。"
                );

                return;
            }

            request.setAttribute(
                "keepaliveCard",
                card
            );

            request.setAttribute(
                "editKeepalive",
                card.getKeepalive()
            );

        } catch (NumberFormatException e) {

            request.setAttribute(
                "pageError",
                "卡片编号格式不正确。"
            );
        }
    }

    private Date parseOptionalDate(
        String value,
        String fieldName) {

        value =
            trim(value);

        if (value.length() == 0) {
            return null;
        }

        try {

            return Date.valueOf(
                value
            );

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                fieldName
                + "格式不正确。"
            );
        }
    }

    private Date parseRequiredDate(
        String value,
        String fieldName) {

        value =
            trim(value);

        if (value.length() == 0) {

            throw new IllegalArgumentException(
                "请选择"
                + fieldName
                + "。"
            );
        }

        try {

            return Date.valueOf(
                value
            );

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                fieldName
                + "格式不正确。"
            );
        }
    }

    private long parseLong(
        String value,
        String fieldName) {

        value =
            trim(value);

        if (value.length() == 0) {

            throw new IllegalArgumentException(
                fieldName
                + "不能为空。"
            );
        }

        try {

            return Long.parseLong(
                value
            );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                fieldName
                + "格式不正确。"
            );
        }
    }

    private int parseInt(
        String value,
        String fieldName) {

        value =
            trim(value);

        if (value.length() == 0) {

            throw new IllegalArgumentException(
                fieldName
                + "不能为空。"
            );
        }

        try {

            return Integer.parseInt(
                value
            );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                fieldName
                + "格式不正确。"
            );
        }
    }

    private void setFlash(
        HttpSession session,
        String message,
        String type) {

        if (session == null) {
            return;
        }

        session.setAttribute(
            "cardFlash",
            message
        );

        session.setAttribute(
            "cardFlashType",
            type
        );
    }

    private void moveFlashToRequest(
        HttpServletRequest request,
        HttpSession session) {

        if (session == null) {
            return;
        }

        Object message =
            session.getAttribute(
                "cardFlash"
            );

        Object type =
            session.getAttribute(
                "cardFlashType"
            );

        if (message != null) {

            request.setAttribute(
                "cardFlash",
                message
            );
        }

        if (type != null) {

            request.setAttribute(
                "cardFlashType",
                type
            );
        }

        session.removeAttribute(
            "cardFlash"
        );

        session.removeAttribute(
            "cardFlashType"
        );
    }

    private String trim(
        String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }

    private Long getLoginUserId(
        HttpSession session) {

        if (session == null) {
            return null;
        }

        Object value =
            session.getAttribute(
                "loginUserId"
            );

        if (value instanceof Long) {

            return (Long) value;
        }

        if (value == null) {

            return null;
        }

        try {

            return new Long(
                value.toString()
            );

        } catch (NumberFormatException e) {

            return null;
        }
    }
}