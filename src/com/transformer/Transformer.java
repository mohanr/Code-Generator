package com.transformer;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import com.antlr.framework.JavaLexer;
import com.antlr.framework.JavaParser;
import org.antlr.v4.runtime.tree.Tree;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

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


        ConfiguredRuleRegenerator rewriters = new ConfiguredRuleRegenerator( stream );
        jp.addParseListener( rewriters );

        ParseTreeWalker walker = new ParseTreeWalker();

        /* walkers */
        walker.walk(rewriters, pt);

        ResourceIterable<Label> labels = jp.getAllLabels();
        Consumer<Label> labelConsumer = label ->
                System.out.println("Compilation unit found: " + label );
        jp.transactionalPrint(labels, labelConsumer);

        ResourceIterable<Node> nodes = jp.getAllNodes();
        Consumer<Label> nodeConsumer = label ->
                System.out.println("Compilation unit found: " + label );
        jp.transactionalPrint( labels,nodeConsumer );
   }


    public static void main( String... argv ) throws IOException {
        Transformer transformer = Transformer.getTransformer();
        transformer.writeModifiedTree();
    }
}
