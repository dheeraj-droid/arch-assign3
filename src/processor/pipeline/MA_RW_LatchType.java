package processor.pipeline;
import generic.Instruction;
public class MA_RW_LatchType {
	
	boolean RW_enable;
	Instruction instruction;
	int ld_result;
	int alu_result;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}
	public Instruction getInstruction() {
		return instruction;
	}

	public void setInstruction(Instruction inst) {
		instruction = inst;
	}

	public void setld_result(int result) {
		ld_result = result;
	}

	public int getld_result() {
		return ld_result;
	}

	public int getALU_result() {
		return alu_result;
	}

	public void setALU_result(int result) {
		alu_result = result;
	}

}
