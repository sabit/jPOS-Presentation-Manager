package org.jpos.ee.pm.core.monitor;

/**This class represents a monitoring unit. Programmer can define an xml ...
 * 
 * @author yero
 * */
public class Monitor {
	/**The id of the monitor. Must be unique*/
	private String id;
	
	/**A descriptive name for the monitor*/
	private String name;
	
	/**The number of visible lines*/
	private Integer window;
	
	/**The source of the monitor information
	 * @see MonitorSource*/
	private MonitorSource source;
	
	/**A formatter for each line generated by monitor.
	 * @see MonitorFormatter */
	private MonitorFormatter formatter;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the window
	 */
	public Integer getWindow() {
		return window;
	}

	/**
	 * @param window the window to set
	 */
	public void setWindow(Integer window) {
		this.window = window;
	}

	/**
	 * @return the source
	 */
	public MonitorSource getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(MonitorSource source) {
		this.source = source;
	}

	/**
	 * @return the formatter
	 */
	public MonitorFormatter getFormatter() {
		return formatter;
	}

	/**
	 * @param formatter the formatter to set
	 */
	public void setFormatter(MonitorFormatter formatter) {
		this.formatter = formatter;
	} 
	
}
