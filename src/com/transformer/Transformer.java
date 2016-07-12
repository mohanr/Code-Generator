package com.transformer;

import com.configuration.YamlConfiguration;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.DOTTreeGenerator;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import test.JavaBaseVisitor;
import test.JavaLexer;
import test.JavaParser;

import java.io.FileInputStream;
import java.io.IOException;
/**
 * Created by Mohan Radhakrishnan on 6/13/2016.
 */
public class Transformer  {


    static Transformer getTransformer(){
        return new Transformer();
    }

    private void writeModifiedTree() throws IOException {

        ANTLRInputStream ais = new ANTLRInputStream( new FileInputStream("D:/Nationwide/DGS/DGS-TPI/DGS-TPI/thirdPartyInquiryWEB/src/main/java/com/nationwide/pc/is/tpi/web/action/LoginAction.java"));
        JavaLexer jl = new JavaLexer(ais);
        CommonTokenStream stream = new CommonTokenStream(jl);
        JavaParser jp = new JavaParser( stream);
        ParseTree pt = jp.compilationUnit();


        /* Remove Struts import declarations*/
        /* Change class name to something else */
        AllRewriters rewriters = new AllRewriters( stream );
        jp.addParseListener( rewriters );

        ParseTreeWalker walker = new ParseTreeWalker();

        /* All walkers */
        walker.walk(rewriters, pt);
   }


    public static void main( String... argv ) throws IOException {
        Transformer transformer = Transformer.getTransformer();
        transformer.writeModifiedTree();
    }
}