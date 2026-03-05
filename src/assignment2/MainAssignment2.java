package assignment2;

public class MainAssignment2{
    public static void main(String[] args){
        String inputFile = "orders.txt";
        OrderProcessor processor = new OrderProcessor();
        processor.process(inputFile);
    }
}