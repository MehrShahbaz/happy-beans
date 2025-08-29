## Stripe Payment Integration

So for payments, we integrated **Stripe Checkout**. Instead of building our own UI for payment forms, we’re leveraging Stripe’s hosted checkout session, which handles all the heavy lifting—security, validation, and different payment methods.

Here’s how the flow works:

* A checkout session is created when the user initiates a payment.
* Once the payment succeeds, Stripe calls back to our **success endpoint**.
* Inside that callback, we grab the **order ID from the payment metadata**.
* Using that ID, we update both the **payment record** and the **order status** in our system.

That way, everything stays in sync automatically once Stripe confirms the transaction.

---

## AWS Setup

For infrastructure, we’re using **AWS Application Load Balancer (ALB)** in front of our application.

* The ALB is handling the routing of traffic to our app.
* We’ve set up **AWS Certificate Manager** to issue and manage our HTTPS certificate.
* This way, all traffic is secure by default, without us needing to worry about certificate renewals.

---

## Email Dispatching

On certain events like order confirmations or notifications we send out emails.
We’re handling this with **Java Mailer**, and right now we’re using Gmail as the SMTP provider.
It’s a lightweight setup but reliable enough for our current needs.

---
