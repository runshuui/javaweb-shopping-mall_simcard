package com.chouwansim.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MarkdownUtil {

    private static final Pattern ORDERED_LIST =
        Pattern.compile(
            "^(\\d+)\\.\\s+(.+)$"
        );

    private MarkdownUtil() {
    }

    /**
     * 将 Wiki 中保存的 Markdown
     * 转换成安全 HTML。
     *
     * 不允许 Markdown 中直接插入 HTML。
     */
    public static String toHtml(
        String markdown) {

        if (markdown == null
            || markdown.length() == 0) {

            return "";
        }

        String normalized =
            markdown.replace(
                "\n",
                "\n"
            ).replace(
                "\r",
                "\n"
            );

        String[] lines =
            normalized.split(
                "\n",
                -1
            );

        StringBuffer html =
            new StringBuffer();

        StringBuffer paragraph =
            new StringBuffer();

        boolean inCodeBlock = false;
        boolean inUnorderedList = false;
        boolean inOrderedList = false;

        for (int i = 0;
             i < lines.length;
             i++) {

            String line =
                lines[i];

            /*
             * fenced code block
             */
            if (line.startsWith(
                    "```")) {

                closeParagraph(
                    html,
                    paragraph
                );

                if (inUnorderedList) {

                    html.append(
                        "</ul>\n"
                    );

                    inUnorderedList =
                        false;
                }

                if (inOrderedList) {

                    html.append(
                        "</ol>\n"
                    );

                    inOrderedList =
                        false;
                }

                if (!inCodeBlock) {

                    html.append(
                        "<pre><code>"
                    );

                    inCodeBlock = true;

                } else {

                    html.append(
                        "</code></pre>\n"
                    );

                    inCodeBlock = false;
                }

                continue;
            }

            /*
             * 代码块中的内容只转义，
             * 不解析任何 Markdown。
             */
            if (inCodeBlock) {

                html.append(
                    escapeHtml(line)
                );

                html.append(
                    "\n"
                );

                continue;
            }

            /*
             * 空行结束段落和列表。
             */
            if (line.trim()
                    .length() == 0) {

                closeParagraph(
                    html,
                    paragraph
                );

                if (inUnorderedList) {

                    html.append(
                        "</ul>\n"
                    );

                    inUnorderedList =
                        false;
                }

                if (inOrderedList) {

                    html.append(
                        "</ol>\n"
                    );

                    inOrderedList =
                        false;
                }

                continue;
            }

            /*
             * 标题。
             */
            int headingLevel =
                getHeadingLevel(
                    line
                );

            if (headingLevel > 0) {

                closeParagraph(
                    html,
                    paragraph
                );

                if (inUnorderedList) {

                    html.append(
                        "</ul>\n"
                    );

                    inUnorderedList =
                        false;
                }

                if (inOrderedList) {

                    html.append(
                        "</ol>\n"
                    );

                    inOrderedList =
                        false;
                }

                String content =
                    line.substring(
                        headingLevel + 1
                    );

                html.append(
                    "<h"
                );

                html.append(
                    headingLevel
                );

                html.append(
                    ">"
                );

                html.append(
                    renderInline(
                        content.trim()
                    )
                );

                html.append(
                    "</h"
                );

                html.append(
                    headingLevel
                );

                html.append(
                    ">\n"
                );

                continue;
            }

            /*
             * 引用。
             */
            if (line.startsWith(
                    "> ")) {

                closeParagraph(
                    html,
                    paragraph
                );

                if (inUnorderedList) {

                    html.append(
                        "</ul>\n"
                    );

                    inUnorderedList =
                        false;
                }

                if (inOrderedList) {

                    html.append(
                        "</ol>\n"
                    );

                    inOrderedList =
                        false;
                }

                html.append(
                    "<blockquote>"
                );

                html.append(
                    renderInline(
                        line.substring(
                            2
                        ).trim()
                    )
                );

                html.append(
                    "</blockquote>\n"
                );

                continue;
            }

            /*
             * 无序列表。
             */
            if (line.startsWith(
                    "- ")
                || line.startsWith(
                    "* ")) {

                closeParagraph(
                    html,
                    paragraph
                );

                if (inOrderedList) {

                    html.append(
                        "</ol>\n"
                    );

                    inOrderedList =
                        false;
                }

                if (!inUnorderedList) {

                    html.append(
                        "<ul>\n"
                    );

                    inUnorderedList =
                        true;
                }

                html.append(
                    "<li>"
                );

                html.append(
                    renderInline(
                        line.substring(
                            2
                        ).trim()
                    )
                );

                html.append(
                    "</li>\n"
                );

                continue;
            }

            /*
             * 有序列表。
             */
            Matcher orderedMatcher =
                ORDERED_LIST.matcher(
                    line
                );

            if (orderedMatcher.matches()) {

                closeParagraph(
                    html,
                    paragraph
                );

                if (inUnorderedList) {

                    html.append(
                        "</ul>\n"
                    );

                    inUnorderedList =
                        false;
                }

                if (!inOrderedList) {

                    html.append(
                        "<ol>\n"
                    );

                    inOrderedList =
                        true;
                }

                html.append(
                    "<li>"
                );

                html.append(
                    renderInline(
                        orderedMatcher
                        .group(2)
                        .trim()
                    )
                );

                html.append(
                    "</li>\n"
                );

                continue;
            }

            /*
             * 普通文字出现时，
             * 先结束仍未关闭的列表。
             */
            if (inUnorderedList) {

                html.append(
                    "</ul>\n"
                );

                inUnorderedList =
                    false;
            }

            if (inOrderedList) {

                html.append(
                    "</ol>\n"
                );

                inOrderedList =
                    false;
            }

            /*
             * 普通段落。
             *
             * Markdown 连续多行文字
             * 合并为一个段落。
             */
            if (paragraph.length() > 0) {

                paragraph.append(
                    " "
                );
            }

            paragraph.append(
                line.trim()
            );
        }

        closeParagraph(
            html,
            paragraph
        );

        if (inUnorderedList) {

            html.append(
                "</ul>\n"
            );
        }

        if (inOrderedList) {

            html.append(
                "</ol>\n"
            );
        }

        /*
         * 如果 Markdown 忘记关闭代码块，
         * 页面仍然生成完整 HTML。
         */
        if (inCodeBlock) {

            html.append(
                "</code></pre>\n"
            );
        }

        return html.toString();
    }

    private static void closeParagraph(
        StringBuffer html,
        StringBuffer paragraph) {

        if (paragraph.length() == 0) {
            return;
        }

        html.append(
            "<p>"
        );

        html.append(
            renderInline(
                paragraph.toString()
            )
        );

        html.append(
            "</p>\n"
        );

        paragraph.setLength(
            0
        );
    }

    /**
     * 判断标题级别。
     *
     * 支持：
     * # 
     * ##
     * ###
     * ####
     */
    private static int getHeadingLevel(
        String line) {

        for (int level = 4;
             level >= 1;
             level--) {

            StringBuffer prefix =
                new StringBuffer();

            for (int i = 0;
                 i < level;
                 i++) {

                prefix.append(
                    "#"
                );
            }

            prefix.append(
                " "
            );

            if (line.startsWith(
                    prefix.toString())) {

                return level;
            }
        }

        return 0;
    }

    /**
     * 行内 Markdown 解析。
     *
     * 支持：
     *
     * **粗体**
     * *斜体*
     * `代码`
     * [文字](https://example.com)
     */
    private static String renderInline(
        String text) {

        if (text == null
            || text.length() == 0) {

            return "";
        }

        StringBuffer result =
            new StringBuffer();

        int index = 0;

        while (index < text.length()) {

            /*
             * 行内代码。
             */
            if (text.charAt(index)
                == '`') {

                int end =
                    text.indexOf(
                        '`',
                        index + 1
                    );

                if (end > index) {

                    result.append(
                        "<code>"
                    );

                    result.append(
                        escapeHtml(
                            text.substring(
                                index + 1,
                                end
                            )
                        )
                    );

                    result.append(
                        "</code>"
                    );

                    index =
                        end + 1;

                    continue;
                }
            }

            /*
             * Markdown 链接。
             */
            if (text.charAt(index)
                == '[') {

                int labelEnd =
                    text.indexOf(
                        "](",
                        index + 1
                    );

                if (labelEnd > index) {

                    int urlEnd =
                        text.indexOf(
                            ')',
                            labelEnd + 2
                        );

                    if (urlEnd
                        > labelEnd) {

                        String label =
                            text.substring(
                                index + 1,
                                labelEnd
                            );

                        String url =
                            text.substring(
                                labelEnd + 2,
                                urlEnd
                            ).trim();

                        if (isSafeUrl(
                                url)) {

                            result.append(
                                "<a href=\""
                            );

                            result.append(
                                escapeHtml(
                                    url
                                )
                            );

                            result.append(
                                "\">"
                            );

                            result.append(
                                renderInline(
                                    label
                                )
                            );

                            result.append(
                                "</a>"
                            );

                            index =
                                urlEnd + 1;

                            continue;
                        }
                    }
                }
            }

            /*
             * 粗体。
             */
            if (index + 1
                    < text.length()
                && text.charAt(index)
                   == '*'
                && text.charAt(index + 1)
                   == '*') {

                int end =
                    text.indexOf(
                        "**",
                        index + 2
                    );

                if (end > index + 2) {

                    result.append(
                        "<strong>"
                    );

                    result.append(
                        renderInline(
                            text.substring(
                                index + 2,
                                end
                            )
                        )
                    );

                    result.append(
                        "</strong>"
                    );

                    index =
                        end + 2;

                    continue;
                }
            }

            /*
             * 斜体。
             */
            if (text.charAt(index)
                == '*') {

                int end =
                    text.indexOf(
                        '*',
                        index + 1
                    );

                if (end > index + 1) {

                    result.append(
                        "<em>"
                    );

                    result.append(
                        renderInline(
                            text.substring(
                                index + 1,
                                end
                            )
                        )
                    );

                    result.append(
                        "</em>"
                    );

                    index =
                        end + 1;

                    continue;
                }
            }

            appendEscapedCharacter(
                result,
                text.charAt(index)
            );

            index++;
        }

        return result.toString();
    }

    /**
     * Wiki 链接暂时只允许
     * http 和 https。
     *
     * javascript:
     * data:
     * file:
     *
     * 等不会生成可点击链接。
     */
    private static boolean isSafeUrl(
        String url) {

        if (url == null) {
            return false;
        }

        String lower =
            url.toLowerCase();

        return lower.startsWith(
                   "http://"
               )
            || lower.startsWith(
                   "https://"
               );
    }

    public static String escapeHtml(
        String text) {

        if (text == null) {
            return "";
        }

        StringBuffer result =
            new StringBuffer();

        for (int i = 0;
             i < text.length();
             i++) {

            appendEscapedCharacter(
                result,
                text.charAt(i)
            );
        }

        return result.toString();
    }

    private static void appendEscapedCharacter(
        StringBuffer result,
        char ch) {

        switch (ch) {

        case '&':

            result.append(
                "&amp;"
            );

            break;

        case '<':

            result.append(
                "&lt;"
            );

            break;

        case '>':

            result.append(
                "&gt;"
            );

            break;

        case '"':

            result.append(
                "&quot;"
            );

            break;

        case '\'':

            result.append(
                "&#39;"
            );

            break;

        default:

            result.append(
                ch
            );

            break;
        }
    }
}
