package processor.pipeline;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException; 
import generic.Instruction;
import processor.Processor;
import generic.Operand.OperandType;
import generic.Instruction.OperationType;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		if(EX_MA_Latch.isMA_enable())
		{
			Instruction instruction = EX_MA_Latch.getInstruction();
			int alu_result= EX_MA_Latch.getALU_result();
			MA_RW_Latch.setALU_result(alu_result);
			OperationType op_ty = instruction.getOperationType();
			switch (op_ty) {
				case load:
					int ld_result = containingProcessor.getMainMemory().getWord(alu_result);
					MA_RW_Latch.setld_result(ld_result);
					break;
				case store:
					int val = containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
					containingProcessor.getMainMemory().setWord(alu_result, val);
			
				default:
					break;
			}
			MA_RW_Latch.setInstruction(instruction);
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setMA_enable(false);
		}
	}

}
