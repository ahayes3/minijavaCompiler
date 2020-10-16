// Generated from /home/andy/Classwork/compilers/MinijavaParser/src/main/antlr/MinijavaParser.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MinijavaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		CLASS=1, PUBLIC=2, STATIC=3, VOID=4, MAIN=5, STRING=6, INTARR=7, INT=8, 
		BOOL=9, EXTENDS=10, LBRACE=11, RBRACE=12, LPAREN=13, RPAREN=14, LBRACK=15, 
		RBRACK=16, RETURN=17, IF=18, ELSE=19, WHILE=20, PRINTLN=21, SEMIC=22, 
		ASSIGN=23, OP=24, PM=25, LENGTH=26, DOT=27, COMMA=28, INTLIT=29, TRUE=30, 
		FALSE=31, THIS=32, NEW=33, NEG=34, ARROW=35, IDENT=36, WS=37, LINE_COMMENT=38, 
		COMMENT=39;
	public static final int
		RULE_goal = 0, RULE_mainClass = 1, RULE_classDeclaration = 2, RULE_varDeclaration = 3, 
		RULE_methodDeclaration = 4, RULE_param = 5, RULE_type = 6, RULE_statement = 7, 
		RULE_expression = 8, RULE_lambda = 9, RULE_identifier = 10;
	private static String[] makeRuleNames() {
		return new String[] {
			"goal", "mainClass", "classDeclaration", "varDeclaration", "methodDeclaration", 
			"param", "type", "statement", "expression", "lambda", "identifier"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'class'", "'public'", "'static'", "'void'", "'main'", "'String'", 
			null, "'int'", "'boolean'", "'extends'", "'{'", "'}'", "'('", "')'", 
			"'['", "']'", "'return'", "'if'", "'else'", "'while'", "'System.out.println'", 
			"';'", "'='", null, null, "'length'", "'.'", "','", null, "'true'", "'false'", 
			"'this'", "'new'", "'!'", "'->'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "CLASS", "PUBLIC", "STATIC", "VOID", "MAIN", "STRING", "INTARR", 
			"INT", "BOOL", "EXTENDS", "LBRACE", "RBRACE", "LPAREN", "RPAREN", "LBRACK", 
			"RBRACK", "RETURN", "IF", "ELSE", "WHILE", "PRINTLN", "SEMIC", "ASSIGN", 
			"OP", "PM", "LENGTH", "DOT", "COMMA", "INTLIT", "TRUE", "FALSE", "THIS", 
			"NEW", "NEG", "ARROW", "IDENT", "WS", "LINE_COMMENT", "COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MinijavaParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MinijavaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class GoalContext extends ParserRuleContext {
		public MainClassContext mainClass() {
			return getRuleContext(MainClassContext.class,0);
		}
		public TerminalNode EOF() { return getToken(MinijavaParser.EOF, 0); }
		public List<ClassDeclarationContext> classDeclaration() {
			return getRuleContexts(ClassDeclarationContext.class);
		}
		public ClassDeclarationContext classDeclaration(int i) {
			return getRuleContext(ClassDeclarationContext.class,i);
		}
		public GoalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_goal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).enterGoal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).exitGoal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinijavaParserVisitor ) return ((MinijavaParserVisitor<? extends T>)visitor).visitGoal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GoalContext goal() throws RecognitionException {
		GoalContext _localctx = new GoalContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_goal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			mainClass();
			setState(26);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CLASS) {
				{
				{
				setState(23);
				classDeclaration();
				}
				}
				setState(28);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(29);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MainClassContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(MinijavaParser.CLASS, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TerminalNode> LBRACE() { return getTokens(MinijavaParser.LBRACE); }
		public TerminalNode LBRACE(int i) {
			return getToken(MinijavaParser.LBRACE, i);
		}
		public TerminalNode PUBLIC() { return getToken(MinijavaParser.PUBLIC, 0); }
		public TerminalNode STATIC() { return getToken(MinijavaParser.STATIC, 0); }
		public TerminalNode VOID() { return getToken(MinijavaParser.VOID, 0); }
		public TerminalNode MAIN() { return getToken(MinijavaParser.MAIN, 0); }
		public TerminalNode LPAREN() { return getToken(MinijavaParser.LPAREN, 0); }
		public TerminalNode STRING() { return getToken(MinijavaParser.STRING, 0); }
		public TerminalNode LBRACK() { return getToken(MinijavaParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(MinijavaParser.RBRACK, 0); }
		public TerminalNode RPAREN() { return getToken(MinijavaParser.RPAREN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public List<TerminalNode> RBRACE() { return getTokens(MinijavaParser.RBRACE); }
		public TerminalNode RBRACE(int i) {
			return getToken(MinijavaParser.RBRACE, i);
		}
		public MainClassContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mainClass; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).enterMainClass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).exitMainClass(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinijavaParserVisitor ) return ((MinijavaParserVisitor<? extends T>)visitor).visitMainClass(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MainClassContext mainClass() throws RecognitionException {
		MainClassContext _localctx = new MainClassContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_mainClass);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(31);
			match(CLASS);
			setState(32);
			identifier();
			setState(33);
			match(LBRACE);
			setState(34);
			match(PUBLIC);
			setState(35);
			match(STATIC);
			setState(36);
			match(VOID);
			setState(37);
			match(MAIN);
			setState(38);
			match(LPAREN);
			setState(39);
			match(STRING);
			setState(40);
			match(LBRACK);
			setState(41);
			match(RBRACK);
			setState(42);
			identifier();
			setState(43);
			match(RPAREN);
			setState(44);
			match(LBRACE);
			setState(45);
			statement();
			setState(46);
			match(RBRACE);
			setState(47);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassDeclarationContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(MinijavaParser.CLASS, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode LBRACE() { return getToken(MinijavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(MinijavaParser.RBRACE, 0); }
		public TerminalNode EXTENDS() { return getToken(MinijavaParser.EXTENDS, 0); }
		public List<VarDeclarationContext> varDeclaration() {
			return getRuleContexts(VarDeclarationContext.class);
		}
		public VarDeclarationContext varDeclaration(int i) {
			return getRuleContext(VarDeclarationContext.class,i);
		}
		public List<MethodDeclarationContext> methodDeclaration() {
			return getRuleContexts(MethodDeclarationContext.class);
		}
		public MethodDeclarationContext methodDeclaration(int i) {
			return getRuleContext(MethodDeclarationContext.class,i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public ClassDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).enterClassDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).exitClassDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinijavaParserVisitor ) return ((MinijavaParserVisitor<? extends T>)visitor).visitClassDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDeclarationContext classDeclaration() throws RecognitionException {
		ClassDeclarationContext _localctx = new ClassDeclarationContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_classDeclaration);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			match(CLASS);
			setState(50);
			identifier();
			setState(53);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(51);
				match(EXTENDS);
				setState(52);
				identifier();
				}
			}

			setState(55);
			match(LBRACE);
			setState(59);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(56);
					varDeclaration();
					}
					} 
				}
				setState(61);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PUBLIC) {
				{
				{
				setState(62);
				methodDeclaration();
				}
				}
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(71);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LBRACE) | (1L << IF) | (1L << WHILE) | (1L << PRINTLN) | (1L << IDENT))) != 0)) {
				{
				{
				setState(68);
				statement();
				}
				}
				setState(73);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(74);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDeclarationContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode SEMIC() { return getToken(MinijavaParser.SEMIC, 0); }
		public VarDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).enterVarDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).exitVarDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinijavaParserVisitor ) return ((MinijavaParserVisitor<? extends T>)visitor).visitVarDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclarationContext varDeclaration() throws RecognitionException {
		VarDeclarationContext _localctx = new VarDeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_varDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			type();
			setState(77);
			identifier();
			setState(78);
			match(SEMIC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodDeclarationContext extends ParserRuleContext {
		public TerminalNode PUBLIC() { return getToken(MinijavaParser.PUBLIC, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ParamContext param() {
			return getRuleContext(ParamContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(MinijavaParser.LBRACE, 0); }
		public TerminalNode RETURN() { return getToken(MinijavaParser.RETURN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMIC() { return getToken(MinijavaParser.SEMIC, 0); }
		public TerminalNode RBRACE() { return getToken(MinijavaParser.RBRACE, 0); }
		public List<VarDeclarationContext> varDeclaration() {
			return getRuleContexts(VarDeclarationContext.class);
		}
		public VarDeclarationContext varDeclaration(int i) {
			return getRuleContext(VarDeclarationContext.class,i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public MethodDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).enterMethodDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).exitMethodDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinijavaParserVisitor ) return ((MinijavaParserVisitor<? extends T>)visitor).visitMethodDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodDeclarationContext methodDeclaration() throws RecognitionException {
		MethodDeclarationContext _localctx = new MethodDeclarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_methodDeclaration);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			match(PUBLIC);
			setState(81);
			type();
			setState(82);
			identifier();
			setState(83);
			param();
			setState(84);
			match(LBRACE);
			setState(88);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(85);
					varDeclaration();
					}
					} 
				}
				setState(90);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(94);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LBRACE) | (1L << IF) | (1L << WHILE) | (1L << PRINTLN) | (1L << IDENT))) != 0)) {
				{
				{
				setState(91);
				statement();
				}
				}
				setState(96);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(97);
			match(RETURN);
			setState(98);
			expression(0);
			setState(99);
			match(SEMIC);
			setState(100);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(MinijavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MinijavaParser.RPAREN, 0); }
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MinijavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MinijavaParser.COMMA, i);
		}
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).enterParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).exitParam(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinijavaParserVisitor ) return ((MinijavaParserVisitor<? extends T>)visitor).visitParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(102);
			match(LPAREN);
			setState(114);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INTARR) | (1L << INT) | (1L << BOOL) | (1L << IDENT))) != 0)) {
				{
				setState(103);
				type();
				setState(104);
				identifier();
				setState(111);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(105);
					match(COMMA);
					setState(106);
					type();
					setState(107);
					identifier();
					}
					}
					setState(113);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(116);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TerminalNode INTARR() { return getToken(MinijavaParser.INTARR, 0); }
		public TerminalNode BOOL() { return getToken(MinijavaParser.BOOL, 0); }
		public TerminalNode INT() { return getToken(MinijavaParser.INT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinijavaParserVisitor ) return ((MinijavaParserVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_type);
		try {
			setState(122);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTARR:
				enterOuterAlt(_localctx, 1);
				{
				setState(118);
				match(INTARR);
				}
				break;
			case BOOL:
				enterOuterAlt(_localctx, 2);
				{
				setState(119);
				match(BOOL);
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 3);
				{
				setState(120);
				match(INT);
				}
				break;
			case IDENT:
				enterOuterAlt(_localctx, 4);
				{
				setState(121);
				identifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(MinijavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(MinijavaParser.RBRACE, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode IF() { return getToken(MinijavaParser.IF, 0); }
		public TerminalNode LPAREN() { return getToken(MinijavaParser.LPAREN, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(MinijavaParser.RPAREN, 0); }
		public TerminalNode ELSE() { return getToken(MinijavaParser.ELSE, 0); }
		public TerminalNode WHILE() { return getToken(MinijavaParser.WHILE, 0); }
		public TerminalNode PRINTLN() { return getToken(MinijavaParser.PRINTLN, 0); }
		public TerminalNode SEMIC() { return getToken(MinijavaParser.SEMIC, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(MinijavaParser.ASSIGN, 0); }
		public TerminalNode LBRACK() { return getToken(MinijavaParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(MinijavaParser.RBRACK, 0); }
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinijavaParserVisitor ) return ((MinijavaParserVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_statement);
		int _la;
		try {
			setState(165);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(124);
				match(LBRACE);
				setState(128);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LBRACE) | (1L << IF) | (1L << WHILE) | (1L << PRINTLN) | (1L << IDENT))) != 0)) {
					{
					{
					setState(125);
					statement();
					}
					}
					setState(130);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(131);
				match(RBRACE);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(132);
				match(IF);
				setState(133);
				match(LPAREN);
				setState(134);
				expression(0);
				setState(135);
				match(RPAREN);
				setState(136);
				statement();
				setState(137);
				match(ELSE);
				setState(138);
				statement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(140);
				match(WHILE);
				setState(141);
				match(LPAREN);
				setState(142);
				expression(0);
				setState(143);
				match(RPAREN);
				setState(144);
				statement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(146);
				match(PRINTLN);
				setState(147);
				match(LPAREN);
				setState(148);
				expression(0);
				setState(149);
				match(RPAREN);
				setState(150);
				match(SEMIC);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(152);
				identifier();
				setState(153);
				match(ASSIGN);
				setState(154);
				expression(0);
				setState(155);
				match(SEMIC);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(157);
				identifier();
				setState(158);
				match(LBRACK);
				setState(159);
				expression(0);
				setState(160);
				match(RBRACK);
				setState(161);
				match(ASSIGN);
				setState(162);
				expression(0);
				setState(163);
				match(SEMIC);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public LambdaContext lambda() {
			return getRuleContext(LambdaContext.class,0);
		}
		public TerminalNode INTLIT() { return getToken(MinijavaParser.INTLIT, 0); }
		public TerminalNode PM() { return getToken(MinijavaParser.PM, 0); }
		public TerminalNode TRUE() { return getToken(MinijavaParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(MinijavaParser.FALSE, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode THIS() { return getToken(MinijavaParser.THIS, 0); }
		public TerminalNode NEW() { return getToken(MinijavaParser.NEW, 0); }
		public TerminalNode INT() { return getToken(MinijavaParser.INT, 0); }
		public TerminalNode LBRACK() { return getToken(MinijavaParser.LBRACK, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode RBRACK() { return getToken(MinijavaParser.RBRACK, 0); }
		public TerminalNode LPAREN() { return getToken(MinijavaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MinijavaParser.RPAREN, 0); }
		public TerminalNode NEG() { return getToken(MinijavaParser.NEG, 0); }
		public TerminalNode OP() { return getToken(MinijavaParser.OP, 0); }
		public TerminalNode DOT() { return getToken(MinijavaParser.DOT, 0); }
		public TerminalNode LENGTH() { return getToken(MinijavaParser.LENGTH, 0); }
		public List<TerminalNode> COMMA() { return getTokens(MinijavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MinijavaParser.COMMA, i);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinijavaParserVisitor ) return ((MinijavaParserVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 16;
		enterRecursionRule(_localctx, 16, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(168);
				lambda();
				}
				break;
			case 2:
				{
				setState(170);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PM) {
					{
					setState(169);
					match(PM);
					}
				}

				setState(172);
				match(INTLIT);
				}
				break;
			case 3:
				{
				setState(173);
				match(TRUE);
				}
				break;
			case 4:
				{
				setState(174);
				match(FALSE);
				}
				break;
			case 5:
				{
				setState(175);
				identifier();
				}
				break;
			case 6:
				{
				setState(176);
				match(THIS);
				}
				break;
			case 7:
				{
				setState(177);
				match(NEW);
				setState(178);
				match(INT);
				setState(179);
				match(LBRACK);
				setState(180);
				expression(0);
				setState(181);
				match(RBRACK);
				}
				break;
			case 8:
				{
				setState(183);
				match(NEW);
				setState(184);
				identifier();
				setState(185);
				match(LPAREN);
				setState(186);
				match(RPAREN);
				}
				break;
			case 9:
				{
				setState(188);
				match(NEG);
				setState(189);
				expression(2);
				}
				break;
			case 10:
				{
				setState(190);
				match(LPAREN);
				setState(191);
				expression(0);
				setState(192);
				match(RPAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(228);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(226);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(196);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(197);
						match(OP);
						setState(198);
						expression(16);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(199);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(200);
						match(PM);
						setState(201);
						expression(15);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(202);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(203);
						match(LBRACK);
						setState(204);
						expression(0);
						setState(205);
						match(RBRACK);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(207);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(208);
						match(DOT);
						setState(209);
						match(LENGTH);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(210);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(211);
						match(DOT);
						setState(212);
						identifier();
						setState(213);
						match(LPAREN);
						setState(222);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LPAREN) | (1L << PM) | (1L << INTLIT) | (1L << TRUE) | (1L << FALSE) | (1L << THIS) | (1L << NEW) | (1L << NEG) | (1L << IDENT))) != 0)) {
							{
							setState(214);
							expression(0);
							setState(219);
							_errHandler.sync(this);
							_la = _input.LA(1);
							while (_la==COMMA) {
								{
								{
								setState(215);
								match(COMMA);
								setState(216);
								expression(0);
								}
								}
								setState(221);
								_errHandler.sync(this);
								_la = _input.LA(1);
							}
							}
						}

						setState(224);
						match(RPAREN);
						}
						break;
					}
					} 
				}
				setState(230);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class LambdaContext extends ParserRuleContext {
		public ParamContext param() {
			return getRuleContext(ParamContext.class,0);
		}
		public TerminalNode ARROW() { return getToken(MinijavaParser.ARROW, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(MinijavaParser.LBRACE, 0); }
		public TerminalNode RETURN() { return getToken(MinijavaParser.RETURN, 0); }
		public TerminalNode SEMIC() { return getToken(MinijavaParser.SEMIC, 0); }
		public TerminalNode RBRACE() { return getToken(MinijavaParser.RBRACE, 0); }
		public List<VarDeclarationContext> varDeclaration() {
			return getRuleContexts(VarDeclarationContext.class);
		}
		public VarDeclarationContext varDeclaration(int i) {
			return getRuleContext(VarDeclarationContext.class,i);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public LambdaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambda; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).enterLambda(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).exitLambda(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinijavaParserVisitor ) return ((MinijavaParserVisitor<? extends T>)visitor).visitLambda(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LambdaContext lambda() throws RecognitionException {
		LambdaContext _localctx = new LambdaContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_lambda);
		int _la;
		try {
			int _alt;
			setState(255);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(231);
				param();
				setState(232);
				match(ARROW);
				setState(233);
				expression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(235);
				param();
				setState(236);
				match(ARROW);
				setState(237);
				match(LBRACE);
				setState(241);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(238);
						varDeclaration();
						}
						} 
					}
					setState(243);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				}
				setState(247);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LBRACE) | (1L << IF) | (1L << WHILE) | (1L << PRINTLN) | (1L << IDENT))) != 0)) {
					{
					{
					setState(244);
					statement();
					}
					}
					setState(249);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(250);
				match(RETURN);
				setState(251);
				expression(0);
				setState(252);
				match(SEMIC);
				setState(253);
				match(RBRACE);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(MinijavaParser.IDENT, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MinijavaParserListener ) ((MinijavaParserListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MinijavaParserVisitor ) return ((MinijavaParserVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257);
			match(IDENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 8:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 15);
		case 1:
			return precpred(_ctx, 14);
		case 2:
			return precpred(_ctx, 13);
		case 3:
			return precpred(_ctx, 12);
		case 4:
			return precpred(_ctx, 10);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3)\u0106\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\3\2\3\2\7\2\33\n\2\f\2\16\2\36\13\2\3\2\3\2\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4"+
		"\3\4\5\48\n\4\3\4\3\4\7\4<\n\4\f\4\16\4?\13\4\3\4\7\4B\n\4\f\4\16\4E\13"+
		"\4\3\4\7\4H\n\4\f\4\16\4K\13\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\7\6Y\n\6\f\6\16\6\\\13\6\3\6\7\6_\n\6\f\6\16\6b\13\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\7\7p\n\7\f\7\16\7s\13\7\5\7u"+
		"\n\7\3\7\3\7\3\b\3\b\3\b\3\b\5\b}\n\b\3\t\3\t\7\t\u0081\n\t\f\t\16\t\u0084"+
		"\13\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\5\t\u00a8\n\t\3\n\3\n\3\n\5\n\u00ad\n\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u00c5"+
		"\n\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\7\n\u00dc\n\n\f\n\16\n\u00df\13\n\5\n\u00e1\n\n\3\n"+
		"\3\n\7\n\u00e5\n\n\f\n\16\n\u00e8\13\n\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\7\13\u00f2\n\13\f\13\16\13\u00f5\13\13\3\13\7\13\u00f8\n\13\f"+
		"\13\16\13\u00fb\13\13\3\13\3\13\3\13\3\13\3\13\5\13\u0102\n\13\3\f\3\f"+
		"\3\f\2\3\22\r\2\4\6\b\n\f\16\20\22\24\26\2\2\2\u0120\2\30\3\2\2\2\4!\3"+
		"\2\2\2\6\63\3\2\2\2\bN\3\2\2\2\nR\3\2\2\2\fh\3\2\2\2\16|\3\2\2\2\20\u00a7"+
		"\3\2\2\2\22\u00c4\3\2\2\2\24\u0101\3\2\2\2\26\u0103\3\2\2\2\30\34\5\4"+
		"\3\2\31\33\5\6\4\2\32\31\3\2\2\2\33\36\3\2\2\2\34\32\3\2\2\2\34\35\3\2"+
		"\2\2\35\37\3\2\2\2\36\34\3\2\2\2\37 \7\2\2\3 \3\3\2\2\2!\"\7\3\2\2\"#"+
		"\5\26\f\2#$\7\r\2\2$%\7\4\2\2%&\7\5\2\2&\'\7\6\2\2\'(\7\7\2\2()\7\17\2"+
		"\2)*\7\b\2\2*+\7\21\2\2+,\7\22\2\2,-\5\26\f\2-.\7\20\2\2./\7\r\2\2/\60"+
		"\5\20\t\2\60\61\7\16\2\2\61\62\7\16\2\2\62\5\3\2\2\2\63\64\7\3\2\2\64"+
		"\67\5\26\f\2\65\66\7\f\2\2\668\5\26\f\2\67\65\3\2\2\2\678\3\2\2\289\3"+
		"\2\2\29=\7\r\2\2:<\5\b\5\2;:\3\2\2\2<?\3\2\2\2=;\3\2\2\2=>\3\2\2\2>C\3"+
		"\2\2\2?=\3\2\2\2@B\5\n\6\2A@\3\2\2\2BE\3\2\2\2CA\3\2\2\2CD\3\2\2\2DI\3"+
		"\2\2\2EC\3\2\2\2FH\5\20\t\2GF\3\2\2\2HK\3\2\2\2IG\3\2\2\2IJ\3\2\2\2JL"+
		"\3\2\2\2KI\3\2\2\2LM\7\16\2\2M\7\3\2\2\2NO\5\16\b\2OP\5\26\f\2PQ\7\30"+
		"\2\2Q\t\3\2\2\2RS\7\4\2\2ST\5\16\b\2TU\5\26\f\2UV\5\f\7\2VZ\7\r\2\2WY"+
		"\5\b\5\2XW\3\2\2\2Y\\\3\2\2\2ZX\3\2\2\2Z[\3\2\2\2[`\3\2\2\2\\Z\3\2\2\2"+
		"]_\5\20\t\2^]\3\2\2\2_b\3\2\2\2`^\3\2\2\2`a\3\2\2\2ac\3\2\2\2b`\3\2\2"+
		"\2cd\7\23\2\2de\5\22\n\2ef\7\30\2\2fg\7\16\2\2g\13\3\2\2\2ht\7\17\2\2"+
		"ij\5\16\b\2jq\5\26\f\2kl\7\36\2\2lm\5\16\b\2mn\5\26\f\2np\3\2\2\2ok\3"+
		"\2\2\2ps\3\2\2\2qo\3\2\2\2qr\3\2\2\2ru\3\2\2\2sq\3\2\2\2ti\3\2\2\2tu\3"+
		"\2\2\2uv\3\2\2\2vw\7\20\2\2w\r\3\2\2\2x}\7\t\2\2y}\7\13\2\2z}\7\n\2\2"+
		"{}\5\26\f\2|x\3\2\2\2|y\3\2\2\2|z\3\2\2\2|{\3\2\2\2}\17\3\2\2\2~\u0082"+
		"\7\r\2\2\177\u0081\5\20\t\2\u0080\177\3\2\2\2\u0081\u0084\3\2\2\2\u0082"+
		"\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0085\3\2\2\2\u0084\u0082\3\2"+
		"\2\2\u0085\u00a8\7\16\2\2\u0086\u0087\7\24\2\2\u0087\u0088\7\17\2\2\u0088"+
		"\u0089\5\22\n\2\u0089\u008a\7\20\2\2\u008a\u008b\5\20\t\2\u008b\u008c"+
		"\7\25\2\2\u008c\u008d\5\20\t\2\u008d\u00a8\3\2\2\2\u008e\u008f\7\26\2"+
		"\2\u008f\u0090\7\17\2\2\u0090\u0091\5\22\n\2\u0091\u0092\7\20\2\2\u0092"+
		"\u0093\5\20\t\2\u0093\u00a8\3\2\2\2\u0094\u0095\7\27\2\2\u0095\u0096\7"+
		"\17\2\2\u0096\u0097\5\22\n\2\u0097\u0098\7\20\2\2\u0098\u0099\7\30\2\2"+
		"\u0099\u00a8\3\2\2\2\u009a\u009b\5\26\f\2\u009b\u009c\7\31\2\2\u009c\u009d"+
		"\5\22\n\2\u009d\u009e\7\30\2\2\u009e\u00a8\3\2\2\2\u009f\u00a0\5\26\f"+
		"\2\u00a0\u00a1\7\21\2\2\u00a1\u00a2\5\22\n\2\u00a2\u00a3\7\22\2\2\u00a3"+
		"\u00a4\7\31\2\2\u00a4\u00a5\5\22\n\2\u00a5\u00a6\7\30\2\2\u00a6\u00a8"+
		"\3\2\2\2\u00a7~\3\2\2\2\u00a7\u0086\3\2\2\2\u00a7\u008e\3\2\2\2\u00a7"+
		"\u0094\3\2\2\2\u00a7\u009a\3\2\2\2\u00a7\u009f\3\2\2\2\u00a8\21\3\2\2"+
		"\2\u00a9\u00aa\b\n\1\2\u00aa\u00c5\5\24\13\2\u00ab\u00ad\7\33\2\2\u00ac"+
		"\u00ab\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae\u00c5\7\37"+
		"\2\2\u00af\u00c5\7 \2\2\u00b0\u00c5\7!\2\2\u00b1\u00c5\5\26\f\2\u00b2"+
		"\u00c5\7\"\2\2\u00b3\u00b4\7#\2\2\u00b4\u00b5\7\n\2\2\u00b5\u00b6\7\21"+
		"\2\2\u00b6\u00b7\5\22\n\2\u00b7\u00b8\7\22\2\2\u00b8\u00c5\3\2\2\2\u00b9"+
		"\u00ba\7#\2\2\u00ba\u00bb\5\26\f\2\u00bb\u00bc\7\17\2\2\u00bc\u00bd\7"+
		"\20\2\2\u00bd\u00c5\3\2\2\2\u00be\u00bf\7$\2\2\u00bf\u00c5\5\22\n\4\u00c0"+
		"\u00c1\7\17\2\2\u00c1\u00c2\5\22\n\2\u00c2\u00c3\7\20\2\2\u00c3\u00c5"+
		"\3\2\2\2\u00c4\u00a9\3\2\2\2\u00c4\u00ac\3\2\2\2\u00c4\u00af\3\2\2\2\u00c4"+
		"\u00b0\3\2\2\2\u00c4\u00b1\3\2\2\2\u00c4\u00b2\3\2\2\2\u00c4\u00b3\3\2"+
		"\2\2\u00c4\u00b9\3\2\2\2\u00c4\u00be\3\2\2\2\u00c4\u00c0\3\2\2\2\u00c5"+
		"\u00e6\3\2\2\2\u00c6\u00c7\f\21\2\2\u00c7\u00c8\7\32\2\2\u00c8\u00e5\5"+
		"\22\n\22\u00c9\u00ca\f\20\2\2\u00ca\u00cb\7\33\2\2\u00cb\u00e5\5\22\n"+
		"\21\u00cc\u00cd\f\17\2\2\u00cd\u00ce\7\21\2\2\u00ce\u00cf\5\22\n\2\u00cf"+
		"\u00d0\7\22\2\2\u00d0\u00e5\3\2\2\2\u00d1\u00d2\f\16\2\2\u00d2\u00d3\7"+
		"\35\2\2\u00d3\u00e5\7\34\2\2\u00d4\u00d5\f\f\2\2\u00d5\u00d6\7\35\2\2"+
		"\u00d6\u00d7\5\26\f\2\u00d7\u00e0\7\17\2\2\u00d8\u00dd\5\22\n\2\u00d9"+
		"\u00da\7\36\2\2\u00da\u00dc\5\22\n\2\u00db\u00d9\3\2\2\2\u00dc\u00df\3"+
		"\2\2\2\u00dd\u00db\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\u00e1\3\2\2\2\u00df"+
		"\u00dd\3\2\2\2\u00e0\u00d8\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00e2\3\2"+
		"\2\2\u00e2\u00e3\7\20\2\2\u00e3\u00e5\3\2\2\2\u00e4\u00c6\3\2\2\2\u00e4"+
		"\u00c9\3\2\2\2\u00e4\u00cc\3\2\2\2\u00e4\u00d1\3\2\2\2\u00e4\u00d4\3\2"+
		"\2\2\u00e5\u00e8\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7"+
		"\23\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e9\u00ea\5\f\7\2\u00ea\u00eb\7%\2\2"+
		"\u00eb\u00ec\5\22\n\2\u00ec\u0102\3\2\2\2\u00ed\u00ee\5\f\7\2\u00ee\u00ef"+
		"\7%\2\2\u00ef\u00f3\7\r\2\2\u00f0\u00f2\5\b\5\2\u00f1\u00f0\3\2\2\2\u00f2"+
		"\u00f5\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\u00f9\3\2"+
		"\2\2\u00f5\u00f3\3\2\2\2\u00f6\u00f8\5\20\t\2\u00f7\u00f6\3\2\2\2\u00f8"+
		"\u00fb\3\2\2\2\u00f9\u00f7\3\2\2\2\u00f9\u00fa\3\2\2\2\u00fa\u00fc\3\2"+
		"\2\2\u00fb\u00f9\3\2\2\2\u00fc\u00fd\7\23\2\2\u00fd\u00fe\5\22\n\2\u00fe"+
		"\u00ff\7\30\2\2\u00ff\u0100\7\16\2\2\u0100\u0102\3\2\2\2\u0101\u00e9\3"+
		"\2\2\2\u0101\u00ed\3\2\2\2\u0102\25\3\2\2\2\u0103\u0104\7&\2\2\u0104\27"+
		"\3\2\2\2\27\34\67=CIZ`qt|\u0082\u00a7\u00ac\u00c4\u00dd\u00e0\u00e4\u00e6"+
		"\u00f3\u00f9\u0101";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
