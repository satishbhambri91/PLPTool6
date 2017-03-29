package edu.asu.plp.tool.backend.mipsisa.sim;

import edu.asu.plp.tool.backend.isa.exceptions.SimulatorException;
import edu.asu.plp.tool.backend.mipsisa.InstructionExtractor;

public class ALU
{
	// 32-bit overflow mask
	private static long ARITHMETIC_OVERFLOW_VALUE = 0x7fffffff;
	
	public ALU()
	{
	}
	
	public long evaluate(long a, long b, long instruction) throws SimulatorException
	{
		//@formatter:off
		switch (InstructionExtractor.opcode(instruction))
		{
			case 0: // R-types
				switch (InstructionExtractor.funct(instruction))
				{
					case 0x24: return a & b;
					case 0x25: return a | b;
					case 0x26: return a ^ b;
					case 0x27: return ~(a | b);
					case 0x20: 
						//OVERFLOW HERE if a+b overflows
						long temp = a + b;
						// if temp is +, compare to largest value
						// OR
						// if temp is -, negate and compare to largest value + 1
						if (temp > ARITHMETIC_OVERFLOW_VALUE || -temp > ARITHMETIC_OVERFLOW_VALUE + 1)
						{
							throw new SimulatorException("Arithematic Overflow");
						}
						return temp;
					case 0x21: return a + b;
					case 0x22:
						long tempSub = a - b;
						if (tempSub > ARITHMETIC_OVERFLOW_VALUE || -tempSub > ARITHMETIC_OVERFLOW_VALUE + 1)
						{
							throw new SimulatorException("Arithematic Overflow");
						}
						return tempSub;
					case 0x23: return a - b;
					case 0x2A:
						int aSigned = (int) a;
						int bSigned = (int) b;
						return (aSigned < bSigned) ? 1 : 0;
					case 0x2B: return (a < b) ? 1 : 0;
					case 0x00: 
						return b << InstructionExtractor.sa(instruction);
					case 0x02:
						return b >>> InstructionExtractor.sa(instruction);
					case 0x10:
						return ((long)(int) a * (long)(int)b) & 0xffffffffL;
					case 0x11:
						return (((long)(int) a * (long)(int)b) & 0xffffffff00000000L) >> 32;
					case 0x01: return a << b;
                    case 0x03: return a >> b;
					case 0x19: return a * b;
					case 0x1B: 
						long value = a % b;
						value = value << 32;
						return (value & 0xffffffff00000000L) | (a / b & 0x00000000ffffffffL);
				}
			case 0x04: return (a - b == 0) ? 1 : 0;
            case 0x05: return (a - b == 0) ? 0 : 1;
            case 0x0c: return a & b;
            case 0x0d: return a | b;
            case 0x0e: return a ^ b;
            case 0x0f: return b << 16;
            case 0x0A:
                int aSigned = (int) a;
                int bSigned = (int) b;
                return (aSigned < bSigned) ? 1 : 0;
            case 0x0B: return (a < b) ? 1 : 0;
            case 0x09:
            case 0x23:
            case 0x2B: return a + b;
            case 0x08:
            	long temp = a + b;
				if (temp > ARITHMETIC_OVERFLOW_VALUE || -temp > ARITHMETIC_OVERFLOW_VALUE + 1)
				{
					throw new SimulatorException("Arithematic Overflow");
				}
				return temp;
            case 0x1C:
            	switch (InstructionExtractor.funct(instruction)) {
            		case 0x20:
            			int n = 0;
            		    if (a <= 0x0000ffff) { n += 16; a <<= 16; }
            		    if (a <= 0x00ffffff) { n +=  8; a <<= 8; }
            		    if (a <= 0x0fffffff) { n +=  4; a <<= 4; }
            		    if (a <= 0x3fffffff) { n +=  2; a <<= 2; }
            		    if (a <= 0x7fffffff) n ++;
            		    return n;
            		case 0x21:
            			int n1 = 0;
            			
            			if (a >= 0xffff0000) { n1 += 16; a >>= 16; }
            			if (a >= 0xff000000) { n1 +=  8; a >>= 8; }
            		    if (a >= 0xf0000000) { n1 +=  4; a >>= 4; }
            		    if (a >= 0x70000000) { n1 +=  2; a >>= 2; }
            		    if (a >= 0x30000000) n1 ++;
            		    return n1;
            		case 0x00:
            			//OVERFLOW??
            		case 0x01:
            			System.out.println("PRINTING A " + a + "PRINTING B " + b);
            			return a + b;
            		case 0x04:
            			//OVERFLOW??
            		case 0x05:
            			return b - a; //ACC value - multiplied result
            			
            	}
            case 0x1F:
            	switch(InstructionExtractor.funct(instruction)) {
            		case 0x18:
            			return (long) (short) (a & 0xffff);
            		case 0x10:
            			return (long) (char) (a & 0xff);
            	}
		}
		//@formatter:on
		return -1;
	}
	
}
