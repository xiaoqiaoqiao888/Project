package com.camelot.pmt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.BaseCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sun
 * @date 2018/5/15 10:17
 */
@Slf4j
@Service
public class BaseCalculateServiceImpl implements BaseCalculateService {

    //工作时间
    private static final int WORK_HOURS = 8;
    //上班时间 AM
    private static final int UP_WORK_AM = 9;
    //下班时间 AM
    private static final int DOWN_WORK_AM = 12;
    //上班时间 PM
    private static final int UP_WORK_PM = 14;
    //下班时间 PM
    private static final int DOWN_WORK_PM = 18;
    /**
     * 中午休息时间
     */
    private static final int NOON_REST_HOUR = UP_WORK_PM - DOWN_WORK_AM;
    /**
     * 年月日
     */
    private static final String YEAR_MONTH_DAY = "yyyyMMdd";
    /**
     * 年月
     */
    private static final String YEAR_MONTH = "yyyyMM";

    @Value("${rest.vacations-path}")
    private String vacationsPath;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public List<SysUser> getPersonCost(List<SysUser> users, Date date) {
        return getPersonCost(users, date, true);
    }

    @Override
    public List<SysUser> getPersonCost(List<SysUser> users, Date date, boolean isDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //获取传入时间月的总天数
        int monthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //获取传入时间的月的休息日
        int restDays = getVacations(date).size();
        //计算传入月的工作天数
        int workDays = monthDays - restDays;
        if (isDay) {
            for (SysUser sysUser : users) {
                sysUser.setCost(sysUser.getCost().divide(BigDecimal.valueOf(workDays)));
            }
        } else {
            for (SysUser sysUser : users) {
                sysUser.setCost(sysUser.getCost().divide(BigDecimal.valueOf(workDays), 2, BigDecimal.ROUND_HALF_UP)
                        .divide(BigDecimal.valueOf(WORK_HOURS), 2, BigDecimal.ROUND_HALF_UP));
            }
        }
        return users;
    }

    @Override
    public double getWorkHours(Date startDate, Date endDate) {
        List<String> days = getDates(startDate, endDate);
        if (days.size() > 0) {
            List<String> startDays = getVacations(startDate);
            days.removeAll(startDays);
            Calendar starCalendar = Calendar.getInstance();
            starCalendar.setTime(startDate);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            if (endCalendar.get(Calendar.MONTH) != endCalendar.get(Calendar.MONTH)) {
                List<String> endDays = getVacations(endDate);
                days.removeAll(endDays);
            }
        }
        double dayHour = getHours(startDate, endDate);
        if (days.size() > 0) {
            dayHour = dayHour + days.size() * WORK_HOURS;
        }
        return dayHour;
    }

    @Override
    public boolean isWorkDays(Date nowDate) {
        List<String> restDays = getVacations(nowDate);
        String nowDay = DateFormatUtils.format(nowDate, YEAR_MONTH_DAY);
        if (restDays.contains(nowDay)) {
            return false;
        }
        return true;
    }

    private double getHours(Date startDate, Date endDate) {
        Calendar start = processBeginTime(startDate);
        int startHour = start.get(Calendar.HOUR_OF_DAY);
        int startMenu = start.get(Calendar.MINUTE);
        Calendar end = processEndTime(endDate);
        int endHour = end.get(Calendar.HOUR_OF_DAY);
        int endMenu = end.get(Calendar.MINUTE);
        //若开始时间晚于结束时间，返回0
        if (startDate.getTime() > endDate.getTime()) {
            return 0;
        }
        int dayHours = 0;
        //处理开始结束是同一天的情况
        if (start.get(Calendar.DAY_OF_MONTH) == end.get(Calendar.DAY_OF_MONTH)) {
            dayHours = endHour - startHour;
            if (startHour >= UP_WORK_AM && startHour <= DOWN_WORK_AM && endHour >= UP_WORK_PM) {
                dayHours = dayHours - NOON_REST_HOUR;
            }
        } else {
            //处理开始结束不是同一天的情况
            int oneDays = DOWN_WORK_PM - startHour;
            int twoDays = endHour - UP_WORK_AM;
            if (startHour <= DOWN_WORK_AM) {
                oneDays = oneDays - NOON_REST_HOUR;
            }
            if (endHour >= UP_WORK_PM) {
                twoDays = twoDays - NOON_REST_HOUR;
            }
            dayHours += (oneDays + twoDays);
        }
        double menu;
        int menus;
        menus = endMenu + dayHours * 60 - startMenu;
        menu = new BigDecimal(menus / 60f).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        return menu;
    }

    private Calendar processEndTime(Date date) {
        Calendar end = Calendar.getInstance();
        end.setTime(date);
        int endHour = end.get(Calendar.HOUR_OF_DAY);
        //若结束时间晚于下午下班时间，将其设置为下午下班时间
        if (endHour >= DOWN_WORK_PM) {
            endHour = DOWN_WORK_PM;
        } else {
            //若结束时间介于中午休息时间，那么设置为上午下班时间
            if (endHour <= UP_WORK_PM && endHour >= DOWN_WORK_AM) {
                endHour = UP_WORK_PM;
            } else {
                //若结束时间早于上午上班时间，将时间设置为下午下班时间
                if (endHour <= UP_WORK_AM) {
                    endHour = DOWN_WORK_PM;
                }
            }
        }
        end.set(Calendar.HOUR_OF_DAY, endHour);
        return end;
    }

    private Calendar processBeginTime(Date date) {
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        int startHour = start.get(Calendar.HOUR_OF_DAY);
        //如果开始时间大于下午下班班时间
        if (startHour >= DOWN_WORK_PM) {
            //设置任务开始时间为第二天的上午上班时间
            startHour = UP_WORK_AM;
        } else {
            //若开始时间介于中午休息时间，那么设置为下午上班时间
            if (startHour >= DOWN_WORK_AM && startHour <= UP_WORK_PM) {
                startHour = UP_WORK_PM;
            } else {
                //若开始时间早于上午上班时间，设置为上午上班时间
                if (startHour <= UP_WORK_AM) {
                    startHour = UP_WORK_AM;
                }
            }
        }
        start.set(Calendar.HOUR_OF_DAY, startHour);
        return start;
    }

    /**
     * 当开始时间大于结束时间，返回中间的日期包含开始结束日期
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return list
     */
    private List<String> getDates(Date startDate, Date endDate) {
        List<String> list = new ArrayList<>();
        String startDay = DateFormatUtils.format(startDate, YEAR_MONTH_DAY);
        String endDay = DateFormatUtils.format(endDate, YEAR_MONTH_DAY);
        //如果日期相等直接返回
        if (startDay.equals(endDay)) {
            return list;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        while (start.before(end)) {
            list.add(DateFormatUtils.format(start, YEAR_MONTH));
            start.add(Calendar.DAY_OF_YEAR, 1);
        }
        list.remove(startDay);
        list.remove(endDay);
        return list;
    }

    /**
     * 查询节假日通过时间
     *
     * @param date 时间
     * @return List = {"20181001","20181002"}
     */
    private List<String> getVacations(Date date) {
        String moth = DateFormatUtils.format(date, YEAR_MONTH);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(vacationsPath + "?m={1}",
                String.class, moth);
        HttpStatus status = responseEntity.getStatusCode();
        if (status.is2xxSuccessful()) {
            String responseEntityBody = responseEntity.getBody();
            try {
                JSONObject obj = JSON.parseObject(responseEntityBody);
                Map<String, ?> map = (Map<String, ?>) obj.get(moth);
                Set<String> set = map.keySet();
                List<String> list = new ArrayList<>();
                for (String str : set) {
                    list.add(moth + str);
                }
                return list;
            } catch (JSONException ex) {
                log.error("[BaseCalculateService-getVacations]JSON转换出错");
                throw new IllegalArgumentException("[BaseCalculateService-getVacations]JSON转换出错");
            }
        } else {
            throw new IllegalArgumentException(
                    "[BaseCalculateService-getVacations]接口调用状态码:" + status.value() + ",原因:" + status.getReasonPhrase());
        }
    }

}
