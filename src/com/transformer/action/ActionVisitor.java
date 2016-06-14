package com.transformer.action;

import com.antlr.java.grammar.JavaBaseVisitor;
import com.antlr.java.grammar.JavaLexer;
import com.antlr.java.grammar.JavaParser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created at 12:19 PM on 14/06/16 for HRG SCOS.
 */
public class ActionVisitor extends JavaBaseVisitor<Void> {

    public static ActionVisitor getVisitor(){
        return new ActionVisitor();
    }

    private void printTree() throws IOException {

        ANTLRInputStream ais = new ANTLRInputStream( new FileInputStream("/Users/radhakrishnan/Downloads/struts-1.3.10/src/apps/cookbook/src/main/java/examples/SuccessAction.java"));
        JavaLexer jl = new JavaLexer(ais);
        CommonTokenStream cts = new CommonTokenStream(jl);
        JavaParser jp = new JavaParser((TokenStream) cts);
        ParseTree pt = jp.compilationUnit();
        System.out.println( pt.toStringTree( jp ));
    }

    public static void main( String... argv ) throws IOException {
        ActionVisitor.getVisitor().printTree();
    }
}
