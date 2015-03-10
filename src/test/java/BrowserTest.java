import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.util.StringUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import com.cos.util.crypto.AESCrypto;
import com.cos.util.crypto.MD5Crypto;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

/**
 * Created by Joseph on 2/15/2015.
 */
// reference: ceph-docs/ceph.com/docs/master/radosgw/s3/bucketops/default.htm
public class BrowserTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserTest.class);
    private static AWSCredentials credentials;
    private static AmazonS3 conn;
    private static Bucket bucket;
    private static String existBucketName = "s3test1815";
    private static String emptyBucket = "emptyBucket";

    @BeforeClass
    public static void setUpBeforeClass() {
        credentials = new BasicAWSCredentials("LXZDUBGMBRP1AGUTBSP0", "uXH34a0Kogb7bT87rn6x839UKhb3xJWFADP09Su3");
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);

        conn = new AmazonS3Client(credentials, clientConfig);
        conn.setEndpoint("192.168.56.101");

        bucket = conn.createBucket("my-new-bucket");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        ObjectListing objects = conn.listObjects(bucket.getName());
        do {
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                System.out.println(objectSummary.getKey() + "\t" +
                        objectSummary.getSize() + "\t" +
                        StringUtils.fromDate(objectSummary.getLastModified()));
                conn.deleteObject(bucket.getName(), objectSummary.getKey());
            }
            objects = conn.listNextBatchOfObjects(objects);
        } while (objects.isTruncated());
        conn.deleteBucket(bucket.getName());
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testOwnedBuckets() {
        List<Bucket> buckets = conn.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName() + "\t" +
                    StringUtils.fromDate(bucket.getCreationDate()));
        }
    }

    @Test
    public void testCreateBucket() {
        Bucket bk = conn.createBucket("new-bucket");
        conn.deleteBucket(bk.getName());
    }

    @Test
    public void testListBucketContent() {
        ObjectListing objects = conn.listObjects(existBucketName);
        do {
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                System.out.println(objectSummary.getKey() + "\t" +
                        objectSummary.getSize() + "\t" +
                        StringUtils.fromDate(objectSummary.getLastModified()));
            }
            objects = conn.listNextBatchOfObjects(objects);
        } while (objects.isTruncated());
    }

    @Test
    public void testDeleteBucket() {
        conn.deleteBucket(emptyBucket);
    }

    @Test
    public void testCreateObject() {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        conn.putObject(bucket.getName(), "hello.txt", input, new ObjectMetadata());
    }

    @Test
    public void testChangeObjectAcl() {
        conn.setObjectAcl(bucket.getName(), "hello.txt", CannedAccessControlList.PublicRead);
        conn.setObjectAcl(bucket.getName(), "secret_plans.txt", CannedAccessControlList.Private);
    }

    @Test
    public void testDownloadObject() {
        conn.getObject(
                new GetObjectRequest(bucket.getName(), "perl_poetry.pdf"),
                new File("/home/larry/documents/perl_poetry.pdf")
        );
    }

    @Test
    public void testAESCryption() {
        String seed = "wallestar";
        String password = "123456";
        byte[] encryptResult = AESCrypto.encrypt(password, seed);
        String encryptResultStr = AESCrypto.byteArrayToHexString(encryptResult);
        byte[] decryptFrom = AESCrypto.hexStringToByteArray(encryptResultStr);
        byte[] decryptResult = AESCrypto.decrypt(decryptFrom, seed);
        String decrytPassword = new String(decryptResult);
        LOGGER.info("password:{}, after encrypt:{}, after decrypt:{}", password, encryptResultStr, new String(decryptResult));
        Assert.assertArrayEquals(encryptResult, decryptFrom);
        Assert.assertEquals(password, decrytPassword);
    }

    @Test
    public void testMD5Cryption() {
        LOGGER.info("md5 admin={}, ,length={}", MD5Crypto.MD5Encode("123456"), MD5Crypto.MD5Encode("123456").length());
    }
}
