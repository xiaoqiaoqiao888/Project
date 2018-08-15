package com.camelot.pmt.service.impl;

import com.camelot.pmt.model.SysUserDTO;
import com.camelot.pmt.service.AbilityExhibitionService;
import com.camelot.pmt.service.SysGroupService;
import com.camelot.pmt.service.SysUserService;
import com.camelot.pmt.service.TaskService;
import com.camelot.pmt.utils.Constant;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.CollationKey;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @description: 职能能力展示服务 实现类
 * @author: Gnerv LiGen
 * @date: 2018-05-16
 **/
@Service
public class AbilityExhibitionServiceImpl implements AbilityExhibitionService {

    @Autowired
    SysGroupService sysGroupService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    TaskService taskService;

    @Override
    public PageInfo<Map<String, Object>> selectAbilityExhibition(Integer page, Integer rows, Integer groupId,
            String realName, Integer cycle) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = null;
        if (cycle > 0) {
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.MONTH, -cycle);
            time = instance.getTime();
        }

        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setGroupId(groupId);
        sysUserDTO.setRealName(realName);
        sysUserDTO.setPageSize(rows);
        sysUserDTO.setPageNum(page);

        PageInfo<Map<String, Object>> mapPageInfo = sysGroupService.listGroupUser(sysUserDTO);
        List<Map<String, Object>> list = mapPageInfo.getList();

        for (Map<String, Object> stringObjectMap : list) {
            Integer id = (Integer) stringObjectMap.get("id");
            int valuePoints = 0;
            PageInfo<Map<String, Object>> listTask = (PageInfo<Map<String, Object>>) taskService.list(page, rows, null,
                    null, id, null, Constant.Status.COMPLETED);
            List<Map<String, Object>> mapList = listTask.getList();
            for (Map<String, Object> objectMap : mapList) {
                if (time != null) {
                    Object relStartTime = objectMap.get("rel_start_time");
                    Date parse;
                    try {
                        parse = simpleDateFormat.parse(relStartTime.toString());
                        boolean after = parse.after(time);
                        if (after) {
                            valuePoints += (int) objectMap.get("task_value");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    valuePoints += (int) objectMap.get("task_value");
                }
            }
            stringObjectMap.put("valuePoints", valuePoints);
        }
        return mapPageInfo;
    }

    @Override
    public PageInfo<Map<String, Object>> selectValuePointsDetailsBySysUserId(Integer page, Integer rows,
            Integer sysUserId) {
        PageInfo<Map<String, Object>> listTask = (PageInfo<Map<String, Object>>) taskService.list(page, rows, null,
                null, sysUserId, null, Constant.Status.COMPLETED);
        return listTask;
    }

    @Override
    public Map<String, Object> selectTaskDetailsByTaskId(Integer taskId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, Object> taskDetails = new HashMap<>(8);
        Map<String, Object> stringObjectMap = taskService.taskDetail(taskId);
        int expTaskTime = (int) stringObjectMap.get("exp_task_time");
        int relTaskTime = (int) stringObjectMap.get("rel_task_time");
        stringObjectMap.put("advanceTime", expTaskTime - relTaskTime);

        Object expEndTime = stringObjectMap.get("exp_end_time");
        Object relEndTime = stringObjectMap.get("rel_end_time");

        try {
            Date parse1 = simpleDateFormat.parse(expEndTime.toString());
            Date parse2 = simpleDateFormat.parse(relEndTime.toString());
            long tempDateTime = parse1.getTime() - parse2.getTime();
            long tempDate = tempDateTime / (24 * 60 * 60 * 1000);
            stringObjectMap.put("delayTime", tempDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        taskDetails.put("taskInfo", stringObjectMap);

        List<Map<String, Object>> stringObjectMap1 = taskService.logTaskByTaskId(taskId);
        taskDetails.put("taskLog", stringObjectMap1);
        return taskDetails;
    }

    @Override
    public Map<String, Object> selectTaskTracing(Integer userId, Integer cycle) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = null;
        if (cycle > 0) {
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.MONTH, -cycle);
            time = instance.getTime();
        }

        Map<String, Object> taskTracing = new HashMap<>(16);

        Integer allTaskCount = 0;
        PageInfo<Map<String, Object>> allTaskCountList = (PageInfo<Map<String, Object>>) taskService.list(null, null,
                null, null, userId, null, null);

        List<Map<String, Object>> list = allTaskCountList.getList();

        if (time != null) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> stringObjectMap = list.get(i);
                Object relStartTime = stringObjectMap.get("rel_start_time");
                try {
                    Date parse = simpleDateFormat.parse(relStartTime.toString());
                    boolean before = parse.before(time);
                    if (before) {
                        list.remove(i);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        allTaskCount = list.size();
        taskTracing.put("allTaskCount", allTaskCount);

        Integer overTimeTaskCount = 0;
        Integer aheadOfTimeTaskCount = 0;
        Integer haveInHandTaskCount = 0;
        Integer delayHaveInHandTaskCount = 0;
        Integer waitForAcceptanceTaskCount = 0;
        Integer completeTaskCount = 0;
        for (Map<String, Object> stringObjectMap : list) {
            Object relEndTime = stringObjectMap.get("rel_end_time");
            Object expEndTime = stringObjectMap.get("exp_end_time");
            Date parse1;
            Date parse2;
            try {
                parse1 = simpleDateFormat.parse(relEndTime.toString());
                parse2 = simpleDateFormat.parse(expEndTime.toString());
                boolean before = parse1.before(parse2);
                if (before) {
                    aheadOfTimeTaskCount++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((int) stringObjectMap.get("task_state") == Constant.Status.COMPLETED) {
                overTimeTaskCount++;
            } else if ((int) stringObjectMap.get("task_state") == Constant.Status.HAVE_IN_HAND) {
                haveInHandTaskCount++;
            } else if ((int) stringObjectMap.get("task_state") == Constant.Status.DELAY_HAVE_IN_HAND) {
                delayHaveInHandTaskCount++;
            } else if ((int) stringObjectMap.get("task_state") == Constant.Status.WAIT_CHECK) {
                waitForAcceptanceTaskCount++;
            } else if ((int) stringObjectMap.get("task_state") == Constant.Status.COMPLETED) {
                completeTaskCount++;
            }
        }
        taskTracing.put("aheadOfTimeTaskCount", aheadOfTimeTaskCount);
        taskTracing.put("overTimeTaskCount", overTimeTaskCount);
        taskTracing.put("haveInHandTaskCount", haveInHandTaskCount);
        taskTracing.put("delayHaveInHandTaskCount", delayHaveInHandTaskCount);
        taskTracing.put("waitForAcceptanceTaskCount", waitForAcceptanceTaskCount);
        taskTracing.put("completeTaskCount", completeTaskCount);

        Map<String, List<Object>> taskDateMap = new TreeMap<>(new Comparator<Object>() {
            Collator collator = Collator.getInstance();

            @Override
            public int compare(Object o1, Object o2) {
                CollationKey key1 = collator.getCollationKey(o1.toString());
                CollationKey key2 = collator.getCollationKey(o2.toString());
                return key2.compareTo(key1);
            }
        });
        Map<String, List<Map<String, Object>>> taskDateTimeMap = new TreeMap<>(new Comparator<Object>() {
            Collator collator = Collator.getInstance();

            @Override
            public int compare(Object o1, Object o2) {
                CollationKey key1 = collator.getCollationKey(o1.toString());
                CollationKey key2 = collator.getCollationKey(o2.toString());
                return key2.compareTo(key1);
            }
        });
        for (Map<String, Object> stringObjectMap : list) {
            Object relStartTime = stringObjectMap.get("rel_start_time");
            getTaskDateTimeTraceSort(relStartTime, taskDateTimeMap, stringObjectMap);
            Object relEndTime = stringObjectMap.get("rel_end_time");
            getTaskDateTimeTraceSort(relEndTime, taskDateTimeMap, stringObjectMap);
        }
        getTaskDateTraceSort(taskDateMap, taskDateTimeMap);
        taskTracing.put("taskList", taskDateMap);
        return taskTracing;
    }

    private Map<String, List<Map<String, Object>>> getTaskDateTimeTraceSort(Object str,
            Map<String, List<Map<String, Object>>> taskDateTimeMap, Map<String, Object> stringObjectMap) {
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("id", stringObjectMap.get("id"));
        objectObjectHashMap.put("task_name", stringObjectMap.get("task_name"));
        objectObjectHashMap.put("rel_start_time", stringObjectMap.get("rel_start_time"));
        objectObjectHashMap.put("rel_end_time", stringObjectMap.get("rel_end_time"));
        objectObjectHashMap.put("actionTime", str);

        if (str != null) {
            List<Map<String, Object>> mapList = taskDateTimeMap.get(str.toString());
            if (mapList == null) {
                List<Map<String, Object>> taskDateTimeListMap = new ArrayList<>();
                taskDateTimeListMap.add(objectObjectHashMap);
                taskDateTimeMap.put(str.toString(), taskDateTimeListMap);
            } else {
                objectObjectHashMap.put("actionTime", str);
                mapList.add(objectObjectHashMap);
            }
        }

        return taskDateTimeMap;
    }

    private Map<String, List<Object>> getTaskDateTraceSort(Map<String, List<Object>> taskDateMap,
            Map<String, List<Map<String, Object>>> taskDateTimeMap) {

        Set<String> strings = taskDateTimeMap.keySet();
        for (String string : strings) {
            List<Map<String, Object>> mapList = taskDateTimeMap.get(string);
            String substring = string.substring(0, 10);
            List<Object> list = taskDateMap.get(substring);
            if (list == null) {
                List<Object> list1 = new ArrayList<>();
                list1.add(mapList);
                taskDateMap.put(substring, list1);
            } else {
                list.add(mapList);
            }
        }
        return taskDateMap;
    }

}
