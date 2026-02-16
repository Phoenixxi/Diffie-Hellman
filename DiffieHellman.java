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
    java.math.BigInteger publicA;
    java.math.BigInteger publicB;
    java.math.BigInteger sharedSecret;

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
        // START: choose prime number and generator
        System.out.println("Hello User 1! To begin, we must choose a prime number and generator to be announced publically over the network. Would you like to have a prime number and generator chosen for you? (yes/no)");
        String response = scan.nextLine();
        // User inputs yes. Get their prime and generator
        if (response.equalsIgnoreCase("no")) {
            System.out.println("Please enter a prime number:");
            primeNumber = scan.nextLong();
            System.out.println("Please enter a generator:");
            generator = scan.nextInt();
        } 
        // User inputs no. Randomly select a prime and generator from the map
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

        // Compute public value
        publicA = ComputeBigX(user1privateKey);
        System.out.println("Your public value has been calculated. User 1 public value is " + publicA + ", this will be shared to everyone on the channel.");
        System.out.println("Please enter the message you want to encrypt:");
        user1message = scan.next();
        System.out.println("Thank you User 1! Now lets move on to User 2.");
        user2();

    }

    private java.math.BigInteger ComputeBigX(int secretVal)
    {
        java.math.BigInteger g = java.math.BigInteger.valueOf(generator);
        java.math.BigInteger p = java.math.BigInteger.valueOf(primeNumber);
        java.math.BigInteger e = java.math.BigInteger.valueOf(secretVal);

        return g.modPow(e, p);
    }


    private void user2()
    {   
        // Acquire a password
        System.out.println("Hello User 2! Please enter a password: ");
        user2Password = scan.next();
        user2Password = scan.next();
        // Set private key
        System.out.println("Please enter your private key:");
        user2privateKey = scan.nextInt();
        // Compute
        publicB = ComputeBigX(user2privateKey);
        System.out.println("Your public value has been calculated. User 2 public value is " + publicB + ", this will be shared to everyone on the channel.");
        ComputeSharedSecret(user2privateKey);
        // Encrypt user 1's message now that we know the shared secret
        EncryptMessage(user1message, sharedSecret, 1);
        EncyptOrDecrypt();

    }

    private void ComputeSharedSecret(int secretVal)
    {
        java.math.BigInteger p = java.math.BigInteger.valueOf(primeNumber);
        java.math.BigInteger base = publicA;
        java.math.BigInteger e = java.math.BigInteger.valueOf(secretVal);

        sharedSecret = base.modPow(e, p);

    }

    private void EncyptOrDecrypt()
    {
        System.out.println("Would you like to decyrpt a message or send an encrypted message? Please write either \"decrypt\" or \"encrypt\"");
        String response = scan.next();

        if (response.equalsIgnoreCase("decrypt")) 
        {
            
        }
        else if(response.equalsIgnoreCase("encrypt"))
        {
            System.out.println("Please enter the message you want to encrypt:");
            user2message = scan.next();
            System.out.println("Your message has been encrypted. You have been logged out");
        }
        else
        {
            System.out.println("There was an error with your response. Please check your spelling and try again");
            EncyptOrDecrypt();
        }
    }

    // private void TryDecrypt()

    private void EncryptMessage(String message, java.math.BigInteger bigShift, int user)
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
                
                // Handle negative modulo results
                if (shifted < 0) {
                    shifted += ASCII_RANGE;
                }
                
                char newChar = (char) (shifted + ASCII_MIN);
                result.append(newChar);
            } else {
                // Keep non-printable characters unchanged
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
        System.out.println("Please enter password to confirm your identity: ");
        String password = scan.next();

        if (password.equalsIgnoreCase(user1Password))
        {

        }
        else if(password.equalsIgnoreCase(user2Password))
        {

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