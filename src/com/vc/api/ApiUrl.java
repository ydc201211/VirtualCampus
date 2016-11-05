package com.vc.api;

public class ApiUrl {

	public static final String URL_HOST = "http://115.29.144.204:8080/Map/";

	public static final String URL_GET_MARK = URL_HOST + "mark/getMarks";
	public static final String URL_GET_MARK_BYID = URL_HOST
			+ "mark/getMarkById";// ��ȡĳId��Ӧ�ý�������Ϣ
	public static final String URL_GET_MARK_BYPARENTID = URL_HOST
			+ "mark/getMarksByParentId";
	public static final String URL_GET_SEARACH_MARKS = URL_HOST
			+ "mark/searchMarks";
	public static final String URL_GET_IMAGE = URL_HOST + "file/markPic/get";
	public static final String URL_GET_IMAGE_NAME = URL_HOST
			+ "mark/getMarksPic";
	public static final String URL_GET_CORRD = URL_HOST + "mark/getCoordinate";

	public static final String URL_LOGIN = URL_HOST + "user/login";
	public static final String URL_REGISTE = URL_HOST + "user/register";
	public static final String URL_CHECKACCOUNT = URL_HOST
			+ "user/checkAccount";

	public static final String URL_SEND_SMS = URL_HOST + "phone/sendMessage";

	public static final String URL_GET_USER = URL_HOST + "user/getUser";
	public static final String URL_CHANGEPSW = URL_HOST + "user/changePassword";

	public static final String URL_CHANGEImage = URL_HOST + "user/editPicInfo";
	
	public static final String URL_GET_TOKEN = URL_HOST + "user/getToken";
}
