import java.util.Random;

public class OTPGenerator {
    // Define the length of the OTP
    private static final int OTP_LENGTH = 6;

    // Method to generate a random OTP
    public static String generateOTP() {
        // Define the characters allowed in the OTP
        String allowedChars = "0123456789";

        // Create a StringBuilder to store the OTP
        StringBuilder otpBuilder = new StringBuilder();

        // Create a random object
        Random random = new Random();

        // Generate OTP of specified length
        for (int i = 0; i < OTP_LENGTH; i++) {
            // Generate a random index within the range of allowedChars
            int randomIndex = random.nextInt(allowedChars.length());

            // Append the character at the random index to the OTP
            otpBuilder.append(allowedChars.charAt(randomIndex));
        }

        // Convert the StringBuilder to a String and return the OTP
        return otpBuilder.toString();
    }
}

