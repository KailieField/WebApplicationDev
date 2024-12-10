package ca.gbc.notificationservice.service;



import ca.gbc.orderservice.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

	private final JavaMailSender javaMailSender;

	@KafkaListener(topics="order-placed")
	public void listen(OrderPlacedEvent orderPlacedEvent) {
		log.info("Received message from order-placed topic: {}", orderPlacedEvent);

		// -- SENDING EMAIL TO CUSTOMER
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("comp3095@georgebrown.ca");
			messageHelper.setTo(orderPlacedEvent.getEmail());
			messageHelper.setSubject(String.format("Order placed for %s", orderPlacedEvent.getEmail()));
			messageHelper.setText(String.format("""
					
					Good day %s %s,
					
					Your order with number %s was successfully placed.
					
					Thank you for yor trust!
					COMP3095 Staff
					
					""",
					orderPlacedEvent.getFirstName(),
					orderPlacedEvent.getLastName(),
					orderPlacedEvent.getOrderNumber()));

		};

		try {

			javaMailSender.send(messagePreparator);
			log.info("Order notification sent to {}", orderPlacedEvent.getEmail());

		} catch (MailException e) {

			log.error("Exception occurred when sending email.", e);
			throw new RuntimeException("Exception occurred when attempting to send the email", e);

		}
	}
}