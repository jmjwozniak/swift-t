/*
 * Copyright 2013 University of Chicago and Argonne National Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package exm.stc.ic.opt;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;

import exm.stc.ic.tree.ICContinuations.Continuation;
import exm.stc.ic.tree.ICInstructions.Instruction;
import exm.stc.ic.tree.ICInstructions.Opcode;
import exm.stc.ic.tree.ICTree.Block;

/**
 * TODO:
 * should divide further
 * - Opcodes that don't spawn or enable further work, so can be put off
 * - Opcodes that are not computationally intense, so don't need to run in parallel 
 * @author tim
 *
 */
public class ProgressOpcodes {
  
  public static boolean isNonProgressOpcode(Opcode op) {
    return nonProgressOpcodes.contains(op);
  }
  
  public static boolean isCheapOpcode(Opcode op) {
    return cheapOpcodes.contains(op);
  }
  
  /**
   * @param logger
   * @param rootBlock
   * @return true if the continuation at the root of the block might do
   *          significant work.
   */
  public static boolean isCheap(Block rootBlock) {
    Deque<Block> stack = new ArrayDeque<Block>();
    stack.add(rootBlock);
    while (!stack.isEmpty()) {
      Block block = stack.pop();
      
      for (Instruction i: block.getInstructions()) {
        if (!isCheapOpcode(i.op)) {
          return false;
        }
      }
      
      for (Continuation c: block.getContinuations()) {
        if (!c.isAsync()) {
          for (Block inner: c.getBlocks()) {
            stack.push(inner);
          }
        }
      }
    }
    return true;
  }
  

  /**
   * Opcodes which we don't consider as making "progress", i.e.
   * won't enable further work to run
   */
  private static HashSet<Opcode> nonProgressOpcodes = initNonProgress();
  
  private static HashSet<Opcode> initNonProgress() { 
    HashSet<Opcode> opcodes = new HashSet<Opcode>();
    opcodes.add(Opcode.ARRAY_DECR_WRITERS);
    opcodes.add(Opcode.DECR_BLOB_REF);
    opcodes.add(Opcode.FREE_BLOB);
    opcodes.add(Opcode.DECR_REF);
    opcodes.add(Opcode.LOCAL_OP);
    opcodes.add(Opcode.CALL_LOCAL);
    opcodes.add(Opcode.CALL_LOCAL_CONTROL);
    opcodes.add(Opcode.COPY_REF);
    opcodes.add(Opcode.ADDRESS_OF);
    opcodes.add(Opcode.LOAD_BOOL);
    opcodes.add(Opcode.LOAD_VOID);
    opcodes.add(Opcode.LOAD_FLOAT);
    opcodes.add(Opcode.LOAD_INT);
    opcodes.add(Opcode.LOAD_REF);
    opcodes.add(Opcode.LOAD_STRING);
    opcodes.add(Opcode.LOAD_BLOB);
    opcodes.add(Opcode.GET_FILENAME);
    opcodes.add(Opcode.GET_OUTPUT_FILENAME);
    return opcodes;
  }
  
  /**
   * Opcodes that don't use much time or CPU
   */
  private static HashSet<Opcode> cheapOpcodes = initCheap();
  private static HashSet<Opcode> initCheap() { 
    HashSet<Opcode> opcodes = new HashSet<Opcode>();
    opcodes.add(Opcode.ARRAY_DECR_WRITERS);
    opcodes.add(Opcode.DECR_BLOB_REF);
    opcodes.add(Opcode.FREE_BLOB);
    opcodes.add(Opcode.DECR_REF);
    opcodes.add(Opcode.LOCAL_OP);
    opcodes.add(Opcode.CALL_LOCAL);
    opcodes.add(Opcode.CALL_LOCAL_CONTROL);
    opcodes.add(Opcode.STORE_BOOL);
    opcodes.add(Opcode.STORE_VOID);
    opcodes.add(Opcode.STORE_INT);
    opcodes.add(Opcode.STORE_FLOAT);
    opcodes.add(Opcode.STORE_STRING);
    opcodes.add(Opcode.STORE_BLOB);
    opcodes.add(Opcode.COPY_REF);
    opcodes.add(Opcode.ADDRESS_OF);
    opcodes.add(Opcode.LOAD_BOOL);
    opcodes.add(Opcode.LOAD_VOID);
    opcodes.add(Opcode.LOAD_FLOAT);
    opcodes.add(Opcode.LOAD_INT);
    opcodes.add(Opcode.LOAD_REF);
    opcodes.add(Opcode.LOAD_STRING);
    opcodes.add(Opcode.LOAD_BLOB);
    opcodes.add(Opcode.GET_FILENAME);
    opcodes.add(Opcode.GET_OUTPUT_FILENAME);
    return opcodes;
  }
}
