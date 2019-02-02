package evosuite.shell;

import static evosuite.shell.EvosuiteForMethod.outputFolder;
import static evosuite.shell.EvosuiteForMethod.projectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.evosuite.result.TestGenerationResult;
import org.slf4j.Logger;

import evosuite.shell.excel.ExcelWriter;
import evosuite.shell.utils.LoggerUtils;

/**
 * 
 * @author lyly
 * Generate report with "Execution Time", "Coverage", "Age" information
 */
public class FitnessEffectiveRecorder extends ExperimentRecorder {
	private Logger log = LoggerUtils.getLogger(FitnessEffectiveRecorder.class);
	private ExcelWriter excelWriter;
	
	public FitnessEffectiveRecorder() {
		super();
		excelWriter = new ExcelWriter(FileUtils.newFile(outputFolder, projectId + "_evotest.xlsx"));
		excelWriter.getSheet("data", new String[]{"Class", "Method", "Execution Time", "Coverage", "Age"}, 0);
	}

	@Override
	public void record(String className, String methodName, TestGenerationResult r) {
		List<Object> rowData = new ArrayList<Object>();
		rowData.add(className);
		rowData.add(methodName);
		rowData.add(r.getElapseTime());
		rowData.add(r.getCoverage());
		rowData.add(r.getGeneticAlgorithm().getAge());
		try {
			excelWriter.writeSheet("data", Arrays.asList(rowData));
			logSuccessfulMethods(className, methodName);
		} catch (IOException e) {
			log.error("Error", e);
		}
	}
	
	@Override
	public void recordError(String className, String methodName, Exception e) {
		List<Object> rowData = new ArrayList<Object>();
		rowData.add(className);
		rowData.add(methodName);
		rowData.add("Error" + e.getMessage());
		try {
			excelWriter.writeSheet("data", Arrays.asList(rowData));
		} catch (IOException ex) {
			log.error("Error", ex);
		}
		
	}
}