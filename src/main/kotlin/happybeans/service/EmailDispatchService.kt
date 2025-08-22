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
        message.subject = subject
        message.text = text
        mailSender.send(message)
    }

    fun sendJoinRequestRejectEmail(to: String) {
        val text = "Sorry to say but admin rejected you"
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = REJECT_INVITE_SUBJECT
        message.text = text
        mailSender.send(message)
    }

    companion object {
        private const val RESTAURANT_OWNER_EMAIL_MESSAGE = "Welcome to Happy Beans"
        private const val REJECT_INVITE_SUBJECT = ""
    }
}
