/*
 * 鏈枃浠舵槸鏋勯�燚ecaf缂栬瘧鍣ㄦ墍闇�瑕佺殑JFlex杈撳叆鑴氭湰銆�
 * 鍦ㄧ涓�闃舵锛屼綘闇�瑕佸畬鎴愯繖涓剼鏈殑鍐呭锛岃鍙傝��"JFlex Manual"涓叧浜庡浣曠紪鍐橨Flex鑴氭湰鐨勮鏄庛��
 *
 * 娉ㄦ剰锛氬湪UNIX绯荤粺涓嬩綘闇�瑕佷繚璇佽繖涓枃浠朵娇鐢║NIX鏂囨湰鏍煎紡锛屽彲浣跨敤dos2unix鍛戒护杩涜鏂囨湰鍚勫紡杞崲銆�
 */

package decaf.frontend;

import decaf.Location;
import decaf.error.*;
import decaf.utils.MiscUtils;
 
%%
%public
%class Lexer
%extends BaseLexer
%byaccj
%line
%column
%switch
%unicode
	
%{
	private Location sloc = null;
	private StringBuilder buffer = new StringBuilder();

	public Location getLocation() {
		return new decaf.Location(yyline + 1, yycolumn + 1);
	}
%}

NEWLINE				= (\r|\n|\r\n)
DIGIT 				= ([0-9])
HEX_DIGIT 			= ([0-9A-Fa-f])
HEX_INTEGER			= (0[Xx]{HEX_DIGIT}+)
DEC_INTEGER			= ({DIGIT}+)
INTEGER				= ({HEX_INTEGER}|{DEC_INTEGER})
IMGPART				= ({INTEGER}j)
IDENTIFIER			= ([A-Za-z][_0-9A-Za-z]*)
SIMPLE_OPERATOR		= ("+"|"-"|"*"|"/"|"%"|"="|"<"|">"|"."|","|";"|"!"|"("|")"|"["|"]"|"{"|"}"|"@"|"$"|"#"|":")
S_COMMENT			= ("//"[^\r\n]*{NEWLINE})
WHITESPACE			= ([ \t]+)

	// 寮�濮嬫潯浠禨琛ㄧず瀛楃涓�
%x S

%%
	// 璇嗗埆娉ㄩ噴鍜岀┖鐧藉瓧绗︾殑瑙勫垯
{WHITESPACE}		{ /* Just ignore */    }
{NEWLINE}			{ /* Just ignore */    }
{S_COMMENT}			{ /* Just ignore */    }


	// 璇嗗埆鍏抽敭瀛楃殑瑙勫垯 
"void"				{ return keyword(Parser.VOID);			}
"int"				{ return keyword(Parser.INT);			}
"bool"				{ return keyword(Parser.BOOL);			}
"string"			{ return keyword(Parser.STRING);		}
"complex"			{ return keyword(Parser.COMPLEX);		}
"new"				{ return keyword(Parser.NEW);			}
"null"				{ return keyword(Parser.NULL);			}
"class"				{ return keyword(Parser.CLASS);			}
"extends"			{ return keyword(Parser.EXTENDS);		}
"this"				{ return keyword(Parser.THIS);			}
"super"				{ return keyword(Parser.SUPER);			}
"dcopy"				{ return keyword(Parser.DCOPY);			}
"scopy"				{ return keyword(Parser.SCOPY);			}
"while"				{ return keyword(Parser.WHILE);			}
"for"				{ return keyword(Parser.FOR);			}
"if"				{ return keyword(Parser.IF);			}
"do"				{ return keyword(Parser.DO);			}
"od"				{ return keyword(Parser.OD);			}
"else"				{ return keyword(Parser.ELSE);			}
"return"			{ return keyword(Parser.RETURN);		}
"break"				{ return keyword(Parser.BREAK);			}
"Print"				{ return keyword(Parser.PRINT);			}
"ReadInteger"		{ return keyword(Parser.READ_INTEGER);	}
"ReadLine"			{ return keyword(Parser.READ_LINE);		}
"static"			{ return keyword(Parser.STATIC);		}
"instanceof"		{ return keyword(Parser.INSTANCEOF);	}
"PrintComp"			{ return keyword(Parser.PRINT_COMP);	}
"case"				{ return keyword(Parser.CASE);			}
"default"			{ return keyword(Parser.DEFAULT);		}

	// 璇嗗埆鎿嶄綔绗︾殑瑙勫垯
"<="				{ return operator(Parser.LESS_EQUAL);	}
">="				{ return operator(Parser.GREATER_EQUAL);}
"=="				{ return operator(Parser.EQUAL);		}
"!="				{ return operator(Parser.NOT_EQUAL);	}
"&&"				{ return operator(Parser.AND);			}
"||"				{ return operator(Parser.OR);			}
"|||"				{ return operator(Parser.BRANCH);		}
{SIMPLE_OPERATOR}	{ return operator((int)yycharat(0));	}

	// 璇嗗埆甯告暟鐨勮鍒�
"true"				{ return boolConst(true);										}
"false"				{ return boolConst(false);										}
{INTEGER}			{ return intConst(yytext());			}
{IMGPART}			{ return imgConst(yytext());			}
<YYINITIAL>\"		{ sloc = getLocation();		  
					  yybegin(S);
					  buffer = new StringBuilder();								    }
<S>{NEWLINE}		{ issueError(new NewlineInStrError(sloc, MiscUtils.quote(buffer.toString())));}
<S><<EOF>>			{ issueError(new UntermStrError(sloc, MiscUtils.quote(buffer.toString())));
					  yybegin(YYINITIAL);											}
<S>\"				{ yybegin(YYINITIAL);
					  return StringConst(buffer.toString(), sloc);						}
<S>"\\n"			{ buffer.append('\n');											}
<S>"\\t"			{ buffer.append('\t'); 											}
<S>"\\\""			{ buffer.append('"');											}
<S>"\\\\"			{ buffer.append('\\'); 											}
<S>.				{ buffer.append(yytext()); 										}

	// 璇嗗埆鏍囪瘑绗︾殑瑙勫垯
{IDENTIFIER}		{ return identifier(yytext());			}
	
	// 涓婇潰瑙勫垯涓嶈兘璇嗗埆鐨勫瓧绗︽�庝箞澶勭悊
.					{ issueError(new UnrecogCharError(getLocation(), yycharat(0))); 		}
