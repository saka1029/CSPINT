package test.jp.saka1029.cspint;

public class Common {

	public static String methodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

}
