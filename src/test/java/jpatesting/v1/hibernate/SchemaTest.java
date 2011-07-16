package jpatesting.v1.hibernate;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class SchemaTest extends AbstractJpaTestCase {

    @Test
    public void testForeignKeysNames() {
        SqlHandler handler = new SqlHandler() {
            public void handle(String sql) {
                assertForeignKeysDoesNotHaveFunnyNames(sql);
            }
        };
        analyzeSchema(handler);
    }

    private static final String FK_LINE_REGEXP = "alter table (.*) add constraint (.*) foreign key .*";
    private static final Pattern FK_LINE_PATTERN = Pattern.compile(FK_LINE_REGEXP);
    private static final Matcher FK_LINE_MATCHER = FK_LINE_PATTERN.matcher("");
    private static final String FK_REGEXP = "fk_[a-z]+_[a-z]+$";
    private static final Pattern FK_PATTERN = Pattern.compile(FK_REGEXP);
    private static final Matcher FK_MATCHER = FK_PATTERN.matcher("");

    private void assertForeignKeysDoesNotHaveFunnyNames(String sql) {
        String[] lines = sql.split("\n");
        StringBuilder buffer = new StringBuilder();
        for (String line : lines) {
            FK_LINE_MATCHER.reset(line);
            if (FK_LINE_MATCHER.find()) {
                String table = FK_LINE_MATCHER.group(1);
                String fk = FK_LINE_MATCHER.group(2);
                if (!isValidFk(fk)) {
                    buffer.append(table).append("(").append(fk).append(") ");
                }
            }
        }
        String violations = buffer.toString();
        if (violations.length() > 0) {
            fail("One or more tables have weird FK names: " + violations);
        }
    }

    private boolean isValidFk(String fk) {
        FK_MATCHER.reset(fk);
        return FK_MATCHER.find();
    }

}
