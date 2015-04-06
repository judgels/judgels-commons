package org.iatoki.judgels.commons;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class LocalFileSystemProvider implements FileSystemProvider {
    private File baseDir;

    public LocalFileSystemProvider(File baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public void createDirectory(List<String> directoryPath) {
        File directory = FileUtils.getFile(baseDir, toArray(directoryPath));
        try {
            FileUtils.forceMkdir(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createFile(List<String> filePath) {
        writeToFile(filePath, "");
    }

    @Override
    public void removeFile(List<String> filePath) {
        File file = FileUtils.getFile(baseDir, toArray(filePath));
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean fileExists(List<String> filePath) {
        return FileUtils.getFile(baseDir, toArray(filePath)).exists();
    }

    @Override
    public void writeToFile(List<String> filePath, String content) {
        File file = FileUtils.getFile(baseDir, toArray(filePath));
        try {
            FileUtils.writeStringToFile(file, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readFromFile(List<String> filePath) {
        File file = FileUtils.getFile(baseDir, toArray(filePath));
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadFile(List<String> destinationDirectoryPath, File file, String destinationFilename) {
        File destinationFile = FileUtils.getFile(FileUtils.getFile(baseDir, toArray(destinationDirectoryPath)), destinationFilename);
        try {
            FileUtils.copyFile(file, destinationFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadZippedFiles(List<String> destinationDirectoryPath, File zippedFiles) {
        File destinationDirectory = FileUtils.getFile(baseDir, toArray(destinationDirectoryPath));

        byte[] buffer = new byte[4096];
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zippedFiles));
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String filename = ze.getName();
                File file = new File(destinationDirectory, filename);

                // only process outer files
                if (file.isDirectory() || destinationDirectory.getAbsolutePath().equals(file.getParentFile().getAbsolutePath())) {
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }

                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ByteArrayOutputStream getZippedFilesInDirectory(List<String> directoryPath) {
        File rootDirectory = FileUtils.getFile(toArray(directoryPath));
        List<File> files = getAllFilesRecursively(rootDirectory);

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        try {
            ZipOutputStream zos = new ZipOutputStream(os);
            for (File file : files) {
                int beginIndex = file.getAbsolutePath().indexOf(rootDirectory.getAbsolutePath()) + rootDirectory.getAbsolutePath().length() + 1;
                ZipEntry ze = new ZipEntry(file.getAbsolutePath().substring(beginIndex));
                zos.putNextEntry(ze);

                try (FileInputStream fin = new FileInputStream(file)) {
                    int len;
                    while ((len = fin.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                }
            }

            zos.closeEntry();
            zos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return os;
    }

    @Override
    public List<FileInfo> listFilesInDirectory(List<String> directoryPath) {
        File directory = FileUtils.getFile(baseDir, toArray(directoryPath));
        File[] files = directory.listFiles();

        if (files == null) {
            return ImmutableList.of();
        }

        ArrayList<FileInfo> fileInfos = Lists.newArrayList(Lists.transform(Arrays.asList(files), file -> new FileInfo(file.getName(), file.length(), new Date(file.lastModified()))));

        Comparator<String> comparator = new NaturalFilenameComparator();
        Collections.sort(fileInfos, (FileInfo a, FileInfo b) -> comparator.compare(a.getName(), b.getName()));

        return fileInfos;
    }

    @Override
    public String getURL(List<String> filePath) {
        return FileUtils.getFile(baseDir, toArray(filePath)).getAbsolutePath();
    }

    private List<File> getAllFilesRecursively(File rootDirectory) {
        ImmutableList.Builder<File> files = ImmutableList.builder();
        visitDirectory(rootDirectory, files);
        return files.build();
    }

    private void visitDirectory(File node, ImmutableList.Builder<File> files) {
        if (node.isFile()) {
            files.add(node);
        } else {
            File[] newNodes = node.listFiles();
            if (newNodes != null) {
                for (File newNode : newNodes) {
                    visitDirectory(newNode, files);
                }
            }
        }
    }

    private String[] toArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }
}
