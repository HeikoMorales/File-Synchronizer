package controller;

import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class App {

    String directory = "";
    String finalDirectory = "";
    String extension = "";
    Scanner scanner;
    long fileCount = 0, startTime, endTime;

    public App() {
        scanner = new Scanner(System.in);
    }

    public void run() {

        getUserData();

        startTime = System.currentTimeMillis();

        ForkJoinPool pool = new ForkJoinPool();
        fileCount = pool.invoke(new Synchronizer(directory, extension, finalDirectory));

        endTime = System.currentTimeMillis();
        System.out.println("Number of files: " + fileCount);
        System.out.println("Time spend: " + (endTime - startTime) + "ms");
    }

    private void getUserData() {
        
        System.out.println("Enter directory: ");
        directory = scanner.nextLine();
        System.out.println("Enter extension: ");
        extension = scanner.nextLine();
        System.out.println("Enter final directory: ");
        finalDirectory = scanner.nextLine();
    }

    public static void main(String[] args) {

        System.out.println("[INFO] Starting...");

        App main = new App();
        main.run();

        System.out.println("[INFO] Finished!");
        System.out.println("[CREATOR] Heiko Morales -> Github: https://github.com/HeikoMorales");
    }
}
