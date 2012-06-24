package org.facile;
import static org.facile.Facile.*;

public class Math {
	static Class<java.lang.Math> math = java.lang.Math.class;
	public static Func<Long> labs  = fn( lng, math, "abs", lng ); 
	public static Func<Double> dabs  = fn( dbl, math, "abs", dbl ); 
	public static Func<Double> abs  = fn( dbl, math, "abs", dbl ); 
	public static Func<Float> fabs  = fn( flt, math, "abs", flt ); 
	public static Func<Integer> iabs  = fn( integer, math, "abs", integer ); 
	public static Func<Double> dsin  = fn( dbl, math, "sin", dbl ); 
	public static Func<Double> sin  = fn( dbl, math, "sin", dbl ); 
	public static Func<Double> dcos  = fn( dbl, math, "cos", dbl ); 
	public static Func<Double> cos  = fn( dbl, math, "cos", dbl ); 
	public static Func<Double> dtan  = fn( dbl, math, "tan", dbl ); 
	public static Func<Double> tan  = fn( dbl, math, "tan", dbl ); 
	public static Func<Double> datan2  = fn( dbl, math, "atan2", dbl,dbl ); 
	public static Func<Double> atan2  = fn( dbl, math, "atan2", dbl,dbl ); 
	public static Func<Double> dsqrt  = fn( dbl, math, "sqrt", dbl ); 
	public static Func<Double> sqrt  = fn( dbl, math, "sqrt", dbl ); 
	public static Func<Double> dlog  = fn( dbl, math, "log", dbl ); 
	public static Func<Double> log  = fn( dbl, math, "log", dbl ); 
	public static Func<Double> dlog10  = fn( dbl, math, "log10", dbl ); 
	public static Func<Double> log10  = fn( dbl, math, "log10", dbl ); 
	public static Func<Double> dpow  = fn( dbl, math, "pow", dbl,dbl ); 
	public static Func<Double> pow  = fn( dbl, math, "pow", dbl,dbl ); 
	public static Func<Double> dexp  = fn( dbl, math, "exp", dbl ); 
	public static Func<Double> exp  = fn( dbl, math, "exp", dbl ); 
	public static Func<Double> dmin  = fn( dbl, math, "min", dbl,dbl ); 
	public static Func<Double> min  = fn( dbl, math, "min", dbl,dbl ); 
	public static Func<Float> fmin  = fn( flt, math, "min", flt,flt ); 
	public static Func<Long> lmin  = fn( lng, math, "min", lng,lng ); 
	public static Func<Integer> imin  = fn( integer, math, "min", integer,integer ); 
	public static Func<Float> fmax  = fn( flt, math, "max", flt,flt ); 
	public static Func<Long> lmax  = fn( lng, math, "max", lng,lng ); 
	public static Func<Integer> imax  = fn( integer, math, "max", integer,integer ); 
	public static Func<Double> dmax  = fn( dbl, math, "max", dbl,dbl ); 
	public static Func<Double> max  = fn( dbl, math, "max", dbl,dbl ); 
	public static Func<Float> fscalb  = fn( flt, math, "scalb", flt,integer ); 
	public static Func<Double> dscalb  = fn( dbl, math, "scalb", dbl,integer ); 
	public static Func<Double> scalb  = fn( dbl, math, "scalb", dbl,integer ); 
	public static Func<Integer> igetExponentd  = fn( integer, math, "getExponent", dbl ); 
	public static Func<Integer> igetExponentf  = fn( integer, math, "getExponent", flt ); 
	public static Func<Float> fsignum  = fn( flt, math, "signum", flt ); 
	public static Func<Double> dsignum  = fn( dbl, math, "signum", dbl ); 
	public static Func<Double> signum  = fn( dbl, math, "signum", dbl ); 
	public static Func<Double> dasin  = fn( dbl, math, "asin", dbl ); 
	public static Func<Double> asin  = fn( dbl, math, "asin", dbl ); 
	public static Func<Double> dacos  = fn( dbl, math, "acos", dbl ); 
	public static Func<Double> acos  = fn( dbl, math, "acos", dbl ); 
	public static Func<Double> datan  = fn( dbl, math, "atan", dbl ); 
	public static Func<Double> atan  = fn( dbl, math, "atan", dbl ); 
	public static Func<Double> dtoRadians  = fn( dbl, math, "toRadians", dbl ); 
	public static Func<Double> toRadians  = fn( dbl, math, "toRadians", dbl ); 
	public static Func<Double> dtoDegrees  = fn( dbl, math, "toDegrees", dbl ); 
	public static Func<Double> toDegrees  = fn( dbl, math, "toDegrees", dbl ); 
	public static Func<Double> dcbrt  = fn( dbl, math, "cbrt", dbl ); 
	public static Func<Double> cbrt  = fn( dbl, math, "cbrt", dbl ); 
	public static Func<Double> dIEEEremainder  = fn( dbl, math, "IEEEremainder", dbl,dbl ); 
	public static Func<Double> IEEEremainder  = fn( dbl, math, "IEEEremainder", dbl,dbl ); 
	public static Func<Double> dceil  = fn( dbl, math, "ceil", dbl ); 
	public static Func<Double> ceil  = fn( dbl, math, "ceil", dbl ); 
	public static Func<Double> dfloor  = fn( dbl, math, "floor", dbl ); 
	public static Func<Double> floor  = fn( dbl, math, "floor", dbl ); 
	public static Func<Double> drint  = fn( dbl, math, "rint", dbl ); 
	public static Func<Double> rint  = fn( dbl, math, "rint", dbl ); 
	public static Func<Integer> iround  = fn( integer, math, "round", flt ); 
	public static Func<Long> lround  = fn( lng, math, "round", dbl ); 
	public static Func<Double> drandom  = fn( dbl, math, "random"  ); 
	public static Func<Double> random  = fn( dbl, math, "random"  ); 
	public static Func<Double> dulp  = fn( dbl, math, "ulp", dbl ); 
	public static Func<Double> ulp  = fn( dbl, math, "ulp", dbl ); 
	public static Func<Float> fulp  = fn( flt, math, "ulp", flt ); 
	public static Func<Double> dsinh  = fn( dbl, math, "sinh", dbl ); 
	public static Func<Double> sinh  = fn( dbl, math, "sinh", dbl ); 
	public static Func<Double> dcosh  = fn( dbl, math, "cosh", dbl ); 
	public static Func<Double> cosh  = fn( dbl, math, "cosh", dbl ); 
	public static Func<Double> dtanh  = fn( dbl, math, "tanh", dbl ); 
	public static Func<Double> tanh  = fn( dbl, math, "tanh", dbl ); 
	public static Func<Double> dhypot  = fn( dbl, math, "hypot", dbl,dbl ); 
	public static Func<Double> hypot  = fn( dbl, math, "hypot", dbl,dbl ); 
	public static Func<Double> dexpm1  = fn( dbl, math, "expm1", dbl ); 
	public static Func<Double> expm1  = fn( dbl, math, "expm1", dbl ); 
	public static Func<Double> dlog1p  = fn( dbl, math, "log1p", dbl ); 
	public static Func<Double> log1p  = fn( dbl, math, "log1p", dbl ); 
	public static Func<Float> fcopySign  = fn( flt, math, "copySign", flt,flt ); 
	public static Func<Double> dcopySign  = fn( dbl, math, "copySign", dbl,dbl ); 
	public static Func<Double> copySign  = fn( dbl, math, "copySign", dbl,dbl ); 
	public static Func<Double> dnextAfter  = fn( dbl, math, "nextAfter", dbl,dbl ); 
	public static Func<Double> nextAfter  = fn( dbl, math, "nextAfter", dbl,dbl ); 
	public static Func<Float> fnextAfter  = fn( flt, math, "nextAfter", flt,dbl ); 
	public static Func<Float> fnextUp  = fn( flt, math, "nextUp", flt ); 
	public static Func<Double> dnextUp  = fn( dbl, math, "nextUp", dbl ); 
	public static Func<Double> nextUp  = fn( dbl, math, "nextUp", dbl ); 


}
