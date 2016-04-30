package com.gaea.common.velocity.tool;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import com.gaea.common.web.context.RequestContext;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.gaea.common.web.xuser.XUser;
import com.gaea.common.web.xuser.XUserSession;

/**
 * Created by Fe on 15/8/6.
 */
public class TitanTool {

    public static final String DEFAULT_DATE_FORMAT = "yyyyMMddHHmmss";

    public String getDateRangeSuffix(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            return StringUtils.EMPTY;
        }

        StringBuffer dateRangeStr = new StringBuffer();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String beginDateStr = simpleDateFormat.format(beginDate);
        dateRangeStr.append("<span>").append(beginDateStr.substring(8, 10) + ":" + beginDateStr.substring(10, 12)).append("</span>");
        dateRangeStr.append("<span>~</span>");
        String endDateStr = simpleDateFormat.format(endDate);
        dateRangeStr.append("<span>").append(endDateStr.substring(8, 10) + ":" + endDateStr.substring(10, 12)).append("</span>");

        return dateRangeStr.toString();
    }

    public String renderCsrfTokenHiddenInput() {
        StringBuffer sb = new StringBuffer();
        sb.append("<input type=\"hidden\" name=\"csrfToken\" id=\"csrfToken\" value=\"" + XUserSession.getCurrent().getCsrfToken() + "\">");
        return sb.toString();
    }

    public String formatDecimal(BigDecimal decimal) {
        if (decimal == null) {
            return StringUtils.EMPTY;
        }
        DecimalFormat decimalFormat = new DecimalFormat("0");
        return decimalFormat.format(decimal.setScale(0, BigDecimal.ROUND_DOWN));
    }

    public String formatPrice(BigDecimal decimal) {
        String text = formatDecimal(decimal);

        if (StringUtils.isBlank(text)) {
            return "电询";
        }

        return "¥ " + text;
    }

    public String subtractionPrice(BigDecimal originalPrice, BigDecimal discountPrice) {
        if (originalPrice != null && discountPrice != null && originalPrice.compareTo(discountPrice) == 1) {
            BigDecimal result = originalPrice.subtract(discountPrice);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(result);
        } else {
            return StringUtils.EMPTY;
        }
    }

    public String cutTime(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        Date now = new Date();

        long cha = date.getTime() - now.getTime();
        return cha + "";
    }

    public String formatDate(Date date) {
        if (date == null) {
            return "常年开课";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return month + "月" + day + "日";
    }

    public boolean hasLogin() {
        XUser xUser = XUserSession.getCurrent().getXUser();
        return xUser.isSignedIn();
    }

    public String processCourseDescription(String description, int length) {
        if (StringUtils.isEmpty(description)) {
            return StringUtils.EMPTY;
        }

        int len = length;
        if (len == 0) {
            len = 15;
        }

        if (description.length() <= len) {
            return description;
        } else {
            return description.substring(0, len) + "...";
        }
    }

    public String randomNum() {
        Random random = new Random();
        int num = random.nextInt(10) + 1;
        return num + "";
    }

    public boolean isEqualsUri(String uri) {
        String reqUri = RequestContext.getRequest().getRequestURI();
        if (reqUri.equals(uri)) return true;
        else return false;
    }

    public boolean needHiddenCashBtn(BigDecimal balance) {
        if (balance == null) {
            return true;
        }

        if (balance.compareTo(new BigDecimal(0)) == 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean contain(Collection<? extends Object> coll, Object obj) {
        if (CollectionUtils.isEmpty(coll)) {
            return false;
        }
        return coll.contains(obj);
    }

    public String toJSON(Object obj) {
        if (obj == null) {
            return StringUtils.EMPTY;
        }
        return JSON.toJSONString(obj);
    }

}
