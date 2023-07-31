package com.swave.urnr.user.mailsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class MailSendImp implements MailServiceInter {

    @Autowired
    JavaMailSender emailsender; // Bean 등록해둔 MailConfig 를 emailsender 라는 이름으로 autowired

    private String ePw; // 인증번호

    // 메일 내용 작성
    @Override
    public MimeMessage createMessage(String to) throws  MessagingException, UnsupportedEncodingException {

		System.out.println("보내는 대상 : " + to);
		System.out.println("인증 번호 : " + ePw);

        MimeMessage message = emailsender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);// 보내는 대상
        message.setSubject("SWAVE 코드 메일");// 제목

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1> 안녕하세요</h1>";
        msgg += "<h1> SWAVE RELEASE NOTE 공유 시스템 입니다</h1>";
        msgg += "<br>";
        msgg += "<p>요청하신 코드를 보내드립니다.<p>";
        msgg += "<br>";
        msgg += "<p>항상 당신의 꿈을 응원합니다. 감사합니다!<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> "; // 메일에 인증번호 넣기
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("artisheep@naver.com", "SWAVE 전강훈"));// 보내는 사람

        return message;
    }

    // 랜덤 인증 코드 전송
    @Override
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    // a~z (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    // A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }

    @Override
    public String sendCodeMessage(String to) throws RuntimeException {

        ePw = createKey();

        /*
        TODO:
        왜 에러를 es로 정의하신 지 궁금합니다
catch로 에러를 잡았으면 그 에러에 대한 행동을 정의하면 됩니다. -> 즉 messaging 오류가 발생했다 다시 message를 보낸다 이런 느낌으로요
catch로 에러를 잡았는데 throw new RuntimeException으로 런타임 오류를 던질 필요는 없을 것 같습니다
아니면 다른 서비스단에서 사용한다면 런타임 에러를 던지지 말고 그냥 에러를 던져서 이 메일 서비스를 사용하는 서비스단에서 이 오류를 잡아서 처리하도록 해도 좋을 것 같습니다

public void call() {
    try {
        runSQL();
    } catch (SQLException e) {
        throw new RuntimeSQLException(e); //기존 예외(e) 포함
    }
}
추가로 예외 처리를 할때 원래 에러, 즉 Exception을 예외처리 한 runtime exception에 넘겨줘서 기록을 남기는 것이 매우 중요합니다 나중에 에러가 발생했을 때 원래의 오류를 찾지 못하는 상황이 생길 수도 있기 때문입니다

         */
        try {// 예외처리
            MimeMessage message = createMessage(to); // 메일 발송
            emailsender.send(message);
        } catch (MessagingException | UnsupportedEncodingException es ) {
            es.printStackTrace();
            throw new RuntimeException(es);
        }


        return ePw; // 메일로 보냈던 인증 코드를 서버로 반환
    }
}