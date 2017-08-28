package itb2.engine;

public interface CommunicationManager {
	
	public void info(String message, Object... param);
	
	public void warning(String message, Object... param);
	
	public void error(String message, Object... param);
	
	public void inProgress(double percent);
	
	public void inProgress(double percent, String message, Object... param);
	
}
