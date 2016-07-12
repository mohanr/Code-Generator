package com.transformer;

import com.configuration.TransformerRuleConfiguration;
import com.configuration.YamlConfiguration;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.*;
import test.JavaBaseListener;
import test.JavaBaseVisitor;
import test.JavaLexer;
import test.JavaParser;

import java.io.FileNotFoundException;
import java.text.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Mohan Radhakrishnan on 6/15/2016.
 *
 */
public class AllRewriters extends JavaBaseListener {

    private final CodePrinter printer;

        TokenStream tokens;

        YamlConfiguration yamlConfiguration;

        TransformerRuleConfiguration transformerRuleConfiguration;

        private static final String NEW_CLASS_IDENTIFIER = "LoginController";

        /*
        Accumulators
        Accumulating the start and end indexes instead of the actual text and then fetching the token
        should result in better  performance or not.
         */
        Optional classModifier;
        String packageModifier;
        List<String> importFilter = new ArrayList<>();
        ConcurrentLinkedQueue<List<Token>> hiddenWhiteSpaceTokens = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<List<Token>> hiddenMethodCommentTokens = new ConcurrentLinkedQueue<>();

    ;

        public List<Interval> getIntervals() {
            return intervals;
        }

        List<Interval> intervals = new ArrayList<Interval>();;

        /*
        Accumulating only indexes.
         */
        List<Interval> methodIntervals = new ArrayList<Interval>();;

        public AllRewriters(TokenStream tokens) throws FileNotFoundException {

            this.tokens = tokens;

            yamlConfiguration = new YamlConfiguration();
            transformerRuleConfiguration = yamlConfiguration.getTransformerRuleConfiguration();

            printer = new CodePrinter( tokens.getTokenSource().getInputStream() );

        }

        @Override
        public void enterCompilationUnit(JavaParser.CompilationUnitContext ctx) {


            classModifier =
            ctx.typeDeclaration().stream()
                    .flatMap(type -> type.classOrInterfaceModifier().stream())
                    .map((f) -> f.getText()).findFirst();

        }

        /*
             "package" is not obtained from the tree. It should be.
         */
        @Override
        public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {

            packageModifier =  ctx.qualifiedName().getText();
            printer.writeText( "package " + packageModifier  + ";");
        }


        /*
             Remove import statements specified in the YAML fild.
             "import" is not obtained from the tree. It should be.
         */
        @Override
        public void exitImportDeclaration(JavaParser.ImportDeclarationContext ctx) {


            ArrayList<Pattern> patterns = new ArrayList<>();
            transformerRuleConfiguration.getOldImports().stream()
                    .map(f -> Pattern.compile("^(?!.*(" + f + ")).*$"))
                    .collect(Collectors.toCollection(() -> patterns));

            patterns.stream().flatMap(( Pattern p1 ) -> Stream.of(ctx.qualifiedName().getText()).
                    map(p1::matcher).
                    filter(Matcher::find).map(matcher -> matcher.group()))
                    .map(p -> "import " + p + ";")
                    .collect(Collectors.toCollection(() -> importFilter));
        }

        @Override
        public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
            printer.write( intervals );
            printer.writeList(importFilter );
            ParseTree pt = ctx.getChild(1);
            /*
                The class name is changed by a visitor because
                we get a ParseTree back
             */
            pt.accept(new ParseTreeVisitor<Object>() {

                @Override
                public Object visitChildren(RuleNode ruleNode) {
                    return null;
                }

                @Override
                public Object visitErrorNode(ErrorNode errorNode) {
                    return null;
                }

                @Override
                public Object visit(ParseTree parseTree) {
                    return null;
                }

                @Override
                public Object visitTerminal(TerminalNode terminalNode) {
                    String className = terminalNode.getText();
                    printer.writeText( "@Controller");
                    printer.writeText( classModifier.get() + " class " + transformerRuleConfiguration.getClassIdentifier() + " {");
                    return null;
                }
            });

        }



    @Override
    public void enterClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx){
        /*
            It looks like this is how one accesses nodes that are not part of the MethodDeclarationContext
            like the modifier. Hidden tokens can also be globally accessed here.
         */
        if (!(ctx.memberDeclaration() != null && ctx.memberDeclaration().methodDeclaration() != null)) {
            // No method declaration.
            return;
        }
        String methodName = ctx.memberDeclaration().methodDeclaration().Identifier().getText();

        for (JavaParser.ModifierContext modifierctx : ctx.modifier()) {
            JavaParser.ClassOrInterfaceModifierContext classOrInterfaceModifierContext =  modifierctx.classOrInterfaceModifier();
            if ( classOrInterfaceModifierContext != null ){
                List<Token> token =
                ((CommonTokenStream) tokens).getHiddenTokensToLeft( classOrInterfaceModifierContext.getStart().getTokenIndex(),
                                                                    Token.HIDDEN_CHANNEL);
                hiddenWhiteSpaceTokens.offer(token);
                token.stream().forEach(f -> System.out.println("Adding [" + f + "][" + methodName + "]\n"));
            }
        }
   }

    /**
     * There is extra code marked by comments which seems
     * to  add only the method modifier which is not part of the
     * MethodDeclarationContext.
     * TODO 1. Debug to understand 2. Find a better way to print method when you enter it.
     * @param ctx
     */
    @Override
    public void enterMethodDeclaration( JavaParser.MethodDeclarationContext ctx){

            ParserRuleContext parent = ctx.getParent();

            while (parent!=null && !(parent instanceof JavaParser.ClassBodyDeclarationContext)) {
                parent = parent.getParent();
            }
            if (parent == null) {
                parent = ctx;
            }
            Token start = parent.start;

            int tokenIndex = start.getTokenIndex();
            while ( tokenIndex > 0 ) {
                tokenIndex--;
            }

            int startIndex = start.getStartIndex();
            int endIndex = ctx.stop.getStopIndex();
            Interval interval = new Interval(startIndex,endIndex);
            methodIntervals.add( interval );

            printMethodMetaData();//Whitespaces and comments

            printer.write( methodIntervals );
            methodIntervals.clear();

            printer.writeText("}");
    }

    private void printMethodMetaData() {
        if( !hiddenWhiteSpaceTokens.isEmpty() ){
            hiddenWhiteSpaceTokens.
                    poll().stream().forEach( f -> printer.writeText( f.getText() ) );
        }
    }

}
