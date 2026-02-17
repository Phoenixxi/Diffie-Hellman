# Diffie-Hellman

What the program does---------------------------------------------------------------
This program simulates a simple Diffie-Hellman key exchange for educational purposes only.

The Diffie-Hellman key exchange (1976) is a method of securely exchanging cryptographic keys over a public channel by allowing two parties to generate a shared secret key without ever transmitting it directly. In this simulation, two users will choose private keys and compute their public values based on a shared, public prime number and generator. The key exhange is secret because of the difficulty of the discrete logarithm problem.

This implementation uses a Caeser Cipher to encrypt and decrypt messages using the shared secret key as the shift value. All messages are shifted within the range of printable ASCII characters (32-126).

How to run it---------------------------------------------------------------
This program is run through the terminal. Because this is built for educational purposes only, this program simulates communication between two users over one device.
No background knowledge is required to run this program. The program prompts the user by asking questions, so read the directions carefully.

Limitations---------------------------------------------------------------
*NOTE*
This program gives the user the option to choose their own prime value and primitive root, but it does not check the accuracy of the primitive root. If user's wish to use their own values instead of the ones provided by the program, they must ensure the correctness of the root on their own.

This program is for educational purposes only and should not be used for securing sensitive information.

Ethical Considerations and Responsible Use-----------------------------------------
While the difficulty of the discrete logarithm problem makes this key exchange very secure, it should be mentioned that it is always possible to decrypt. Please use with caution and note that when used in practice, it is best to use prime numbers that are at least 2048 bits in length to increase security.

If the network running the exchange doesn't authenticate the parties involved, it is possible that this exchange could be vulnerable to man-in-the-middle attacks- where an attacker could intercept the key exchange and provide different public values to each party, causing all messages to be filtered to the attacker. Please always take caution when putting information publically online.








