package com.camelot.pmt.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TreeUtilCommon {

    /**
     * 实体类名称
     */
    public static final String SYSRESOURCE = "com.camelot.pmt.model.SysResource";

    /**
     * 部门组实体
     */
    public static final String SYSGROUP = "com.camelot.pmt.model.SysGroupDTO";
    /**
     * 组装实体树
     *
     * @param nodes
     *            要组装成树的实体List
     * @param className
     *            实体全类名
     * @param idName
     *            id字段名 类型为基本类型，自动封装类型（如Long、Integer），String
     * @param pidName
     *            父id字段名 类型为基本类型，自动封装类型（如Long、Integer），String
     * @param childrenListName
     *            子节点存放List的字段名
     * @return
     */

    public static <T> List<T> buildTree(List<T> nodes, String className, String idName, String pidName,
            String childrenListName) throws Exception {
        List<T> tree = new ArrayList<>();// 树
        for (T node : nodes) {
            // 遍历
            tree = findChildren(node, tree, className, idName, pidName, childrenListName);
            boolean isChild = isChild(node, tree, className, idName, pidName, childrenListName);
            if (!isChild) {
                // 树上没有父节点，把node挂到树上
                tree.add(node);
            }
        }
        return tree;
    }

    /**
     * 根据ID从实体树上获取节点
     *
     * @param tree
     *            实体树
     * @param id
     *            id值
     * @param className
     *            实体全类名
     * @param idName
     *            id字段名 类型为基本类型，自动封装类型（如Long、Integer），String
     * @param pidName
     *            父id字段名 类型为基本类型，自动封装类型（如Long、Integer），String
     * @param childrenListName
     *            子节点存放List的字段名
     * @return 实体节点
     * @throws Exception
     */
    public static <T> T getNodeById(List<T> tree, Object id, String className, String idName, String pidName,
            String childrenListName) throws Exception {
        if (tree == null || tree.size() == 0) {
            return null;
        }
        for (T node : tree) {
            Class<?> classObject = Class.forName(className);
            Method getId = classObject.getMethod("get" + upFirst(idName));
            Object nodeId = getId.invoke(node);

            // TODO 先不处理类型的问题，直接用简单类型、封装类型、String判断
            if (nodeId.toString().equals(id.toString())) {
                return node;
            } else {
                Method getChildrenList = classObject.getMethod("get" + upFirst(childrenListName));
                List<?> childrenList = (List<?>) getChildrenList.invoke(node);
                T object = (T) getNodeById(childrenList, id, className, idName, pidName, childrenListName);
                if (object != null) {
                    return object;
                }
            }
        }
        return null;
    }

    // node是n的父节点
    private static <T> boolean isParent(T object, T node, String className, String idName, String pidName,
            String childrenListName) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // node是n的父节点
        Class<?> classObject = Class.forName(className);
        Method getId = classObject.getMethod("get" + upFirst(idName));
        Method getPid = classObject.getMethod("get" + upFirst(pidName));
        Object nodeId = getId.invoke(node);
        Object nodePid = getPid.invoke(object);
        // 根据值的类型比较是否相同
        // TODO 先不处理类型的问题，直接用简单类型、封装类型、String判断
        if (nodePid != null && nodePid.toString().equals(nodeId.toString())) {
            return true;
        }
        return false;
    }

    private static <T> boolean isChild(T node, List<T> tree, String className, String idName, String pidName,
            String childrenListName) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> classObject = Class.forName(className);
        Method getPid = classObject.getMethod("get" + upFirst(pidName));
        Object nodePid = getPid.invoke(node);
        if (nodePid == null) {
            return false;
        }
        if (tree == null || tree.size() == 0) {
            // 要遍历的list长度为0
            return false;
        }
        for (T n : tree) {
            Method getChildrenList = classObject.getMethod("get" + upFirst(childrenListName));
            List<T> childrenList = (List<T>) getChildrenList.invoke(n);
            if (isParent(node, n, className, idName, pidName, childrenListName)) {
                // n是否是node的父亲
                if (childrenList == null) {
                    Method setChildrenList = classObject.getMethod("set" + upFirst(childrenListName), List.class);
                    setChildrenList.invoke(n, new ArrayList<T>());
                }
                childrenList = (List<T>) getChildrenList.invoke(n);
                childrenList.add(node);
                return true;
            } else {
                // 不是，则遍历n的子节点
                if (isChild(node, childrenList, className, idName, pidName, childrenListName)) {
                    return true;
                }
            }
        }
        return false;
    }

    // tree的部分根节点是node的子节点
    private static <T> List<T> findChildren(T node, List<T> tree, String className, String idName, String pidName,
            String childrenListName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, ClassNotFoundException {
        List<T> newTree = new ArrayList<T>();// 新树
        for (T n : tree) {
            if (isParent(n, node, className, idName, pidName, childrenListName)) {
                Class<?> classObject = Class.forName(className);
                Method getChildrenList = classObject.getMethod("get" + upFirst(childrenListName));
                List<T> childrenList = (List<T>) getChildrenList.invoke(node);
                if (childrenList == null) {
                    Method setChildrenList = classObject.getMethod("set" + upFirst(childrenListName), List.class);
                    setChildrenList.invoke(node, new ArrayList<T>());
                }
                childrenList = (List<T>) getChildrenList.invoke(node);
                childrenList.add(n);
            } else {
                // 不是孩子
                newTree.add(n);
            }
        }
        return newTree;
    }

    private static String upFirst(String name) {
        // 首字母大写
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

}