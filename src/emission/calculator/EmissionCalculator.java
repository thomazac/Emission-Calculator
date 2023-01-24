package emission.calculator;
/**
 *
 * @author Zach Thomas
 */

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EmissionCalculator {
    public static void main(String[] args) throws IOException, URISyntaxException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the carbon emission calculator. Type start to begin or type exit to terminate.");
        
        String input = scanner.nextLine();
        if(input.toLowerCase().compareTo("exit") == 0){
            System.exit(0);
        }else if(input.toLowerCase().compareTo("start") != 0){
            System.out.print("Invalid input. Terminating.\n");
            System.exit(0);
        }
        
        //Determine the year, make, and model of the user's car
        System.out.println("Enter your car's year, make, and model. (Formatted: 2021,Toyota,Camry)");
        String[] carIn = new String[3];
        input = scanner.nextLine();
        carIn = input.split(",");
        //System.out.println("Your car is a : " + carIn[0] + " " + carIn[1] + " " + carIn[2] + ".");
        
        //Determine miles the user will drive
        int miles;
        while(true){
            try{
                System.out.println("How many miles will you drive?");
                miles = scanner.nextInt();
                if(miles <= 0){
                    System.out.println("Please enter a number greater than 0.");
                }else{
                    break;
                }
            }catch(InputMismatchException e){
                System.out.println("Please enter an integer.");
                scanner.next();
            }
        }
        //System.out.println("You will drive " + miles + " miles.");
        
        //Open scanner at the path the csv is in
        URL path = EmissionCalculator.class.getResource("vehicles.csv");
        Scanner sc2 = new Scanner(new File(path.toURI()));
        
        //Uses getCar method to get the contents of a certain row pertaining to the user's
        //input along with the co2 emission of their vehicle
        String[] car = getCar(sc2, carIn);
        if (car == null){
            System.out.println("Vehicle not found.");
            System.exit(0);
        }
        
        //First calculates co2 amount in both grams and pounds. Then prints contents from new car array
        double co2;
        try{
            co2 = Double.parseDouble(car[3]);
        }catch(NumberFormatException e){
            co2 = 0;
        }
        double tripCO2lbs = (miles * co2) / 453.592; //in lbs
        double tripCO2g = miles * co2; //in grams
        System.out.printf("Your %s %s %s will product %.2f grams(%.2f lbs) of co2 from driving %d miles.\n"
                + "This would cost $%.2f to offset.\n", 
            car[0],car[1],car[2],tripCO2g,tripCO2lbs,miles,tripCO2lbs*.01);
        
        sc2.close();
        scanner.close();
    }    
    
    //Checks if the current row's contents equals the user's vehicle data
    //63(year),46(make),47(model),14(co2 emission)
    //Data is placed into a new string array and returned if the row is valid
    //If not, null is returned
    public static String[] getCar(Scanner sc, String[] car){
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String[] values = line.split(",");
            if (values[63].equals(car[0]) && values[46].toLowerCase().equals(car[1].toLowerCase()) && values[47].toLowerCase().equals(car[2].toLowerCase())){
                String[] carInfo = {values[63], values[46], values[47], values[14]};
                return carInfo;
            }
        }
    return null;
    }
}
