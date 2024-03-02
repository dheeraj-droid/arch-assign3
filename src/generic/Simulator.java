package generic;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException; 
import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
  		try{
				FileInputStream file = new FileInputStream(assemblyProgramFile);
   				DataInputStream inp = new DataInputStream(file);
   				int data = 0;
   				if(inp.available()>0)
				{
   					data = inp.readInt();
    				processor.getRegisterFile().setProgramCounter(data);
   				}
   				for(int start=0;inp.available()>0;start++)
				{
  			  		data=inp.readInt();
   					processor.getMainMemory().setWord(start, data);
				}
   				processor.getRegisterFile().setValue(0, 0);
   				processor.getRegisterFile().setValue(1,65535);
  				processor.getRegisterFile().setValue(2, 65535);
  				inp.close();
  			}
  		catch(IOException e)
			{
   				e.printStackTrace();
			}
	}
	public static void simulate()
	{
		while(simulationComplete == false)
		{
			processor.getIFUnit().performIF();
			processor.getOFUnit().performOF();
			processor.getEXUnit().performEX();
			processor.getMAUnit().performMA();
			processor.getRWUnit().performRW();
			Clock.incrementClock();
			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
			Statistics.setNumberOfCycles(Statistics.getNumberOfCycles() + 1);
		}
		
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
