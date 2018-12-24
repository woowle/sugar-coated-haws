package com.woowle.sugarcoatedhaws;

import com.woowle.sugarcoatedhaws.common.VO.Result;
import com.woowle.sugarcoatedhaws.controller.CategoryController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SugarCoatedHawsApplicationTests {

    @Autowired
    private CategoryController categoryController;

    @Test
    public void getAllCategory() {
        Result result = categoryController.getAllCategory();
        System.err.println(result);
    }

    @Test
    public void getChildNode() {
        Result result = categoryController.getChildNode("123");
        System.err.println(result);
    }

    @Test
    public void contextLoads() {
    }

}

