package com.blog.marublo;

import java.io.BufferedInputStream;

import java.io.FileInputStream;
import java.util.Properties;
/*
 * 初期化用クラスこのクラス内にユーザーID パスワードなどをラップしている
 *
 */


public class SettingInitializer{
	private static Properties properties = init();
	public static String GENDAMA_USERID = getGendamaId();
	public static String GENDAMA_PASSWORD = getGendamaPass();
	public static String MOPPY_USERID = getMoppyId();
	public static String MOPPY_PASSWORD = getMoppyPass();


	public static Properties init() {
		Properties properties = new Properties();
		try(BufferedInputStream buf = new BufferedInputStream(new FileInputStream("/var/lib/jenkins/jobs/pointSend/workspace/target/configuration.properties"));){
		//ローカル環境設定
		//try(BufferedInputStream buf = new BufferedInputStream(new FileInputStream("configuration.properties"));){
		properties.load(buf);
		}catch (Exception e) {
			System.out.println("エラー処理に入った");
			System.out.println("プロパティファイルが読み込めません");
			System.exit(0);
		}
		return properties;
	}


	public static String getGendamaId(){
		return (properties.getProperty("GENDAMA_USERID"));
	}

	public static String getGendamaPass(){
		return (properties.getProperty("GENDAMA_PASSWORD"));
	}

	public static String getMoppyId(){
		return (properties.getProperty("MOPPY_USERID"));
	}

	public static String getMoppyPass(){
		return (properties.getProperty("MOPPY_PASSWORD"));
	}

	public static String getGmailId(){
		return (properties.getProperty("GMAIL_USERID_POINT"));
	}

	public static String getGmailTrade(){
		return (properties.getProperty("GMAIL_USERID_TRADE"));
	}

	public static String getGmailPass(){
		return (properties.getProperty("GMAIL_PASSWORD_HORIPRO"));
	}

	public static String getMySQL(){
		return (properties.getProperty("MY_SQL_URL"));
	}

	public static String getMySQLUser(){
		return (properties.getProperty("MY_SQL_USER"));
	}

	public static String getMySQLPass(){
		return (properties.getProperty("MY_SQL_PASS"));
	}

}
