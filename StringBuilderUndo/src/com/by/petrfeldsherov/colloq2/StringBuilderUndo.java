package com.by.petrfeldsherov.colloq2;

import java.util.Stack;

class StringBuilderUndo {

    private StringBuilder stringBuilder; // delegate
    private Stack<UndoAction> actions = new Stack<>();

    private class DeleteAction implements UndoAction {
	private int size;

	public DeleteAction(int size) {
	    this.size = size;
	}

	public void undo() {
	    stringBuilder.delete(stringBuilder.length() - size, stringBuilder.length());
	}
    }

    public StringBuilderUndo() {
	stringBuilder = new StringBuilder();
    }

    public StringBuilderUndo(String s) {
	this.stringBuilder = new StringBuilder(s);
    }

    public void undo() {
	if (!actions.isEmpty()) {
	    actions.pop().undo();
	}
    }

    public StringBuilderUndo reverse() {
	stringBuilder.reverse();
	UndoAction undoAction = new UndoAction() {
	    public void undo() {
		stringBuilder.reverse();
	    }
	};
	actions.add(undoAction);
	return this;
    }

    public StringBuilderUndo append(String str) {
	stringBuilder.append(str);
	UndoAction undoAction = new UndoAction() {
	    public void undo() {
		stringBuilder.delete(stringBuilder.length() - str.length(), stringBuilder.length());
	    }
	};
	actions.add(undoAction);
	return this;
    }

    public StringBuilderUndo appendCodePoint(int codePoint) {
	int lenghtBefore = stringBuilder.length();
	stringBuilder.appendCodePoint(codePoint);
	actions.add(new DeleteAction(stringBuilder.length() - lenghtBefore));
	return this;
    }

    public StringBuilderUndo insert(int index, char[] str, int offset, int len) {
	stringBuilder.insert(index, str, offset, len);
	actions.add(() -> stringBuilder.delete(index, len));
	return this;
    }

    public StringBuilderUndo insert(int offset, String str) {
	stringBuilder.insert(offset, str);
	actions.add(() -> stringBuilder.delete(offset, str.length()));
	return this;
    }

    public StringBuilderUndo delete(int start, int end) {
	String deleted = stringBuilder.substring(start, end);
	stringBuilder.delete(start, end);
	actions.add(() -> stringBuilder.insert(start, deleted));
	return this;
    }

    public StringBuilderUndo deleteCharAt(int index) {
	char deleted = stringBuilder.charAt(index);
	stringBuilder.deleteCharAt(index);
	actions.add(() -> stringBuilder.insert(index, deleted));
	return this;
    }

    public StringBuilderUndo replace(int start, int end, String str) {
	String deleted = stringBuilder.substring(start, end);
	stringBuilder.replace(start, end, str);
	actions.add(() -> stringBuilder.replace(start, start + str.length(), deleted));
	return this;
    }

    public String toString() {
	return stringBuilder.toString();
    }
}