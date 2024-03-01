package processor.pipeline;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException; 
import generic.Instruction;
import generic.Simulator;
import processor.Processor;
import generic.Operand.OperandType;
import generic.Instruction.OperationType;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			Instruction instruction = MA_RW_Latch.getInstruction();
			int alu_result= MA_RW_Latch.getALU_result();
			OperationType op_ty = instruction.getOperationType();
			switch(op_ty)
			{	
				case beq:
				case bne:
				case blt:
				case bgt:
				case jmp:
				case store:
							break;
				case load:
						int ld_result = MA_RW_Latch.getld_result();
						containingProcessor.getRegisterFile().setValue(instruction.getDestinationOperand().getValue(), ld_result);
						break;
				case end:
						Simulator.setSimulationComplete(true);
						break;
				default:
						containingProcessor.getRegisterFile().setValue(instruction.getDestinationOperand().getValue(),alu_result);
						break;
				
			}
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
