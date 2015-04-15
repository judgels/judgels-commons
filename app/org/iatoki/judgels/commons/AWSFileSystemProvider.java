package org.iatoki.judgels.commons;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SetObjectAclRequest;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class AWSFileSystemProvider implements FileSystemProvider {
    private AmazonS3 s3;
    private String cloudFrontURL;
    private String bucket;
    private LRUCache<String, AWSFileURL> cache;

    public AWSFileSystemProvider(AmazonS3 s3, String bucket, Region region) {
        this.s3 = s3;
        this.bucket = bucket;
        this.cache = new LRUCache<>(1000);
        if (!s3.doesBucketExist(bucket)) {
            s3.createBucket(new CreateBucketRequest(bucket, region));
        }
    }

    public AWSFileSystemProvider(AmazonS3 s3, String bucket, String cloudFrontURL, Region region) {
        this.s3 = s3;
        this.cloudFrontURL = cloudFrontURL;
        this.bucket = bucket;
        this.cache = new LRUCache<>(1000);
        if (!s3.doesBucketExist(bucket)) {
            s3.createBucket(new CreateBucketRequest(bucket, region));
        }
    }

    @Override
    public void createDirectory(List<String> directoryPath) {
        s3.putObject(new PutObjectRequest(bucket, StringUtils.join(directoryPath, File.separator) + File.separator, new ByteArrayInputStream(new byte[0]), null));
    }

    @Override
    public void createFile(List<String> filePath) {
        s3.putObject(new PutObjectRequest(bucket, StringUtils.join(filePath, File.separator), new ByteArrayInputStream(new byte[0]), null));
    }

    @Override
    public void removeFile(List<String> filePath) {
        s3.deleteObject(new DeleteObjectRequest(bucket, StringUtils.join(filePath, File.separator)));
    }

    @Override
    public boolean directoryExists(List<String> directoryPath) {
        try {
            s3.getObjectMetadata(new GetObjectMetadataRequest(bucket, (StringUtils.join(directoryPath, File.separator)) + File.separator));
            return true;
        } catch (AmazonS3Exception e) {
            return false;
        }
    }

    @Override
    public boolean fileExists(List<String> filePath) {
        try {
            s3.getObjectMetadata(new GetObjectMetadataRequest(bucket, (StringUtils.join(filePath, File.separator))));
            return true;
        } catch (AmazonS3Exception e) {
            return false;
        }
    }

    @Override
    public void makeFilePublic(List<String> filePath) {
        s3.setObjectAcl(new SetObjectAclRequest(bucket, StringUtils.join(filePath, File.separator), CannedAccessControlList.PublicRead));
    }

    @Override
    public void makeFilePrivate(List<String> filePath) {
        s3.setObjectAcl(new SetObjectAclRequest(bucket, StringUtils.join(filePath, File.separator), CannedAccessControlList.AuthenticatedRead));
    }

    @Override
    public void writeToFile(List<String> filePath, String content) {
        String cannonicalFilename = StringUtils.join(filePath, File.separator);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(URLConnection.guessContentTypeFromName(cannonicalFilename));
        s3.putObject(new PutObjectRequest(bucket, cannonicalFilename, new ByteArrayInputStream(content.getBytes()), objectMetadata));
    }

    @Override
    public String readFromFile(List<String> filePath) {
        try {
            return IOUtils.toString(s3.getObject(new GetObjectRequest(bucket, StringUtils.join(filePath, File.separator))).getObjectContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadFile(List<String> destinationDirectoryPath, File file, String destinationFilename) {
        try {
            StringBuilder canonicalFileNameBuilder = new StringBuilder();
            if (!destinationDirectoryPath.isEmpty()) {
                canonicalFileNameBuilder.append(StringUtils.join(destinationDirectoryPath, File.separator));
                canonicalFileNameBuilder.append(File.separator);
            }
            canonicalFileNameBuilder.append(destinationFilename);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            String contentType = URLConnection.guessContentTypeFromName(destinationFilename);
            objectMetadata.setContentType(contentType);
            if (contentType.startsWith("image/")) {
                objectMetadata.setCacheControl("no-transform,public,max-age=300,s-maxage=900");
            }
            s3.putObject(new PutObjectRequest(bucket, canonicalFileNameBuilder.toString(), new FileInputStream(file), objectMetadata));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadZippedFiles(List<String> destinationDirectoryPath, File zippedFiles) {
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zippedFiles));
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String filename = ze.getName();

                if (ze.isDirectory()) {
                    s3.putObject(new PutObjectRequest(bucket, StringUtils.join(destinationDirectoryPath, File.separator) + File.separator + filename + File.separator, new ByteArrayInputStream(new byte[0]), null));
                } else {
                    ObjectMetadata objectMetadata = new ObjectMetadata();
                    objectMetadata.setContentType(URLConnection.guessContentTypeFromStream(zis));
                    s3.putObject(new PutObjectRequest(bucket, StringUtils.join(destinationDirectoryPath, File.separator) + File.separator + filename, zis, objectMetadata));
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
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectListing objectListing;
        if (directoryPath.isEmpty()) {
            objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucket).withPrefix(""));
        } else {
            objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucket).withPrefix(StringUtils.join(directoryPath, File.separator) + File.separator));
        }

        try {
            ZipOutputStream zos = new ZipOutputStream(os);
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                int beginIndex = StringUtils.join(directoryPath, File.separator).length();
                if (!directoryPath.isEmpty()) {
                    beginIndex += 1;
                }
                S3Object s3Object = s3.getObject(new GetObjectRequest(bucket, objectSummary.getKey()));
                ZipEntry ze = new ZipEntry(objectSummary.getKey().substring(beginIndex));
                zos.putNextEntry(ze);

                zos.write(IOUtils.toString(s3Object.getObjectContent()).getBytes());
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
        ObjectListing objectListing;
        if (directoryPath.isEmpty()) {
            objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucket).withPrefix(""));
        } else {
            objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucket).withPrefix(StringUtils.join(directoryPath, File.separator) + File.separator));
        }

        List<FileInfo> fileInfos = Lists.newArrayList();
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            int beginIndex = StringUtils.join(directoryPath, File.separator).length();
            if (!directoryPath.isEmpty()) {
                beginIndex += 1;
            }
            if (!objectSummary.getKey().substring(beginIndex).contains(File.separator)) {
                fileInfos.add(new FileInfo(objectSummary.getKey().substring(beginIndex), objectSummary.getSize(), objectSummary.getLastModified()));
            }
        }

        Comparator<String> comparator = new NaturalFilenameComparator();
        Collections.sort(fileInfos, (FileInfo a, FileInfo b) -> comparator.compare(a.getName(), b.getName()));

        return fileInfos;
    }

    @Override
    public String getURL(List<String> filePath) {
        String key = StringUtils.join(filePath, File.separator);
        if (cloudFrontURL == null) {
            boolean canPublicRead = false;
            for (Grant grant : s3.getObjectAcl(bucket, key).getGrantsAsList()) {
                if ((grant.getGrantee().equals(GroupGrantee.AllUsers)) && (grant.getPermission().equals(Permission.Read))) {
                    canPublicRead = true;
                }
            }

            if (canPublicRead) {
                return "https://" + bucket + ".s3.amazonaws.com/" + key;
            } else {
                AWSFileURL checkCache = cache.get(key);
                if (checkCache != null) {
                    if (System.currentTimeMillis() >= (checkCache.getExpireTime() - TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES))) {
                        String generatedURL = generateSignedURL(key, getDefaultExpireTime());
                        cache.put(key, new AWSFileURL(generatedURL, getDefaultExpireTime()));
                        return generatedURL;
                    } else {
                        return checkCache.getURL();
                    }
                } else {
                    String generatedURL = generateSignedURL(key, getDefaultExpireTime());
                    cache.put(key, new AWSFileURL(generatedURL, getDefaultExpireTime()));
                    return generatedURL;
                }
            }
        } else {
            return cloudFrontURL + key;
        }
    }

    private String generateSignedURL(String key, long expireTime) {
        GeneratePresignedUrlRequest presignedUrlRequest = new GeneratePresignedUrlRequest(bucket, key);
        presignedUrlRequest.setExpiration(new Date(expireTime));
        return s3.generatePresignedUrl(presignedUrlRequest).toString();
    }

    public long getDefaultExpireTime() {
        return System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
    }
}
