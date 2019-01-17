// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.direct;

import org.jetbrains.annotations.NotNull;

import ru.arsmagna.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static ru.arsmagna.Utility.writeInt32Network;

/**
 * MST file record.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class MstRecord64 {

    /**
     * MST file offset of the record.
     */
    public long offset;

    /**
     * Leader.
     */
    public MstRecordLeader64 leader;

    /**
     * Dictionary.
     */
    public List<MstDictionaryEntry64> dictionary;

    //=========================================================================

    public MstRecord64() {
        leader = new MstRecordLeader64();
        dictionary = new ArrayList<>();
    }

    //=========================================================================

    /**
     * Whether the record is deleted?
     *
     * @return true or false
     */
    public boolean isDeleted() {
        return (leader.status & (RecordStatus.PHYSICALLY_DELETED
                | RecordStatus.LOGICALLY_DELETED)) != 0;
    }

    public static RecordField decodeField(@NotNull MstDictionaryEntry64 entry)
            throws IOException {
        return RecordField.parse(entry.tag, entry.text);
    }

    public MarcRecord decodeRecord() throws IOException {
        MarcRecord result = new MarcRecord();
        result.mfn = leader.mfn;
        result.status = leader.status;
        result.previousOffset = leader.previous;
        result.version = leader.version;

        for (MstDictionaryEntry64 entry: dictionary) {
            RecordField field = decodeField(entry);
            result.fields.add(field);
        }

        return result;
    }

    public static MstDictionaryEntry64 encodeField(@NotNull RecordField field) {
        MstDictionaryEntry64 result = new MstDictionaryEntry64();
        result.tag = field.tag;
        result.text = field.toText();

        return result;
    }

    public static MstRecord64 encodeRecord(@NotNull MarcRecord record) {
        MstRecordLeader64 leader = new MstRecordLeader64();
        leader.mfn = record.mfn;
        leader.status = record.status;
        leader.previous = record.previousOffset;
        leader.version = record.version;

        MstRecord64 result = new MstRecord64();
        result.leader = leader;

        for (RecordField field: record.fields) {
            MstDictionaryEntry64 entry = encodeField(field);
            result.dictionary.add(entry);
        }

        return result;
    }

    public void prepare() {
        MstRecordLeader64 leader = this.leader;
        Charset encoding = IrbisEncoding.utf();
        leader.nvf = dictionary.size();
        int recordSize = MstRecordLeader64.LEADER_SIZE
                + leader.nvf * MstDictionaryEntry64.ENTRY_SIZE;
        leader.base = recordSize;
        int position = 0;
        for (int i = 0; i < leader.nvf; i++) {
            MstDictionaryEntry64 entry = dictionary.get(i);
            entry.position = position;
            entry.bytes = IrbisEncoding.encode(encoding, entry.text);
            int length = entry.bytes.length;
            entry.length = length;
            recordSize += length;
            position += length;
        }
        leader.length = recordSize;
    }

    public void write(@NotNull OutputStream stream) throws IOException {
        leader.write(stream);

        for (MstDictionaryEntry64 entry: dictionary) {
            writeInt32Network(stream, entry.tag);
            writeInt32Network(stream, entry.position);
            writeInt32Network(stream, entry.length);
        }

        for (MstDictionaryEntry64 entry: dictionary) {
            stream.write(entry.bytes);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Leader: ");
        result.append(leader.toString());
        result.append('\n');

        for (MstDictionaryEntry64 entry: dictionary) {
            result.append(entry.toString());
        }
        
        return result.toString();
    }
}
