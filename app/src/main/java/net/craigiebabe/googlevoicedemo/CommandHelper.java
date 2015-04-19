package net.craigiebabe.googlevoicedemo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.util.InvalidFormatException;

public class CommandHelper {
	
	public enum EnumCommand {
		INVALID,
		FOREHAND_TOPSPIN_DRILL,
		FOREHAND_SLICE_DRILL,
		BACKHAND_TOPSPIN_DRILL,
		BACKHAND_SLICE_DRILL,
		GROUNDSTROKE_DRILL,
		TOPSPIN_GROUNDSTROKE_DRILL,
		SLICE_GROUNDSTROKE_DRILL,
		VOLLEY_DRILL,
		BACKHAND_VOLLEY_DRILL,
		FOREHAND_VOLLEY_DRILL,
		LOB_DRILL,
		CARDIO_DRILL,
		FASTER,
		MUCH_FASTER,
		FASTEST,
		SLOWER,
		MUCH_SLOWER,
		SLOWEST,
		STOP,
		START,
	} ;
	
	public static boolean isCommand(ArrayList<String> textMatchList) {
		for(String word : textMatchList) {
			if(word.contains("coach"))
				return true;
		}
		return false;
	}

	static DocumentCategorizerME myCategorizer = null;
	
	/**
	 * See http://opennlp.apache.org/documentation/1.5.2-incubating/manual/opennlp.html
	 * 
	 * @param textMatchList
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static EnumCommand identifyCommand(InputStream trainerInputStream, StringBuffer inputText) throws InvalidFormatException, IOException {
		if(myCategorizer == null) {
			DoccatModel model = new DoccatModel(trainerInputStream);
			myCategorizer = new DocumentCategorizerME(model);
		}
		double[] outcomes = myCategorizer.categorize(inputText.toString());
		String category = myCategorizer.getBestCategory(outcomes);
		EnumCommand command ;
		try {
			command = EnumCommand.valueOf(category);
		} catch (Exception e) {
			command = EnumCommand.INVALID;
		}
		return command;
	}
	
	public static void sendCommand(EnumCommand command) {
		if(command != EnumCommand.INVALID)
			System.out.println("Sending: " + command.toString() + " to robot!");
		else
			System.out.println("NOT sending invalid commands to robot!");
	}
}
