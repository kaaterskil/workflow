package com.kaaterskil.workflow.bpm.foundation;

import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.kaaterskil.workflow.engine.parser.BpmModel;
import com.kaaterskil.workflow.util.XmlConverter;

public class ParseTest {

    private XmlConverter converter;

    @BeforeMethod
    public void setUp() {
        converter = XmlConverter.getInstance();
    }

    @Test
    public void testConverterNotNull() {
        Assert.assertNotNull(converter);
    }

    @Test
    public void testParse() throws Exception {
        final BpmModel model = (BpmModel) converter.read("/test-process.xml");
        System.out.println(model.toString());
    }
}
