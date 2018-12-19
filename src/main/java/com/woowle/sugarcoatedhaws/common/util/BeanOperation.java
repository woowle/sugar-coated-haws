package com.woowle.sugarcoatedhaws.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//此类通过反射来进行简单的java类的值插入
public class BeanOperation {
    private BeanOperation() {}

    //此方法用于给对象插入属性
    //emp.ename:zhangsan|emp.job:Coder
    public static void setBeanValue(Object obj, String data) throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //当前obj表示EmpAction对象
        //第一次拆分
        String[] result = data.split("\\|");
        for (int i = 0; i < result.length; i++) {
            //emp.ename:zhangsan
            String[] temp = result[i].split(":");
            //取得要插入的数据
            String value = temp[1];
            //取得具体className emp
            String className = temp[0].split("\\.")[0];
            //取得具体属性名
            String fieldName = temp[0].split("\\.")[1];

            Object realObject = getRealObject(obj, className);
            //拿到具体操作的Emp类之后，开始调用具体的set方法来设置属性
            Class<?> realClass = realObject.getClass();
            Class fieldType = realClass.getDeclaredField(fieldName).getType();

                Method setMethod = realClass.getDeclaredMethod("set" + initCap(fieldName), fieldType);
            setMethod.invoke(realObject, getVal2(value,fieldType));
        }       
    }
    //
    private static Object getRealObject(Object obj, String className) throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //拿到EmpAcrion类的Class对象
        Class<?> cls = obj.getClass();
        Field field = cls.getDeclaredField(className);
        //在当前类中没有找到此属性
        if(field == null) {
            //再从父类中找此属性
            field = cls.getField(className);
            if(field == null) {
                return null;
            }   
        }
        //获取get属性名
        String methodName = "get" + initCap(className);
        Method getObjectMethod = cls.getDeclaredMethod(methodName);
        //相当于调用empAction.getEmp();
        return getObjectMethod.invoke(obj);
    }

    private static String initCap(String value) {
        //将首字母大写然后返回
        return value.substring(0, 1).toUpperCase()+value.substring(1);
    }

    public static <T> T getVal2(String val, Class<T> type) {
        // 把val转换成type类型返回 比如说getVal("123",Integer.class) 返回一个123
        T value = null;
        try {
            Constructor<T> constructor = type.getConstructor(String.class);
            constructor.setAccessible(true);
            value = constructor.newInstance(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}