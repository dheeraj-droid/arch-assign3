package processor.pipeline;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException; 
import processor.Processor;
import generic.Instruction;
import generic.Operand;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}


	public static char invert(char c){
        return (c == '0') ? '1' : '0';
    }
	
	public static String two_c(String bin){
        String twos = "", ones = "";
        for (int i = 0; i < bin.length(); i++){
            ones += invert(bin.charAt(i));
        }

        StringBuilder builder = new StringBuilder(ones);
        boolean b = false;
        for (int i = ones.length() - 1; i > 0; i--){
            if (ones.charAt(i) == '1'){
                builder.setCharAt(i, '0');
            }
            else{
                builder.setCharAt(i, '1');
                b = true;
                break;
            }
        }
        if (!b){
            builder.append("1", 0, 7);
        }
        twos = builder.toString();
        return twos;
    }

	public static int Convrt(String str)
    {
        int num=0;
        int two=1;
        if(String.valueOf(str.charAt(0))==String.valueOf("1"))
        {
           for(int i=str.length()-1;i>=0;i--)
           {
            num=num-Integer.parseInt(String.valueOf(str.charAt(i)))*two;
            two=two*2;
           }
        }
        else
        {
            for(int i=str.length()-1;i>=0;i--)
           {
            num=num+Integer.parseInt(String.valueOf(str.charAt(i)))*two;
            two=two*2;
           }
        }
       
        return num;
    }

	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			String new_instr=Integer.toBinaryString(IF_OF_Latch.getInstruction());
			while(new_instr.length()<32)
			{
				new_instr="0" +new_instr;
			}
			String opcode=new_instr.substring(0, 5);
			OperationType[] op_ty = OperationType.values();
			int op_ind = Convrt(opcode);
			OperationType opertn = op_ty[op_ind];
			Instruction instruction = new Instruction();
			switch(opertn)
			{
				case add:
				case sub:
				case mul:
				case div:
				case and:
				case or:
				case xor:
				case slt:
				case sll:
				case srl:
				case sra:
							{
							Operand rs1 = new Operand();
							Operand rs2 = new Operand();
							Operand rd = new Operand();

							rs1.setOperandType(OperandType.Register);
							rs2.setOperandType(OperandType.Register);
							rd.setOperandType(OperandType.Register);

							int rs1_num =Convrt(new_instr.substring(5,10));
							int rs2_num =Convrt(new_instr.substring(10,15));
							int rd_num = Convrt(new_instr.substring(15, 20));

							rs1.setValue(rs1_num);
							rs2.setValue(rs2_num);
							rd.setValue(rd_num);

							instruction.setOperationType(opertn);
							instruction.setSourceOperand1(rs1);
							instruction.setSourceOperand2(rs2);
							instruction.setDestinationOperand(rd);
							break;
							}
			case addi :
			case subi :
			case muli :
			case divi : 
			case andi : 
			case ori : 
			case xori : 
			case slti : 
			case slli : 
			case srli : 
			case srai :
			case load :
			case store :
			{
							Operand rs1 = new Operand();
							Operand rs2 = new Operand();
							Operand rd = new Operand();

							rs1.setOperandType(OperandType.Register);
							rs2.setOperandType(OperandType.Immediate);
							rd.setOperandType(OperandType.Register);

							int rs1_num = Convrt(new_instr.substring(5,10));
							int rd_num =Convrt(new_instr.substring(10,15));
							int imm = Convrt(new_instr.substring(15,32));
							

							rs1.setValue(rs1_num);
							rs2.setValue(imm);
							rd.setValue(rd_num);

							instruction.setOperationType(opertn);
							instruction.setSourceOperand1(rs1);
							instruction.setSourceOperand2(rs2);
							instruction.setDestinationOperand(rd);
							break;
			}
			case beq : 
			case bne : 
			case blt : 
			case bgt :
					{
						Operand rs1 = new Operand();
						Operand rs2 = new Operand();
						Operand rd = new Operand();

						rs1.setOperandType(OperandType.Register);
						rs2.setOperandType(OperandType.Register);
						rd.setOperandType(OperandType.Immediate);

						int rs1_num = Convrt(new_instr.substring(5,10));
						int rs2_num =Convrt(new_instr.substring(10,15));
						String imm = new_instr.substring(15, 32);
						int imm_val = Integer.parseInt(imm, 2);
						if (imm.charAt(0) == '1')
						{
							imm = two_c(imm);
							imm_val = Integer.parseInt(imm, 2) * -1;
						}
					
						rs1.setValue(rs1_num);
						rs2.setValue(rs2_num);
						rd.setValue(imm_val);

						instruction.setOperationType(opertn);
						instruction.setSourceOperand1(rs1);
						instruction.setSourceOperand2(rs2);
						instruction.setDestinationOperand(rd);
						break;
					}
			case end:
					{
						instruction.setOperationType(opertn);
						break;
					}
			default:
					{
						
					Operand dest = new Operand();
					String imm = new_instr.substring(10, 32);
					int imm_val = Integer.parseInt(imm, 2);
					if (imm.charAt(0) == '1'){
						imm = two_c(imm);
						imm_val = Integer.parseInt(imm, 2) * -1;
					}
					if (imm_val != 0){
						dest.setOperandType(OperandType.Immediate);
						dest.setValue(imm_val);
					}
					else{
						int reg = Integer.parseInt(new_instr.substring(5, 10), 2);
						dest.setOperandType(OperandType.Register);
						dest.setValue(reg);
					}

					instruction.setOperationType(op_ty[op_ind]);
					instruction.setDestinationOperand(dest);
					break;
					}
				
					}
			OF_EX_Latch.setInstruction(instruction);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
