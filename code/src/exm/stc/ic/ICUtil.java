package exm.stc.ic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import exm.stc.common.lang.Arg;
import exm.stc.common.lang.Arg.ArgType;
import exm.stc.common.lang.Variable;
import exm.stc.ic.tree.ICContinuations.Continuation;
import exm.stc.ic.tree.ICInstructions.Instruction;
import exm.stc.ic.tree.ICTree.Block;

/**
 * Miscellaneous useful utilities that are used in multiple places in the intermediate 
 * code
 *
 */
public class ICUtil {

  public static final String indent = "  ";
  
  /** Print a formal argument list, e.g. "(int a, int b, int c)" */
  public static void prettyPrintFormalArgs(StringBuilder sb,
                                                  List<Variable> args) {
    boolean first = true;
    sb.append("(");
    for (Variable a: args) {
      if (!first) {
        sb.append(", ");
      }
      sb.append(a.getType().typeName() + " " + a.getName());
      first = false;
    }
    sb.append(")");
  }

  public static void prettyPrintVarInfo(StringBuilder sb,
          List<Variable> usedVariables, List<Variable> containersToBeRegistered) {
    boolean printed = false;
    if (usedVariables.size() > 0 ) {
      sb.append("#passin[");
      prettyPrintVarList(sb, usedVariables);
      sb.append("]");
      printed = true;
    }

    if (containersToBeRegistered.size() > 0) {
      if (printed)
        sb.append(" ");
      sb.append("#keepopen[");
      prettyPrintVarList(sb, containersToBeRegistered);
      sb.append("]");
    }
  }

  /**
   * print a comma separated list of var names to sb
   * @param sb
   * @param vars
   */
  public static void prettyPrintVarList(StringBuilder sb, 
                Collection<Variable> vars) {
    boolean first = true;
    for (Variable v: vars) {
      if (first) {
        first = false;
      } else {
        sb.append(", ");
      }
      sb.append(v.getName());
    }
  }

  /**
   * Replace variables by name in list
   * Remove variables with duplicate names
   * @param replacements
   * @param vars
   */
  public static void replaceVarsInList(Map<String, Arg> replacements,
      List<Variable> vars, boolean removeDupes) {
    replaceVarsInList(replacements, vars, removeDupes, true);
  }
  
  public static void replaceVarsInList(Map<String, Arg> replacements,
        List<Variable> vars, boolean removeDupes, boolean removeMapped) {
    // Remove new duplicates
    ArrayList<String> alreadySeen = null;
    if (removeDupes) {
      alreadySeen = new ArrayList<String>(vars.size());
    }
    
    ListIterator<Variable> it = vars.listIterator();
    while (it.hasNext()) {
      Variable v = it.next();
      String varName = v.getName();
      if (replacements.containsKey(varName)) {
        Arg oa = replacements.get(varName);
        if (oa.getType() == ArgType.VAR) {
          if (removeDupes && 
                  alreadySeen.contains(oa.getVar().getName())) {
            it.remove();
          } else {
            it.set(oa.getVar());
            if (removeDupes) {
              alreadySeen.add(oa.getVar().getName());
            }
          }
        }
      } else {
        if (removeDupes) {
          if (alreadySeen.contains(varName)) {
            it.remove();
          } else {
            alreadySeen.add(varName);
          }
        }
      }
    }
  }
  
  public static void removeDuplicates(List<Variable> varList) {
    ListIterator<Variable> it = varList.listIterator();
    HashSet<String> alreadySeen = new HashSet<String>();
    while (it.hasNext()) {
      Variable v = it.next();
      if (alreadySeen.contains(v.getName())) {
        it.remove();
      } else {
        alreadySeen.add(v.getName());
      }
    }
  }

  public static void replaceOpargsInList(Map<String, Arg> renames,
      List<Arg> args) {
    for (int i = 0; i < args.size(); i++) {
      Arg oa = args.get(i);
      if (oa.getType() == ArgType.VAR) {
        String oldName = oa.getVar().getName();
        if (renames.containsKey(oldName)) {
          args.set(i, renames.get(oldName));
        }
      }
    }
  }
  
  /**
   * If oa is a variable with a name in the renames map, replace
   * @param renames
   * @param oa
   * @param nullsOk set to true if oa may be null, otherwise exception
   *    will be thrown
   * @return null if oa is null. If oa is variable and
   *      is in renames, return the replacements.  If it isn't,
   *      return the argument 
   */
  public static Arg replaceOparg(Map<String, Arg> renames, Arg oa, boolean nullsOk) {
    assert(nullsOk || oa != null);
    if (oa != null && oa.getType() == ArgType.VAR) {
      String name = oa.getVar().getName();
      if (renames.containsKey(name)) {
        Arg res = renames.get(name);
        assert(res != null);
        return res;
      }
    }
    return oa;
  }
  
  public static void removeVarInList(List<Variable> varList,
        String toRemove) {
    int n = varList.size();
    for (int i = 0; i < n; i ++) {
      if (varList.get(i).getName().equals(toRemove)) {
        varList.remove(i);
        i--; n--;
      }
    }
  }
  
  public static void removeVarsInList(List<Variable> varList,
      Set<String> removeVars) {
      int n = varList.size();
      
      for (int i = 0; i < n; i++) {
        if (removeVars.contains(varList.get(i).getName())) {
          varList.remove(i);
          n--;
          i--;
        }
      }
   }

  public static LinkedList<Instruction> cloneInstructions(
      List<Instruction> instructions) {
    LinkedList<Instruction> output = new LinkedList<Instruction>();
    for (Instruction i : instructions) {
      output.add(i.clone());
    }
    return output;
  }

  public static ArrayList<Continuation> cloneContinuations(
      List<Continuation> conts) {
    ArrayList<Continuation> newContinuations = 
                        new ArrayList<Continuation>(conts.size());
    for (Continuation old: conts) {
      newContinuations.add(old.clone());
    }
    return newContinuations;
  }

  public static ArrayList<Block> cloneBlocks(List<Block> blocks) {
    ArrayList<Block> newBlocks = new ArrayList<Block>(blocks.size());
    for (Block old: blocks) {
      newBlocks.add(old.clone());
    }
    return newBlocks;
  }
  
  /** 
   * Replace the current instruction with the provided sequence
   * After this is done, next() will return the instruction after
   * the inserted sequence
   */
  public static void replaceInsts(ListIterator<Instruction> it, 
              List<Instruction> replacements) {

    if (replacements.size() == 1) {
      it.set(replacements.get(0));
    } else if (replacements.size() == 0) {
      it.remove();
    } else {
      it.set(replacements.get(0));
      List<Instruction> rest = replacements.subList(1, replacements.size());
      for (Instruction newInst: rest) {
        it.add(newInst);
      }
    }
  }
  public static void rewindIterator(
      @SuppressWarnings("rawtypes") ListIterator it, int n) {
    for (int i = 0; i < n; i++) {
      it.previous();
    }
  }
  

  /**
   * Get variables from list if they are in set
   * @param varNames
   * @param varList
   */
  public static List<Variable> getVarsByName(Set<String> varNames,
                      Collection<Variable> varList) {
    List<Variable> res = new ArrayList<Variable>(varNames.size());
    for (Variable v: varList) {
      if (varNames.contains(v.getName())) {
        res.add(v);
      }
    }
    return res;
  }

  /**
   * Return a list of all the variables contained in the
   * input list.  Ignore any non-variable args
   * @param inputs
   * @return
   */
  public static List<Variable> extractVars(List<Arg> args) {
    ArrayList<Variable> res = new ArrayList<Variable>();
    for (Arg a: args) {
      if (a.getType() == ArgType.VAR) {
        res.add(a.getVar());
      }
    }
    return res;
  }
}
