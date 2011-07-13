package jpatesting.v1.dataaccess;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.*;

import static org.junit.Assert.assertNotNull;

public abstract class AbstractJpaDbUnitTestCase extends AbstractJpaTestCase {
    private static final String IGNORE_SCHEMA = null;
    protected static HsqldbConnection dbunitConnection;

    @BeforeClass
    public static void setupDbUnit() throws Exception {
        dbunitConnection = new HsqldbConnection(connection, IGNORE_SCHEMA);
    }

    @AfterClass
    public static void closeDbUnit() throws Exception {
        if (dbunitConnection != null) {
            dbunitConnection = null;
        }
    }

    public static IDataSet getDataSet(String name) throws Exception {
        InputStream inputStream = getInputStream(name);
        Reader reader = new InputStreamReader(inputStream);
        FlatXmlDataSet dataset = new FlatXmlDataSet(reader);
        return dataset;
    }

    private static InputStream getInputStream(String path) {
        InputStream inputStream = AbstractJpaDbUnitTestCase.class.getResourceAsStream(path);
        assertNotNull("file " + path + " not found in classpath", inputStream);
        return inputStream;
    }

    public static IDataSet getReplacedDataSet(String name, long id) throws Exception {
        IDataSet originalDataSet = getDataSet(name);
        return getReplacedDataSet(originalDataSet, id);
    }

    public static IDataSet getReplacedDataSet(IDataSet originalDataSet, long id) throws Exception {
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(originalDataSet);
        replacementDataSet.addReplacementObject("[NULL]", null);
        return replacementDataSet;
    }

    protected static IDataSet getActualDataset() throws Exception {
        IDataSet actualDataSet = dbunitConnection.createDataSet();
        return actualDataSet;
    }

    protected static IDataSet getStrippedDataset(IDataSet expectedDataSet) throws Exception {
        String[] tableNames = expectedDataSet.getTableNames();
        IDataSet strippedDataSet = dbunitConnection.createDataSet(tableNames);
        return strippedDataSet;
    }

    public static String toString(IDataSet dataSet) throws DataSetException, IOException {
        StringWriter writer = new StringWriter();
        try {
            if (dataSet != null) {
                FlatXmlDataSet.write(dataSet, writer);
            } else {
                writer.write("null");
            }
            return writer.toString();
        } finally {
            writer.close();
        }
    }

}
