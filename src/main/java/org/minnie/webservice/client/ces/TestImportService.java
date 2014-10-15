package org.minnie.webservice.client.ces;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class TestImportService {
    sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();

    public static void main(String[] argss) {
//        new TestImportService().importFolder();
        // new TestImportService().importFile();
    	new TestImportService().importFile2();
    }
    
    public void importFile2() {
        try {
            String endpointURL = "http://58.215.198.186:1080/YXHKY/services/ImportService";
            Service service = new Service();
            Call call = (Call) service.createCall();

            call.setTargetEndpointAddress(new java.net.URL(endpointURL));
            call.setOperationName(new QName("http://example3.userguide.samples", "importFile"));
            call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);

            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("file");
            root.addElement("fondsCode").setText("宜兴环科园");
            root.addElement("archiveTypeId").setText(String.valueOf(System.currentTimeMillis()));
            root.addElement("status").setText("30");
            root.addElement("titleProper").setText("文件导入测试-xxxx" + new Date());
            root.addElement("yearCode").setText("2014");
            root.addElement("createUser").setText("1001");
            root.addElement("lastUser").setText("1001");

            Element documents = root.addElement("documents");
            Element doc = null;
            java.io.File f = null;
            FileInputStream in = null;
            ByteArrayOutputStream out = null;
            doc = documents.addElement("document");
            doc.addElement("yearCode").setText("2014");
            f = new java.io.File("D:\\test.png");
            doc.addElement("path").setText(f.getAbsolutePath());
            doc.addElement("fileSize").setText(f.length() + "");
            in = new FileInputStream(f);
            out = new ByteArrayOutputStream();
            encoder.encodeBuffer(in, out);
            doc.addElement("contents").setText(out.toString());
            in.close();
            out.close();
//
//            doc = documents.addElement("document");
//            doc.addElement("yearCode").setText("2010");
//            f = new java.io.File("D:\\图片\\1.tif");
//            doc.addElement("path").setText(f.getAbsolutePath());
//            doc.addElement("fileSize").setText(f.length() + "");
//            in = new FileInputStream(f);
//            out = new ByteArrayOutputStream();
//            encoder.encodeBuffer(in, out);
//            doc.addElement("contents").setText(out.toString());
//            in.close();
//            out.close();

            String ret = (String) call.invoke(new Object[] { document.asXML() });
            // System.out.println(ret);
            write(ret, "D:\\test.xml");
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void importFolder() {
        try {
            String endpointURL = "http://localhost:8082/gdda4.1/services/ImportService";
            Service service = new Service();
            Call call = (Call) service.createCall();

            call.setTargetEndpointAddress(new java.net.URL(endpointURL));
            call.setOperationName(new QName("http://example3.userguide.samples", "importFolder"));
            call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);

            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("folder");
            root.addElement("archiveTypeId").setText("1250752693843");
            root.addElement("status").setText("30");
            root.addElement("titleProper").setText("案卷导入测试-沈涛" + new Date());
            root.addElement("yearCode").setText("2010");
            root.addElement("createUser").setText("1001");
            root.addElement("lastUser").setText("1001");

            Element files = root.addElement("files");
            Element file = files.addElement("file");
            file.addElement("titleProper").setText("卷内文件1");
            file.addElement("yearCode").setText("2010");

            file = files.addElement("file");
            file.addElement("titleProper").setText("卷内文件1");
            file.addElement("yearCode").setText("2010");

            file = files.addElement("file");
            file.addElement("titleProper").setText("卷内文件");
            file.addElement("yearCode").setText("2010");

            Element documents = file.addElement("documents");
            Element doc = null;
            java.io.File f = null;
            FileInputStream in = null;
            ByteArrayOutputStream out = null;
            doc = documents.addElement("document");
            doc.addElement("yearCode").setText("2010");
            f = new java.io.File("D:\\图片\\bl201.jpg");
            doc.addElement("path").setText(f.getAbsolutePath());
            doc.addElement("fileSize").setText(f.length() + "");
            in = new FileInputStream(f);
            out = new ByteArrayOutputStream();
            encoder.encodeBuffer(in, out);
            doc.addElement("contents").setText(out.toString());
            in.close();
            out.close();

            doc = documents.addElement("document");
            doc.addElement("yearCode").setText("2010");
            f = new java.io.File("D:\\图片\\1.tif");
            doc.addElement("path").setText(f.getAbsolutePath());
            doc.addElement("fileSize").setText(f.length() + "");
            in = new FileInputStream(f);
            out = new ByteArrayOutputStream();
            encoder.encodeBuffer(in, out);
            doc.addElement("contents").setText(out.toString());
            in.close();
            out.close();

            String ret = (String) call.invoke(new Object[] { "c:\\test.xml" });
            // System.out.println(ret);
            // write(ret,"c:\\test.xml");
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void importFile() {
        try {
            String endpointURL = "http://localhost:8082/gdda4.1/services/ImportService";
            Service service = new Service();
            Call call = (Call) service.createCall();

            call.setTargetEndpointAddress(new java.net.URL(endpointURL));
            call.setOperationName(new QName("http://example3.userguide.samples", "importFile"));
            call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);

            Document document = DocumentHelper.createDocument();
            Element root = document.addElement("file");
            root.addElement("archiveTypeId").setText("1250752664562");
            root.addElement("status").setText("30");
            root.addElement("titleProper").setText("文件导入测试-沈涛" + new Date());
            root.addElement("yearCode").setText("2010");
            root.addElement("createUser").setText("1001");
            root.addElement("lastUser").setText("1001");

            Element documents = root.addElement("documents");
            Element doc = null;
            java.io.File f = null;
            FileInputStream in = null;
            ByteArrayOutputStream out = null;
            doc = documents.addElement("document");
            doc.addElement("yearCode").setText("2010");
            f = new java.io.File("D:\\图片\\bl201.jpg");
            doc.addElement("path").setText(f.getAbsolutePath());
            doc.addElement("fileSize").setText(f.length() + "");
            in = new FileInputStream(f);
            out = new ByteArrayOutputStream();
            encoder.encodeBuffer(in, out);
            doc.addElement("contents").setText(out.toString());
            in.close();
            out.close();

            doc = documents.addElement("document");
            doc.addElement("yearCode").setText("2010");
            f = new java.io.File("D:\\图片\\1.tif");
            doc.addElement("path").setText(f.getAbsolutePath());
            doc.addElement("fileSize").setText(f.length() + "");
            in = new FileInputStream(f);
            out = new ByteArrayOutputStream();
            encoder.encodeBuffer(in, out);
            doc.addElement("contents").setText(out.toString());
            in.close();
            out.close();

            String ret = (String) call.invoke(new Object[] { document.asXML() });
            // System.out.println(ret);
            write(ret, "c:\\test.xml");
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * 写入文件
     * 
     * @param aString
     */
    public boolean write(String aString, String htmlPath) {
        boolean rtn = true;
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlPath), "GBK"));
            out.write("\n" + aString);
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return rtn;
    }
}
