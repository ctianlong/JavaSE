package test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Test;

import sun.reflect.generics.tree.VoidDescriptor;


public class SmallTest {
	

	public static void main(String[] args) {
		System.out.println("main");
		// String s1 = "";
		// String s2 = "aa";
//		 StringTest st1 = new StringTest("aa");
		// StringTest st2 = new StringTest("aa");
		//
		//
		// System.out.println(s1.length());

		// Scanner input = new Scanner(System.in);
		// System.out.println("Enter:");
		// String inputString = "11";
		// inputString = input.next();
		// String input = "(5+3)*(6-2)";
		// String[] newArr = input.replaceAll("([\\+\\-\\*\\/\\(\\)])", " $1
		// ").trim().replaceAll(" ", " ").split(" ");
		// System.out.println(newArr.length);
		// for (String string : newArr) {
		// System.out.println(string);
		//
		// }

//		ScriptEngineManager manager = new ScriptEngineManager();
//
//		ScriptEngine engine = manager.getEngineByName("javascript");
//		try {
//			Double d = (Double) engine.eval("10*(5.5+9)-25/2");
//			System.out.println(d);
//		} catch (ScriptException ex) {
//
//		}

		// System.out.println(inputString.length());
		// input.close();
		// while(input.hasNext()){
		// inputString = input.next();
		// if(inputString == "1")
		// break;
		// }
		// System.out.println(inputString);
		// input.close();
	}
	
	
	@Test
	public void testGetResource(){
		System.out.println(SmallTest.class.getResource(""));
        System.out.println(SmallTest.class.getResource("/"));
        System.out.println(SmallTest.class.getClassLoader().getResource(""));  //总是相对于类加载根路径
        System.out.println(SmallTest.class.getClassLoader().getResource("/")); //没有这种用法
	}
	
	
	@Test
	public void testInteger() throws Exception{
		
		Integer i1 = new Integer(10);
		Integer i2 = new Integer(10);
		Integer i3 = Integer.valueOf(10);
		System.out.println(i1 == i2);
		
		Class cache = Integer.class.getDeclaredClasses()[0]; // 1
		Field myCache = cache.getDeclaredField("cache"); // 2
		myCache.setAccessible(true);// 3

		Integer[] newCache = (Integer[]) myCache.get(cache); // 4
		newCache[132] = newCache[133]; // 5

		int a = 2;
		int b = a + a;
		System.out.printf("%d + %d = %d", a, a, b); //
	}
	
	
	@Test
	public void testPlusPlus(){
		int[] r = new int[]{0,0,0};
		int[] t = new int[]{1,2,3};
		int i = 0;
//		r[i] = t[++i];
		r[++i] = t[i];
		System.out.println(Arrays.toString(r));
		System.out.println(Arrays.toString(t));
		System.out.println(i);
	}
	
	
	@Test
	public void testYou(){
		Integer aInteger = 1;
		test1(aInteger);
		
		// . 在正则表达式中代表任意字符，除去 行终结符 \n
		String classFile = "com.jd.".replaceAll(".", "/") + "MyClass.class";
	    System.out.println(classFile);
	    
	}
	
	public void test1(final Integer a){
	}
	
	
	
	@Test
	public void testArray(){
	    float[] f1 = new float[]{1.0f,2.0f};
	    float[] f2 = {1.0f,2.0f};
	    //System.out.println(f1 == f2[0]); 
	    
	    
	    int[] ints = {1, 2, 3};
//	    Integer[] ints = {1, 2, 3};
	    String[] strings = {"a", "b", "c"};
	    //数组也可以用foreach，但数组不是 Iterable
	    for(int i : ints){
	    	System.out.println(i);
	    }
	    for(String s: strings){
	    	System.out.println(s);
	    }
	    //下面两行出错
	    //test2(ints);
	    //test2(strings);
	    //必须明确将数组转换成  Iterable
	    test2(Arrays.asList(ints));
	    test2(Arrays.asList(strings));
	    
	    //若将基本类型数组传入 Arrays.asList ，则将整个数组当作一个元素，返回的List中只有一个元素
	    System.out.println("111" + Arrays.asList(ints).get(0)[2]);
	    
	    
	    int[] ii;
	    //ii = {1,2}; wrong
	    
	    //不能直接创建泛型数组，可以通过强制转换
	    //泛型容器比泛型数组好
//	   List<String>[] lists = new List<String>[12];  //wrong
	    List<String>[] lists2 = (List<String>[])new List[10];
	    lists2[0] = new ArrayList<String>();
//	    lists2[1] = new ArrayList<Integer>();  //wrong
	    Object[] objects = lists2;
	    objects[1] = new ArrayList<Integer>(); //actually wrong, but not show
	    List<String> a = null;
	    Object o = a;
//	    List<Object> lo = a;  //wrong
//	    List<Object>[] lObjects = lists2; //wrong
	}
	
	public <T> void test2(Iterable<T> ib){
		for(T t : ib){
			System.out.println(t);
		}
	}
	
	
	@Test
	public void testImage(){
//		ImageIO
	}
	
	

}

class StringTest {
	static {
		System.out.println("StringTest");
	}
	public String s;

	public StringTest(String s) {
		this.s = s;
	}
}
