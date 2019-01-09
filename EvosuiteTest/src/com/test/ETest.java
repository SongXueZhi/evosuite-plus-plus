package com.test;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.evosuite.EvoSuite;
import org.evosuite.Properties;
import org.evosuite.Properties.Algorithm;
import org.evosuite.Properties.Criterion;
import org.evosuite.Properties.StatisticsBackend;
import org.evosuite.result.TestGenerationResult;

public class ETest {

	class Tuple {
		int time;
		double coverage;
		int age;

		public Tuple(int time, double coverage, int age) {
			super();
			this.time = time;
			this.coverage = coverage;
			this.age = age;
		}

	}

	@SuppressWarnings("unchecked")
	public Tuple evosuite(String targetClass, String targetMethod, String cp, int seconds, boolean instrumentContext) {
		EvoSuite evo = new EvoSuite();
		Properties.TARGET_CLASS = targetClass;
		// Properties.TARGET_METHOD = targetClass+".test(DDI)V";
//		Properties.ALGORITHM = Algorithm.MONOTONICGA;
//		Properties.ALGORITHM = Algorithm.RANDOM;
		Properties.TRACK_COVERED_GRADIENT_BRANCHES = true;
		Properties.CRITERION = new Criterion[] { Criterion.BRANCH };
//		Properties.STRATEGY = Strategy.RANDOM;
		

		String[] command = new String[] { 
				"-generateRandom",
//				"-generateSuite",
				// "-generateMOSuite",
//				"-generateSuiteUsingDSE",
				"-Dstrategy", "random",
				"-class", targetClass, 
				"-projectCP", cp, 
				"-Dtarget_method", targetMethod, 
				"-Dsearch_budget", String.valueOf(seconds),
				"-Dcriterion", "branch",
				"-Dinstrument_context", String.valueOf(instrumentContext), 
				"-seed", "100"
				};

		// command = new String[] { "-generateSuite", "-class", targetClass,
		// "-projectCP", cp, "-Dsearch_budget",
		// String.valueOf(seconds), "-criterion", "branch"};

		List<List<TestGenerationResult>> list = (List<List<TestGenerationResult>>) evo.parseCommandLine(command);
		for (List<TestGenerationResult> l : list) {
			for (TestGenerationResult r : l) {
				// System.out.println(r);
				System.out.println(r.getProgressInformation());
				if(r.getDistribution() != null){
					for(int i=0; i<r.getDistribution().length; i++){
						System.out.println(r.getDistribution()[i]);					
					}					
				}
				return new Tuple(r.getElapseTime(), r.getCoverage(), r.getGeneticAlgorithm().getAge());
			}
		}

		return null;
	}

	public static void main(String[] args) {
		Class<?> clazz = Example.class;
		
		String targetClass = clazz.getCanonicalName();
		Method method = clazz.getMethods()[0];

		String targetMethod = method.getName() + getSignature(method);
		String cp = "bin";

		// Properties.LOCAL_SEARCH_RATE = 1;
//		Properties.DEBUG = true;
//		Properties.PORT = 8000;
		Properties.CLIENT_ON_THREAD = true;
		Properties.STATISTICS_BACKEND = StatisticsBackend.DEBUG;

		int timeBudget = 30;
		ETest t = new ETest();
		Tuple tu = t.evosuite(targetClass, targetMethod, cp, timeBudget, true);
		
//		List<Tuple> l = new ArrayList<>();
//		for(int i=0; i<7; i++){
//			Tuple tu = t.evosuite(targetClass, targetMethod, cp, timeBudget, true);
//			l.add(tu);
//		}
//		
//		for(Tuple lu: l){
//			System.out.println(lu.time + ", " + lu.age);
//		}
	}

	public static String getSignature(Method m) {
		String sig;
		try {
			Field gSig = Method.class.getDeclaredField("signature");
			gSig.setAccessible(true);
			sig = (String) gSig.get(m);
			if (sig != null)
				return sig;
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}

		StringBuilder sb = new StringBuilder("(");
		for (Class<?> c : m.getParameterTypes())
			sb.append((sig = Array.newInstance(c, 0).toString()).substring(1, sig.indexOf('@')));
		String str = sb.append(')')
				.append(m.getReturnType() == void.class ? "V"
						: (sig = Array.newInstance(m.getReturnType(), 0).toString()).substring(1, sig.indexOf('@')))
				.toString();
		return str.replace(".", "/");
	}

}
