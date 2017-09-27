package org.athento.nuxeo.csvimport.operation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.work.api.WorkManager;
import org.nuxeo.ecm.core.work.api.WorkManager.Scheduling;
import org.nuxeo.ecm.csv.core.CSVImporterOptions;
import org.nuxeo.ecm.csv.core.CSVImporterWork;
import org.nuxeo.runtime.api.Framework;

import java.io.File;

/**
 * Import CSV operation.
 * 
 * @author victorsanchez
 * 
 */
@Operation(id = CSVImportOperation.ID, category = "CSV", label = "LoadCSV", description = "CSV Import operation.")
public class CSVImportOperation {

	/** Log. */
	public static final Log LOG = LogFactory.getLog(CSVImportOperation.class);

	/** Operation ID. */
	public final static String ID = "CSV.Import";

	/** Session. */
	@Context
	protected CoreSession session;

	/** Destiny path. */
	@Param(name = "destinyPath")
	protected String destinyPath;

	/** Schedule mode. */
	@Param(name = "scheduleMode", required = false)
    protected String scheduleMode;

	/** Notify by email. */
	@Param(name = "email", required = false)
    protected boolean email = true;

	/** Copy file. */
	@Param(name = "copyFile", required = false)
    protected boolean copyFile = true;

	/**
	 * Run method of Import a CSV file application operation.
	 * 
	 * @throws Exception
	 *             on error
	 */
	@OperationMethod
	public void run(Blob blob) throws Exception {
		// Make a file blob
		FileBlob blobFile = new FileBlob(blob.getStream());
		if (copyFile) {
		    String path = Framework.getProperty("csv.import.copyfile.path", null);
		    if (path != null) {
		        File copiedDir = new File(path);
		        if (!copiedDir.exists()) {
                    copiedDir.mkdirs();
                }
                File copiedFile = new File(copiedDir.getAbsoluteFile() + "/" + blob.getFilename());
                FileUtils.copy(blobFile.getFile(), copiedFile);
                if (LOG.isInfoEnabled()) {
                    LOG.info("CSV to " + copiedDir.getAbsolutePath() + " copied.");
                }
            }
		}
		// Options for notification
		CSVImporterOptions options = new CSVImporterOptions.Builder()
				.sendEmail(email).build();
		// Make the importer instance
		CSVImporterWork work = new CSVImporterWork(session.getRepositoryName(),
				destinyPath, session.getPrincipal().getName(),
				blobFile, options);
		WorkManager workManager = Framework.getLocalService(WorkManager.class);
		// Get scheduling mode
		WorkManager.Scheduling scheduling = getSchedulingMode(scheduleMode);
		LOG.info("Starting CSV importing of " + blob.getFilename() + " to "
				+ destinyPath + " with mode " + scheduling.name());
		// Schedule work
		workManager.schedule(work, scheduling);
	}

	/**
	 * Get scheduling mode. Default Enqueue.
	 * 
	 * @param mode
	 *            of scheduling
	 * @return scheduling or null
	 */
	private Scheduling getSchedulingMode(String mode) {
		if (mode != null) {
			for (WorkManager.Scheduling scheduleMode : WorkManager.Scheduling
					.values()) {
				if (scheduleMode.name().equalsIgnoreCase(mode)) {
					return scheduleMode;
				}
			}
		}
		return WorkManager.Scheduling.ENQUEUE;
	}
}
