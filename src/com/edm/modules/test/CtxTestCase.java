package com.edm.modules.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Spring的支持依赖注入的JUnit4 集成测试基类, 相比Spring原基类名字更短.
 * 
 * 子类需要定义applicationContext文件的位置,如:
 * @ContextConfiguration(locations = { "/app-ctx.xml" })
 */
public abstract class CtxTestCase extends AbstractJUnit4SpringContextTests {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
}
