package encodedata.qssq666.a.myapplication;

import java.io.UnsupportedEncodingException;

/**
 * 
 * 面试题：                                                                 
截取字符串指定指定字节数的内容，如果指定的字节数在汉字的中间，汉字不能截取部分，只截取前面的内容。            
如"ab我",截取3个字节的字符，如果采用非iso-8859-1编码 汉字所占字符超过1个字节，所以此时只能截取"ab" 。       
题目意思：                                                                
 应该是字符串中不存在乱码的情况下，如果编码形式是iso-8859-1,那么截取字符串就是截取的字节格式，
所以主要考核的是非iso-8859-1编码格式是如何截取。                                        
                                                                     
思路：                                                                  
假设截取 n个字节，                                                           
截取字符串的n个字符，n个字符的GBK编码的字节数一定>=要截取字节个数，如果等于说明全是字母                      
如果不等，说明包含汉字，截取的n个字符的字节数>需要截取的n个字节,故截取字符n-1，                          
再次进行比较，直到，字节数n	和		截取字符串的字节数相等,那么所截取的字符串就是结果。
 iso885 1 gbk eng 1,chin 2 utf8 chinese 3 eng 1
 * @author
 *
 */
public class SubStringByBytes {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		String en = "a";
		String cn = "我";
		System.out.println("英文_ISO-8859-1：" + en.getBytes("ISO-8859-1").length);
		System.out.println("汉字_ISO-8859-1：" + cn.getBytes("ISO-8859-1").length);
		System.out.println("英文_GBK：" + en.getBytes("GBK").length);
		System.out.println("汉字_GBK：" + cn.getBytes("GBK").length);
		System.out.println("英文_UTF-8：" + en.getBytes("UTF-8").length);
		System.out.println("汉字_UTF-8：" + cn.getBytes("UTF-8").length);
		
		String str = "abc我们啊ddd";
		/*
		 * 这里还和截取何种编码的字节数有关，如果截取9个字节
		 * GBK 是  abc我们啊
		 * UTF8是  abc我们
		 */
		System.out.println(subStringByBytes(str, 9,"gbk"));
		System.out.println(subStringByBytes(str, 9,"utf-8"));
	}
	/**
	 * 
	 * @param str 要截取的字符串
	 * @param bytes 截取的字节数
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String subStringByBytes(String str, int bytes,String charSetName) throws UnsupportedEncodingException {
		String subAfter = str.substring(0, bytes);
		int temp = bytes;
		try {
			//直到截取的字符串的字节数  和   需要的 截取的字节数相等位为止
			while(bytes < subAfter.getBytes(charSetName).length){
				subAfter = subAfter.substring(0,--temp );
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return subAfter;
	}
	/**
	 * 
	 * @return 当前系统的编码格式 
	 */
	public static String getSystemEncode() {
		System.getProperties().list(System.out);// 得到当前的系统属性。并将属性列表输出到控制台
		String encoding = System.getProperty("file.encoding");
		System.out.println("Encoding:" + encoding);
		return encoding;
	}
}
