import java.util.*; // Import the java .util class to use the Scanner,
import java.util.concurrent.TimeUnit; //Import to be able to use the seconds.Sleep method
import java.io.*; // Import this class to handle errors

public class App {
    // Class variable that can be used from different methods in the class
    static String qafile = "qa.txt"; //Filename that contains questions and answers
    static int numberOfQA = 0; // Total number of questions
    static String questions[]; // Array of questions
    static String answers[];  // Array of answers
    static int randomNumbers[]; //Array of random numbers for the questions to be random

    //Main method that is called when the program starts
    public static void main(String[] args) throws InterruptedException, IOException {       
        Scanner in = new Scanner(System.in);
        int correctanswers = 0; // Keeps track of number of correct answers
        double maxsecperQ = 20; // Maximum seconds allocated for the first question
        double readtime = 2;  // Time allocated for reading the questions
        double timeReduction = 0.95; // Time reduction after every correct asnwers
        //Random rand = new Random();
        // learned about random numbers from this link: https://www.educative.io/edpresso/how-to-generate-random-numbers-in-java

        //Call ReadQA and GetRandomArray methods to read QA and generate random sequence for questions
        ReadQA();
        GetRandomArray();

        //Ask if participant is ready
        String placeholder = "";
        System.out.println("You will be taking a test, you will start out with 20 seconds per question and it will get shorter as it goes on, please type: ready, when you are ready");
        while(!(placeholder.toLowerCase().equals("ready"))){
            placeholder = in.nextLine();
            if(!(placeholder.toLowerCase().equals("ready"))){
                System.out.println("Invalid Input, please type: ready, when you are ready");
            }
        }

        //Give 3 seconds to prepare
        System.out.println("3...");
        TimeUnit.SECONDS.sleep(1);
        //i learned about the Timeunit.sleep library and method at this URL: https://stackoverflow.com/questions/24104313/how-do-i-make-a-delay-in-java
        System.out.println("2...");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("1...");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("GO!!!");
        
        //Ask all the question based on the random sequence array
        for(int cq=0;cq<randomNumbers.length; cq++){
          System.out.println(questions[randomNumbers[cq]]);
          
          //Give 3 seconds to prepare
          for(int rtime=0; rtime<readtime; rtime++){
            TimeUnit.SECONDS.sleep(1);
          }

          //Record the start time in nano sec
          long startTime = System.nanoTime();
          System.out.println("Total points: "+ correctanswers +".  Answer in " + String.format("%.2f", maxsecperQ) +" sec: ");   
          String ans = in.nextLine();

          //Find out the difference in time by substrating recorded starting time and dividing by a billion
          long timeTakenToAnswer = (System.nanoTime() - startTime)/1000000000;
          //System.out.println(timeTakenToAnswer);
          System.out.println("your answer:     " + ans);
          System.out.println("expected answer: " + answers[randomNumbers[cq]]);
          
          //If answer is correct and if the time taken to answer is withint the allocated time give point
          if(ans.toLowerCase().equals(answers[randomNumbers[cq]].toLowerCase()) && (timeTakenToAnswer <= maxsecperQ)){
            correctanswers++;
          }
          //If the answer is correct but the answered time is more than allocated time do not give point
          else if(ans.toLowerCase().equals(answers[randomNumbers[cq]].toLowerCase()) && timeTakenToAnswer > maxsecperQ)
          {
             System.out.println("Time taken to answer: " + timeTakenToAnswer +" is greater than Time allocated: " + String.format("%.2f", maxsecperQ));
             break;
          }
          else{
            break;
          }
          //reduce the time for the next round
          maxsecperQ = (timeReduction * maxsecperQ);
        }

       //Show correct message based on the number of points 
        if(correctanswers == numberOfQA){
          System.out.println("\n\nThe score you got is:\t"+correctanswers+" points! Execellent job!");
        }
        else if(correctanswers > numberOfQA/2){
          System.out.println("\n\nThe score you got is:\t"+correctanswers+" points! Nice attempt!");
        }
        else{
          System.out.println("\n\nThe score you got is:\t"+correctanswers+" points! Better luck next time!");
        }
      in.close();
    }

    //This method helps to read the QA file
    public static void ReadQA()  throws IOException{
       //Reads the file 
        BufferedReader myfile = new BufferedReader(new FileReader(qafile));
        Scanner myReader = new Scanner(myfile);
        
       //Finds the number of questions
        numberOfQA = Integer.parseInt(myReader.nextLine());
       //allocate the array space based on number of questions
        questions = new String[numberOfQA];
        answers = new String[numberOfQA];
        
        int currentQA = 0;
        while (myReader.hasNextLine()) {
          //adds the question and answer in correct array
          questions[currentQA] = myReader.nextLine();
          answers[currentQA] = myReader.nextLine();
          //System.out.println(questions[currentQA]);
          //System.out.println(answers[currentQA]);
          currentQA++;
        }
      //closes the Scanner
      myReader.close();
   }
   

  //This method helps to generate random number and puts it in the array. So, questions can be called based on random sequence.
   public static void GetRandomArray(){
        //Allocate array space based on number of question
        randomNumbers = new int[numberOfQA];
        //learned about this method at this link: https://stackoverflow.com/questions/4040001/creating-random-numbers-with-no-duplicates
        ArrayList<Integer> list = new ArrayList<Integer>(numberOfQA);
        //After this the Array list will havr number from 0 to 6 
        for(int i = 0; i < numberOfQA; i++) {
            list.add(i);
        }
        Random rand = new Random();
        int currentRandom = 0;
        while(list.size() > 0) {
            //It gets the random number based on size of the ArrayList 
            //size keeps on reducing as we remove a number 
            int index = rand.nextInt(list.size());
            //The next random number is the index of ArrayList that will be removed
            randomNumbers[currentRandom]=list.remove(index);
            //System.out.println(randomNumbers[currentRandom]);
            currentRandom++;
        }
   }
}
