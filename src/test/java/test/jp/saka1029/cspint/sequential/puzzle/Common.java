package test.jp.saka1029.cspint.sequential.puzzle;

public class Common {

	static String methodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

}
