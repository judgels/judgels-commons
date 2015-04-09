package org.iatoki.judgels.commons;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public interface FileSystemProvider {
    void createDirectory(List<String> directoryPath);

    void createFile(List<String> filePath);

    void removeFile(List<String> filePath);

    boolean directoryExists(List<String> directoryPath);

    boolean fileExists(List<String> filePath);

    void makeFilePublic(List<String> filePath);

    void makeFilePrivate(List<String> filePath);

    void writeToFile(List<String> filePath, String content);

    String readFromFile(List<String> filePath);

    void uploadFile(List<String> destinationDirectoryPath, File file, String destinationFilename);

    void uploadZippedFiles(List<String> destinationDirectoryPath, File zippedFiles);

    ByteArrayOutputStream getZippedFilesInDirectory(List<String> directoryPath);

    List<FileInfo> listFilesInDirectory(List<String> directoryPath);

    String getURL(List<String> filePath);
}
