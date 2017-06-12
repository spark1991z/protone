package project.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class FilteredPrintStream extends OutputStream {
	
	private PrintStream system;
	
	public FilteredPrintStream(PrintStream system){
		this.system = system;
	}

	@Override
	public void write(int b) throws IOException {
		if(system!=null)
			system.write(b);
	}
}
