package processor.pipeline;

import javax.swing.SwingConstants;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;
import processor.Processor;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException; 
public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX()
	{
		if(OF_EX_Latch.isEX_enable())
		{
			
			Instruction instruction = OF_EX_Latch.getInstruction();
			System.out.println(instruction);
			OperationType opertn = instruction.getOperationType();
			OperationType[] op_ty = OperationType.values();
			int op_ind = 0;
			for(int i=0;i<30;i++)
			{
				if(op_ty[i]==opertn)
				{
					op_ind=i;
				}
			}
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter()-1;
			EX_MA_Latch.setInstruction(instruction);
			int alu_result=0;
			if(op_ind<21 && op_ind%2==0)
			{
				int op1=containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
				int op2=containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand2().getValue());
				switch(opertn)
				{
					case add:
						
						alu_result = op1 + op2;
						break;
					case sub:
						alu_result = op1 - op2;
						break;
					case mul:
						alu_result = op1 * op2;
						break;
					case div:
						alu_result = op1 / op2;
						int rm = op1 % op2;
						containingProcessor.getRegisterFile().setValue(31, rm);
						break;
					case and:
						alu_result = op1 & op2;
						break;
					case or:
						alu_result = op1 | op2;
						break;
					case xor:
						alu_result = op1 ^ op2;
						break;
					case slt:
						if(op1 < op2)
							alu_result = 1;
						else
							alu_result = 0;
						break;
					case sll:
						alu_result = op1 << op2;
						break;
					case srl:
						alu_result = op1 >>> op2;
						break;
					case sra:
						alu_result = op1 >> op2;
						break;
					case load:
						alu_result = op1 + op2;
						break;
					default:
						break;
				}
			}
			else if(op_ind < 23)
			{
				int op1=containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
				int op2=instruction.getSourceOperand2().getValue();
				switch(opertn)
				{
					case addi:
						alu_result = op1 + op2;
						break;
					case subi:
						alu_result = op1 - op2;
						break;
					case muli:
						alu_result = op1 * op2;
						break;
					case divi:
						alu_result = op1 / op2;
						int rm = op1 % op2;
						containingProcessor.getRegisterFile().setValue(31, rm);
						break;
					case andi:
						alu_result = op1 & op2;
						break;
					case ori:
						alu_result = op1 | op2;
						break;
					case xori:
						alu_result = op1 ^ op2;
						break;
					case slti:
						if(op1 < op2)
							alu_result = 1;
						else
							alu_result = 0;
						break;
					case slli:
						alu_result = op1 << op2;
						break;
					case srli:
						alu_result = op1 >>> op2;
						break;
					case srai:
						alu_result = op1 >> op2;
						break;
					case load:
						alu_result = op1 + op2;
						break;
					default:
						break;
				}
			}
			else if(op_ind==23)
			{
				int op1=containingProcessor.getRegisterFile().getValue(instruction.getDestinationOperand().getValue());
				int op2=instruction.getSourceOperand2().getValue();
				alu_result=op1 + op2;
			}
			else if(op_ind==24)
			{
				int val =0;
				OperandType opera_type = instruction.getDestinationOperand().getOperandType();
				if(opera_type == OperandType.Register)
				{
					 val = containingProcessor.getRegisterFile().getValue(instruction.getDestinationOperand().getValue());
				}
				else
				{
					 val = instruction.getDestinationOperand().getValue();
				}
				alu_result = currentPC + val;
				EX_IF_Latch.setIS_enable(true,alu_result);
			}
			else if(op_ind<29)
			{
				int op1=containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand1().getValue());
				int op2=containingProcessor.getRegisterFile().getValue(instruction.getSourceOperand2().getValue());
				int val = instruction.getDestinationOperand().getValue();
				switch (opertn) {
					case beq:
						if(op1==op2)
						{
							alu_result=currentPC+val;
							EX_IF_Latch.setIS_enable(true,alu_result);
						}
						break;
					case bne:
						if(op1!=op2)
						{
							alu_result=currentPC+val;
							EX_IF_Latch.setIS_enable(true,alu_result);
						}
						break;
					case blt:
						if(op1<op2)
						{
							alu_result=currentPC+val;
							EX_IF_Latch.setIS_enable(true,alu_result);
						}
						break;
					case bgt:
						if(op1>op2)
						{
							alu_result=currentPC+val;
							EX_IF_Latch.setIS_enable(true,alu_result);
						}
						break;
						
				
					default:
						break;
				}
			}
			EX_MA_Latch.setALU_result(alu_result);

		}


		OF_EX_Latch.setEX_enable(false);
		EX_MA_Latch.setMA_enable(true);
		
	}

}
