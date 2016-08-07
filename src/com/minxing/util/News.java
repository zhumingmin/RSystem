package com.minxing.util;

public class News {
	private String biaoti;
	private String leibie;

	private String yueduliang2;
	private String body;

	public News(String biaoti, String leibie, String yueduliang2, String body) {
		this.biaoti = biaoti;
		this.leibie = leibie;

		this.yueduliang2 = yueduliang2;
		this.body = body;
	}

	public String getBiaoTi() {
		return biaoti;
	}

	public String getLeiBie() {
		return leibie;
	}

	public String getYueDuLiang2() {
		return yueduliang2;
	}

	public String getBody() {
		return body;
	}

}
