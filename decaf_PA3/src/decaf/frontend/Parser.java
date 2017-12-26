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
    0,    3,    3,    1,    0,    2,    0,    2,    4,    5,
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
    0,    0,    0,    0,   29,   33,   34,   35,   36,   37,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   46,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   73,   74,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   67,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   79,    0,    0,  103,   94,
    0,   78,   77,    0,   39,   42,    0,    0,   49,    0,
    0,   95,    0,    0,   80,    0,   85,   40,    0,   82,
   50,    0,    0,   98,    0,   81,    0,   99,    0,    0,
    0,   84,    0,    0,    0,   83,   96,    0,    0,   87,
   86,
};
final static short yydgoto[] = {                          2,
    3,    4,   73,   21,   34,    8,   11,   23,   35,   36,
   74,   46,   75,   76,   77,   78,   79,   80,   81,   82,
   83,   84,  105,  152,   85,   94,   95,   88,  190,   89,
  205,  211,  212,  144,  204,
};
final static short yysindex[] = {                      -245,
 -259,    0, -245,    0, -236,    0, -247,  -79,    0,    0,
  407,    0,    0,    0,    0, -239,    0,  -50,    0,    0,
   -9,  -90,    0,    0,  -86,    0,   19,  -25,   31,  -50,
    0,  -50,    0,  -85,   34,   10,   35,    0,  -45,  -50,
  -45,    0,    0,    0,    0,    2,    0,    0,   39,   41,
    0,   43,  291,    0, -153,   46,   47,   51,   75,    0,
   76,   77,   78,  291,   80,  291,  291,   88,    0,  291,
  291,  291,    0,    0,    0,   25,    0,    0,    0,   63,
   67,   70,   71,   72,  976,   74,    0, -155,    0,  291,
  291,  291,  976,    0,    0,   96,   48,  291,   97,   99,
  291,  291,  291,  291, -145,  538,  291,  -29,  -29, -138,
  560,  -29,  -29,  -29,    0,    0,    0,    0,    0,    0,
  291,  291,  291,  291,  291,  291,  291,  291,  291,  291,
  291,  291,  291,    0,  291,  291,  102,  571,   89,  593,
  109,  111,  976,  -14,    0,    0,    8,  737,  759,  830,
  291, -132,   37,  852,  113,    0, 1029, 1018,  -24,  -24,
 1040, 1040,    9,    9,  -29,  -29,  -29,  -24,  -24,  863,
  976,  291,   37,  291,   37,    0,  885,  291,    0,    0,
   32,    0,    0, -145,    0,    0, -129,  291,    0,  118,
  124,    0,  915, -102,    0,  976,    0,    0,  136,    0,
    0,  291,   37,    0, -240,    0,  140,    0,  125,  126,
   60,    0,   37,  291,  291,    0,    0,  926,  950,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  186,    0,   69,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  131,    0,    0,  152,
    0,  152,    0,    0,    0,  153,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -55,    0,    0,    0,    0,
    0,    0,  -39,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -91,    0,  -91,  -91,  -91,    0,  -91,
  -91,  -91,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  997,  499,    0,    0,  -91,
  -55,  -91,  139,    0,    0,    0,    0,  -91,    0,    0,
  -91,  -91,  -91,  -91,  -80,    0,  -91,  120,  129,    0,
    0,  388,  397,  424,    0,    0,    0,    0,    0,    0,
  -91,  -91,  -91,  -91,  -91,  -91,  -91,  -91,  -91,  -91,
  -91,  -91,  -91,    0,  -91,  -91,   52,    0,    0,    0,
    0,  -91,   16,    0,    0,    0,    0,    0,    0,    0,
  -91,    0,  -55,    0,    0,    0,  613,  162,   44,  528,
  615,  652, 1070, 1123,  433,  460,  469,  551,  907,    0,
  -26,  -31,  -55,  -91,  -55,    0,    0,  -91,    0,    0,
    0,    0,    0,  -80,    0,    0,    0,  -91,    0,    0,
  154,    0,    0,  -33,    0,   17,    0,    0,    0,    0,
    0,  -30,  -55,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -55,  -91,  -91,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,  196,  189,   13,   14,    0,    0,    0,  182,    0,
   24,    0, -139,  -83,    0,    0,    0,    0,    0,    0,
    0,    0,   64,   33, 1302, 1059, 1080,    0,    0,   11,
    0,    0,    0,  -92,    0,
};
final static int YYTABLESIZE=1517;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        100,
   28,  100,  100,   45,   28,   28,  100,  139,  147,   91,
   45,  100,  131,  186,   43,    1,  134,  129,  127,  102,
  128,  134,  130,   47,   22,  100,  179,    5,    7,  178,
  100,   25,   43,  192,   67,  194,   72,   71,   60,    9,
  209,   68,   33,   10,   33,  131,   66,   24,  180,   26,
  129,  178,   44,   40,  134,  130,   93,   92,   30,   93,
   92,  135,   43,  208,   45,   70,  135,   31,   97,   67,
   32,   72,   71,  217,   39,   41,   68,   42,   90,  191,
   91,   66,   92,  115,   63,   98,   99,   63,   48,  100,
  100,  100,   48,   48,   48,   48,   48,   48,   48,  135,
   70,   63,   63,   12,   13,   14,   15,   16,   17,   48,
   48,   48,   48,   48,  101,  102,  103,  104,  207,  107,
   67,  116,   72,   71,   42,  117,   69,   68,  118,  119,
  120,  137,   66,   96,  136,  141,   63,  145,  142,  146,
  151,  172,   48,   67,   48,   72,   71,  174,  155,  176,
   68,   70,  185,  188,  197,   66,   68,  199,  201,   42,
   68,   68,   68,   68,   68,   69,   68,  178,  203,   69,
   69,   69,   69,   69,   70,   69,  206,   68,   68,   68,
  213,   68,  214,  215,  216,    1,   69,   69,   69,    5,
   69,   15,   20,   19,   90,   47,   27,  101,    6,   20,
   29,   38,   66,   31,   41,   66,   12,   13,   14,   15,
   16,   17,   68,   37,  184,  210,  198,    0,    0,   66,
   66,   69,    0,  100,  100,  100,  100,  100,  100,    0,
  100,   47,  100,  100,  100,  100,  100,    0,  100,  100,
  100,  100,  100,  100,  100,  100,  100,   47,  100,  100,
  100,  100,  100,  100,   66,   47,   47,  100,   12,   13,
   14,   15,   16,   17,    0,   47,    0,   48,   49,   50,
   51,   52,    0,   53,   54,   55,   56,   57,   58,   59,
   60,   61,    0,   62,   63,   64,    0,    0,    0,    0,
    0,    0,   65,   12,   13,   14,   15,   16,   17,    0,
   47,    0,   48,   49,   50,   51,   52,    0,   53,   54,
   55,   56,   57,   58,   59,   60,   61,    0,   62,   63,
   64,    0,    0,   67,    0,   72,   71,   65,    0,    0,
   68,   63,   63,    0,    0,   66,    0,   63,   63,   48,
   48,    0,    0,   48,   48,   48,   48,    0,  110,    0,
    0,   47,    0,   48,   70,    0,   51,    0,    0,    0,
    0,   55,    0,   57,   58,    0,   60,   61,    0,   62,
   63,    0,    0,    0,   47,    0,   48,    0,   65,   51,
    0,    0,    0,    0,   55,    0,   57,   58,    0,   60,
   61,    0,   62,   63,    0,    0,    0,    0,    0,    0,
    0,   65,    0,    0,    0,    0,    0,   68,   68,    0,
    0,   68,   68,   68,   68,    0,   69,   69,    0,    0,
   69,   69,   69,   69,   70,    0,    0,    0,   70,   70,
   70,   70,   70,   71,   70,    0,    0,   71,   71,   71,
   71,   71,    0,   71,    0,   70,   70,   70,    0,   70,
   66,    0,    0,    0,   71,   71,   71,    0,   71,    0,
   72,    0,    0,    0,   72,   72,   72,   72,   72,   56,
   72,    0,    0,   56,   56,   56,   56,   56,    0,   56,
   70,   72,   72,   72,    0,   72,    0,    0,    0,   71,
   56,   56,   56,    0,   56,    0,   57,    0,    0,    0,
   57,   57,   57,   57,   57,   58,   57,    0,    0,   58,
   58,   58,   58,   58,    0,   58,   72,   57,   57,   57,
    0,   57,    0,    0,    0,   56,   58,   58,   58,    0,
   58,   19,    0,    0,    0,   52,    0,    0,    0,   44,
   52,   52,    0,   52,   52,   52,    0,    0,    0,    0,
    0,    0,   57,    0,   47,    0,   48,   44,   52,   51,
   52,   58,    0,    0,   55,    0,   57,   58,   64,   60,
   61,   64,   62,   63,  131,    0,    0,    0,    0,  129,
  127,   65,  128,  134,  130,   64,   64,    0,    0,   52,
    0,   62,    0,    0,   62,  153,  131,  133,    0,  132,
  156,  129,  127,    0,  128,  134,  130,  131,   62,   62,
    0,  173,  129,  127,    0,  128,  134,  130,    0,  133,
   64,  132,    0,    0,    0,    0,    0,    0,  135,  131,
  133,    0,  132,  175,  129,  127,    0,  128,  134,  130,
    0,    0,    0,   62,    0,    0,    0,    0,    0,    0,
  135,    0,  133,   65,  132,   59,   65,    0,   59,    0,
    0,  135,    0,   12,   13,   14,   15,   16,   17,    0,
   65,   65,   59,   59,    0,   70,   70,    0,    0,   70,
   70,   70,   70,  135,   71,   71,    0,    0,   71,   71,
   71,   71,   60,    0,    0,   60,   18,    0,    0,    0,
    0,    0,    0,    0,    0,   65,    0,   59,    0,   60,
   60,   72,   72,    0,    0,   72,   72,   72,   72,    0,
   56,   56,    0,    0,   56,   56,   56,   56,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   60,    0,    0,   57,   57,    0,
    0,   57,   57,   57,   57,    0,   58,   58,    0,    0,
   58,   58,   58,   58,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  131,    0,    0,    0,  181,  129,  127,
    0,  128,  134,  130,    0,    0,   52,   52,    0,    0,
   52,   52,   52,   52,    0,  131,  133,    0,  132,  182,
  129,  127,    0,  128,  134,  130,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   64,   64,    0,  133,    0,
  132,   64,   64,    0,    0,  121,  122,  135,    0,  123,
  124,  125,  126,    0,    0,    0,    0,    0,   62,   62,
    0,    0,    0,    0,   62,   62,    0,  121,  122,  135,
    0,  123,  124,  125,  126,    0,    0,    0,  121,  122,
    0,    0,  123,  124,  125,  126,  131,    0,    0,    0,
  183,  129,  127,    0,  128,  134,  130,    0,    0,    0,
  121,  122,    0,    0,  123,  124,  125,  126,  131,  133,
    0,  132,    0,  129,  127,  187,  128,  134,  130,  131,
   65,   65,   59,   59,  129,  127,    0,  128,  134,  130,
    0,  133,    0,  132,    0,    0,    0,    0,    0,    0,
  135,  131,  133,    0,  132,    0,  129,  127,    0,  128,
  134,  130,    0,    0,    0,    0,    0,    0,    0,   60,
   60,    0,  135,    0,  133,    0,  132,   61,    0,    0,
   61,  131,    0,  135,    0,  189,  129,  127,    0,  128,
  134,  130,  131,    0,   61,   61,    0,  129,  127,    0,
  128,  134,  130,  202,  133,  135,  132,  195,    0,    0,
    0,    0,    0,    0,  220,  133,  131,  132,    0,    0,
    0,  129,  127,    0,  128,  134,  130,    0,    0,   61,
    0,    0,    0,    0,    0,  135,    0,    0,  221,  133,
    0,  132,  131,    0,    0,    0,  135,  129,  127,    0,
  128,  134,  130,    0,  121,  122,    0,    0,  123,  124,
  125,  126,    0,   51,    0,  133,    0,  132,   51,   51,
  135,   51,   51,   51,    0,    0,  121,  122,    0,    0,
  123,  124,  125,  126,  131,    0,   51,    0,   51,  129,
  127,    0,  128,  134,  130,  131,  135,    0,    0,    0,
  129,  127,    0,  128,  134,  130,  131,  133,    0,  132,
    0,  129,  127,    0,  128,  134,  130,   51,  133,    0,
  132,    0,    0,    0,    0,    0,    0,    0,    0,  133,
    0,  132,    0,    0,   86,    0,    0,    0,  135,    0,
   54,    0,   54,   54,   54,    0,    0,  121,  122,  135,
    0,  123,  124,  125,  126,   87,    0,   54,   54,   54,
  135,   54,    0,    0,    0,    0,    0,    0,    0,  121,
  122,    0,    0,  123,  124,  125,  126,    0,    0,   86,
  121,  122,    0,    0,  123,  124,  125,  126,    0,    0,
    0,    0,   54,   55,    0,   55,   55,   55,    0,    0,
   87,    0,  121,  122,    0,    0,  123,  124,  125,  126,
   55,   55,   55,    0,   55,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   61,   61,    0,    0,    0,    0,
   61,   61,  121,  122,    0,    0,  123,  124,  125,  126,
    0,   86,    0,  121,  122,   55,    0,  123,  124,  125,
  126,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   86,   87,   86,    0,    0,    0,  121,  122,    0,
    0,  123,  124,  125,  126,    0,    0,    0,    0,    0,
    0,    0,   87,    0,   87,    0,    0,    0,    0,    0,
   86,   86,    0,  121,  122,    0,    0,  123,  124,  125,
  126,   86,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   87,   87,    0,   51,   51,    0,    0,   51,   51,
   51,   51,   87,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  121,    0,    0,    0,  123,
  124,  125,  126,    0,    0,    0,    0,    0,    0,    0,
  123,  124,  125,  126,    0,    0,    0,    0,    0,    0,
    0,  123,  124,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   93,    0,    0,   54,   54,    0,
    0,   54,   54,   54,   54,  106,    0,  108,  109,  111,
    0,  112,  113,  114,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  138,    0,  140,    0,    0,    0,    0,    0,  143,
    0,    0,  143,  148,  149,  150,    0,    0,  154,    0,
   55,   55,    0,    0,   55,   55,   55,   55,    0,    0,
    0,    0,  157,  158,  159,  160,  161,  162,  163,  164,
  165,  166,  167,  168,  169,    0,  170,  171,    0,    0,
    0,    0,    0,  177,    0,    0,    0,    0,    0,    0,
    0,    0,  106,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  143,    0,  193,    0,    0,    0,  196,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  200,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  218,  219,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   91,   35,   36,   59,   91,   91,   40,   91,  101,   41,
   41,   45,   37,  153,   41,  261,   46,   42,   43,   59,
   45,   46,   47,  264,   11,   59,   41,  287,  265,   44,
   64,   18,   59,  173,   33,  175,   35,   36,  279,  287,
  281,   40,   30,  123,   32,   37,   45,  287,   41,   59,
   42,   44,   40,   44,   46,   47,   41,   41,   40,   44,
   44,   91,   39,  203,   41,   64,   91,   93,   55,   33,
   40,   35,   36,  213,   41,   41,   40,  123,   40,  172,
   40,   45,   40,   59,   41,   40,   40,   44,   37,  123,
   40,  125,   41,   42,   43,   44,   45,   46,   47,   91,
   64,   58,   59,  257,  258,  259,  260,  261,  262,   58,
   59,   60,   61,   62,   40,   40,   40,   40,  202,   40,
   33,   59,   35,   36,  123,   59,  125,   40,   59,   59,
   59,  287,   45,  287,   61,   40,   93,   41,   91,   41,
  286,   40,   91,   33,   93,   35,   36,   59,  287,   41,
   40,   64,  285,   41,  123,   45,   37,  287,   41,  123,
   41,   42,   43,   44,   45,   37,   47,   44,  271,   41,
   42,   43,   44,   45,   64,   47,   41,   58,   59,   60,
   41,   62,   58,   58,  125,    0,   58,   59,   60,   59,
   62,  123,   41,   41,   41,  287,  287,   59,    3,   11,
  287,  287,   41,   93,  285,   44,  257,  258,  259,  260,
  261,  262,   93,   32,  151,  205,  184,   -1,   -1,   58,
   59,   93,   -1,  257,  258,  259,  260,  261,  262,   -1,
  264,  287,  266,  267,  268,  269,  270,   -1,  272,  273,
  274,  275,  276,  277,  278,  279,  280,  287,  282,  283,
  284,  285,  286,  287,   93,  287,  287,  291,  257,  258,
  259,  260,  261,  262,   -1,  264,   -1,  266,  267,  268,
  269,  270,   -1,  272,  273,  274,  275,  276,  277,  278,
  279,  280,   -1,  282,  283,  284,   -1,   -1,   -1,   -1,
   -1,   -1,  291,  257,  258,  259,  260,  261,  262,   -1,
  264,   -1,  266,  267,  268,  269,  270,   -1,  272,  273,
  274,  275,  276,  277,  278,  279,  280,   -1,  282,  283,
  284,   -1,   -1,   33,   -1,   35,   36,  291,   -1,   -1,
   40,  288,  289,   -1,   -1,   45,   -1,  294,  295,  288,
  289,   -1,   -1,  292,  293,  294,  295,   -1,  261,   -1,
   -1,  264,   -1,  266,   64,   -1,  269,   -1,   -1,   -1,
   -1,  274,   -1,  276,  277,   -1,  279,  280,   -1,  282,
  283,   -1,   -1,   -1,  264,   -1,  266,   -1,  291,  269,
   -1,   -1,   -1,   -1,  274,   -1,  276,  277,   -1,  279,
  280,   -1,  282,  283,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  291,   -1,   -1,   -1,   -1,   -1,  288,  289,   -1,
   -1,  292,  293,  294,  295,   -1,  288,  289,   -1,   -1,
  292,  293,  294,  295,   37,   -1,   -1,   -1,   41,   42,
   43,   44,   45,   37,   47,   -1,   -1,   41,   42,   43,
   44,   45,   -1,   47,   -1,   58,   59,   60,   -1,   62,
  289,   -1,   -1,   -1,   58,   59,   60,   -1,   62,   -1,
   37,   -1,   -1,   -1,   41,   42,   43,   44,   45,   37,
   47,   -1,   -1,   41,   42,   43,   44,   45,   -1,   47,
   93,   58,   59,   60,   -1,   62,   -1,   -1,   -1,   93,
   58,   59,   60,   -1,   62,   -1,   37,   -1,   -1,   -1,
   41,   42,   43,   44,   45,   37,   47,   -1,   -1,   41,
   42,   43,   44,   45,   -1,   47,   93,   58,   59,   60,
   -1,   62,   -1,   -1,   -1,   93,   58,   59,   60,   -1,
   62,  125,   -1,   -1,   -1,   37,   -1,   -1,   -1,   41,
   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,
   -1,   -1,   93,   -1,  264,   -1,  266,   59,   60,  269,
   62,   93,   -1,   -1,  274,   -1,  276,  277,   41,  279,
  280,   44,  282,  283,   37,   -1,   -1,   -1,   -1,   42,
   43,  291,   45,   46,   47,   58,   59,   -1,   -1,   91,
   -1,   41,   -1,   -1,   44,   58,   37,   60,   -1,   62,
   41,   42,   43,   -1,   45,   46,   47,   37,   58,   59,
   -1,   41,   42,   43,   -1,   45,   46,   47,   -1,   60,
   93,   62,   -1,   -1,   -1,   -1,   -1,   -1,   91,   37,
   60,   -1,   62,   41,   42,   43,   -1,   45,   46,   47,
   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,   -1,   -1,
   91,   -1,   60,   41,   62,   41,   44,   -1,   44,   -1,
   -1,   91,   -1,  257,  258,  259,  260,  261,  262,   -1,
   58,   59,   58,   59,   -1,  288,  289,   -1,   -1,  292,
  293,  294,  295,   91,  288,  289,   -1,   -1,  292,  293,
  294,  295,   41,   -1,   -1,   44,  290,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   93,   -1,   93,   -1,   58,
   59,  288,  289,   -1,   -1,  292,  293,  294,  295,   -1,
  288,  289,   -1,   -1,  292,  293,  294,  295,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   93,   -1,   -1,  288,  289,   -1,
   -1,  292,  293,  294,  295,   -1,  288,  289,   -1,   -1,
  292,  293,  294,  295,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   37,   -1,   -1,   -1,   41,   42,   43,
   -1,   45,   46,   47,   -1,   -1,  288,  289,   -1,   -1,
  292,  293,  294,  295,   -1,   37,   60,   -1,   62,   41,
   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  288,  289,   -1,   60,   -1,
   62,  294,  295,   -1,   -1,  288,  289,   91,   -1,  292,
  293,  294,  295,   -1,   -1,   -1,   -1,   -1,  288,  289,
   -1,   -1,   -1,   -1,  294,  295,   -1,  288,  289,   91,
   -1,  292,  293,  294,  295,   -1,   -1,   -1,  288,  289,
   -1,   -1,  292,  293,  294,  295,   37,   -1,   -1,   -1,
   41,   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,
  288,  289,   -1,   -1,  292,  293,  294,  295,   37,   60,
   -1,   62,   -1,   42,   43,   44,   45,   46,   47,   37,
  288,  289,  288,  289,   42,   43,   -1,   45,   46,   47,
   -1,   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,   -1,
   91,   37,   60,   -1,   62,   -1,   42,   43,   -1,   45,
   46,   47,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  288,
  289,   -1,   91,   -1,   60,   -1,   62,   41,   -1,   -1,
   44,   37,   -1,   91,   -1,   93,   42,   43,   -1,   45,
   46,   47,   37,   -1,   58,   59,   -1,   42,   43,   -1,
   45,   46,   47,   59,   60,   91,   62,   93,   -1,   -1,
   -1,   -1,   -1,   -1,   59,   60,   37,   62,   -1,   -1,
   -1,   42,   43,   -1,   45,   46,   47,   -1,   -1,   93,
   -1,   -1,   -1,   -1,   -1,   91,   -1,   -1,   59,   60,
   -1,   62,   37,   -1,   -1,   -1,   91,   42,   43,   -1,
   45,   46,   47,   -1,  288,  289,   -1,   -1,  292,  293,
  294,  295,   -1,   37,   -1,   60,   -1,   62,   42,   43,
   91,   45,   46,   47,   -1,   -1,  288,  289,   -1,   -1,
  292,  293,  294,  295,   37,   -1,   60,   -1,   62,   42,
   43,   -1,   45,   46,   47,   37,   91,   -1,   -1,   -1,
   42,   43,   -1,   45,   46,   47,   37,   60,   -1,   62,
   -1,   42,   43,   -1,   45,   46,   47,   91,   60,   -1,
   62,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   60,
   -1,   62,   -1,   -1,   46,   -1,   -1,   -1,   91,   -1,
   41,   -1,   43,   44,   45,   -1,   -1,  288,  289,   91,
   -1,  292,  293,  294,  295,   46,   -1,   58,   59,   60,
   91,   62,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  288,
  289,   -1,   -1,  292,  293,  294,  295,   -1,   -1,   91,
  288,  289,   -1,   -1,  292,  293,  294,  295,   -1,   -1,
   -1,   -1,   93,   41,   -1,   43,   44,   45,   -1,   -1,
   91,   -1,  288,  289,   -1,   -1,  292,  293,  294,  295,
   58,   59,   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  288,  289,   -1,   -1,   -1,   -1,
  294,  295,  288,  289,   -1,   -1,  292,  293,  294,  295,
   -1,  153,   -1,  288,  289,   93,   -1,  292,  293,  294,
  295,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  173,  153,  175,   -1,   -1,   -1,  288,  289,   -1,
   -1,  292,  293,  294,  295,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  173,   -1,  175,   -1,   -1,   -1,   -1,   -1,
  202,  203,   -1,  288,  289,   -1,   -1,  292,  293,  294,
  295,  213,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  202,  203,   -1,  288,  289,   -1,   -1,  292,  293,
  294,  295,  213,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  288,   -1,   -1,   -1,  292,
  293,  294,  295,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  292,  293,  294,  295,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  292,  293,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   53,   -1,   -1,  288,  289,   -1,
   -1,  292,  293,  294,  295,   64,   -1,   66,   67,   68,
   -1,   70,   71,   72,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   90,   -1,   92,   -1,   -1,   -1,   -1,   -1,   98,
   -1,   -1,  101,  102,  103,  104,   -1,   -1,  107,   -1,
  288,  289,   -1,   -1,  292,  293,  294,  295,   -1,   -1,
   -1,   -1,  121,  122,  123,  124,  125,  126,  127,  128,
  129,  130,  131,  132,  133,   -1,  135,  136,   -1,   -1,
   -1,   -1,   -1,  142,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  151,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  172,   -1,  174,   -1,   -1,   -1,  178,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  188,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  214,  215,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=297;
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
"NOT_EQUAL","UMINUS","EMPTY",
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
"DoStmt : DO DoSubStmt DoBranchList OD",
"DoBranchList : BRANCH DoSubStmt DoBranchList",
"DoBranchList :",
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

//#line 511 "Parser.y"
    
	/**
	 * 鎵撳嵃褰撳墠褰掔害鎵�鐢ㄧ殑璇硶瑙勫垯<br>
	 * 璇峰嬁淇敼銆�
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
//#line 732 "Parser.java"
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
                        yyval.stmt = new Tree.DoStmt(val_peek(2).dstmt, val_peek(1).dslist, val_peek(3).loc);
                    }
break;
case 40:
//#line 215 "Parser.y"
{
						yyval.dslist = new ArrayList<Tree.DoSubStmt>();
						yyval.dslist.add(val_peek(1).dstmt);
						yyval.dslist.addAll(val_peek(0).dslist);
					}
break;
case 41:
//#line 221 "Parser.y"
{
						yyval.dslist = new ArrayList<Tree.DoSubStmt>();
					}
break;
case 42:
//#line 227 "Parser.y"
{
						yyval.dstmt = new Tree.DoSubStmt(val_peek(2).expr, val_peek(0).stmt, val_peek(2).loc);
					}
break;
case 43:
//#line 233 "Parser.y"
{
						yyval.stmt = new Tree.Assign(val_peek(2).lvalue, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 44:
//#line 237 "Parser.y"
{
                		yyval.stmt = new Tree.Exec(val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 45:
//#line 241 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 47:
//#line 248 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 48:
//#line 254 "Parser.y"
{
						yyval.lvalue = new Tree.Ident(val_peek(1).expr, val_peek(0).ident, val_peek(0).loc);
						if (val_peek(1).loc == null) {
							yyval.loc = val_peek(0).loc;
						}
					}
break;
case 49:
//#line 261 "Parser.y"
{
                		yyval.lvalue = new Tree.Indexed(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 50:
//#line 267 "Parser.y"
{
						yyval.expr = new Tree.CallExpr(val_peek(4).expr, val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
						if (val_peek(4).loc == null) {
							yyval.loc = val_peek(3).loc;
						}
					}
break;
case 51:
//#line 276 "Parser.y"
{
						yyval.expr = val_peek(0).lvalue;
					}
break;
case 54:
//#line 282 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 55:
//#line 286 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 56:
//#line 290 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 57:
//#line 294 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 58:
//#line 298 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 59:
//#line 302 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 60:
//#line 306 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 61:
//#line 310 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 62:
//#line 314 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 63:
//#line 318 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 64:
//#line 322 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 65:
//#line 326 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 66:
//#line 330 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 67:
//#line 334 "Parser.y"
{
                		yyval = val_peek(1);
                	}
break;
case 68:
//#line 338 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NEG, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 69:
//#line 342 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NOT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 70:
//#line 346 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.AT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 71:
//#line 350 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.DOLLAR, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 72:
//#line 354 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.SHARP, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 73:
//#line 358 "Parser.y"
{
                		yyval.expr = new Tree.ReadIntExpr(val_peek(2).loc);
                	}
break;
case 74:
//#line 362 "Parser.y"
{
                		yyval.expr = new Tree.ReadLineExpr(val_peek(2).loc);
                	}
break;
case 75:
//#line 366 "Parser.y"
{
                		yyval.expr = new Tree.ThisExpr(val_peek(0).loc);
                	}
break;
case 76:
//#line 370 "Parser.y"
{
                		yyval.expr = new Tree.SuperExpr(val_peek(0).loc);
                	}
break;
case 77:
//#line 374 "Parser.y"
{
                		yyval.expr = new Tree.DCopyExpr(val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 78:
//#line 378 "Parser.y"
{
                		yyval.expr = new Tree.SCopyExpr(val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 79:
//#line 382 "Parser.y"
{
                		yyval.expr = new Tree.NewClass(val_peek(2).ident, val_peek(3).loc);
                	}
break;
case 80:
//#line 386 "Parser.y"
{
                		yyval.expr = new Tree.NewArray(val_peek(3).type, val_peek(1).expr, val_peek(4).loc);
                	}
break;
case 81:
//#line 390 "Parser.y"
{
                		yyval.expr = new Tree.TypeTest(val_peek(3).expr, val_peek(1).ident, val_peek(5).loc);
                	}
break;
case 82:
//#line 394 "Parser.y"
{
                		yyval.expr = new Tree.TypeCast(val_peek(2).ident, val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 83:
//#line 398 "Parser.y"
{
                		yyval.expr = new Tree.CaseBlock(val_peek(5).expr, val_peek(2).celist, val_peek(1).expr, val_peek(7).loc);
                	}
break;
case 84:
//#line 404 "Parser.y"
{
						yyval.celist.add(val_peek(0).cexpr);
					}
break;
case 85:
//#line 408 "Parser.y"
{
                		yyval = new SemValue();
						yyval.celist = new ArrayList<Tree.CaseExpr>();
                	}
break;
case 86:
//#line 415 "Parser.y"
{
						yyval.cexpr = new Tree.CaseExpr((Tree.Literal)val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
					}
break;
case 87:
//#line 421 "Parser.y"
{
						yyval.expr = val_peek(1).expr;
					}
break;
case 88:
//#line 427 "Parser.y"
{
						yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
					}
break;
case 89:
//#line 431 "Parser.y"
{
						yyval.expr = new Null(val_peek(0).loc);
					}
break;
case 91:
//#line 438 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Tree.Expr>();
                	}
break;
case 92:
//#line 445 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 93:
//#line 449 "Parser.y"
{
                		yyval.elist = new ArrayList<Tree.Expr>();
						yyval.elist.add(val_peek(0).expr);
                	}
break;
case 94:
//#line 456 "Parser.y"
{
						yyval.stmt = new Tree.PrintComp(val_peek(1).elist, val_peek(3).loc);
					}
break;
case 95:
//#line 461 "Parser.y"
{
						yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 96:
//#line 467 "Parser.y"
{
						yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
					}
break;
case 97:
//#line 473 "Parser.y"
{
						yyval.stmt = new Tree.Break(val_peek(0).loc);
					}
break;
case 98:
//#line 479 "Parser.y"
{
						yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 99:
//#line 485 "Parser.y"
{
						yyval.stmt = val_peek(0).stmt;
					}
break;
case 100:
//#line 489 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 101:
//#line 495 "Parser.y"
{
						yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 102:
//#line 499 "Parser.y"
{
                		yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                	}
break;
case 103:
//#line 505 "Parser.y"
{
						yyval.stmt = new Print(val_peek(1).elist, val_peek(3).loc);
					}
break;
//#line 1424 "Parser.java"
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
