package simplest.client;

import ru.arsmagna.*;
import ru.arsmagna.infrastructure.*;
import ru.arsmagna.menus.MenuFile;

import java.util.Arrays;

public class SimplestClient
{
    public static void main(String[] args)
    {
        try
        {
            IrbisConnection connection = new IrbisConnection();
            connection.username = "user";
            connection.password = "password";
            connection.connect();

            IrbisVersion version = connection.getServerVersion();
            System.out.println(version);

            IrbisProcessInfo[] processes = connection.listProcesses();
            System.out.println(Arrays.toString(processes));

            ServerStat stat = connection.getServerStat();
            System.out.println(stat);

            int maxMfn = connection.getMaxMfn();
            System.out.printf("Max MFN (IBIS)=%d", maxMfn);
            System.out.println();

            connection.noOp();

            maxMfn = connection.getMaxMfn("RDR");
            System.out.printf("Max MFN (RDR)=%d", maxMfn);
            System.out.println();

            int[] found = connection.search("K=БЕТОН");
            System.out.printf("Found: %d", found.length);
            System.out.println();

            FileSpecification specification = new FileSpecification(3, "IBIS", "NASPI.txt");
            String text = connection.readTextFile(specification);
            System.out.println(text);

            MarcRecord record = connection.readRecord(connection.database, 1);
            System.out.println(record.toString());

            specification = new FileSpecification(3, "IBIS", "*.txt");
            String[] files = connection.listFiles(specification);
            System.out.println(String.join(", ", files));

            String formatted = connection.formatRecord("@brief", 1);
            System.out.println(formatted);

            record = new MarcRecord();
            record.fields.add(new RecordField(100, "Field100"));
            record.fields.add(new RecordField(200, "Field200"));
            String format = "v100/v200";
            formatted = connection.formatRecord(format, record);
            System.out.println(formatted);

            TermParameters parameters = new TermParameters();
            parameters.database = "IBIS";
            parameters.startTerm = "K=";
            parameters.numberOfTerms = 10;
            TermInfo[] terms = connection.readTerms(parameters);
            for (TermInfo term: terms)
            {
                System.out.println(term);
            }

            PostingParameters postingParameters = new PostingParameters();
            postingParameters.database = "IBIS";
            postingParameters.term = "K=&C";
            postingParameters.firstPosting = 1;
            postingParameters.numberOfPostings = 100;
            TermPosting[] postings = connection.readPostings(postingParameters);
            for (TermPosting posting: postings)
            {
                System.out.println(posting);
            }

            specification = new FileSpecification(IrbisPath.MASTER_FILE, "IBIS", "NAZN.MNU");
            MenuFile menu = MenuFile.read(connection, specification);
            System.out.println(menu);

            connection.disconnect();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
