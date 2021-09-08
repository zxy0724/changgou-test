package com.changgou.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
    /**
     * 从yyyy-MM-dd HH:mm格式转成yyyyMMddHH格式
     * @param dateStr
     * @return
     */
    public static  String formatStr(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = simpleDateFormat.parse(dateStr);
            simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定日期的凌晨
     * @param date
     * @return
     */
    public static Date toDayStartHour(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date start = calendar.getTime();
        return start;
    }

    /**
     * 时间增加N分钟
     * @param date
     * @param minutes
     * @return
     */
    public static Date addDateMinutes(Date date,int minutes){
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,minutes);//24小时制
        date = calendar.getTime();
        return date;
    }
    /***
     * 时间递增N小时（注意这里用hour[12小时制]有点问题,但测试没问题，最好用HOUR_OF_DAY）
     * @param hour
     * @return
     */
    public static Date addDateHour(Date date,int hour){
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY,hour);//24小时制
        date = calendar.getTime();
        return date;
    }
    /***
     * 获取时间菜单
     * @return
     */
    public static List<Date> getDateMenus(){
        //定义一个List<Date>集合，存储所有时间段
        List<Date> dates = new ArrayList<>();
        //循环12次
        Date date = toDayStartHour(new Date());//凌晨
        for (int i=0;i<12;i++){
            //每次递增2小时，将每次递增的时间存入到List<Date>集合中
            dates.add(addDateHour(date,i*2));
        }
        //判断当前时间属于哪个时间范围
        Date now = new Date();
        for (Date cdate : dates) {
            //开始时间<=当前时间<开始时间+2小时
            if(cdate.getTime()<=now.getTime()&&now.getTime()<addDateHour(cdate,2).getTime()){
                now =cdate;
                break;
            }
        }
        //当前需要显示的时间菜单
        List<Date> dateMenus = new ArrayList<Date>();
        for(int i=0;i<5;i++){
            dateMenus.add(addDateHour(now,i*2));
        }
        return dateMenus;
    }

    /**
     * 时间转成yyyyMMddHH
     * @param date
     * @return
     */
    public static String date2Str(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
        return simpleDateFormat.format(date);
    }

    public static void main(String[] args) {
        //定义存储结果的集合
        List<Date> dateList = new ArrayList<>();
        //================测试凌晨时间点是否设置正确（时间间隔为2小时）====================
        //获取本日凌晨的时间点
        Date cuurentData = toDayStartHour(new Date());
        //循环12次（因为要获取每隔两个时间未一个时间段的值）
        for(int i=0;i<12;i++){
            dateList.add(addDateHour(cuurentData,i*2));
        }
        for (Date date : dateList) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = simpleDateFormat.format(date);
            System.out.println(format);
        }
        //================测试凌晨时间点是否设置正确（时间间隔为2小时）====================

        //================测试获取时间菜单====================
        System.out.println("获取时间菜单======================");
        dateList = getDateMenus();
        for (Date date : dateList) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = simpleDateFormat.format(date);
            System.out.println(format);
        }
    }
}
