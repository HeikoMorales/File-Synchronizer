package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class Synchronizer extends RecursiveTask<Long> {

    String directory;
    String finalDirectory;
    String extension;

    public Synchronizer(String directory, String extension, String finalDirectory) {
        this.directory = directory;
        this.extension = extension;
        this.finalDirectory = finalDirectory;
    }

    @Override
    protected Long compute() {
        long fileCount = 0;

        List<RecursiveTask<Long>> tasks = new java.util.ArrayList<>();

        for (File subFile : new File(directory).listFiles()) {

            if (subFile.isDirectory()) {
                Synchronizer sc = new Synchronizer(subFile.getAbsolutePath(), extension, finalDirectory);
                tasks.add(sc);
                sc.fork();
            } else {

                if (subFile.getName().endsWith(extension)) {

                    try {
                        copyFile(subFile, new File(finalDirectory));
                    } catch (FileAlreadyExistsException e) {
                        fileCount++;

                        if (subFile.lastModified() > new File(finalDirectory + "\\" + subFile.getName()).lastModified()) {

                            System.out.println("[INFO] Name: " + subFile.getName() + " Path: "
                                    + subFile.getAbsolutePath() + " Last modified: " + subFile.lastModified() + "\n" +
                                    "[INFO] Name: " + subFile.getName() + " Path: " + finalDirectory + "\\" + subFile.getName()
                                    + " Last modified: " + new File(finalDirectory + "\\" + subFile.getName()).lastModified());

                            try {
                            
                                Files.copy(subFile.toPath(), new File(finalDirectory).toPath().resolve(subFile.getName()), StandardCopyOption.REPLACE_EXISTING);
                            
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        
                    }
                }
            }
        }

        for (RecursiveTask<Long> task : tasks) {
            fileCount += task.join();
        }

        return fileCount;
    }

    private void copyFile(File subFile, File file) throws IOException {
        Files.copy(subFile.toPath(), file.toPath().resolve(subFile.getName()));
    }

}
