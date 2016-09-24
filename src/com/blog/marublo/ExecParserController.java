package com.blog.marublo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ExecParserController {

	/**
	 * @param args
	 */
	public static void main(String[] args) {


		WebDriver driver = new FirefoxDriver();

		driver.get("http://96ut.com/ipo/schedule.php");
		//#content-body > table > tbody > tr > td > table:nth-child(20)
		//

		List<WebElement> elementOddList = driver.findElements(By.cssSelector(".odd"));
		List<WebElement> elementEvenList = driver.findElements(By.cssSelector(".even"));
		List<WebElement> elementList = new ArrayList<>();
		elementList.addAll(elementEvenList);
		elementList.addAll(elementOddList);

		List<IpoDto> ipoDtoList = new ArrayList<>();

		for(WebElement element : elementList){
			IpoDto ipoDto = new IpoDto();
			//#content > div > div > div.tablewrap > table > tbody > tr:nth-child(2) > td:nth-child(1)
			ipoDto.setOpenDate(element.findElement(By.cssSelector("td:nth-child(1)")).getText());
			ipoDto.setMeigaraCode(element.findElement(By.cssSelector("td:nth-child(2)")).getText());
			ipoDto.setMeigaraName(element.findElement(By.cssSelector("td:nth-child(3)")).getText());
			ipoDto.setBBDate(element.findElement(By.cssSelector("td:nth-child(5)")).getText());

			ipoDtoList.add(ipoDto);
		}
		//BBdate 10/07 (金)\n10/14 (金)
		//02/16 (火)\n02/22 (月)
		//opendate 10/25 (火)
		//opendate 03/03 (木)
		//from-toのパース

		//上場日が本日対象となっているか？　またはfrom-toの範囲なのかを確認する
		Calendar cal = Calendar.getInstance();


		List<IpoDto> ipoDtopMailList = new ArrayList<>();
		for(IpoDto dto : ipoDtoList){
			String []arrayDate = dto.BBDate.split("\n");
			String tempStr = arrayDate[0];
			String fromBBdateMonth = arrayDate[0].substring(0,2);
			String fromBBdateDay = arrayDate[0].substring(3,5);
			String toBBdateMonth = arrayDate[1].substring(0,2);
			String toBBdateDay = arrayDate[1].substring(3,5);

			//String todayAll = cal.get(Calendar.YEAR) +

			String todayAll = String.valueOf(cal.get(Calendar.YEAR));

			int monthTmep = cal.get(Calendar.MONTH) + 1;
			if (monthTmep < 10){
				todayAll+= ("0" + String.valueOf(monthTmep));
			}else{
				todayAll+= String.valueOf(monthTmep);
			}

			int dayTemp = cal.get(Calendar.DATE);
			if (dayTemp < 10){
				todayAll+= ("0" + String.valueOf(dayTemp));
			}else{
				todayAll+= ( String.valueOf(dayTemp));
			}
			String fromBBdateAll = cal.get(Calendar.YEAR) + fromBBdateMonth + fromBBdateDay;
			String toBBdateAll = cal.get(Calendar.YEAR) + toBBdateMonth + toBBdateDay;
			String openAll = cal.get(Calendar.YEAR) + dto.openDate.substring(0,2) + dto.openDate.substring(3,5);
			if((Integer.valueOf(todayAll) >= Integer.valueOf(fromBBdateAll) && Integer.valueOf(todayAll) <= Integer.valueOf(toBBdateAll)) || (Integer.valueOf(todayAll) == Integer.valueOf(openAll))){
				ipoDtopMailList.add(dto);
			}
		}
		driver.close();
		//メール

		sendMail(ipoDtopMailList);

		System.out.println("処理完了");

	}

	private static void sendMail(List<IpoDto> ipoDtopMailList) {
		final String to = "yanagisawa.trade@gmail.com";
		final String from = "yanagisawa.trade@gmail.com";
	    // Google account mail address
	    final String username =  SettingInitializer.getGmailId();
	    // Google App password
	    final String password = SettingInitializer.getGendamaPass();
	    final String charset = "UTF-8";
	    final String encoding = "base64";
	    // for gmail
	    String host = "smtp.gmail.com";
	    String port = "587";
	    String starttls = "true";
	    Properties props = new Properties();
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.port", port);
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", starttls);
	    props.put("mail.smtp.connectiontimeout", "10000");
	    props.put("mail.smtp.timeout", "10000");
	    props.put("mail.debug", "true");

	    String subject = "IPO株についてのイベントが本日発生しています！";
	    String content = "";
	    content = content + "IPO株イベント情報 \n\n本日下記のイベントが発生してます\n";
	    content = content + "=========================";
	    for(IpoDto dto:ipoDtopMailList){
	    	content = content + dto.meigaraCode + ":" + dto.meigaraName + "\n";
	    	content = content + "BB期間" + dto.BBDate  + "\n";
	    	content = content + "上場日" + dto.openDate +  "\n";
	    	content = content +  "\n\n";
	    }
	    Session session = Session.getInstance(props,
	    new javax.mail.Authenticator() {
	       protected PasswordAuthentication getPasswordAuthentication() {
	          return new PasswordAuthentication(username, password);
	       }
	    });

	    try {
	      MimeMessage message = new MimeMessage(session);

	      // Set From:
	      message.setFrom(new InternetAddress(from, "IPO情報"));
	      // Set ReplyTo:
	      message.setReplyTo(new Address[]{new InternetAddress(from)});
	      // Set To:
	      message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

	      message.setSubject(subject, charset);
	      message.setText(content, charset);

	      message.setHeader("Content-Transfer-Encoding", encoding);

	      Transport.send(message);

	    } catch (MessagingException e) {
	      throw new RuntimeException(e);
	    } catch (UnsupportedEncodingException e) {
	      throw new RuntimeException(e);
	    }
	}

}
