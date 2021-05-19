package com.ailin.util;


import javassist.*;

public class FileStreamUtil {

    public static void createTestClass() throws Exception {

        ClassPool pool = ClassPool.getDefault();

        //创建一个空类
        CtClass cc = pool.makeClass("com.ailin.test");

        //新增字段
        CtField param = new CtField(pool.get("java.lang.String"), "name", cc);

        //设置访问级别
        param.setModifiers(Modifier.PRIVATE);

        // 初始值是 "test"
        cc.addField(param, CtField.Initializer.constant("test"));

        // 6. 创建一个名为printName方法，无参数，无返回值，输出name值
        CtMethod ctMethod = new CtMethod(CtClass.voidType, "printName", new CtClass[]{}, cc);

        //这里会将这个创建的类对象编译为.class文件
        cc.writeFile("F:\\newClass");
    }

    public static void main(String[] args) throws Exception {
        createTestClass();
    }
}
