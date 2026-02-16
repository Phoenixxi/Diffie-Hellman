import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class DiffieHellman {
    private Scanner scan = new Scanner(System.in);
    private Map<Integer, Long> map = new HashMap<>();
    private int[] generators = {2, 5, 2, 10, 3, 11, 3, 5, 7, 13};

    private static final int ASCII_MIN = 32;  
    private static final int ASCII_MAX = 126;  
    private static final int ASCII_RANGE = ASCII_MAX - ASCII_MIN + 1;

    long primeNumber;
    int generator;
    int user1privateKey;
    int user2privateKey;
    String user1message;
    String user1EncryptedMessage;
    String user1Password;
    String user2message;
    String user2EncryptedMessage;
    String user2Password;
    BigInteger publicA;
    BigInteger publicB;
    BigInteger sharedSecret;

    int currentUser = 0; // Default=0, user1=1, user2=2

    DiffieHellman()
    {
        map.put(0, 8179014253L);
        map.put(1, 74474898457L);
        map.put(2, 713775568283L);
        map.put(3, 2668641477937L);
        map.put(4, 46670736726617L);
        map.put(5, 403745822152391L);
        map.put(6, 2529321556562767L);
        map.put(7, 23293952545242217L);
        map.put(8, 667132611114420599L);
        map.put(9, 6313977416501168399L);
    }


    private void user1() {
        // Choose prime number and generator to start
        System.out.println("Hello User 1! To begin, we must choose a prime number and generator to be announced publically over the network. Would you like to have a prime number and generator chosen for you? (yes/no)");
        String response = scan.nextLine();
        // User inputs no. Get their prime and generator
        if (response.equalsIgnoreCase("no")) {
            System.out.println("Please enter a prime number:");
            primeNumber = scan.nextLong();
            System.out.println("Please enter a generator:");
            generator = scan.nextInt();
            scan.nextLine(); 
        } 
        // User inputs yes. Randomly select a prime and generator from the map
        else 
        {
            Random random = new Random();
            int randomIndex = random.nextInt(10);
            primeNumber = map.get(randomIndex);
            generator = generators[randomIndex];
            
        }
        System.out.println("The public prime number is " + primeNumber + " and public generator is " + generator);
        
        // Set up first time password
        System.out.println("Please choose a password: ");
        user1Password = scan.nextLine();

        // Acquire user 1 private key
        System.out.println("Please enter your private key. Note that your key will not be made public: ");
        user1privateKey = scan.nextInt();
        scan.nextLine(); // Consume leftover newline

        // Compute public value
        publicA = ComputeBigX(user1privateKey);
        System.out.println("Your public value has been calculated. User 1 public value is " + publicA + ", this will be shared to everyone on the channel.");
        System.out.println("Please enter the message you want to encrypt:");
        user1message = scan.nextLine();
        System.out.println("Thank you User 1! Now lets move on to User 2.");

    }

    private BigInteger ComputeBigX(int secretVal)
    {
        BigInteger g = BigInteger.valueOf(generator);
        BigInteger p = BigInteger.valueOf(primeNumber);
        BigInteger e = BigInteger.valueOf(secretVal);

        return g.modPow(e, p);
    }


    private void user2()
    {   
        // Acquire a first time password
        System.out.println("Hello User 2! Please enter a password: ");
        user2Password = scan.nextLine();
        // Set private key
        System.out.println("Please enter your private key:");
        user2privateKey = scan.nextInt();
        scan.nextLine();
        // Compute
        publicB = ComputeBigX(user2privateKey);
        System.out.println("Your public value has been calculated. User 2 public value is " + publicB + ", this will be shared to everyone on the channel.");
        ComputeSharedSecret(user2privateKey);
        // Encrypt user 1's message now that we know the shared secret
        EncryptMessage(user1message, sharedSecret, 1);
        System.out.println("(User 1's message has been encrypted to: " + user1EncryptedMessage + ")");
        System.out.println("\nThank you users. You will be logged out now. Please log back in to encrypt or decrypt messages.");
        Login();

    }

    private void ComputeSharedSecret(int secretVal)
    {
        BigInteger p = BigInteger.valueOf(primeNumber);
        BigInteger base = publicA;
        BigInteger e = BigInteger.valueOf(secretVal);

        sharedSecret = base.modPow(e, p);
        System.out.println("Shared secret has been computed: " + sharedSecret);

    }

    private BigInteger TryComputeSharedSecret(int privateKey, BigInteger base)
    {
        BigInteger p = BigInteger.valueOf(primeNumber);
        return base.modPow(BigInteger.valueOf(privateKey), p);
    }

    private void EncyptOrDecrypt()
    {
        System.out.println("\nWould you like to decrypt a message or send an encrypted message? Please write either \"decrypt\" or \"encrypt\"");
        String response = scan.nextLine();

        if (response.equalsIgnoreCase("decrypt")) 
        {
            System.out.println("Please enter your secret key to confirm your identity: ");
            int privateKey = scan.nextInt();
            scan.nextLine(); 
            
            if(currentUser == 1)
            {
                if(user2EncryptedMessage == null){
                    System.out.println("There are no messages from user 2 to decrypt");
                    System.out.println("Logging out...");
                    return;
                }
                BigInteger secretKey = TryComputeSharedSecret(privateKey, publicB);
                System.out.println("Based on the private key you provided, the shared secret computed was: " + secretKey);
                System.out.println("Decrypting message with shared secret: ");
                System.out.println("...");
                System.out.println("...");
                System.out.println("...");
                String message = DecryptMessage(user2EncryptedMessage, secretKey, 1);
                System.out.println("The decrypted message is: " + message);
                System.out.println("Logging out...");
            }
            else if(currentUser == 2)
            {
                if(user1EncryptedMessage == null){
                    System.out.println("There are no messages from user 1 to decrypt");
                    System.out.println("Logging out...");
                    return;
                }
                BigInteger secretKey = TryComputeSharedSecret(privateKey, publicA);
                System.out.println("Based on the private key you provided, the shared secret computed was: " + secretKey);
                System.out.println("Decrypting message with shared secret: ");
                System.out.println("...");
                System.out.println("...");
                System.out.println("...");
                String message = DecryptMessage(user1EncryptedMessage, secretKey, 2);
                System.out.println("The decrypted message is: " + message);
                System.out.println("Logging out...");
            }


        }
        else if(response.equalsIgnoreCase("encrypt"))
        {
            
            if(currentUser == 1) 
            {
                System.out.println("Please enter the message you want to encrypt:");
                user1message = scan.nextLine();
                System.out.println("Please enter your private key to encrypt the message:");
                int privateKey = scan.nextInt();
                scan.nextLine(); 
                BigInteger secretKey = TryComputeSharedSecret(privateKey, publicB);
                EncryptMessage(user1message, secretKey, 1);
                System.out.println("Your message has been encrypted to: " + user1EncryptedMessage);
                System.out.println("You have been logged out");
                Login();
            }
            else if(currentUser == 2) {
                System.out.println("Please enter the message you want to encrypt:");
                user2message = scan.nextLine();
                System.out.println("Please enter your private key to encrypt the message:");
                int privateKey = scan.nextInt();
                scan.nextLine(); 
                BigInteger secretKey = TryComputeSharedSecret(privateKey, publicA);
                EncryptMessage(user2message, secretKey, 2);
                System.out.println("Your message has been encrypted to: " + user2EncryptedMessage);
                System.out.println("You have been logged out");
                Login();
            } 
        }
        else
        {
            System.out.println("There was an error with your response. Please check your spelling and try again");
            EncyptOrDecrypt();
        }
    }

    private String DecryptMessage(String message, BigInteger bigShift, int user)
    {
        int shift = (bigShift.mod(BigInteger.valueOf(ASCII_RANGE))).intValue();
        shift = -shift;

        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            
            // Check if character is in printable ASCII range
            if (ch >= ASCII_MIN && ch <= ASCII_MAX) {
                // Normalize to 0-94 range, shift, wrap around, then denormalize
                int normalized = ch - ASCII_MIN;
                int shifted = (normalized + shift) % ASCII_RANGE;
                
                // Ensure no negative results
                if (shifted < 0) {
                    shifted += ASCII_RANGE;
                }
                
                char newChar = (char) (shifted + ASCII_MIN);
                result.append(newChar);
            } else {
                // Keep non-ascii characters unchanged
                result.append(ch);
            }
        }
        return result.toString();
    }

    private void EncryptMessage(String message, BigInteger bigShift, int user)
    {
        int shift = (bigShift.mod(BigInteger.valueOf(ASCII_RANGE))).intValue();
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            
            // Check if character is in printable ASCII range
            if (ch >= ASCII_MIN && ch <= ASCII_MAX) {
                // Normalize to 0-94 range, shift, wrap around, then denormalize
                int normalized = ch - ASCII_MIN;
                int shifted = (normalized + shift) % ASCII_RANGE;
                
                // Ensure no negative results
                if (shifted < 0) {
                    shifted += ASCII_RANGE;
                }
                
                char newChar = (char) (shifted + ASCII_MIN);
                result.append(newChar);
            } else {
                // Keep non-ascii characters unchanged
                result.append(ch);
            }
        }
        
        if(user==1)
            user1EncryptedMessage = result.toString();
        else
            user2EncryptedMessage = result.toString();

    }

    private void Login()
    {
        System.out.println("\nPlease enter password to confirm your identity: ");
        String password = scan.nextLine();

        if (password.equals(user1Password))
        {
            currentUser = 1;
            System.out.println("Logged in as User 1");
            EncyptOrDecrypt();
        }
        else if(password.equals(user2Password))
        {
            currentUser = 2;
            System.out.println("Logged in as User 2");
            EncyptOrDecrypt();
        }
        else
        {
            System.out.println("Password is incorrect. Please check spelling and try again");
            Login();
        }

    }

    public static void main(String[] args) {
        System.out.println("This program will simulate how 2 people can share private keys across a public network to secretly encode and decode messages.");
        DiffieHellman diffieHellman = new DiffieHellman();
        diffieHellman.user1();
        diffieHellman.user2();

    }
}