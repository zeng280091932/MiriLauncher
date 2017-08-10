package com.miri.launcher.crashReporter;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 邮件类
 */
public class Mail extends javax.mail.Authenticator {

    private String mUser;

    private String mPass;

    private String[] mTo;

    private String mFrom;

    private String mPort;

    private String mSport;

    private String mHost;

    private String mSubject;

    private String mBody;

    private boolean isAuth;

    private boolean isDebuggable;

    private Multipart mMultipart;

    public Mail() {
        mHost = "smtp.gmail.com"; // default smtp server   
        mPort = "465"; // default smtp port   
        mSport = "465"; // default socketfactory port    
        mUser = ""; // username    
        mPass = ""; // password    
        mFrom = ""; // email sent from   
        mSubject = ""; // email subject  
        mBody = ""; // email body     
        isDebuggable = false; // debug mode on or off - default off    
        isAuth = true; // smtp authentication - default on    
        mMultipart = new MimeMultipart();
        // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.   
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }

    public Mail(String user, String pass) {
        this();
        mUser = user;
        mPass = pass;
    }

    public boolean send() throws Exception {
        Properties props = _setProperties();
        if (!mUser.equals("") && !mPass.equals("") && mTo.length > 0 && !mFrom.equals("")
                && !mSubject.equals("")) {
            Session session = Session.getDefaultInstance(props, this);
            MimeMessage msg = new MimeMessage(session);
            //加载发件人地址
            msg.setFrom(new InternetAddress(mFrom));
            InternetAddress[] addressTo = new InternetAddress[mTo.length];
            for (int i = 0; i < mTo.length; i++) {
                addressTo[i] = new InternetAddress(mTo[i]);
            }
            //加载收件人地址
            msg.setRecipients(RecipientType.TO, addressTo);
            //加载标题
            msg.setSubject(mSubject);
            msg.setSentDate(new Date());
            // setup message body    
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(mBody);
            mMultipart.addBodyPart(messageBodyPart);
            // Put parts in message       
            msg.setContent(mMultipart);
            // send email      
            Transport.send(msg);
            return true;
        } else {
            return false;
        }
    }

    public void addAttachment(String filename) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        mMultipart.addBodyPart(messageBodyPart);
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(mUser, mPass);
    }

    private Properties _setProperties() {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", mHost);
        if (isDebuggable) {
            props.put("mail.debug", "true");
        }
        if (isAuth) {
            props.put("mail.smtp.auth", "true");
        }
        props.put("mail.smtp.port", mPort);
        props.put("mail.smtp.socketFactory.port", mSport);
        //        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.quitwait", "false");
        return props;
    }

    // the getters and setters   

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        mUser = user;
    }

    public String getPass() {
        return mPass;
    }

    public void setPass(String pass) {
        mPass = pass;
    }

    public String[] getTo() {
        return mTo;
    }

    public void setTo(String[] to) {
        mTo = to;
    }

    public String getFrom() {
        return mFrom;
    }

    public void setFrom(String from) {
        mFrom = from;
    }

    public String getPort() {
        return mPort;
    }

    public void setPort(String port) {
        mPort = port;
    }

    public String getSport() {
        return mSport;
    }

    public void setSport(String sport) {
        mSport = sport;
    }

    public String getHost() {
        return mHost;
    }

    public void setHost(String host) {
        mHost = host;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public boolean isDebuggable() {
        return isDebuggable;
    }

    public void setDebuggable(boolean debuggable) {
        isDebuggable = debuggable;
    }

}
