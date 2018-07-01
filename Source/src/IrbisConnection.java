import com.sun.media.jfxmediaimpl.MediaDisposer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class IrbisConnection
{
    /**
     * Адрес сервера.
     */
    public String Host;

    /**
     * Порт сервера.
     */
    public int Port;

    /**
     * Имя пользователя (логин).
     */
    public String Username;

    /**
     * Пароль.
     */
    public String Password;

    /**
     * Имя текущей базы данных.
     */
    public String Database;

    /**
     * Тип АРМ.
     */
    public char Workstation;

    /**
     * Идентификатор клиента.
     */
    public int ClientID;

    /**
     * Номер команды.
     */
    public int QueryID;

    /**
     * Конструктор.
     */
    public IrbisConnection()
    {
        Host = "127.0.0.1";
        Port = 6666;
        Workstation = 'C';
    }

    public void ActualizeRecord
        (
            String database,
            int mfn
        )
    {
        throw new NotImplementedException();
    }

    public void Connect()
    {
        throw new NotImplementedException();
    }

    public void CreateDatabase
        (
            String databaseName,
            String description,
            boolean readerAccess,
            String template
        )
    {
        throw new NotImplementedException();
    }

    public void CreateDictionary
        (
            String databaseName
        )
    {
        throw new NotImplementedException();
    }

    public void DeleteDatabase
        (
            String databaseName
        )
    {
        throw new NotImplementedException();
    }

    public void Disconnect()
    {
        throw new NotImplementedException();
    }

    public String FormatRecord
        (
            String format,
            int mfn
        )
    {
        throw new NotImplementedException();
    }

    public String FormatRecord
        (
            String format,
            MarcRecord record
        )
    {
        throw new NotImplementedException();
    }

    public int GetMaxMfn()
    {
        throw new NotImplementedException();
    }

    public int GetMaxMfn
        (
            String databaseName
        )
    {
        throw new NotImplementedException();
    }

    public String[] ListFiles
        (
            FileSpecification specification
        )
    {
        throw new NotImplementedException();
    }

    public String[] ListFiles
        (
            FileSpecification[] specifications
        )
    {
        throw new NotImplementedException();
    }

    public void NoOp()
    {
        throw new NotImplementedException();
    }

    public void ParseConnectionString
        (
            String connectionString
        )
    {
        throw new NotImplementedException();
    }

    public byte[] ReadBinaryFile
        (
            FileSpecification specification
        )
    {
        throw new NotImplementedException();
    }

    public TermPosting[] ReadPostings
        (
            PostingParameters parameters
        )
    {
        throw new NotImplementedException();
    }

    public MarcRecord ReadRecord
        (
            String database,
            int mfn,
            boolean lockFlag,
            String format
        )
    {
        throw new NotImplementedException();
    }

    public MarcRecord ReadRecord
        (
            String database,
            int mfn,
            int versionNumber,
            String format
        )
    {
        throw new NotImplementedException();
    }

    public TermInfo[] ReadTerms
        (
            TermParameters parameters
        )
    {
        throw new NotImplementedException();
    }

    public String ReadTextFile
        (
            FileSpecification specification
        )
    {
        throw new NotImplementedException();
    }

    public String[] ReadTextFiles
        (
            FileSpecification[] specifications
        )
    {
        throw new NotImplementedException();
    }

    public void ReloadDictionary
        (
            String database
        )
    {
        throw new NotImplementedException();
    }

    public void ReloadMasterFile
        (
            String database
        )
    {
        throw new NotImplementedException();
    }

    public void RestartServer()
    {
        throw new NotImplementedException();
    }

    public int[] Search
        (
            String expression
        )
    {
        throw new NotImplementedException();
    }

    public int[] SequentialSearch
        (
            SearchParameters parameters
        )
    {
        throw new NotImplementedException();
    }

    public void TruncateDatabase
        (
            String database
        )
    {
        throw new NotImplementedException();
    }

    public void UnlockDatabase
        (
            String database
        )
    {
        throw new NotImplementedException();
    }

    public void UnlockRecords
        (
            String database,
            int[] mfnList
        )
    {
        throw new NotImplementedException();
    }

    public void UpdateIniFile
        (
            String[] lines
        )
    {
        throw new NotImplementedException();
    }

    public MarcRecord WriteRecord
        (
            MarcRecord record,
            boolean lockFlag,
            boolean actualize,
            boolean dontParseResponse
        )
    {
        throw new NotImplementedException();
    }

    public MarcRecord[] WriteRecords
        (
            MarcRecord[] records,
            boolean lockFlag,
            boolean actualize
        )
    {
        throw new NotImplementedException();
    }

    public void WriteTextFile
        (
            FileSpecification specification
        )
    {
        throw new NotImplementedException();
    }
}
