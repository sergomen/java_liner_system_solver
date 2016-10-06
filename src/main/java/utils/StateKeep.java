package utils;

import DAO.RowState;
import main.Main;

import java.io.*;
import java.util.ArrayList;

/**
 * Keep system solution state for each time step
 * Created by Georg Mayur on 11.09.16.
 */
public final class StateKeep {

	private static ArrayList<RowState> rowStates = null;

	/**
	 * @return ArrayList with all serialized rowStates
	 */
	public static ArrayList<RowState> getRowStates() {
		return rowStates;
	}

	/**
	 * Make rowStates if not exist and append
	 *
	 * @param next to rowStates
	 */
	public static void addState(RowState next) {
		if (rowStates == null) {
			rowStates = new ArrayList<>();
		}
		rowStates.add(next);
	}

	/**
	 * Serialize rowStates to file
	 */
	public static void serialize() {
		Main.log(StateKeep.class.getName(),
			"serialize to:" + StateKeep.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "rowStates");
		try {
			FileOutputStream fout = new FileOutputStream(StateKeep.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "rowStates");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(rowStates);
		} catch (IOException e) {
			Main.err(StateKeep.class.getName(), "cannot serialize rowStates");
			Main.err(StateKeep.class.getName(), e.getMessage());
		}
	}

	/**
	 * Try to load rowStates from file and
	 *
	 * @return operation status as true|false with accordance
	 * operation result
	 */
	public static boolean deserialize() {
		Main.log(StateKeep.class.getName(),
			"deserialize from:" + StateKeep.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "rowStates");
		try {
			FileInputStream streamIn = new FileInputStream(StateKeep.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "rowStates");
			ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
			rowStates = (ArrayList<RowState>) objectinputstream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			Main.err(StateKeep.class.getName(), "cannot deserialize rowStates");
			Main.err(StateKeep.class.getName(), e.getMessage());
		}

		if (rowStates != null)
			return true;
		return false;
	}

	/**
	 * Try to remove file include serialized state data
	 */
	public static void drop() {
		File serialized = new File(
			StateKeep.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "rowStates");
		Main.log(StateKeep.class.getName(), "remove state:" + serialized.getPath());

		if (rowStates != null) {
			Main.log(StateKeep.class.getName(), "remove rowStates array");
			rowStates.clear();
			rowStates = null;
		}

		if (serialized.delete()) {
			Main.log(StateKeep.class.getName(), "state dump successfully");
		} else
			Main.err(StateKeep.class.getName(), "state wasn't removed");
	}
}

