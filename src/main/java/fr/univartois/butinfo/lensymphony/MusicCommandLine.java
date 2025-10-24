package fr.univartois.butinfo.lensymphony;

import picocli.CommandLine;
import picocli.CommandLine.Option;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

class MusicCommandLine implements Callable<Integer> {
	@Option(names = {"-i", "--input"}, description = "Fichier MusicXML d'entrée", required = true,interactive = true)
	private String input;

	@Option(names = {"-o", "--output"}, description = "Fichier de sortie (optionnel)",interactive = true)
	private String output;

	@Option(names = {"-p", "--play"}, description = "Jouer en temps réel")
	private boolean play ;

	@Option(names = {"-v", "--voice"}, description = "Configuration de voix au format id:instrument (ex: P1:piano). Peut être présent plusieurs fois.")
	private List<String> voices = new ArrayList<>();

	public String getInput() { return input; }
	public String getOutput() { return output; }
	public boolean isPlay() { return play; }

	public void play(){
		System.out.println("play");
	}

	public Integer call() throws Exception {

		System.out.printf("ton fichier d'entrée c'est " + input + " ton fichier de sortie c'est " + output + "\n");

		return 0;
	}


	public static void main(String[] args) {
		MusicCommandLine cmd = new MusicCommandLine();
		new CommandLine(cmd).execute("-i","-o","-p");
		if(cmd.isPlay()){
			cmd.play();//TODO modifier pour que ça fasse autre chose
		}
	}
}