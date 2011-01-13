package lejos.pc.tools;

import lejos.pc.comm.NXTCommFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

/**
 * Console output monitor class.
 * This class provides access to console output from a NXT program. The program
 * simply writes strings using the NXT RConsole class. These are sent to the
 * PC via the USB (or Bluetooth) connection.
 *
 */ 
public class Console implements ConsoleViewerUI {

	public static void main(String[] args){
		int r;
		try {
			r = new Console().run(args);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			r = 1;
		}
		System.exit(r);
	}


	private int run(String[] args)
	{
		ConsoleCommandLineParser fParser = new ConsoleCommandLineParser(Console.class, "[options]");
		CommandLine commandLine;
		try
		{
			commandLine = fParser.parse(args);
		}
		catch (ParseException e)
		{
			fParser.printHelp(System.err, e);
			return 1;
		}
		
		if (commandLine.hasOption("h"))
		{
			fParser.printHelp(System.out);
			return 0;
		}
		
		int protocols = 0;
		boolean blueTooth = commandLine.hasOption("b");
		boolean usb = commandLine.hasOption("u");
		String name = commandLine.getOptionValue("n");
		String address = commandLine.getOptionValue("d");
        String debugFile = commandLine.getOptionValue("gr");
        ConsoleDebugDisplay debug = new ConsoleDebugDisplay(this, debugFile);
		ConsoleViewComms comm = new ConsoleViewComms(this, debug, false, false);
		if (blueTooth) protocols |= NXTCommFactory.BLUETOOTH;
		if (usb) protocols |= NXTCommFactory.USB;
		if (protocols == 0) protocols = NXTCommFactory.ALL_PROTOCOLS;
		boolean connected = comm.connectTo(name, address, protocols);
		if (!connected) {
			logMessage("Failed to connect to NXT");
			return 1;
		}
        comm.waitComplete();
		return 0;
	}

	public void append(String value) {
		System.out.print(value);
	}
    
    public void updateLCD(byte[] buffer) {
    }

	public void connectedTo(String name, String address) {
	}

	public void logMessage(String msg) {
		System.out.println(msg);		
	}

	public void setStatus(String msg) {
	}
}