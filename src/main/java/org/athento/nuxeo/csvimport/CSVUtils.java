package org.athento.nuxeo.csvimport;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;

import java.io.*;

/**
 * CSV Utils.
 */
public final class CSVUtils {

    /**
     * Stream to file.
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static File stream2file (InputStream in) throws IOException {
        final File tempFile = File.createTempFile("athento-stream", ".tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }

    /**
     * Generate CSV with columns.
     *
     * @param csvFile
     * @param delimiter
     * @return
     */
    public static File generateCSVWithColumns(File csvFile, String delimiter, String destinyPath, boolean email) throws IOException {
        BufferedWriter bw = null;
        BufferedReader br = null;
        try {
            File resultFile = File.createTempFile("athento-csv", ".tmp");
            bw = new BufferedWriter(new FileWriter(resultFile));
            br = new BufferedReader(new FileReader(csvFile));
            String line;
            int pos = 0;
            while ((line = br.readLine()) != null) {
                if (pos++ > 0) {
                    bw.write(line + delimiter + "\"" + destinyPath + "\"" + delimiter + "\"" + email + "\"" + "\r\n");
                } else {
                    bw.write(line + delimiter + "\"DestinyPath\"" + delimiter + "\"Email\"" + "\r\n");
                }
            }
            return resultFile;
        } finally {
            if (bw != null) {
                bw.close();
            }
            if (br != null) {
                br.close();
            }
        }
    }
}
