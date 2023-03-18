import java.io.IOException;




public class mailResults {
	public static String content;
	
	public static void sendMail() throws IOException  {

		//File directory = new File (".");
		//String keyWordXls = directory.getCanonicalPath()+"//TestCases.xls";
		//String [][] arrTestCases = ReadExcel.main(keyWordXls,"Build_Details");// contains project details 

		String flagUpdateMail = TestSuitRunner.arrTestCaseBuild[5][1];
		String mailRecipents = TestSuitRunner.arrTestCaseBuild[6][1];
		String mailReplyTo = TestSuitRunner.arrTestCaseBuild[7][1];
		
				if (flagUpdateMail.toUpperCase().contentEquals("YES")){		
					try {
						Mail.sendMail(mailReplyTo,mailRecipents,"Automated test run report",content,SaveResult.resultFilePath);
					} catch (Exception e) {
						TestSuitRunner.logger.error("mailResults|sendMail|Unable to mail result.",e);
					}
				}
	}
}

