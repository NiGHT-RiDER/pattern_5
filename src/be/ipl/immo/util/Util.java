package be.ipl.immo.util;

import be.ipl.immo.exceptions.ArgumentInvalideException;

public final class Util {
	public static void checkObject(Object o) throws ArgumentInvalideException {
		if (o == null)
			throw new ArgumentInvalideException();
	}
	
	public static void checkString(String s) throws ArgumentInvalideException {
		checkObject(s);
		if (s.trim().equals(""))
			throw new ArgumentInvalideException();
	}
	public static void checkNegativeOrZero(double d) throws ArgumentInvalideException {
		if (d > 0.0)
			throw new ArgumentInvalideException();
	}
	public static void checkPositiveOrZero(double d) throws ArgumentInvalideException {
		if (d < 0)
			throw new ArgumentInvalideException();
	}
	public static void checkPositive(double d) throws ArgumentInvalideException {
		if (d<=0.00001)
			throw new ArgumentInvalideException();
	}
	
	public static void checkPositive(int i) throws ArgumentInvalideException {
		if (i<=0)
			throw new ArgumentInvalideException();
	}
}
