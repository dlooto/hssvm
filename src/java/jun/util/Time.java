package jun.util;

/**
 * Tool class for calculating the time of program execution 
 * @author jun
 *
 */

public class Time {
    
	private static long startTime; 
	
    /**
     * show time using  (XXh  XXm  XXs XXms) format. This method is safe
     * @param interval  the time interval
     */
    public static String show(long interval){
        int ms = (int)(interval % 1000);
        
        long   totalSeconds = interval / 1000;
        int s = (int)(totalSeconds % 60);
        
        long totalMinutes = totalSeconds / 60;
        int m = (int)(totalMinutes % 60);
        
        long totalHours = totalMinutes / 60;
        int h = (int)(totalHours % 24);
        
        return toString(h, m, s, ms);
    }

    /**
     * Get current time,returns number of milliseconds
     */
    public static long get(){
        return System.currentTimeMillis();
    }
    
    /**
     * begin to time by getting current time. This can be used only in that 
     * there is no any method changing the startTime value after start() is
     * called.
     */
    public static void start() {
    	startTime = System.currentTimeMillis();
    }
    
    /**
     * show time interval.
     * this method must be called come after calling method Time.start()
     */
    public static String show() {
    	return show(System.currentTimeMillis() - startTime);
    }
    
    private static String toStringPro(int h, int m, int s, int ms) {
        StringBuffer result = new StringBuffer(100);
        if(h > 0){
            result.append(h + "h ");
        } 
        if(m > 0){
            result.append(m + "m ");
        }
        if(s > 0){
            result.append(s + "s ");
        }
        result.append(ms + "ms");
        
        return result.toString();
    }
    
    private static String toString(int h, int m, int s, int ms) {
        StringBuffer result = new StringBuffer(100);
        if(h > 0){
            result.append(h + "h ");
        } 
        if(m > 0){
            result.append(m + "m ");
        }
        if(s > 0){
            result.append(s + (double)ms/1000 + "s");
        } else {
            result.append((double)ms/1000 + "s");
        }
        
        return result.toString();
    }
}
