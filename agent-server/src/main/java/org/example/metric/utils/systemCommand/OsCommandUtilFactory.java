package org.example.metric.utils.systemCommand;

public class OsCommandUtilFactory {
	public static String os = System.getProperty("os.name").toLowerCase();
	public static OsCommandUtil getOsNetworkMetricsImpl(){

		if (os.contains("win")){
			return new WindowsCommandUtilImpl();
		}
		return new CentosCommandUtilImpl();
	}

}
