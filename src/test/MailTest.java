package test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

/**
 * @author ctl
 * @date 2021/12/1
 */
public class MailTest {

	private static final JavaMailSenderImpl mailSender;

	static {
		mailSender = new JavaMailSenderImpl();
		// qq邮箱smtp设置
//		mailSender.setHost("smtp.qq.com");
//		mailSender.setPort(465);
//		mailSender.setUsername("770626703@qq.com");
//		mailSender.setPassword("mssjbyujwbzmbaid");

		// 163邮箱smtp设置
		mailSender.setHost("smtp.163.com");
		mailSender.setPort(465);
		mailSender.setUsername("chengtl0131@163.com");
		mailSender.setPassword("KFESSGDZHTEZSXGL");

		mailSender.setDefaultEncoding("UTF-8");
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.port", "465");
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.smtp.socketFactory.port", "465");
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		mailSender.setJavaMailProperties(properties);

	}

	@Test
	public void testSendMail() {
//		String[] to = {"chengtl0131@163.com"};
		String[] to = {"770626703@qq.com"};
		String[] cc = {};
		String subject = "小鹏飞公司问题反馈";
		String content = "你好，这是鹏飞公司的问题反馈";
		System.out.println(send(to, cc, subject, content, null));
	}

	/**
	 * @param to 收件人列表
	 * @param cc 抄送人列表
	 * @param subject 标题
	 * @param content 内容
	 * @param fileName 附件本地路径
	 */
	public boolean send(String[] to, String[] cc, String subject, String content, String fileName) {
		String from = mailSender.getUsername();

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = null;
		try {
			helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(from);
//			helper.setFrom(new InternetAddress("nickname" + " <" + from + ">"));
			helper.setTo(to);
			helper.setCc(cc);
			helper.setSubject(subject);
			helper.setText(content, true);

			if(StringUtils.isNotBlank(fileName)) {
				File file = new File(fileName);
				helper.addAttachment(file.getName(), file);
			}
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
