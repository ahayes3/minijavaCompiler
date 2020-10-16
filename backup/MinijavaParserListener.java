// Generated from /home/andy/Classwork/compilers/MinijavaParser/src/main/antlr/MinijavaParser.g4 by ANTLR 4.8
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MinijavaParser}.
 */
public interface MinijavaParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MinijavaParser#goal}.
	 * @param ctx the parse tree
	 */
	void enterGoal(MinijavaParser.GoalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinijavaParser#goal}.
	 * @param ctx the parse tree
	 */
	void exitGoal(MinijavaParser.GoalContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinijavaParser#mainClass}.
	 * @param ctx the parse tree
	 */
	void enterMainClass(MinijavaParser.MainClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinijavaParser#mainClass}.
	 * @param ctx the parse tree
	 */
	void exitMainClass(MinijavaParser.MainClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinijavaParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(MinijavaParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinijavaParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(MinijavaParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinijavaParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaration(MinijavaParser.VarDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinijavaParser#varDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaration(MinijavaParser.VarDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinijavaParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(MinijavaParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinijavaParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(MinijavaParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinijavaParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(MinijavaParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinijavaParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(MinijavaParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinijavaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(MinijavaParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinijavaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(MinijavaParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinijavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(MinijavaParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinijavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(MinijavaParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinijavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(MinijavaParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinijavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(MinijavaParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinijavaParser#lambda}.
	 * @param ctx the parse tree
	 */
	void enterLambda(MinijavaParser.LambdaContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinijavaParser#lambda}.
	 * @param ctx the parse tree
	 */
	void exitLambda(MinijavaParser.LambdaContext ctx);
	/**
	 * Enter a parse tree produced by {@link MinijavaParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(MinijavaParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MinijavaParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(MinijavaParser.IdentifierContext ctx);
}
