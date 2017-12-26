package decaf.translate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import decaf.tree.Tree;
import decaf.tree.Tree.CaseBlock;
import decaf.tree.Tree.CaseExpr;
import decaf.tree.Tree.DoStmt;
import decaf.tree.Tree.PrintComp;
import decaf.tree.Tree.SCopyExpr;
import decaf.backend.OffsetCounter;
import decaf.machdesc.Intrinsic;
import decaf.scope.ClassScope;
import decaf.symbol.Class;
import decaf.symbol.Symbol;
import decaf.symbol.Variable;
import decaf.tac.Label;
import decaf.tac.Temp;
import decaf.type.BaseType;
import decaf.type.ClassType;

public class TransPass2 extends Tree.Visitor {

	private Translater tr;

	private Temp currentThis;

	private Stack<Label> loopExits;

	public TransPass2(Translater tr) {
		this.tr = tr;
		loopExits = new Stack<Label>();
	}

	@Override
	public void visitClassDef(Tree.ClassDef classDef) {
		for (Tree f : classDef.fields) {
			f.accept(this);
		}
	}

	@Override
	public void visitMethodDef(Tree.MethodDef funcDefn) {
		if (!funcDefn.statik) {
			currentThis = ((Variable) funcDefn.symbol.getAssociatedScope()
					.lookup("this")).getTemp();
		}
		tr.beginFunc(funcDefn.symbol);
		funcDefn.body.accept(this);
		tr.endFunc();
		currentThis = null;
	}

	@Override
	public void visitTopLevel(Tree.TopLevel program) {
		for (Tree.ClassDef cd : program.classes) {
			cd.accept(this);
		}
	}

	@Override
	public void visitVarDef(Tree.VarDef varDef) {
		if (varDef.symbol.isLocalVar()) {
			Temp t;
			if(varDef.type.type.equal(BaseType.COMPLEX))
			{
				Temp csize = tr.genLoadImm4(OffsetCounter.WORD_SIZE * 2);
				tr.genParm(csize);
				t = tr.genIntrinsicCall(Intrinsic.ALLOCATE);
			}
			else
			{
				t = Temp.createTempI4();
			}
			t.sym = varDef.symbol;
			varDef.symbol.setTemp(t);
		}
	}

	@Override
	public void visitBinary(Tree.Binary expr) {
		expr.left.accept(this);
		expr.right.accept(this);
		switch (expr.tag) {
		case Tree.PLUS:
			if(expr.left.type.equal(BaseType.COMPLEX)
					|| expr.right.type.equal(BaseType.COMPLEX))
			{
				Temp size = tr.genLoadImm4(OffsetCounter.WORD_SIZE * 2);
				tr.genParm(size);
				expr.val = tr.genIntrinsicCall(Intrinsic.ALLOCATE);
				tr.genStore(tr.genAdd(tr.genLoad(expr.left.val, 0), 
						tr.genLoad(expr.right.val, 0)), expr.val, 0);
				tr.genStore(tr.genAdd(tr.genLoad(expr.left.val, 4), 
						tr.genLoad(expr.right.val, 4)), expr.val, 4);
			}
			else
				expr.val = tr.genAdd(expr.left.val, expr.right.val);
			break;
		case Tree.MINUS:
			expr.val = tr.genSub(expr.left.val, expr.right.val);
			break;
		case Tree.MUL:
			if(expr.left.type.equal(BaseType.COMPLEX)
					|| expr.right.type.equal(BaseType.COMPLEX))
			{
				Temp size = tr.genLoadImm4(OffsetCounter.WORD_SIZE * 2);
				tr.genParm(size);
				expr.val = tr.genIntrinsicCall(Intrinsic.ALLOCATE);
				tr.genStore(tr.genSub(tr.genMul(tr.genLoad(expr.left.val, 0), tr.genLoad(expr.right.val, 0)),
						tr.genMul(tr.genLoad(expr.left.val, 4), tr.genLoad(expr.right.val, 4))), expr.val, 0);
				tr.genStore(tr.genAdd(tr.genMul(tr.genLoad(expr.left.val, 0), tr.genLoad(expr.right.val, 4)),
						tr.genMul(tr.genLoad(expr.left.val, 4), tr.genLoad(expr.right.val, 0))), expr.val, 4);
			}
			else
				expr.val = tr.genMul(expr.left.val, expr.right.val);
			break;
		case Tree.DIV:
			if(expr.right.type.equal(BaseType.INT)) {
				tr.genCheckDivByZero(expr.right.val);
			}
			expr.val = tr.genDiv(expr.left.val, expr.right.val);
			break;
		case Tree.MOD:
			if(expr.right.type.equal(BaseType.INT)) {
				tr.genCheckDivByZero(expr.right.val);
			}
			expr.val = tr.genMod(expr.left.val, expr.right.val);
			break;
		case Tree.AND:
			expr.val = tr.genLAnd(expr.left.val, expr.right.val);
			break;
		case Tree.OR:
			expr.val = tr.genLOr(expr.left.val, expr.right.val);
			break;
		case Tree.LT:
			expr.val = tr.genLes(expr.left.val, expr.right.val);
			break;
		case Tree.LE:
			expr.val = tr.genLeq(expr.left.val, expr.right.val);
			break;
		case Tree.GT:
			expr.val = tr.genGtr(expr.left.val, expr.right.val);
			break;
		case Tree.GE:
			expr.val = tr.genGeq(expr.left.val, expr.right.val);
			break;
		case Tree.EQ:
		case Tree.NE:
			genEquNeq(expr);
			break;
		}
	}

	private void genEquNeq(Tree.Binary expr) {
		if (expr.left.type.equal(BaseType.STRING)
				|| expr.right.type.equal(BaseType.STRING)) {
			tr.genParm(expr.left.val);
			tr.genParm(expr.right.val);
			expr.val = tr.genDirectCall(Intrinsic.STRING_EQUAL.label,
					BaseType.BOOL);
			if(expr.tag == Tree.NE){
				expr.val = tr.genLNot(expr.val);
			}
		} else {
			if(expr.tag == Tree.EQ)
				expr.val = tr.genEqu(expr.left.val, expr.right.val);
			else
				expr.val = tr.genNeq(expr.left.val, expr.right.val);
		}
	}

	@Override
	public void visitDoStmt(Tree.DoStmt doStmt) {
		Label loop = Label.createLabel();
		Label exit = Label.createLabel();
		tr.genMark(loop);
		Map<Tree.DoSubStmt, Label> labels = new HashMap<Tree.DoSubStmt, Label>();
		for(Tree.DoSubStmt sub: doStmt.branches) {
			labels.put(sub, Label.createLabel());
			sub.expr.accept(this);
			tr.genBnez(sub.expr.val, labels.get(sub));
		}
		tr.genBranch(exit);
		loopExits.push(exit);
		for(Tree.DoSubStmt sub: doStmt.branches) {
			tr.genMark(labels.get(sub));
			sub.stmt.accept(this);
			tr.genBranch(loop);
		}
		loopExits.pop();
		tr.genMark(exit);
	}
	
	@Override
	public void visitAssign(Tree.Assign assign) {
		assign.left.accept(this);
		assign.expr.accept(this);
		switch (assign.left.lvKind) {
		case ARRAY_ELEMENT:
			Tree.Indexed arrayRef = (Tree.Indexed) assign.left;
			Temp esz = tr.genLoadImm4(OffsetCounter.WORD_SIZE);
			Temp t = tr.genMul(arrayRef.index.val, esz);
			Temp base = tr.genAdd(arrayRef.array.val, t);
			if(assign.left.type.equal(BaseType.COMPLEX)
					|| assign.expr.type.equal(BaseType.COMPLEX)) {
				tr.genStore(tr.genLoad(assign.expr.val, 0), base, 0);
				tr.genStore(tr.genLoad(assign.expr.val, 4), base, 4);
			}
			else {
				tr.genStore(assign.expr.val, base, 0);
			}
			break;
		case MEMBER_VAR:
			Tree.Ident varRef = (Tree.Ident) assign.left;
			if(assign.left.type.equal(BaseType.COMPLEX)
					|| assign.expr.type.equal(BaseType.COMPLEX)) {
				Temp base2 = tr.genAdd(varRef.owner.val, 
						tr.genLoadImm4(varRef.symbol.getOffset())); 
				tr.genStore(tr.genLoad(assign.expr.val, 0), tr.genLoad(base2, 0), 0);
				tr.genStore(tr.genLoad(assign.expr.val, 4), tr.genLoad(base2, 0), 4);
			} else {
				tr.genStore(assign.expr.val, varRef.owner.val, varRef.symbol
						.getOffset());
			}
			break;
		case PARAM_VAR:
		case LOCAL_VAR:
			if(assign.left.type.equal(BaseType.COMPLEX)
					|| assign.expr.type.equal(BaseType.COMPLEX)) {
				tr.genStore(tr.genLoad(assign.expr.val, 0), 
						((Tree.Ident) assign.left).symbol.getTemp(), 0);
				tr.genStore(tr.genLoad(assign.expr.val, 4), 
						((Tree.Ident) assign.left).symbol.getTemp(), 4);
			} else {
				tr.genAssign(((Tree.Ident) assign.left).symbol.getTemp(),
						assign.expr.val);
			}
			break;
		}
	}

	@Override
	public void visitLiteral(Tree.Literal literal) {
		switch (literal.typeTag) {
		case Tree.INT:
			literal.val = tr.genLoadImm4(((Integer)literal.value).intValue());
			break;
		case Tree.BOOL:
			literal.val = tr.genLoadImm4((Boolean)(literal.value) ? 1 : 0);
			break;
		case Tree.IMGPART:
			Temp csize = tr.genLoadImm4(OffsetCounter.WORD_SIZE * 2);
			tr.genParm(csize);
			literal.val = tr.genIntrinsicCall(Intrinsic.ALLOCATE);
			tr.genStore(tr.genLoadImm4(0), literal.val, 0);
			tr.genStore(tr.genLoadImm4(((Integer)literal.value).intValue()), literal.val, 4);
			break;
		default:
			literal.val = tr.genLoadStrConst((String)literal.value);
		}
	}

	@Override
	public void visitExec(Tree.Exec exec) {
		exec.expr.accept(this);
	}

	@Override
	public void visitUnary(Tree.Unary expr) {
		expr.expr.accept(this);
		switch (expr.tag){
		case Tree.NEG:
			expr.val = tr.genNeg(expr.expr.val);
			break;
		case Tree.NOT:
			expr.val = tr.genLNot(expr.expr.val);
			break;
		case Tree.AT:
			expr.val = tr.genLoad(expr.expr.val, 0);
			break;
		case Tree.DOLLAR:
			expr.val = tr.genLoad(expr.expr.val, 4);
			break;
		case Tree.SHARP:
			Temp csize = tr.genLoadImm4(OffsetCounter.WORD_SIZE * 2);
			tr.genParm(csize);
			expr.val = tr.genIntrinsicCall(Intrinsic.ALLOCATE);
			tr.genStore(expr.expr.val, expr.val, 0);
			tr.genStore(tr.genLoadImm4(0), expr.val, 4);
			break;
		}
	}

	@Override
	public void visitNull(Tree.Null nullExpr) {
		nullExpr.val = tr.genLoadImm4(0);
	}

	@Override
	public void visitBlock(Tree.Block block) {
		for (Tree s : block.block) {
			s.accept(this);
		}
	}

	@Override
	public void visitThisExpr(Tree.ThisExpr thisExpr) {
		thisExpr.val = currentThis;
	}

	@Override
	public void visitSuperExpr(Tree.SuperExpr superExpr) {
		superExpr.val = currentThis;
	}

	@Override
	public void visitReadIntExpr(Tree.ReadIntExpr readIntExpr) {
		readIntExpr.val = tr.genIntrinsicCall(Intrinsic.READ_INT);
	}

	@Override
	public void visitReadLineExpr(Tree.ReadLineExpr readStringExpr) {
		readStringExpr.val = tr.genIntrinsicCall(Intrinsic.READ_LINE);
	}

	@Override
	public void visitReturn(Tree.Return returnStmt) {
		if (returnStmt.expr != null) {
			returnStmt.expr.accept(this);
			tr.genReturn(returnStmt.expr.val);
		} else {
			tr.genReturn(null);
		}

	}

	@Override
	public void visitPrintComp(Tree.PrintComp printComp) {
		for(Tree.Expr expr: printComp.exprs) {
			expr.accept(this);
			tr.genParm(tr.genLoad(expr.val, 0));
			tr.genIntrinsicCall(Intrinsic.PRINT_INT);
			Temp plus = tr.genLoadStrConst("+");
			tr.genParm(plus);
			tr.genIntrinsicCall(Intrinsic.PRINT_STRING);
			tr.genParm(tr.genLoad(expr.val, 4));
			tr.genIntrinsicCall(Intrinsic.PRINT_INT);
			Temp j = tr.genLoadStrConst("j");
			tr.genParm(j);
			tr.genIntrinsicCall(Intrinsic.PRINT_STRING);
		}
	}
	
	@Override
	public void visitPrint(Tree.Print printStmt) {
		for (Tree.Expr r : printStmt.exprs) {
			r.accept(this);
			tr.genParm(r.val);
			if (r.type.equal(BaseType.BOOL)) {
				tr.genIntrinsicCall(Intrinsic.PRINT_BOOL);
			} else if (r.type.equal(BaseType.INT)) {
				tr.genIntrinsicCall(Intrinsic.PRINT_INT);
			} else if (r.type.equal(BaseType.STRING)) {
				tr.genIntrinsicCall(Intrinsic.PRINT_STRING);
			}
		}
	}

	@Override
	public void visitIndexed(Tree.Indexed indexed) {
		indexed.array.accept(this);
		indexed.index.accept(this);
		tr.genCheckArrayIndex(indexed.array.val, indexed.index.val);
		Temp esz = tr.genLoadImm4(OffsetCounter.WORD_SIZE);
		Temp t = tr.genMul(indexed.index.val, esz);
		Temp base = tr.genAdd(indexed.array.val, t);
		indexed.val = tr.genLoad(base, 0);
	}

	@Override
	public void visitIdent(Tree.Ident ident) {
		if(ident.lvKind == Tree.LValue.Kind.MEMBER_VAR){
			ident.owner.accept(this);
		}
		
		switch (ident.lvKind) {
		case MEMBER_VAR:
			ident.val = tr.genLoad(ident.owner.val, ident.symbol.getOffset());
			break;
		default:
			ident.val = ident.symbol.getTemp();
			break;
		}
	}
	
	@Override
	public void visitBreak(Tree.Break breakStmt) {
		tr.genBranch(loopExits.peek());
	}

	@Override
	public void visitCallExpr(Tree.CallExpr callExpr) {
		if (callExpr.isArrayLength) {
			callExpr.receiver.accept(this);
			callExpr.val = tr.genLoad(callExpr.receiver.val,
					-OffsetCounter.WORD_SIZE);
		} else {
			if (callExpr.receiver != null) {
				callExpr.receiver.accept(this);
			}
			for (Tree.Expr expr : callExpr.actuals) {
				expr.accept(this);
			}
			if (callExpr.receiver != null) {
				tr.genParm(callExpr.receiver.val);
			}
			for (Tree.Expr expr : callExpr.actuals) {
				tr.genParm(expr.val);
			}
			if (callExpr.receiver == null) {
				callExpr.val = tr.genDirectCall(
						callExpr.symbol.getFuncty().label, callExpr.symbol
								.getReturnType());
			} else {
				if(callExpr.receiver.tag == Tree.SUPEREXPR) {
					Temp vt = tr.genLoadVTable(callExpr.symbol.getScope().getOwner().getVtable());
					Temp func = tr.genLoad(vt, callExpr.symbol.getOffset());
					callExpr.val = tr.genIndirectCall(func, callExpr.symbol
							.getReturnType());
				} else if(callExpr.receiver.tag == Tree.THISEXPR) {
					Temp vt = tr.genLoadVTable(callExpr.symbol.getScope().getOwner().getVtable());
					Temp func = tr.genLoad(vt, callExpr.symbol.getOffset());
					callExpr.val = tr.genIndirectCall(func, callExpr.symbol
							.getReturnType());
				} else {
					Temp vt = tr.genLoad(callExpr.receiver.val, 0);
					Temp func = tr.genLoad(vt, callExpr.symbol.getOffset());
					callExpr.val = tr.genIndirectCall(func, callExpr.symbol
							.getReturnType());
				}
			}
		}

	}

	@Override
	public void visitForLoop(Tree.ForLoop forLoop) {
		if (forLoop.init != null) {
			forLoop.init.accept(this);
		}
		Label cond = Label.createLabel();
		Label loop = Label.createLabel();
		tr.genBranch(cond);
		tr.genMark(loop);
		if (forLoop.update != null) {
			forLoop.update.accept(this);
		}
		tr.genMark(cond);
		forLoop.condition.accept(this);
		Label exit = Label.createLabel();
		tr.genBeqz(forLoop.condition.val, exit);
		loopExits.push(exit);
		if (forLoop.loopBody != null) {
			forLoop.loopBody.accept(this);
		}
		tr.genBranch(loop);
		loopExits.pop();
		tr.genMark(exit);
	}

	@Override
	public void visitIf(Tree.If ifStmt) {
		ifStmt.condition.accept(this);
		if (ifStmt.falseBranch != null) {
			Label falseLabel = Label.createLabel();
			tr.genBeqz(ifStmt.condition.val, falseLabel);
			ifStmt.trueBranch.accept(this);
			Label exit = Label.createLabel();
			tr.genBranch(exit);
			tr.genMark(falseLabel);
			ifStmt.falseBranch.accept(this);
			tr.genMark(exit);
		} else if (ifStmt.trueBranch != null) {
			Label exit = Label.createLabel();
			tr.genBeqz(ifStmt.condition.val, exit);
			if (ifStmt.trueBranch != null) {
				ifStmt.trueBranch.accept(this);
			}
			tr.genMark(exit);
		}
	}

	@Override
	public void visitNewArray(Tree.NewArray newArray) {
		newArray.length.accept(this);
		newArray.val = tr.genNewArray(newArray.length.val, 
				newArray.elementType.type.equal(BaseType.COMPLEX));
	}

	@Override
	public void visitNewClass(Tree.NewClass newClass) {
		newClass.val = tr.genDirectCall(newClass.symbol.getNewFuncLabel(),
				BaseType.INT);
	}
	
	@Override
	public void visitSCopyExpr(Tree.SCopyExpr sCopyExpr) {
		sCopyExpr.expr.accept(this);
		sCopyExpr.val = tr.genDirectCall(((ClassType)(sCopyExpr.expr.type)).getSymbol()
				.getNewFuncLabel(), BaseType.INT);
		Iterator<Symbol> iter = ((ClassType)(sCopyExpr.expr.type)).getSymbol()
				.getAssociatedScope().iterator();
		int offset = OffsetCounter.WORD_SIZE;
		Temp src, dst;
		while(iter.hasNext()) {
			Symbol sym = iter.next();
			if(sym.isVariable()) {
				src = tr.genAdd(sCopyExpr.expr.val, tr.genLoadImm4(offset));
				dst = tr.genAdd(sCopyExpr.val, tr.genLoadImm4(offset));
				if(sym.getType().equal(BaseType.COMPLEX)) {
					Temp size = tr.genLoadImm4(OffsetCounter.WORD_SIZE * 2);
					tr.genParm(size);
					tr.genStore(tr.genIntrinsicCall(Intrinsic.ALLOCATE), dst, 0);
					Temp srcPtr = tr.genLoad(src, 0);
					Temp dstPtr = tr.genLoad(dst, 0);
					tr.genStore(tr.genLoad(srcPtr, 0), dstPtr, 0);
					tr.genStore(tr.genLoad(srcPtr, 4), dstPtr, 4);
				} else {
					tr.genStore(tr.genLoad(src, 0), dst, 0);
				}
				offset += 4;
			}
		}
	}
	
	private void deepCopy(Class clazz, Temp srcPtr, Temp dstPtr) {
		tr.genAssign(dstPtr, tr.genDirectCall(clazz.getNewFuncLabel(), BaseType.INT));
		Iterator<Symbol> iter = clazz.getAssociatedScope().iterator();
		int offset = OffsetCounter.WORD_SIZE;
		Temp src, dst;
		while(iter.hasNext()) {
			Symbol sym = iter.next();
			if(sym.isVariable()) {
				src = tr.genAdd(srcPtr, tr.genLoadImm4(offset));
				dst = tr.genAdd(dstPtr, tr.genLoadImm4(offset));
				if(sym.getType().equal(BaseType.COMPLEX)) {
					Temp size = tr.genLoadImm4(OffsetCounter.WORD_SIZE * 2);
					tr.genParm(size);
					tr.genStore(tr.genIntrinsicCall(Intrinsic.ALLOCATE), dst, 0);
					tr.genStore(tr.genLoad(tr.genLoad(src, 0), 0), tr.genLoad(dst, 0), 0);
					tr.genStore(tr.genLoad(tr.genLoad(src, 0), 4), tr.genLoad(dst, 0), 4);
				} else if(sym.getType().isClassType()) {
					Temp newDst = Temp.createTempI4();
					deepCopy(((ClassType)(sym.getType())).getSymbol(), tr.genLoad(src, 0), newDst);
					tr.genStore(newDst, dst, 0);
				} else {
					tr.genStore(tr.genLoad(src, 0), dst, 0);
				}
				offset += 4;
			}
		}
	}
	
	@Override
	public void visitDCopyExpr(Tree.DCopyExpr dCopyExpr) {
		dCopyExpr.expr.accept(this);
		dCopyExpr.val = Temp.createTempI4();
		deepCopy(((ClassType)(dCopyExpr.expr.type)).getSymbol(), dCopyExpr.expr.val, dCopyExpr.val);
	}

	@Override
	public void visitCaseExpr(Tree.CaseExpr caseExpr) {
		caseExpr.constant.accept(this);
		caseExpr.expr.accept(this);
		if(caseExpr.expr.type.equal(BaseType.COMPLEX)) {
			Temp size = tr.genLoadImm4(OffsetCounter.WORD_SIZE * 2);
			tr.genParm(size);
			caseExpr.val = tr.genIntrinsicCall(Intrinsic.ALLOCATE);
			tr.genStore(tr.genLoad(caseExpr.expr.val, 0), caseExpr.val, 0);
			tr.genStore(tr.genLoad(caseExpr.expr.val, 4), caseExpr.val, 4);
		}
		else {
			caseExpr.val = Temp.createTempI4();
			tr.genAssign(caseExpr.val, caseExpr.expr.val);
		}
	}
	
	@Override
	public void visitCaseBlock(Tree.CaseBlock treeBlock) {
		treeBlock.mainExpr.accept(this);
		Label exit = Label.createLabel();
		treeBlock.defaultExpr.accept(this);
		Label defolt = Label.createLabel();
		Map<Tree.CaseExpr, Label> labels = new HashMap<Tree.CaseExpr, Label>();
		for(Tree.CaseExpr ce: treeBlock.caseExprs) {
			ce.accept(this);
			labels.put(ce, Label.createLabel());
		}
		
		if(treeBlock.defaultExpr.type.equal(BaseType.COMPLEX)) {
			Temp size = tr.genLoadImm4(OffsetCounter.WORD_SIZE * 2);
			tr.genParm(size);
			treeBlock.val = tr.genIntrinsicCall(Intrinsic.ALLOCATE);
		}
		else {
			treeBlock.val = Temp.createTempI4();
		}
		for(Tree.CaseExpr ce: treeBlock.caseExprs) {
			Temp cond = tr.genEqu(treeBlock.mainExpr.val, ce.constant.val);
			tr.genBnez(cond, labels.get(ce));
		}
		tr.genBranch(defolt);
		
		if(treeBlock.defaultExpr.type.equal(BaseType.COMPLEX)) {
			for(Tree.CaseExpr ce: treeBlock.caseExprs) {
				tr.genMark(labels.get(ce));
				tr.genStore(tr.genLoad(ce.val, 0), treeBlock.val, 0);
				tr.genStore(tr.genLoad(ce.val, 4), treeBlock.val, 4);
				tr.genBranch(exit);
			}
			tr.genMark(defolt);
			tr.genStore(tr.genLoad(treeBlock.defaultExpr.val, 0), treeBlock.val, 0);
			tr.genStore(tr.genLoad(treeBlock.defaultExpr.val, 4), treeBlock.val, 4);
		}
		else {
			for(Tree.CaseExpr ce: treeBlock.caseExprs) {
				tr.genMark(labels.get(ce));
				tr.genAssign(treeBlock.val, ce.val);
				tr.genBranch(exit);
			}
			tr.genMark(defolt);
			tr.genAssign(treeBlock.val, treeBlock.defaultExpr.val);
		}
		tr.genMark(exit);
	}
	
	@Override
	public void visitWhileLoop(Tree.WhileLoop whileLoop) {
		Label loop = Label.createLabel();
		tr.genMark(loop);
		whileLoop.condition.accept(this);
		Label exit = Label.createLabel();
		tr.genBeqz(whileLoop.condition.val, exit);
		loopExits.push(exit);
		if (whileLoop.loopBody != null) {
			whileLoop.loopBody.accept(this);
		}
		tr.genBranch(loop);
		loopExits.pop();
		tr.genMark(exit);
	}

	@Override
	public void visitTypeTest(Tree.TypeTest typeTest) {
		typeTest.instance.accept(this);
		typeTest.val = tr.genInstanceof(typeTest.instance.val,
				typeTest.symbol);
	}

	@Override
	public void visitTypeCast(Tree.TypeCast typeCast) {
		typeCast.expr.accept(this);
		if (!typeCast.expr.type.compatible(typeCast.symbol.getType())) {
			tr.genClassCast(typeCast.expr.val, typeCast.symbol);
		}
		typeCast.val = typeCast.expr.val;
	}
}
