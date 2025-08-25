package happybeans.service

import happybeans.model.Order
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailDispatchService(
    private val mailSender: JavaMailSender,
) {
    // TODO Create Email Templates
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

    fun sendOrderConfirmationEmail(order: Order) {
        val text = "Hi, Congrats your order was confirmed ${order.id}"
        val message = SimpleMailMessage()
        message.setTo(order.userEmail)
        message.subject = CONFIRM_ORDER_SUBJECT
        message.text = text
        mailSender.send(message)
    }

    fun sendOrderFailEmail(order: Order) {
        val text = "Hi, Sad to say your order failed ${order.id}"
        val message = SimpleMailMessage()
        message.setTo(order.userEmail)
        message.subject = FAILED_ORDER_SUBJECT
        message.text = text
        mailSender.send(message)
    }

    // TODO fix the messages
    companion object {
        private const val RESTAURANT_OWNER_EMAIL_MESSAGE = "Welcome to Happy Beans"
        private const val REJECT_INVITE_SUBJECT = "Hello, you were Rejected"
        private const val CONFIRM_ORDER_SUBJECT = "Your order has been confirmed"
        private const val FAILED_ORDER_SUBJECT = "Your Order has been rejected"
    }
}
