package decaf.frontend;

import java.util.List;

import decaf.Location;
import decaf.tree.Tree;
import decaf.tree.Tree.CaseExpr;
import decaf.tree.Tree.ClassDef;
import decaf.tree.Tree.DoSubStmt;
import decaf.tree.Tree.Expr;
import decaf.tree.Tree.MethodDef;
import decaf.tree.Tree.LValue;
import decaf.tree.Tree.TopLevel;
import decaf.tree.Tree.VarDef;
import decaf.tree.Tree.TypeLiteral;
import decaf.utils.MiscUtils;

public class SemValue {

	public int code;

	public Location loc;

	public int typeTag;
	
	public Object literal;
	
	public String ident;

	public List<ClassDef> clist;

	/**
	 * field list
	 */
	public List<Tree> flist;

	public List<VarDef> vlist;


	/**
	 * statement list
	 */
	public List<Tree> slist;

	public List<Expr> elist;
	
	public List<CaseExpr> celist;

	public List<DoSubStmt> dslist;

	public TopLevel prog;

	public ClassDef cdef;

	public VarDef vdef;

	public MethodDef fdef;

	public TypeLiteral type;

	public Tree stmt;

	public Expr expr;
	
	public CaseExpr cexpr;
	
	public DoSubStmt dstmt;

	public LValue lvalue;

	/**
	 * 鍒涘缓涓�涓叧閿瓧鐨勮涔夊��
	 * 
	 * @param code
	 *            鍏抽敭瀛楃殑浠ｈ〃鐮�
	 * @return 瀵瑰簲鍏抽敭瀛楃殑璇箟鍊�
	 */
	public static SemValue createKeyword(int code) {
		SemValue v = new SemValue();
		v.code = code;
		return v;
	}

	/**
	 * 鍒涘缓涓�涓搷浣滅鐨勮涔夊��
	 * 
	 * @param code
	 *            鎿嶄綔绗︾殑浠ｈ〃鐮�
	 * @return 瀵瑰簲鎿嶄綔绗︾殑璇箟鍊�
	 */
	public static SemValue createOperator(int code) {
		SemValue v = new SemValue();
		v.code = code;
		return v;
	}

	/**
	 * 鍒涘缓涓�涓父閲忕殑璇箟鍊�
	 * 
	 * @param value
	 *            甯搁噺鐨勫��
	 * @return 瀵瑰簲鐨勮涔夊��
	 */
	public static SemValue createLiteral(int tag, Object value) {
		SemValue v = new SemValue();
		v.code = Parser.LITERAL;
		v.typeTag = tag;
		v.literal = value;
		return v;
	}

	/**
	 * 鍒涘缓涓�涓爣璇嗙鐨勮涔夊��
	 * 
	 * @param name
	 *            鏍囪瘑绗︾殑鍚嶅瓧
	 * @return 瀵瑰簲鐨勮涔夊�硷紙鏍囪瘑绗﹀悕瀛楀瓨鏀惧湪sval鍩燂級
	 */
	public static SemValue createIdentifier(String name) {
		SemValue v = new SemValue();
		v.code = Parser.IDENTIFIER;
		v.ident = name;
		return v;
	}

	/**
	 * 鑾峰彇杩欎釜璇箟鍊肩殑瀛楃涓茶〃绀�<br>
	 * 
	 * 鎴戜滑寤鸿浣犲湪鏋勯�犺瘝娉曞垎鏋愬櫒涔嬪墠鍏堥槄璇讳竴涓嬭繖涓嚱鏁般��
	 */
	public String toString() {
		String msg;
		switch (code) {
		// 鍏抽敭瀛�
		case Parser.BOOL:
			msg = "keyword  : bool";
			break;
		case Parser.BREAK:
			msg = "keyword  : break";
			break;
		case Parser.CLASS:
			msg = "keyword  : class";
			break;
		case Parser.ELSE:
			msg = "keyword  : else";
			break;
		case Parser.EXTENDS:
			msg = "keyword  : extends";
			break;
		case Parser.FOR:
			msg = "keyword  : for";
			break;
		case Parser.IF:
			msg = "keyword  : if";
			break;
		case Parser.INT:
			msg = "keyword  : int";
			break;
		case Parser.INSTANCEOF:
			msg = "keyword  : instanceof";
			break;
		case Parser.NEW:
			msg = "keyword  : new";
			break;
		case Parser.NULL:
			msg = "keyword  : null";
			break;
		case Parser.PRINT:
			msg = "keyword  : Print";
			break;
		case Parser.PRINT_COMP:
			msg = "keyword  : PrintComp";
			break;
		case Parser.READ_INTEGER:
			msg = "keyword  : ReadInteger";
			break;
		case Parser.READ_LINE:
			msg = "keyword  : ReadLine";
			break;
		case Parser.RETURN:
			msg = "keyword  : return";
			break;
		case Parser.STRING:
			msg = "keyword  : string";
			break;
		case Parser.THIS:
			msg = "keyword  : this";
			break;
		case Parser.SUPER:
			msg = "keyword  : super";
			break;
		case Parser.VOID:
			msg = "keyword  : void";
			break;
		case Parser.WHILE:
			msg = "keyword  : while";
			break;
		case Parser.STATIC:
			msg = "keyword : static";
			break;
		case Parser.CASE:
			msg = "keyword : case";
			break;
		case Parser.DEFAULT:
			msg = "keyword : default";
			break;
		case Parser.SCOPY:
			msg = "keyword : scopy";
			break;
		case Parser.DCOPY:
			msg = "keyword : dcopy";
			break;
		case Parser.DO:
			msg = "keyword : do";
			break;
		case Parser.OD:
			msg = "keyword : od";
			break;

		// 甯搁噺
		case Parser.LITERAL:
			switch (typeTag) {
			case Tree.INT:
			case Tree.BOOL:
				msg = "constant : " + literal;
				break;
			case Tree.IMGPART:
				msg = "constant : " + literal + "j";
				break;
			default:
				msg = "constant : " + MiscUtils.quote((String)literal);
			}
			break;
			
		// 鏍囪瘑绗�
		case Parser.IDENTIFIER:
			msg = "identifier: " + ident;
			break;

		// 鎿嶄綔绗�
		case Parser.AND:
			msg = "operator : &&";
			break;
		case Parser.EQUAL:
			msg = "operator : ==";
			break;
		case Parser.GREATER_EQUAL:
			msg = "operator : >=";
			break;
		case Parser.LESS_EQUAL:
			msg = "operator : <=";
			break;
		case Parser.NOT_EQUAL:
			msg = "operator : !=";
			break;
		case Parser.OR:
			msg = "operator : ||";
			break;
		case Parser.BRANCH:
			msg = "operator : |||";
			break;
		default:
			msg = "operator : " + (char) code;
			break;
		}
		return (String.format("%-15s%s", loc, msg));
	}
}
