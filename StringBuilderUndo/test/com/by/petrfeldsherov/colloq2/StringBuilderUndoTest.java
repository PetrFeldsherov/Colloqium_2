package com.by.petrfeldsherov.colloq2;

import static org.junit.Assert.*;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class StringBuilderUndoTest {

    private static final String INITIAL_STRING = "string to test";
    private static final int INITIAL_LENGTH = INITIAL_STRING.length();
    private static StringBuilder sb;
    private static StringBuilderUndo sbu;

    @Before
    public void initialiseStringBuilders() {
	sb = new StringBuilder(INITIAL_STRING);
	sbu = new StringBuilderUndo(INITIAL_STRING);
    }

    @Test
    public void testUndoReverse() {
	sbu.reverse();
	sbu.undo();
	assertTrue(sbu.toString().equals(INITIAL_STRING));
    }

    @Test
    public void testUndoAppend() {
	sbu.append(" appended1").append(" appended2");
	sbu.undo();
	assertTrue(became(INITIAL_STRING + " appended1"));
	checkUndoToDefault();
    }

    @Test
    public void testUndoAppendCodePoint() {
	sbu.appendCodePoint(51).appendCodePoint(52);
	sbu.undo();
	assertTrue(became(INITIAL_STRING + (char) 51));
	checkUndoToDefault();
    }

    @Test
    public void testUndoDelete() {
	sbu.delete(INITIAL_LENGTH - 1, INITIAL_LENGTH).delete(INITIAL_LENGTH - 2, INITIAL_LENGTH - 1);
	sbu.undo();
	assertTrue(became(sb.delete(INITIAL_LENGTH - 1, INITIAL_LENGTH).toString()));
	checkUndoToDefault();
    }

    @Test
    public void testUndoDeleteCharAt() {
	sbu.deleteCharAt(0).deleteCharAt(0);
	sbu.undo();
	assertTrue(became(sb.deleteCharAt(0).toString()));
	checkUndoToDefault();
    }

    @Test
    public void testUndoInsertManyArgs() {
	sbu.insert(0, INITIAL_STRING.toCharArray(), 0, INITIAL_LENGTH).insert(0, INITIAL_STRING.toCharArray(), 0, INITIAL_LENGTH);
	sbu.undo();
	assertTrue(became(INITIAL_STRING + INITIAL_STRING));
	checkUndoToDefault();
    }

    @Test
    public void testUndoDeleteFewArgs() {
	sbu.insert(0, INITIAL_STRING).insert(0, INITIAL_STRING);
	sbu.undo();
	assertTrue(became(INITIAL_STRING + INITIAL_STRING));
	checkUndoToDefault();
    }

    @Test
    public void testUndoReplace() {
	sbu.replace(INITIAL_LENGTH - 1, INITIAL_LENGTH, "LAST_CHAR");
	sbu.replace(0, 1, "FIRST_CHAR");
	sbu.undo();
	assertTrue(became(sb.replace(INITIAL_LENGTH - 1, INITIAL_LENGTH, "LAST_CHAR").toString()));
	checkUndoToDefault();
    }

    @Test
    public void testReverse() {
	sb.reverse();
	sbu.reverse();
	assertTrue(sameResult());
    }

    @Test
    public void testAppend() {
	sb.append(" appended");
	sbu.append(" appended");
	assertTrue(sameResult());
    }

    @Test
    public void testAppendCodePoint() {
	sb.appendCodePoint(50);
	sbu.appendCodePoint(50);
	assertTrue(sameResult());
    }

    @Test
    public void testInsertIntCharArrayIntInt() {
	sb.insert(1, INITIAL_STRING.toCharArray(), 0, INITIAL_STRING.length());
	sbu.insert(1, INITIAL_STRING.toCharArray(), 0, INITIAL_STRING.length());
	assertTrue(sameResult());
    }

    @Test
    public void testInsertIntString() {
	sb.append(INITIAL_STRING);
	sbu.append(INITIAL_STRING);
	assertTrue(sameResult());
    }

    @Test
    public void testDelete() {
	sb.delete(0, 3);
	sbu.delete(0, 3);
	assertTrue(sameResult());
    }

    @Test
    public void testDeleteCharAt() {
	sb.deleteCharAt(0);
	sbu.deleteCharAt(0);
	assertTrue(sameResult());
    }

    @Test
    public void testReplace() {
	sb.replace(0, 1, INITIAL_STRING);
	sbu.replace(0, 1, INITIAL_STRING);
	assertTrue(sameResult());
    }

    private boolean sameResult() {
	return sb.toString().equals(sbu.toString());
    }

    private boolean became(String s) {
	return sbu.toString().equals(s);
    }

    private void checkUndoToDefault() {
        sbu.undo();
        assertTrue(became(INITIAL_STRING));
        sbu.undo();
        assertTrue(became(INITIAL_STRING));
    }
}
