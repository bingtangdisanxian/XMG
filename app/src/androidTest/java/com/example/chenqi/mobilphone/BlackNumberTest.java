package com.example.chenqi.mobilphone;

import android.test.AndroidTestCase;

import com.example.chenqi.mobilphone.database.dao.BlackNumberDao;

import java.util.Random;

/**
 * 测试框架运行逻辑:先找到类的字节码文件,用反射创建出对象,然后准备测试环境,把测试环境的上下文传入-->getContext()必须在测试
 * 框架准备完成之后才能调用(否则拿不到上下文),如果一定要在测试方法外初始化一些变量,可以在setUp()中完成,在tearDown()释放.
 */

public class BlackNumberTest extends AndroidTestCase {
    private BlackNumberDao dao;

    //测试框架的初始化方法
    @Override
    protected void setUp() throws Exception {
        dao = new BlackNumberDao(getContext());
        super.setUp();
    }

    //测试框架擦皮的方法
    @Override
    protected void tearDown() throws Exception {
        dao = null;
        super.tearDown();
    }

    //测试添加的方法
    public void testAdd() {
        Random random = new Random();
        boolean result = false;
        for (int i = 0; i < 200; i++) {
            result = dao.addBlackNumber("186277521" + i, String.valueOf(random.nextInt(3)));
        }
        //断言
        assertEquals(result, true);
    }

    public void testDelete() {
        boolean result = dao.deleteBlackNumber("18627752172");
        assertEquals(result, true);
    }

    public void testUpdate() {
        boolean result = dao.updateMode("18627752172", "2");
        assertEquals(result, true);
    }

    public void testFind() {
        String mode = dao.findBlackNumber("18627752172");
        System.out.println(mode);
        assertEquals(mode, "2");
    }
}
