//### This file created by BYACC 1.8(/Java extension  1.13)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//###           14 Sep 06  -- Keltin Leung-- ReduceListener support, eliminate underflow report in error recovery
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 11 "Parser.y"
package decaf.frontend;

import decaf.tree.Tree;
import decaf.tree.Tree.*;
import decaf.error.*;
import java.util.*;
//#line 25 "Parser.java"
interface ReduceListener {
  public boolean onReduce(String rule);
}




public class Parser
             extends BaseParser
             implements ReduceListener
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

ReduceListener reduceListener = null;
void yyclearin ()       {yychar = (-1);}
void yyerrok ()         {yyerrflag=0;}
void addReduceListener(ReduceListener l) {
  reduceListener = l;}


//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:SemValue
String   yytext;//user variable to return contextual strings
SemValue yyval; //used to return semantic vals from action routines
SemValue yylval;//the 'lval' (result) I got from yylex()
SemValue valstk[] = new SemValue[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new SemValue();
  yylval=new SemValue();
  valptr=-1;
}
final void val_push(SemValue val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    SemValue[] newstack = new SemValue[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final SemValue val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final SemValue val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short VOID=257;
public final static short BOOL=258;
public final static short INT=259;
public final static short STRING=260;
public final static short CLASS=261;
public final static short COMPLEX=262;
public final static short IMGPART=263;
public final static short NULL=264;
public final static short EXTENDS=265;
public final static short THIS=266;
public final static short WHILE=267;
public final static short FOR=268;
public final static short SUPER=269;
public final static short IF=270;
public final static short ELSE=271;
public final static short RETURN=272;
public final static short BREAK=273;
public final static short NEW=274;
public final static short PRINT=275;
public final static short READ_INTEGER=276;
public final static short READ_LINE=277;
public final static short PRINT_COMP=278;
public final static short LITERAL=279;
public final static short CASE=280;
public final static short DEFAULT=281;
public final static short SCOPY=282;
public final static short DCOPY=283;
public final static short DO=284;
public final static short OD=285;
public final static short BRANCH=286;
public final static short IDENTIFIER=287;
public final static short AND=288;
public final static short OR=289;
public final static short STATIC=290;
public final static short INSTANCEOF=291;
public final static short LESS_EQUAL=292;
public final static short GREATER_EQUAL=293;
public final static short EQUAL=294;
public final static short NOT_EQUAL=295;
public final static short UMINUS=296;
public final static short EMPTY=297;
public final static short DoBranchList=298;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    4,    5,    5,    5,    5,    5,
    5,    5,    2,    6,    6,    7,    7,    7,    9,    9,
   10,   10,    8,    8,   11,   12,   12,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   13,   18,   24,
   24,   23,   14,   14,   14,   28,   28,   26,   26,   27,
   25,   25,   25,   25,   25,   25,   25,   25,   25,   25,
   25,   25,   25,   25,   25,   25,   25,   25,   25,   25,
   25,   25,   25,   25,   25,   25,   25,   25,   25,   25,
   25,   25,   25,   31,   31,   33,   32,   30,   30,   29,
   29,   34,   34,   19,   16,   17,   22,   15,   35,   35,
   20,   20,   21,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    2,    1,    1,    1,    1,    1,
    2,    3,    6,    2,    0,    2,    2,    0,    1,    0,
    3,    1,    7,    6,    3,    2,    0,    1,    2,    1,
    1,    1,    2,    2,    2,    2,    2,    1,    4,    3,
    1,    3,    3,    1,    0,    2,    0,    2,    4,    5,
    1,    1,    1,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    2,    2,    2,
    2,    2,    3,    3,    1,    1,    4,    4,    4,    5,
    6,    5,    8,    2,    0,    4,    4,    1,    1,    1,
    0,    3,    1,    4,    5,    9,    1,    6,    2,    0,
    2,    1,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    3,    0,    2,    0,    0,   14,   18,
    0,    7,    8,    6,   10,    0,    9,    0,   13,   16,
    0,    0,   17,   11,    0,    4,    0,    0,    0,    0,
   12,    0,   22,    0,    0,    0,    0,    5,    0,    0,
    0,   27,   24,   21,   23,    0,   89,   75,    0,    0,
   76,    0,    0,   97,    0,    0,    0,    0,    0,   88,
    0,    0,    0,    0,    0,    0,    0,    0,   25,    0,
    0,    0,   28,   38,   26,    0,   30,   31,   32,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   53,    0,
    0,    0,    0,   51,   52,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   29,   33,   34,   35,   36,   37,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   46,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   73,   74,    0,    0,    0,    0,    0,
    0,    0,    0,   67,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   79,    0,    0,  103,   94,    0,   78,
   77,   39,    0,    0,    0,   49,    0,    0,   95,    0,
    0,   80,    0,   85,   42,    0,   82,   50,    0,    0,
   98,    0,   81,    0,   99,    0,    0,    0,   84,    0,
    0,    0,   83,   96,    0,    0,   87,   86,
};
final static short yydgoto[] = {                          2,
    3,    4,   73,   21,   34,    8,   11,   23,   35,   36,
   74,   46,   75,   76,   77,   78,   79,   80,   81,   82,
   83,   84,  150,    0,   85,   94,   95,   88,  187,   89,
  202,  208,  209,  143,  201,
};
final static short yysindex[] = {                      -248,
 -260,    0, -248,    0, -229,    0, -247,  -82,    0,    0,
  409,    0,    0,    0,    0, -243,    0, -142,    0,    0,
  -10,  -90,    0,    0,  -85,    0,   13,  -34,   27, -142,
    0, -142,    0,  -80,   28,   31,   39,    0,  -40, -142,
  -40,    0,    0,    0,    0,    2,    0,    0,   46,   47,
    0,   86,  291,    0, -153,   89,   90,   91,   92,    0,
   95,   96,   98, -159,  100,  291,  291,   88,    0,  291,
  291,  291,    0,    0,    0,   82,    0,    0,    0,   83,
   94,   99,  109,  110,  946,   87,    0, -138,    0,  291,
  291,  291,  946,    0,    0,  114,   59,  291,  118,  136,
  291,  291,  291,  291,  291,  291,  -30,  -30, -132,  538,
  -30,  -30,  -30,    0,    0,    0,    0,    0,    0,  291,
  291,  291,  291,  291,  291,  291,  291,  291,  291,  291,
  291,  291,    0,  291,  291,  141,  550,  124,  562,  143,
  111,  946,   19,    0,    0,   24,  574,  602,  613, -100,
  723,  758,  145,    0, 1019, 1012,  -13,  -13, 1045, 1045,
    9,    9,  -30,  -30,  -30,  -13,  -13,  838,  946,  291,
   37,  291,   37,    0,  872,  291,    0,    0,   67,    0,
    0,    0,   37,  -95,  291,    0,  152,  150,    0,  893,
  -76,    0,  946,    0,    0,  155,    0,    0,  291,   37,
    0, -236,    0,  157,    0,  142,  147,   74,    0,   37,
  291,  291,    0,    0,  914,  935,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  201,    0,   80,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  149,    0,    0,  165,
    0,  165,    0,    0,    0,  168,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -55,    0,    0,    0,    0,
    0,    0,  -39,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -75,  -75,  -75,    0,  -75,
  -75,  -75,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  982,  499,    0,    0,  -75,
  -55,  -75,  151,    0,    0,    0,    0,  -75,    0,    0,
  -75,  -75,  -75,  -75,  -75,  -75,  120,  129,    0,    0,
  388,  397,  424,    0,    0,    0,    0,    0,    0,  -75,
  -75,  -75,  -75,  -75,  -75,  -75,  -75,  -75,  -75,  -75,
  -75,  -75,    0,  -75,  -75,   52,    0,    0,    0,    0,
  -75,   30,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  820,  -36,   44,  715,  829, 1042,
 1053, 1079,  433,  460,  469,  927,  955,    0,  -20,  -32,
  -55,  -75,  -55,    0,    0,  -75,    0,    0,    0,    0,
    0,    0,  -55,    0,  -75,    0,    0,  170,    0,    0,
  -33,    0,   35,    0,    0,    0,    0,    0,  -31,  -55,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -55,
  -75,  -75,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  211,  204,   18,    7,    0,    0,    0,  184,    0,
  -22,    0, -119,  -77,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, 1286,  227,  532,    0,    0,   15,
    0,    0,    0,  -86,    0,
};
final static int YYTABLESIZE=1498;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        100,
   28,  100,  100,   45,   66,   28,  100,   66,   91,   45,
   28,  100,    1,  138,  146,  133,   43,   22,   45,  102,
   43,   66,   66,  130,   25,  100,    5,   47,  128,  126,
  100,  127,  133,  129,   67,    7,   72,   71,   43,    9,
   10,   68,   60,   24,  206,  130,   66,   33,   26,   33,
  128,  189,   30,  191,  133,  129,   66,   44,   31,  177,
  134,   97,  176,  195,  178,   70,   32,  176,   39,   67,
   93,   72,   71,   93,   40,   92,   68,  134,   92,   41,
  205,   66,   42,  188,   63,   90,   91,   63,   48,  100,
  214,  100,   48,   48,   48,   48,   48,   48,   48,  134,
   70,   63,   63,   12,   13,   14,   15,   16,   17,   48,
   48,   48,   48,   48,   12,   13,   14,   15,   16,   17,
   67,  204,   72,   71,   42,   92,   69,   68,   98,   99,
  100,  101,   66,   96,  102,  103,   63,  104,  105,  106,
  114,  115,   48,   67,   48,   72,   71,  135,  136,  141,
   68,   70,  116,  140,  153,   66,   68,  117,  144,   42,
   68,   68,   68,   68,   68,   69,   68,  118,  119,   69,
   69,   69,   69,   69,   70,   69,  145,   68,   68,   68,
  170,   68,  172,  174,  182,  185,   69,   69,   69,  194,
   69,  196,  198,  176,  200,  203,   27,  210,  213,  211,
    1,   29,   15,   31,  212,   20,   38,    5,   19,  101,
   90,   47,   68,    6,   20,   37,  207,    0,    0,    0,
    0,   69,    0,  100,  100,  100,  100,  100,  100,    0,
  100,   47,  100,  100,  100,  100,  100,    0,  100,  100,
  100,  100,  100,  100,  100,  100,  100,   47,  100,  100,
  100,  100,   66,  100,   47,   47,    0,  100,   12,   13,
   14,   15,   16,   17,    0,   47,    0,   48,   49,   50,
   51,   52,   86,   53,   54,   55,   56,   57,   58,   59,
   60,   61,    0,   62,   63,   64,    0,    0,    0,    0,
    0,    0,   65,   12,   13,   14,   15,   16,   17,    0,
   47,    0,   48,   49,   50,   51,   52,    0,   53,   54,
   55,   56,   57,   58,   59,   60,   61,   86,   62,   63,
   64,    0,    0,   67,    0,   72,   71,   65,    0,    0,
   68,   63,   63,    0,    0,   66,    0,   63,   63,   48,
   48,    0,    0,   48,   48,   48,   48,    0,  109,    0,
    0,   47,    0,   48,   70,    0,   51,    0,    0,    0,
    0,   55,    0,   57,   58,    0,   60,   61,    0,   62,
   63,    0,    0,    0,   47,    0,   48,    0,   65,   51,
    0,    0,    0,    0,   55,    0,   57,   58,    0,   60,
   61,    0,   62,   63,    0,    0,    0,   86,    0,   86,
    0,   65,    0,    0,    0,    0,    0,   68,   68,   86,
    0,   68,   68,   68,   68,    0,   69,   69,    0,    0,
   69,   69,   69,   69,   70,   86,   86,    0,   70,   70,
   70,   70,   70,   71,   70,    0,   86,   71,   71,   71,
   71,   71,    0,   71,    0,   70,   70,   70,    0,   70,
    0,    0,    0,    0,   71,   71,   71,    0,   71,    0,
   72,    0,    0,    0,   72,   72,   72,   72,   72,   56,
   72,    0,    0,   56,   56,   56,   56,   56,    0,   56,
   70,   72,   72,   72,    0,   72,    0,    0,    0,   71,
   56,   56,   56,    0,   56,    0,   57,    0,    0,    0,
   57,   57,   57,   57,   57,   58,   57,    0,    0,   58,
   58,   58,   58,   58,    0,   58,   72,   57,   57,   57,
    0,   57,    0,    0,    0,   56,   58,   58,   58,    0,
   58,    0,    0,   19,    0,   52,    0,    0,    0,   44,
   52,   52,    0,   52,   52,   52,    0,    0,    0,    0,
    0,    0,   57,    0,   47,    0,   48,   44,   52,   51,
   52,   58,    0,    0,   55,    0,   57,   58,    0,   60,
   61,    0,   62,   63,  130,    0,    0,   87,  154,  128,
  126,   65,  127,  133,  129,    0,  130,    0,    0,   52,
  171,  128,  126,    0,  127,  133,  129,  132,  130,  131,
    0,    0,  173,  128,  126,    0,  127,  133,  129,  132,
  130,  131,    0,    0,  179,  128,  126,    0,  127,  133,
  129,  132,   87,  131,    0,    0,    0,    0,  134,    0,
    0,    0,    0,  132,    0,  131,    0,    0,  130,    0,
  134,    0,  180,  128,  126,    0,  127,  133,  129,  130,
    0,    0,  134,  181,  128,  126,    0,  127,  133,  129,
    0,  132,    0,  131,  134,   12,   13,   14,   15,   16,
   17,    0,  132,    0,  131,   70,   70,    0,    0,   70,
   70,   70,   70,    0,   71,   71,    0,    0,   71,   71,
   71,   71,  134,    0,    0,    0,    0,    0,   18,    0,
    0,    0,   87,  134,   87,    0,    0,    0,    0,    0,
    0,   72,   72,    0,   87,   72,   72,   72,   72,    0,
   56,   56,    0,    0,   56,   56,   56,   56,    0,    0,
   87,   87,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   87,    0,    0,    0,    0,    0,   57,   57,    0,
    0,   57,   57,   57,   57,   64,   58,   58,   64,  130,
   58,   58,   58,   58,  128,  126,    0,  127,  133,  129,
    0,    0,   64,   64,    0,    0,    0,    0,    0,    0,
  183,    0,  132,    0,  131,    0,   52,   52,    0,    0,
   52,   52,   52,   52,  130,    0,    0,    0,    0,  128,
  126,  184,  127,  133,  129,    0,    0,   64,    0,    0,
    0,    0,    0,  134,    0,    0,    0,  132,    0,  131,
    0,    0,    0,    0,    0,  120,  121,    0,    0,  122,
  123,  124,  125,    0,    0,    0,    0,  120,  121,    0,
    0,  122,  123,  124,  125,    0,    0,    0,  134,  120,
  121,    0,    0,  122,  123,  124,  125,    0,    0,    0,
   65,  120,  121,   65,    0,  122,  123,  124,  125,   59,
    0,    0,   59,    0,  130,    0,    0,   65,   65,  128,
  126,    0,  127,  133,  129,    0,   59,   59,    0,  120,
  121,    0,    0,  122,  123,  124,  125,  132,    0,  131,
  120,  121,    0,    0,  122,  123,  124,  125,  130,    0,
    0,    0,   65,  128,  126,    0,  127,  133,  129,    0,
    0,   59,    0,    0,    0,    0,    0,    0,  134,  130,
  186,  132,    0,  131,  128,  126,    0,  127,  133,  129,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  130,  199,  132,    0,  131,  128,  126,    0,  127,  133,
  129,    0,  134,    0,  192,    0,    0,   62,    0,    0,
   62,  130,  217,  132,    0,  131,  128,  126,    0,  127,
  133,  129,  130,  134,   62,   62,    0,  128,  126,    0,
  127,  133,  129,  218,  132,   61,  131,    0,   61,    0,
    0,    0,   64,   64,  134,  132,    0,  131,   64,   64,
  120,  121,   61,   61,  122,  123,  124,  125,   51,   62,
    0,    0,    0,   51,   51,  134,   51,   51,   51,    0,
    0,    0,    0,    0,    0,    0,  134,    0,    0,    0,
    0,   51,    0,   51,    0,  120,  121,   61,  130,  122,
  123,  124,  125,  128,  126,  130,  127,  133,  129,    0,
  128,  126,    0,  127,  133,  129,    0,    0,    0,    0,
    0,  132,   51,  131,    0,    0,    0,    0,  132,    0,
  131,  130,   60,    0,    0,   60,  128,  126,    0,  127,
  133,  129,    0,   54,    0,   54,   54,   54,    0,   60,
   60,    0,  134,    0,  132,    0,  131,   65,   65,  134,
   54,   54,   54,    0,   54,    0,   59,   59,    0,   55,
    0,   55,   55,   55,    0,  120,  121,    0,    0,  122,
  123,  124,  125,    0,   60,  134,   55,   55,   55,    0,
   55,    0,    0,    0,    0,   54,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  120,
  121,    0,    0,  122,  123,  124,  125,    0,    0,    0,
    0,   55,    0,    0,    0,    0,    0,    0,    0,    0,
  120,  121,    0,    0,  122,  123,  124,  125,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  120,  121,    0,    0,  122,  123,  124,  125,    0,
    0,    0,    0,    0,   62,   62,    0,    0,    0,    0,
   62,   62,  120,  121,    0,    0,  122,  123,  124,  125,
    0,    0,    0,  120,  121,    0,    0,  122,  123,  124,
  125,    0,   61,   61,    0,    0,    0,    0,   61,   61,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   51,
   51,    0,    0,   51,   51,   51,   51,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  120,
    0,    0,    0,  122,  123,  124,  125,    0,    0,    0,
  122,  123,  124,  125,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   60,
   60,    0,    0,    0,    0,    0,  122,  123,   93,    0,
   54,   54,    0,    0,   54,   54,   54,   54,    0,    0,
    0,  107,  108,  110,    0,  111,  112,  113,    0,    0,
    0,    0,    0,    0,    0,    0,   55,   55,    0,    0,
   55,   55,   55,   55,    0,  137,    0,  139,    0,    0,
    0,    0,    0,  142,    0,    0,  142,  147,  148,  149,
  151,  152,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  155,  156,  157,  158,  159,
  160,  161,  162,  163,  164,  165,  166,  167,    0,  168,
  169,    0,    0,    0,    0,    0,  175,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  142,    0,  190,    0,    0,
    0,  193,    0,    0,    0,    0,    0,    0,    0,    0,
  197,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  215,  216,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   91,   35,   36,   59,   41,   91,   40,   44,   41,   41,
   91,   45,  261,   91,  101,   46,   39,   11,   41,   59,
   41,   58,   59,   37,   18,   59,  287,  264,   42,   43,
   64,   45,   46,   47,   33,  265,   35,   36,   59,  287,
  123,   40,  279,  287,  281,   37,   45,   30,   59,   32,
   42,  171,   40,  173,   46,   47,   93,   40,   93,   41,
   91,   55,   44,  183,   41,   64,   40,   44,   41,   33,
   41,   35,   36,   44,   44,   41,   40,   91,   44,   41,
  200,   45,  123,  170,   41,   40,   40,   44,   37,  123,
  210,  125,   41,   42,   43,   44,   45,   46,   47,   91,
   64,   58,   59,  257,  258,  259,  260,  261,  262,   58,
   59,   60,   61,   62,  257,  258,  259,  260,  261,  262,
   33,  199,   35,   36,  123,   40,  125,   40,   40,   40,
   40,   40,   45,  287,   40,   40,   93,   40,  298,   40,
   59,   59,   91,   33,   93,   35,   36,   61,  287,   91,
   40,   64,   59,   40,  287,   45,   37,   59,   41,  123,
   41,   42,   43,   44,   45,   37,   47,   59,   59,   41,
   42,   43,   44,   45,   64,   47,   41,   58,   59,   60,
   40,   62,   59,   41,  285,   41,   58,   59,   60,  123,
   62,  287,   41,   44,  271,   41,  287,   41,  125,   58,
    0,  287,  123,   93,   58,   41,  287,   59,   41,   59,
   41,  287,   93,    3,   11,   32,  202,   -1,   -1,   -1,
   -1,   93,   -1,  257,  258,  259,  260,  261,  262,   -1,
  264,  287,  266,  267,  268,  269,  270,   -1,  272,  273,
  274,  275,  276,  277,  278,  279,  280,  287,  282,  283,
  284,  285,  289,  287,  287,  287,   -1,  291,  257,  258,
  259,  260,  261,  262,   -1,  264,   -1,  266,  267,  268,
  269,  270,   46,  272,  273,  274,  275,  276,  277,  278,
  279,  280,   -1,  282,  283,  284,   -1,   -1,   -1,   -1,
   -1,   -1,  291,  257,  258,  259,  260,  261,  262,   -1,
  264,   -1,  266,  267,  268,  269,  270,   -1,  272,  273,
  274,  275,  276,  277,  278,  279,  280,   91,  282,  283,
  284,   -1,   -1,   33,   -1,   35,   36,  291,   -1,   -1,
   40,  288,  289,   -1,   -1,   45,   -1,  294,  295,  288,
  289,   -1,   -1,  292,  293,  294,  295,   -1,  261,   -1,
   -1,  264,   -1,  266,   64,   -1,  269,   -1,   -1,   -1,
   -1,  274,   -1,  276,  277,   -1,  279,  280,   -1,  282,
  283,   -1,   -1,   -1,  264,   -1,  266,   -1,  291,  269,
   -1,   -1,   -1,   -1,  274,   -1,  276,  277,   -1,  279,
  280,   -1,  282,  283,   -1,   -1,   -1,  171,   -1,  173,
   -1,  291,   -1,   -1,   -1,   -1,   -1,  288,  289,  183,
   -1,  292,  293,  294,  295,   -1,  288,  289,   -1,   -1,
  292,  293,  294,  295,   37,  199,  200,   -1,   41,   42,
   43,   44,   45,   37,   47,   -1,  210,   41,   42,   43,
   44,   45,   -1,   47,   -1,   58,   59,   60,   -1,   62,
   -1,   -1,   -1,   -1,   58,   59,   60,   -1,   62,   -1,
   37,   -1,   -1,   -1,   41,   42,   43,   44,   45,   37,
   47,   -1,   -1,   41,   42,   43,   44,   45,   -1,   47,
   93,   58,   59,   60,   -1,   62,   -1,   -1,   -1,   93,
   58,   59,   60,   -1,   62,   -1,   37,   -1,   -1,   -1,
   41,   42,   43,   44,   45,   37,   47,   -1,   -1,   41,
   42,   43,   44,   45,   -1,   47,   93,   58,   59,   60,
   -1,   62,   -1,   -1,   -1,   93,   58,   59,   60,   -1,
   62,   -1,   -1,  125,   -1,   37,   -1,   -1,   -1,   41,
   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,
   -1,   -1,   93,   -1,  264,   -1,  266,   59,   60,  269,
   62,   93,   -1,   -1,  274,   -1,  276,  277,   -1,  279,
  280,   -1,  282,  283,   37,   -1,   -1,   46,   41,   42,
   43,  291,   45,   46,   47,   -1,   37,   -1,   -1,   91,
   41,   42,   43,   -1,   45,   46,   47,   60,   37,   62,
   -1,   -1,   41,   42,   43,   -1,   45,   46,   47,   60,
   37,   62,   -1,   -1,   41,   42,   43,   -1,   45,   46,
   47,   60,   91,   62,   -1,   -1,   -1,   -1,   91,   -1,
   -1,   -1,   -1,   60,   -1,   62,   -1,   -1,   37,   -1,
   91,   -1,   41,   42,   43,   -1,   45,   46,   47,   37,
   -1,   -1,   91,   41,   42,   43,   -1,   45,   46,   47,
   -1,   60,   -1,   62,   91,  257,  258,  259,  260,  261,
  262,   -1,   60,   -1,   62,  288,  289,   -1,   -1,  292,
  293,  294,  295,   -1,  288,  289,   -1,   -1,  292,  293,
  294,  295,   91,   -1,   -1,   -1,   -1,   -1,  290,   -1,
   -1,   -1,  171,   91,  173,   -1,   -1,   -1,   -1,   -1,
   -1,  288,  289,   -1,  183,  292,  293,  294,  295,   -1,
  288,  289,   -1,   -1,  292,  293,  294,  295,   -1,   -1,
  199,  200,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  210,   -1,   -1,   -1,   -1,   -1,  288,  289,   -1,
   -1,  292,  293,  294,  295,   41,  288,  289,   44,   37,
  292,  293,  294,  295,   42,   43,   -1,   45,   46,   47,
   -1,   -1,   58,   59,   -1,   -1,   -1,   -1,   -1,   -1,
   58,   -1,   60,   -1,   62,   -1,  288,  289,   -1,   -1,
  292,  293,  294,  295,   37,   -1,   -1,   -1,   -1,   42,
   43,   44,   45,   46,   47,   -1,   -1,   93,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   -1,   -1,   60,   -1,   62,
   -1,   -1,   -1,   -1,   -1,  288,  289,   -1,   -1,  292,
  293,  294,  295,   -1,   -1,   -1,   -1,  288,  289,   -1,
   -1,  292,  293,  294,  295,   -1,   -1,   -1,   91,  288,
  289,   -1,   -1,  292,  293,  294,  295,   -1,   -1,   -1,
   41,  288,  289,   44,   -1,  292,  293,  294,  295,   41,
   -1,   -1,   44,   -1,   37,   -1,   -1,   58,   59,   42,
   43,   -1,   45,   46,   47,   -1,   58,   59,   -1,  288,
  289,   -1,   -1,  292,  293,  294,  295,   60,   -1,   62,
  288,  289,   -1,   -1,  292,  293,  294,  295,   37,   -1,
   -1,   -1,   93,   42,   43,   -1,   45,   46,   47,   -1,
   -1,   93,   -1,   -1,   -1,   -1,   -1,   -1,   91,   37,
   93,   60,   -1,   62,   42,   43,   -1,   45,   46,   47,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   37,   59,   60,   -1,   62,   42,   43,   -1,   45,   46,
   47,   -1,   91,   -1,   93,   -1,   -1,   41,   -1,   -1,
   44,   37,   59,   60,   -1,   62,   42,   43,   -1,   45,
   46,   47,   37,   91,   58,   59,   -1,   42,   43,   -1,
   45,   46,   47,   59,   60,   41,   62,   -1,   44,   -1,
   -1,   -1,  288,  289,   91,   60,   -1,   62,  294,  295,
  288,  289,   58,   59,  292,  293,  294,  295,   37,   93,
   -1,   -1,   -1,   42,   43,   91,   45,   46,   47,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   91,   -1,   -1,   -1,
   -1,   60,   -1,   62,   -1,  288,  289,   93,   37,  292,
  293,  294,  295,   42,   43,   37,   45,   46,   47,   -1,
   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,
   -1,   60,   91,   62,   -1,   -1,   -1,   -1,   60,   -1,
   62,   37,   41,   -1,   -1,   44,   42,   43,   -1,   45,
   46,   47,   -1,   41,   -1,   43,   44,   45,   -1,   58,
   59,   -1,   91,   -1,   60,   -1,   62,  288,  289,   91,
   58,   59,   60,   -1,   62,   -1,  288,  289,   -1,   41,
   -1,   43,   44,   45,   -1,  288,  289,   -1,   -1,  292,
  293,  294,  295,   -1,   93,   91,   58,   59,   60,   -1,
   62,   -1,   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  288,
  289,   -1,   -1,  292,  293,  294,  295,   -1,   -1,   -1,
   -1,   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  288,  289,   -1,   -1,  292,  293,  294,  295,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  288,  289,   -1,   -1,  292,  293,  294,  295,   -1,
   -1,   -1,   -1,   -1,  288,  289,   -1,   -1,   -1,   -1,
  294,  295,  288,  289,   -1,   -1,  292,  293,  294,  295,
   -1,   -1,   -1,  288,  289,   -1,   -1,  292,  293,  294,
  295,   -1,  288,  289,   -1,   -1,   -1,   -1,  294,  295,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  288,
  289,   -1,   -1,  292,  293,  294,  295,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  288,
   -1,   -1,   -1,  292,  293,  294,  295,   -1,   -1,   -1,
  292,  293,  294,  295,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  288,
  289,   -1,   -1,   -1,   -1,   -1,  292,  293,   53,   -1,
  288,  289,   -1,   -1,  292,  293,  294,  295,   -1,   -1,
   -1,   66,   67,   68,   -1,   70,   71,   72,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  288,  289,   -1,   -1,
  292,  293,  294,  295,   -1,   90,   -1,   92,   -1,   -1,
   -1,   -1,   -1,   98,   -1,   -1,  101,  102,  103,  104,
  105,  106,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  120,  121,  122,  123,  124,
  125,  126,  127,  128,  129,  130,  131,  132,   -1,  134,
  135,   -1,   -1,   -1,   -1,   -1,  141,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  170,   -1,  172,   -1,   -1,
   -1,  176,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  185,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  211,  212,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=298;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,"'#'","'$'","'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",
"';'","'<'","'='","'>'",null,"'@'",null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"VOID","BOOL","INT","STRING",
"CLASS","COMPLEX","IMGPART","NULL","EXTENDS","THIS","WHILE","FOR","SUPER","IF",
"ELSE","RETURN","BREAK","NEW","PRINT","READ_INTEGER","READ_LINE","PRINT_COMP",
"LITERAL","CASE","DEFAULT","SCOPY","DCOPY","DO","OD","BRANCH","IDENTIFIER",
"AND","OR","STATIC","INSTANCEOF","LESS_EQUAL","GREATER_EQUAL","EQUAL",
"NOT_EQUAL","UMINUS","EMPTY","DoBranchList",
};
final static String yyrule[] = {
"$accept : Program",
"Program : ClassList",
"ClassList : ClassList ClassDef",
"ClassList : ClassDef",
"VariableDef : Variable ';'",
"Variable : Type IDENTIFIER",
"Type : INT",
"Type : VOID",
"Type : BOOL",
"Type : COMPLEX",
"Type : STRING",
"Type : CLASS IDENTIFIER",
"Type : Type '[' ']'",
"ClassDef : CLASS IDENTIFIER ExtendsClause '{' FieldList '}'",
"ExtendsClause : EXTENDS IDENTIFIER",
"ExtendsClause :",
"FieldList : FieldList VariableDef",
"FieldList : FieldList FunctionDef",
"FieldList :",
"Formals : VariableList",
"Formals :",
"VariableList : VariableList ',' Variable",
"VariableList : Variable",
"FunctionDef : STATIC Type IDENTIFIER '(' Formals ')' StmtBlock",
"FunctionDef : Type IDENTIFIER '(' Formals ')' StmtBlock",
"StmtBlock : '{' StmtList '}'",
"StmtList : StmtList Stmt",
"StmtList :",
"Stmt : VariableDef",
"Stmt : SimpleStmt ';'",
"Stmt : IfStmt",
"Stmt : WhileStmt",
"Stmt : ForStmt",
"Stmt : DoStmt ';'",
"Stmt : PrintCompStmt ';'",
"Stmt : ReturnStmt ';'",
"Stmt : PrintStmt ';'",
"Stmt : BreakStmt ';'",
"Stmt : StmtBlock",
"DoStmt : DO DoBranchList DoSubStmt OD",
"DoSubList : DoSubList BRANCH DoSubStmt",
"DoSubList : DoSubStmt",
"DoSubStmt : Expr ':' Stmt",
"SimpleStmt : LValue '=' Expr",
"SimpleStmt : Call",
"SimpleStmt :",
"Receiver : Expr '.'",
"Receiver :",
"LValue : Receiver IDENTIFIER",
"LValue : Expr '[' Expr ']'",
"Call : Receiver IDENTIFIER '(' Actuals ')'",
"Expr : LValue",
"Expr : Call",
"Expr : Constant",
"Expr : Expr '+' Expr",
"Expr : Expr '-' Expr",
"Expr : Expr '*' Expr",
"Expr : Expr '/' Expr",
"Expr : Expr '%' Expr",
"Expr : Expr EQUAL Expr",
"Expr : Expr NOT_EQUAL Expr",
"Expr : Expr '<' Expr",
"Expr : Expr '>' Expr",
"Expr : Expr LESS_EQUAL Expr",
"Expr : Expr GREATER_EQUAL Expr",
"Expr : Expr AND Expr",
"Expr : Expr OR Expr",
"Expr : '(' Expr ')'",
"Expr : '-' Expr",
"Expr : '!' Expr",
"Expr : '@' Expr",
"Expr : '$' Expr",
"Expr : '#' Expr",
"Expr : READ_INTEGER '(' ')'",
"Expr : READ_LINE '(' ')'",
"Expr : THIS",
"Expr : SUPER",
"Expr : DCOPY '(' Expr ')'",
"Expr : SCOPY '(' Expr ')'",
"Expr : NEW IDENTIFIER '(' ')'",
"Expr : NEW Type '[' Expr ']'",
"Expr : INSTANCEOF '(' Expr ',' IDENTIFIER ')'",
"Expr : '(' CLASS IDENTIFIER ')' Expr",
"Expr : CASE '(' Expr ')' '{' ACaseExprList DefaultExpr '}'",
"ACaseExprList : ACaseExprList ACaseExpr",
"ACaseExprList :",
"ACaseExpr : Constant ':' Expr ';'",
"DefaultExpr : DEFAULT ':' Expr ';'",
"Constant : LITERAL",
"Constant : NULL",
"Actuals : ExprList",
"Actuals :",
"ExprList : ExprList ',' Expr",
"ExprList : Expr",
"PrintCompStmt : PRINT_COMP '(' ExprList ')'",
"WhileStmt : WHILE '(' Expr ')' Stmt",
"ForStmt : FOR '(' SimpleStmt ';' Expr ';' SimpleStmt ')' Stmt",
"BreakStmt : BREAK",
"IfStmt : IF '(' Expr ')' Stmt ElseClause",
"ElseClause : ELSE Stmt",
"ElseClause :",
"ReturnStmt : RETURN Expr",
"ReturnStmt : RETURN",
"PrintStmt : PRINT '(' ExprList ')'",
};

//#line 510 "Parser.y"
    
	/**
	 * 打印当前归约所用的语法规则<br>
	 * 请勿修改。
	 */
    public boolean onReduce(String rule) {
		if (rule.startsWith("$$"))
			return false;
		else
			rule = rule.replaceAll(" \\$\\$\\d+", "");

   	    if (rule.endsWith(":"))
    	    System.out.println(rule + " <empty>");
   	    else
			System.out.println(rule);
		return false;
    }
    
    public void diagnose() {
		addReduceListener(this);
		yyparse();
	}
//#line 726 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    //if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      //if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        //if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        //if (yychar < 0)    //it it didn't work/error
        //  {
        //  yychar = 0;      //change it to default string (no -1!)
          //if (yydebug)
          //  yylexdebug(yystate,yychar);
        //  }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        //if (yydebug)
          //debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      //if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0 || valptr<0)   //check for under & overflow here
            {
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            //if (yydebug)
              //debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            //if (yydebug)
              //debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0 || valptr<0)   //check for under & overflow here
              {
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        //if (yydebug)
          //{
          //yys = null;
          //if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          //if (yys == null) yys = "illegal-symbol";
          //debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          //}
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    //if (yydebug)
      //debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    if (reduceListener == null || reduceListener.onReduce(yyrule[yyn])) // if intercepted!
      switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 54 "Parser.y"
{
						tree = new Tree.TopLevel(val_peek(0).clist, val_peek(0).loc);
					}
break;
case 2:
//#line 60 "Parser.y"
{
						yyval.clist.add(val_peek(0).cdef);
					}
break;
case 3:
//#line 64 "Parser.y"
{
                		yyval.clist = new ArrayList<Tree.ClassDef>();
                		yyval.clist.add(val_peek(0).cdef);
                	}
break;
case 5:
//#line 74 "Parser.y"
{
						yyval.vdef = new Tree.VarDef(val_peek(0).ident, val_peek(1).type, val_peek(0).loc);
					}
break;
case 6:
//#line 80 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.INT, val_peek(0).loc);
					}
break;
case 7:
//#line 84 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.VOID, val_peek(0).loc);
                	}
break;
case 8:
//#line 88 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.BOOL, val_peek(0).loc);
                	}
break;
case 9:
//#line 92 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.COMPLEX, val_peek(0).loc);
                	}
break;
case 10:
//#line 96 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.STRING, val_peek(0).loc);
                	}
break;
case 11:
//#line 100 "Parser.y"
{
                		yyval.type = new Tree.TypeClass(val_peek(0).ident, val_peek(1).loc);
                	}
break;
case 12:
//#line 104 "Parser.y"
{
                		yyval.type = new Tree.TypeArray(val_peek(2).type, val_peek(2).loc);
                	}
break;
case 13:
//#line 110 "Parser.y"
{
						yyval.cdef = new Tree.ClassDef(val_peek(4).ident, val_peek(3).ident, val_peek(1).flist, val_peek(5).loc);
					}
break;
case 14:
//#line 116 "Parser.y"
{
						yyval.ident = val_peek(0).ident;
					}
break;
case 15:
//#line 120 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 16:
//#line 126 "Parser.y"
{
						yyval.flist.add(val_peek(0).vdef);
					}
break;
case 17:
//#line 130 "Parser.y"
{
						yyval.flist.add(val_peek(0).fdef);
					}
break;
case 18:
//#line 134 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.flist = new ArrayList<Tree>();
                	}
break;
case 20:
//#line 142 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.vlist = new ArrayList<Tree.VarDef>(); 
                	}
break;
case 21:
//#line 149 "Parser.y"
{
						yyval.vlist.add(val_peek(0).vdef);
					}
break;
case 22:
//#line 153 "Parser.y"
{
                		yyval.vlist = new ArrayList<Tree.VarDef>();
						yyval.vlist.add(val_peek(0).vdef);
                	}
break;
case 23:
//#line 160 "Parser.y"
{
						yyval.fdef = new MethodDef(true, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 24:
//#line 164 "Parser.y"
{
						yyval.fdef = new MethodDef(false, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 25:
//#line 170 "Parser.y"
{
						yyval.stmt = new Block(val_peek(1).slist, val_peek(2).loc);
					}
break;
case 26:
//#line 176 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 27:
//#line 180 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.slist = new ArrayList<Tree>();
                	}
break;
case 28:
//#line 187 "Parser.y"
{
						yyval.stmt = val_peek(0).vdef;
					}
break;
case 29:
//#line 192 "Parser.y"
{
                		if (yyval.stmt == null) {
                			yyval.stmt = new Tree.Skip(val_peek(0).loc);
                		}
                	}
break;
case 39:
//#line 209 "Parser.y"
{
						yyval.stmt = new Tree.DoStmt(val_peek(2).dslist, val_peek(3).loc);
					}
break;
case 40:
//#line 215 "Parser.y"
{
						yyval.dslist.add(val_peek(0).dstmt);
					}
break;
case 41:
//#line 219 "Parser.y"
{
						yyval.dslist = new ArrayList<Tree.DoSubStmt>();
						yyval.dslist.add(val_peek(0).dstmt);
					}
break;
case 42:
//#line 226 "Parser.y"
{
						yyval.dstmt = new Tree.DoSubStmt(val_peek(2).expr, val_peek(0).stmt, val_peek(2).loc);
					}
break;
case 43:
//#line 232 "Parser.y"
{
						yyval.stmt = new Tree.Assign(val_peek(2).lvalue, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 44:
//#line 236 "Parser.y"
{
                		yyval.stmt = new Tree.Exec(val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 45:
//#line 240 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 47:
//#line 247 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 48:
//#line 253 "Parser.y"
{
						yyval.lvalue = new Tree.Ident(val_peek(1).expr, val_peek(0).ident, val_peek(0).loc);
						if (val_peek(1).loc == null) {
							yyval.loc = val_peek(0).loc;
						}
					}
break;
case 49:
//#line 260 "Parser.y"
{
                		yyval.lvalue = new Tree.Indexed(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 50:
//#line 266 "Parser.y"
{
						yyval.expr = new Tree.CallExpr(val_peek(4).expr, val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
						if (val_peek(4).loc == null) {
							yyval.loc = val_peek(3).loc;
						}
					}
break;
case 51:
//#line 275 "Parser.y"
{
						yyval.expr = val_peek(0).lvalue;
					}
break;
case 54:
//#line 281 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 55:
//#line 285 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 56:
//#line 289 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 57:
//#line 293 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 58:
//#line 297 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 59:
//#line 301 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 60:
//#line 305 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 61:
//#line 309 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 62:
//#line 313 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 63:
//#line 317 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 64:
//#line 321 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 65:
//#line 325 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 66:
//#line 329 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 67:
//#line 333 "Parser.y"
{
                		yyval = val_peek(1);
                	}
break;
case 68:
//#line 337 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NEG, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 69:
//#line 341 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NOT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 70:
//#line 345 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.AT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 71:
//#line 349 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.DOLLAR, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 72:
//#line 353 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.SHARP, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 73:
//#line 357 "Parser.y"
{
                		yyval.expr = new Tree.ReadIntExpr(val_peek(2).loc);
                	}
break;
case 74:
//#line 361 "Parser.y"
{
                		yyval.expr = new Tree.ReadLineExpr(val_peek(2).loc);
                	}
break;
case 75:
//#line 365 "Parser.y"
{
                		yyval.expr = new Tree.ThisExpr(val_peek(0).loc);
                	}
break;
case 76:
//#line 369 "Parser.y"
{
                		yyval.expr = new Tree.SuperExpr(val_peek(0).loc);
                	}
break;
case 77:
//#line 373 "Parser.y"
{
                		yyval.expr = new Tree.DCopyExpr(val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 78:
//#line 377 "Parser.y"
{
                		yyval.expr = new Tree.SCopyExpr(val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 79:
//#line 381 "Parser.y"
{
                		yyval.expr = new Tree.NewClass(val_peek(2).ident, val_peek(3).loc);
                	}
break;
case 80:
//#line 385 "Parser.y"
{
                		yyval.expr = new Tree.NewArray(val_peek(3).type, val_peek(1).expr, val_peek(4).loc);
                	}
break;
case 81:
//#line 389 "Parser.y"
{
                		yyval.expr = new Tree.TypeTest(val_peek(3).expr, val_peek(1).ident, val_peek(5).loc);
                	}
break;
case 82:
//#line 393 "Parser.y"
{
                		yyval.expr = new Tree.TypeCast(val_peek(2).ident, val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 83:
//#line 397 "Parser.y"
{
                		yyval.expr = new Tree.CaseBlock(val_peek(5).expr, val_peek(2).celist, val_peek(1).expr, val_peek(7).loc);
                	}
break;
case 84:
//#line 403 "Parser.y"
{
						yyval.celist.add(val_peek(0).cexpr);
					}
break;
case 85:
//#line 407 "Parser.y"
{
                		yyval = new SemValue();
						yyval.celist = new ArrayList<Tree.CaseExpr>();
                	}
break;
case 86:
//#line 414 "Parser.y"
{
						yyval.cexpr = new Tree.CaseExpr(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
					}
break;
case 87:
//#line 420 "Parser.y"
{
						yyval.expr = val_peek(1).expr;
					}
break;
case 88:
//#line 426 "Parser.y"
{
						yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
					}
break;
case 89:
//#line 430 "Parser.y"
{
						yyval.expr = new Null(val_peek(0).loc);
					}
break;
case 91:
//#line 437 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Tree.Expr>();
                	}
break;
case 92:
//#line 444 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 93:
//#line 448 "Parser.y"
{
                		yyval.elist = new ArrayList<Tree.Expr>();
						yyval.elist.add(val_peek(0).expr);
                	}
break;
case 94:
//#line 455 "Parser.y"
{
						yyval.stmt = new Tree.PrintComp(val_peek(1).elist, val_peek(3).loc);
					}
break;
case 95:
//#line 460 "Parser.y"
{
						yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 96:
//#line 466 "Parser.y"
{
						yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
					}
break;
case 97:
//#line 472 "Parser.y"
{
						yyval.stmt = new Tree.Break(val_peek(0).loc);
					}
break;
case 98:
//#line 478 "Parser.y"
{
						yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 99:
//#line 484 "Parser.y"
{
						yyval.stmt = val_peek(0).stmt;
					}
break;
case 100:
//#line 488 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 101:
//#line 494 "Parser.y"
{
						yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 102:
//#line 498 "Parser.y"
{
                		yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                	}
break;
case 103:
//#line 504 "Parser.y"
{
						yyval.stmt = new Print(val_peek(1).elist, val_peek(3).loc);
					}
break;
//#line 1417 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    //if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      //if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        //if (yychar<0) yychar=0;  //clean, if necessary
        //if (yydebug)
          //yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      //if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
