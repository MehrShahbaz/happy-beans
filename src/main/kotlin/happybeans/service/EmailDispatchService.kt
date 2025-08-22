package happybeans.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailDispatchService(
    private val mailSender: JavaMailSender,
) {
    fun sendRestaurantOwnerWelcomeEmail(
        to: String,
        password: String,
        subject: String = RESTAURANT_OWNER_EMAIL_MESSAGE,
    ) {
        val text = "Welcome to happy beans, login credentials password:$password. Note: Use the same email"
        val message = SimpleMailMessage()
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)
        mailSender.send(message)
    }

    companion object {
        private const val RESTAURANT_OWNER_EMAIL_MESSAGE = "Welcome to Happy Beans"
    }
}
