import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class DiffieHellman {
    private Scanner scan = new Scanner(System.in);
    private Map<Integer, Long> map = new HashMap<>();
    private int[] generators = {2, 5, 2, 10, 3, 11, 3, 5, 7, 13};
    long primeNumber;
    int generator;
    int user1privateKey;
    int user2privateKey;
    String user1message;
    String user2message;

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
        System.out.println("Hello User 1! Would you like to choose your own prime number and generator? (yes/no)");
        String response = scan.nextLine();
        // User inputs yes. Get their prime and generator
        if (response.equalsIgnoreCase("yes")) {
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
            System.out.println("Your prime number and generator have been chosen.");
            
        }

        System.out.println("Please enter your private key:");
        user1privateKey = scan.nextInt();
        System.out.println("Please enter the message you want to encrypt:");
        user1message = scan.next();
        System.out.println("Thank you User 1! Now lets move on to User 2.");

    }
    private void user2()
    {
        System.out.println("Hello User 2! Please enter your private key:");

    }

    public static void main(String[] args) {
        //make a map
        // Map<Long, Integer> primeGeneratorMap = new HashMap<>();
        DiffieHellman diffieHellman = new DiffieHellman();
        

        diffieHellman.user1();

    }
}