package fr.univartois.butinfo.lensymphony;

import picocli.CommandLine.Option;

import java.util.List;
import java.util.concurrent.Callable;

class MusicCommandLine implements Callable<Integer> {
	@Option(names = {"-i", "--input"}, description = "Fichier MusicXML d'entrée", required = true)
	private String input;

	@Option(names = {"-o", "--output"}, description = "Fichier de sortie (optionnel)")
	private String output;

	@Option(names = {"-p", "--play"}, description = "Jouer en temps réel")
	private boolean play ;

	@Option(names = {"-v", "--voice"}, description = "Configuration de voix au format id:instrument. Peut être présent plusieurs fois.",split=",")
	private List<String> voices;

	public String getInput() { return input; }
	public String getOutput() { return output; }
	public boolean isPlay() { return play; }
	public List<String> getVoices() { return voices; }

	public Integer call() throws Exception {
		return 0;
	}
}