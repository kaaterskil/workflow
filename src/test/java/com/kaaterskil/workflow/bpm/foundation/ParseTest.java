package com.kaaterskil.workflow.bpm.foundation;

import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.kaaterskil.workflow.bpm.common.process.Process;
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
        final Process process = (Process) converter.read("/test-process.xml");
        System.out.println(process.toString());
    }
}
