package org.athento.csv.test;

import junit.framework.TestCase;
import org.athento.nuxeo.csvimport.CSVUtils;
import org.junit.Test;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by victorsanchez on 5/10/17.
 */
public class TestCSV extends TestCase {

    @Test
    public void testFormatCSV() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.csv");
        File fis = CSVUtils.stream2file(is);
        File f = CSVUtils.generateCSVWithColumns(fis, ",", "/abc/1/asdf", true);
        System.out.println(f.getAbsolutePath());
    }
}
