package sample.utils;

import sample.Main;

import java.io.*;
import java.util.ArrayList;

/**
 * Keep system solution state for each time step
 * Created by Georg Mayur on 11.09.16.
 */
public final class StateKeep {

	private static ArrayList<State> states = null;

	/**
	 * @return ArrayList with all serialized states
	 */
	public static ArrayList<State> getStates() {
		return states;
	}

	/**
	 * Make states if not exist and append
	 *
	 * @param next to states
	 */
	public static void addState(State next) {
		if (states == null) {
			states = new ArrayList<>();
		}
		states.add(next);
	}

	/**
	 * Serialize states to file
	 */
	public static void serialize() {
		Main.log(StateKeep.class.getName(),
				"serialize to:" + StateKeep.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "states");
		try {
			FileOutputStream fout = new FileOutputStream(StateKeep.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "states");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(states);
		} catch (IOException e) {
			Main.err(StateKeep.class.getName(), "cannot serialize states");
			Main.err(StateKeep.class.getName(), e.getMessage());
		}
	}

	/**
	 * Try to load states from file and
	 *
	 * @return operation status as true|false with accordance
	 * operation result
	 */
	public static boolean deserialize() {
		Main.log(StateKeep.class.getName(),
				"deserialize from:" + StateKeep.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "states");
		try {
			FileInputStream streamIn = new FileInputStream(StateKeep.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "states");
			ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
			states = (ArrayList<State>) objectinputstream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			Main.err(StateKeep.class.getName(), "cannot deserialize states");
			Main.err(StateKeep.class.getName(), e.getMessage());
		}

		if (states != null)
			return true;
		return false;
	}

	/**
	 * Try to remove file include serialized state data
	 */
	public static void drop() {
		File serialized = new File(
				StateKeep.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "states");
		Main.log(StateKeep.class.getName(), "remove state:" + serialized.getPath());

		if (states != null) {
			Main.log(StateKeep.class.getName(), "remove states array");
			states.clear();
			states = null;
		}

		if (serialized.delete()) {
			Main.log(StateKeep.class.getName(), "state dump successfully");
		} else
			Main.err(StateKeep.class.getName(), "state wasn't removed");
	}
}

