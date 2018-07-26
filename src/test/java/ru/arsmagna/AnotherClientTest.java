package ru.arsmagna;

import org.junit.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

@Ignore
public class AnotherClientTest {

    IrbisConnection connection;

    @Before
    public void setUp() throws IOException, IrbisException {
        connection = new IrbisConnection();
        connection.username = "user";
        connection.password = "password";
        connection.workstation = 'A';
        connection.connect();
    }

    @After
    public void tearDown() throws IOException {
        connection.disconnect();
        connection = null;
    }

    @Test
    public void getMaxMfn_1() throws IOException, IrbisException {
        System.out.println("getMaxMfn_1");
        int maxMfn = connection.getMaxMfn(connection.database);
        System.out.printf("Max MFN=%d", maxMfn);
        System.out.println();
        System.out.println();
    }

    @Test
    public void readTextFile_1() throws IOException {
        System.out.println("readTextFile_1");
        FileSpecification specification = new FileSpecification(IrbisPath.MASTER_FILE, "IBIS", "NASPI.txt");
        String text = connection.readTextContent(specification);
        System.out.println(text);
    }

    @Test
    public void readTextFile_2() throws IOException {
        System.out.println("readTextFile_2");
        FileSpecification specification = new FileSpecification(IrbisPath.MASTER_FILE, "IBIS", "NASPI.txt");
        InputStream stream = connection.readBinaryFile(specification);
        if (stream != null) {
            try {
                Scanner scanner = new Scanner(stream);
                while (scanner.hasNext()) {
                    System.out.println(scanner.nextLine());
                }
            }
            finally {
                stream.close();
            }
        }
    }

    @Test
    public void readBinaryFile_1() throws IOException {
        System.out.println("readBinaryFile_1");
        FileSpecification specification = new FileSpecification(IrbisPath.SYSTEM, null, "IRBIS.GIF");
        byte[] content = connection.readBinaryContent(specification);
        int length = content == null ? 0 : content.length;
        System.out.println(length);
        System.out.println();
    }

}
