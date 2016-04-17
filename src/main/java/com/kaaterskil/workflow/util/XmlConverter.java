package com.kaaterskil.workflow.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class XmlConverter {

    public static XmlConverter getInstance() {
        final XmlConverter converter = new XmlConverter();
        converter.setMarshaller(getMarshaller());
        converter.setUnmarshaller(getMarshaller());
        return converter;
    }

    private static Jaxb2Marshaller getMarshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.kaaterskil.workflow.bpm");

        final Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("jaxb.formatted.output", true);
        marshaller.setMarshallerProperties(propertyMap);
        return marshaller;
    }

    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    protected XmlConverter() {
    }

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    public void write(String fileName, Object graph) throws XmlMappingException, IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            marshaller.marshal(graph, new StreamResult(fos));
        } finally {
            fos.close();
        }
    }

    public Object read(String fileName) throws XmlMappingException, IOException {
        InputStream is = null;
        try {
            is = XmlConverter.class.getResourceAsStream(fileName);
            return unmarshaller.unmarshal(new StreamSource(is));
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

}
