package com.miri.launcher.model;

import java.io.Serializable;

/**
 * 照片
 * 
 * @author zengjiantao
 * 
 */
public class Photo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7414112347792080261L;

	private String name;

	private String path;

	private String dateModify;

	private String dateAdd;

	public Photo() {
	}

	public Photo(String name, String path, String dateModify,
			String dateAdd) {
		super();
		this.name = name;
		this.path = path;
		this.dateModify = dateModify;
		this.dateAdd = dateAdd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDateModify() {
		return dateModify;
	}

	public void setDateModify(String dateModify) {
		this.dateModify = dateModify;
	}

	public String getDateAdd() {
		return dateAdd;
	}

	public void setDateAdd(String dateAdd) {
		this.dateAdd = dateAdd;
	}

}
